package contactbook.ui.dialogs;

import contactbook.model.Message;

import javax.swing.*;
import java.awt.*;

public class MessageFormDialog extends BaseFormDialog {
    private final JComboBox<String> directionCombo = new JComboBox<>(new String[]{"مرسلة", "مستلمة"});
    private final JTextArea bodyArea = new JTextArea(6, 30);

    public MessageFormDialog(Window owner) {
        super(owner, "إضافة رسالة");

        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx = 0; gc.gridy = 0;
        form.add(new JLabel("النوع:"), gc);
        gc.gridx = 1;
        form.add(directionCombo, gc);

        gc.gridx = 0; gc.gridy++;
        gc.anchor = GridBagConstraints.NORTH;
        form.add(new JLabel("نص الرسالة:"), gc);
        gc.gridx = 1;
        gc.fill = GridBagConstraints.BOTH;
        form.add(new JScrollPane(bodyArea), gc);

        JButton okBtn = new JButton("حفظ");
        JButton cancelBtn = new JButton("إلغاء");
        okBtn.addActionListener(e -> {
            if (bodyArea.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "الرجاء كتابة نص الرسالة.", "تنبيه", JOptionPane.WARNING_MESSAGE);
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

    public Message getResult(long contactId) {
        if (!isConfirmed()) return null;
        Message m = new Message();
        m.setContactId(contactId);
        m.setDirection(directionCombo.getSelectedIndex() == 0 ? "SENT" : "RECEIVED");
        m.setBody(bodyArea.getText().trim());
        return m;
    }
}
