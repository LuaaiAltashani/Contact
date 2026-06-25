package contactbook.ui.dialogs;

import contactbook.model.CallLog;

import javax.swing.*;
import java.awt.*;

//قائمة توشح نوع المكالمة والمدة بالثواني
public class CallFormDialog extends BaseFormDialog {
    private final JComboBox<String> typeCombo = new JComboBox<>(new String[]{"واردة", "صادرة", "فائتة"});
    private final JSpinner durationSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 60 * 60 * 24, 10));

    public CallFormDialog(Window owner) {
        super(owner, "إضافة مكالمة"); // استدعاء BaseFormDialog واضافة عنوان اضافة مكالمة

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx = 0; gc.gridy = 0;
        form.add(new JLabel("نوع المكالمة:"), gc);
        gc.gridx = 1;
        form.add(typeCombo, gc); // combobox انواع المكالمات واردة صادرة فائتة

        gc.gridx = 0; gc.gridy++;
        form.add(new JLabel("المدة (ثواني):"), gc);
        gc.gridx = 1;
        form.add(durationSpinner, gc);

        JButton okBtn = new JButton("حفظ");
        JButton cancelBtn = new JButton("إلغاء");
        okBtn.addActionListener(e -> {
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
    public CallLog getResult(long contactId) {
        if (!isConfirmed()) return null; // في حال عدم التاكيد يرجع null
        CallLog c = new CallLog();
        c.setContactId(contactId);
        //تحويل نوع المكالمة للغة الانجليزية
        c.setType(switch (typeCombo.getSelectedIndex()) { 
            case 0 -> "INCOMING";
            case 1 -> "OUTGOING";
            default -> "MISSED";
        });
        c.setDurationSeconds((Integer) durationSpinner.getValue());
        return c;
    }
}
