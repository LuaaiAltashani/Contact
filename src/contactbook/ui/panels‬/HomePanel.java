package contactbook.ui.panels;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    public HomePanel() {
        super(new BorderLayout());

        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setText("""
                برنامج دليل الهاتف

                  تطبيق بسيط مشروع لطالب دراسات عليا: بالأكاديمية الليبية للدراسات العليا :
                  المادة Advanced Object-Oriented Programming
                - الاسم : لؤي عبدالرازق الطشاني
                - الفصل الأول 
                - قسم تقنية المعلومات
                - الرقم الدراسي : 2798

                """);

        JLabel title = new JLabel("الواجهة الرئيسية", SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        center.add(new JScrollPane(info), BorderLayout.CENTER);

        add(title, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }
}

