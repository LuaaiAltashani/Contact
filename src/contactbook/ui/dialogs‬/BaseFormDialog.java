package contactbook.ui.dialogs;

import javax.swing.*;
import java.awt.*;

public abstract class BaseFormDialog extends JDialog {
    protected boolean ok = false;

    public BaseFormDialog(Window owner, String title) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    protected JPanel createActionsPanel(JButton okBtn, JButton cancelBtn) {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actions.add(okBtn);
        actions.add(cancelBtn);
        return actions;
    }

    protected JPanel createRootPanel(Component center, Component south) {
        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        root.add(center, BorderLayout.CENTER);
        root.add(south, BorderLayout.SOUTH);
        return root;
    }

    protected void closeWithSuccess() {
        ok = true;
        dispose();
    }

    protected boolean isConfirmed() {
        return ok;
    }
}

