package contactbook.ui.dialogs;

import javax.swing.*;
import java.awt.*;

//كلاس abstract شكل موحد للنوافذ
public abstract class BaseFormDialog extends JDialog {
    protected boolean ok = false;
//انشاء نافذه 
    public BaseFormDialog(Window owner, String title) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    //انشاء لوحة افقية بها زر حفظ والغاء فقط 
    protected JPanel createActionsPanel(JButton okBtn, JButton cancelBtn) {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actions.add(okBtn);
        actions.add(cancelBtn);
        return actions;
    }
//انشاء اللوحة الرئيسية
    protected JPanel createRootPanel(Component center, Component south) {
        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); //  هوامش 10px
        root.add(center, BorderLayout.CENTER);//  الحقول
        root.add(south, BorderLayout.SOUTH);// الازرار
        return root;
    }

    //اغلاق النافذه
    //بعد التاكد من ان المدخلات صحيحه
    protected void closeWithSuccess() {
        ok = true;
        dispose();
    }
//التاكيد إذا كان المستخدم اكد العملية
    protected boolean isConfirmed() {
        return ok;
    }
}

