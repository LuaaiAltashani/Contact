package contactbook.ui.panels;

import contactbook.model.Contact;
import contactbook.ui.AppContext;
import contactbook.ui.UIUtils;
import contactbook.ui.dialogs.ContactFormDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ContactsPanel extends JPanel {
    private final AppContext ctx;
    private final JTextField searchField = new JTextField(25);
    private final DefaultTableModel model;
    private final JTable table;
    private List<Contact> current = new ArrayList<>();

    public ContactsPanel(AppContext ctx) {
        super(new BorderLayout(10, 10));
        this.ctx = ctx;

        model = new DefaultTableModel(new Object[]{"المعرف", "الاسم", "الهاتف", "البريد", "ملاحظات"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    editSelected();
                }
            }
        });

        add(buildTopBar(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildActions(), BorderLayout.SOUTH);

        refreshAll();
    }

    private JComponent buildTopBar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEADING));
        p.add(new JLabel("بحث (اسم/رقم):"));
        p.add(searchField);
        JButton searchBtn = new JButton("بحث");
        JButton allBtn = new JButton("إظهار الكل");
        searchBtn.addActionListener(e -> search());
        allBtn.addActionListener(e -> refreshAll());
        p.add(searchBtn);
        p.add(allBtn);
        return p;
    }

    private JComponent buildActions() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton addBtn = new JButton("إضافة");
        addBtn.setBackground(Color.cyan);
        addBtn.setForeground(Color.BLUE);
        JButton editBtn = new JButton("تعديل");
        JButton delBtn = new JButton("حذف");
        JButton refreshBtn = new JButton("تحديث");

        addBtn.addActionListener(e -> addNew());
        editBtn.addActionListener(e -> editSelected());
        delBtn.addActionListener(e -> deleteSelected());
        refreshBtn.addActionListener(e -> refreshAll());

        p.add(addBtn);
        p.add(editBtn);
        p.add(delBtn);
        p.add(refreshBtn);
        return p;
    }

    private void addNew() {
        ContactFormDialog dlg = new ContactFormDialog(SwingUtilities.getWindowAncestor(this), "إضافة جهة اتصال", null);
        dlg.setVisible(true);
        Contact c = dlg.getResult(null);
        if (c == null) return;

        try {
            ctx.getContactDao().insert(c);
            refreshAll();
            ctx.fireContactsChanged();
        } catch (Exception ex) {
            UIUtils.showError(this, ex);
        }
    }

    private void editSelected() {
        Contact selected = getSelectedContact();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "اختر جهة اتصال أولاً.", "تنبيه", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ContactFormDialog dlg = new ContactFormDialog(SwingUtilities.getWindowAncestor(this), "تعديل جهة اتصال", selected);
        dlg.setVisible(true);
        Contact c = dlg.getResult(selected);
        if (c == null) return;

        try {
            ctx.getContactDao().update(c);
            refreshAll();
            ctx.fireContactsChanged();
        } catch (Exception ex) {
            UIUtils.showError(this, ex);
        }
    }

    private void deleteSelected() {
        Contact selected = getSelectedContact();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "اختر جهة اتصال أولاً.", "تنبيه", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!UIUtils.confirm(this, "هل تريد حذف جهة الاتصال؟ سيتم حذف الرسائل والمكالمات المرتبطة أيضاً.")) return;

        try {
            ctx.getContactDao().delete(selected.getId());
            refreshAll();
            ctx.fireContactsChanged();
        } catch (Exception ex) {
            UIUtils.showError(this, ex);
        }
    }

    private void refreshAll() {
        try {
            current = ctx.getContactDao().findAll();
            render(current);
        } catch (Exception ex) {
            UIUtils.showError(this, ex);
        }
    }

    private void search() {
        try {
            current = ctx.getContactDao().search(searchField.getText());
            render(current);
        } catch (Exception ex) {
            UIUtils.showError(this, ex);
        }
    }

    private void render(List<Contact> contacts) {
        model.setRowCount(0);
        for (Contact c : contacts) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getName(),
                    c.getPhone(),
                    c.getEmail() == null ? "" : c.getEmail(),
                    c.getNotes() == null ? "" : c.getNotes()
            });
        }
    }

    private Contact getSelectedContact() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        Object idObject = getCellObject(row, 0);
        if (idObject == null) return null;
        long id = Long.parseLong(idObject.toString());
        for (Contact c : current) {
            if (c.getId() != null && c.getId() == id) return c;
        }
        return null;
    }

    private Object getCellObject(int row, int column) {
        return model.getValueAt(row, column);
    }
}
