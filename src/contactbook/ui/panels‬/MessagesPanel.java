package contactbook.ui.panels;

import contactbook.model.Contact;
import contactbook.model.Message;
import contactbook.ui.AppContext;
import contactbook.ui.UIUtils;
import contactbook.ui.dialogs.MessageFormDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MessagesPanel extends JPanel {
    private final AppContext ctx;
    private final JComboBox<Contact> contactCombo = new JComboBox<>();
    private final DefaultTableModel model;
    private final JTable table;
    private List<Contact> contacts = new ArrayList<>();
    private List<Message> current = new ArrayList<>();

    public MessagesPanel(AppContext ctx) {
        super(new BorderLayout(10, 10));
        this.ctx = ctx;

        model = new DefaultTableModel(new Object[]{"المعرف", "النوع", "نص الرسالة", "التاريخ"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        contactCombo.addActionListener(e -> refreshMessages());

        add(buildTopBar(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildActions(), BorderLayout.SOUTH);

        ctx.onContactsChanged(this::loadContacts);
        loadContacts();
    }

    private JComponent buildTopBar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEADING));
        p.add(new JLabel("جهة الاتصال:"));
        p.add(contactCombo);
        JButton refreshBtn = new JButton("تحديث");
        refreshBtn.addActionListener(e -> {
            loadContacts();
            refreshMessages();
        });
        p.add(refreshBtn);
        return p;
    }

    private JComponent buildActions() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addBtn = new JButton("إضافة رسالة");
        JButton delBtn = new JButton("حذف رسالة");
        JButton refreshBtn = new JButton("تحديث");

        addBtn.addActionListener(e -> addMessage());
        delBtn.addActionListener(e -> deleteSelected());
        refreshBtn.addActionListener(e -> refreshMessages());

        p.add(addBtn);
        p.add(delBtn);
        p.add(refreshBtn);
        return p;
    }

    private void loadContacts() {
        try {
            contacts = ctx.getContactDao().findAll();
            Contact selected = getSelectedContactObject();
            contactCombo.removeAllItems();
            for (Contact c : contacts) contactCombo.addItem(c);
            if (selected != null) contactCombo.setSelectedItem(findById(selected.getId()));
        } catch (Exception ex) {
            UIUtils.showError(this, ex);
        }
    }

    private Contact findById(Long id) {
        if (id == null) return null;
        for (Contact c : contacts) {
            if (c.getId() != null && c.getId().equals(id)) return c;
        }
        return null;
    }

    private void refreshMessages() {
        Contact c = getSelectedContactObject();
        if (c == null || c.getId() == null) {
            model.setRowCount(0);
            return;
        }
        try {
            current = ctx.getMessageDao().findByContact(c.getId());
            render(current);
        } catch (Exception ex) {
            UIUtils.showError(this, ex);
        }
    }

    private void addMessage() {
        Contact c = getSelectedContactObject();
        if (c == null || c.getId() == null) {
            JOptionPane.showMessageDialog(this, "لا توجد جهة اتصال محددة.", "تنبيه", JOptionPane.WARNING_MESSAGE);
            return;
        }

        MessageFormDialog dlg = new MessageFormDialog(SwingUtilities.getWindowAncestor(this));
        dlg.setVisible(true);
        Message m = dlg.getResult(c.getId());
        if (m == null) return;

        try {
            ctx.getMessageDao().insert(m);
            refreshMessages();
        } catch (Exception ex) {
            UIUtils.showError(this, ex);
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "اختر رسالة أولاً.", "تنبيه", JOptionPane.WARNING_MESSAGE);
            return;
        }
        long id = Long.parseLong(model.getValueAt(row, 0).toString());
        if (!UIUtils.confirm(this, "هل تريد حذف الرسالة؟")) return;
        try {
            ctx.getMessageDao().delete(id);
            refreshMessages();
        } catch (Exception ex) {
            UIUtils.showError(this, ex);
        }
    }

    private void render(List<Message> msgs) {
        model.setRowCount(0);
        for (Message m : msgs) {
            model.addRow(new Object[]{
                    m.getId(),
                    "SENT".equalsIgnoreCase(m.getDirection()) ? "مرسلة" : "مستلمة",
                    m.getBody(),
                    m.getSentAt()
            });
        }
    }

    private Contact getSelectedContactObject() {
        Object selectedObject = contactCombo.getSelectedItem();
        if (selectedObject instanceof Contact contact) {
            return contact;
        }
        return null;
    }
}
