package contactbook.ui;

import contactbook.ui.panels.CallsPanel;
import contactbook.ui.panels.ContactsPanel;
import contactbook.ui.panels.HomePanel;
import contactbook.ui.panels.MessagesPanel;

import javax.swing.*;
import java.awt.*;

//كلاس الواجهة الرئيسية للبرنامج
//يرث من كلاس JFrame
public class MainFrame extends JFrame {
    private final AppContext ctx = new AppContext(); //استدعات ما سيتم عرضه رسائل -مكالمات-جهات اتصال

    //الواجهة الرسومية للمنظومة
    public MainFrame() {
        super("دليل الهاتف - جهات الاتصال والرسائل والمكالمات");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        setJMenuBar(buildMenu()); //شريط قوائم

        //رأس Header يعرض عنوان التطبيق
        JLabel header = new JLabel("دليل الهاتف (جهات الاتصال / الرسائل / المكالمات)", SwingConstants.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        //انشاء Tab او لوحة علامات 4اقسام
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("الرئيسية", new HomePanel());
        tabs.addTab("جهات الاتصال", new ContactsPanel(ctx));
        tabs.addTab("الرسائل", new MessagesPanel(ctx));
        tabs.addTab("سجل المكالمات", new CallsPanel(ctx));

        JPanel root = new JPanel(new BorderLayout());
        root.add(header, BorderLayout.NORTH);
        root.add(tabs, BorderLayout.CENTER);
        setContentPane(root);
    }

    //انشاء شريط القوائم 
    private JMenuBar buildMenu() {
        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("ملف");
        JMenuItem exit = new JMenuItem("خروج");
        exit.addActionListener(e -> dispose());
        file.add(exit);

        bar.add(file);
     
        return bar;
    }
}
