package contactbook.ui.dialogs;

import contactbook.model.Contact;

import javax.swing.*;
import java.awt.*;

//نافذة اضافة و تعديل جهة اتصال
public class ContactFormDialog extends BaseFormDialog {
    private final JTextField nameField = new JTextField(25);
    private final JTextField phoneField = new JTextField(25);
    private final JTextField emailField = new JTextField(25);
    private final JTextArea notesArea = new JTextArea(4, 25);

    public ContactFormDialog(Window owner, String title, Contact initial) {
        super(owner, title);

        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);

        //تعبئة الخانات للتعديل 
        if (initial != null) {
            nameField.setText(initial.getName());
            phoneField.setText(initial.getPhone());
            emailField.setText(initial.getEmail());
            notesArea.setText(initial.getNotes());
        }

        //نشاء GridBagLayout مع 4 خانات
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx = 0; gc.gridy = 0;
        form.add(new JLabel("الاسم الكامل:"), gc);
        gc.gridx = 1;
        form.add(nameField, gc);

        gc.gridx = 0; gc.gridy++;
        form.add(new JLabel("رقم الهاتف:"), gc);
        gc.gridx = 1;
        form.add(phoneField, gc);

        gc.gridx = 0; gc.gridy++;
        form.add(new JLabel("البريد الإلكتروني:"), gc);
        gc.gridx = 1;
        form.add(emailField, gc);

        gc.gridx = 0; gc.gridy++;
        gc.anchor = GridBagConstraints.NORTH;
        form.add(new JLabel("ملاحظات:"), gc);
        gc.gridx = 1;
        gc.fill = GridBagConstraints.BOTH;
        form.add(new JScrollPane(notesArea), gc);

        JButton okBtn = new JButton("حفظ");
        JButton cancelBtn = new JButton("إلغاء");
        okBtn.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty() || phoneField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "تأكد من إدخال الاسم ورقم الهاتف", "تنبيه", JOptionPane.WARNING_MESSAGE);
                return;
            }
            closeWithSuccess();
        });
        cancelBtn.addActionListener(e -> dispose());

        JPanel actions = createActionsPanel(okBtn, cancelBtn);
        JPanel root = createRootPanel(form, actions);
        setContentPane(root);
        pack();
        setLocationRelativeTo(owner);
    }

    //انشاء object للبيانات المدرجة
    public Contact getResult(Contact base) {
        if (!isConfirmed()) return null;
        Contact c = base == null ? new Contact() : base; // ينشئ جديد في حال base = null
        c.setName(nameField.getText().trim());
        c.setPhone(phoneField.getText().trim());
        c.setEmail(emptyToNull(emailField.getText())); //للخانات الاختيارية 
        c.setNotes(emptyToNull(notesArea.getText()));
        return c;
    }

    //تعيد null في حال النص فارغ
    private String emptyToNull(String s) {
        if (s == null) return null;
        s = s.trim(); // ازالة الفراغ
        return s.isEmpty() ? null : s;
    }
}
