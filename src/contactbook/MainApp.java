package contactbook;

import com.formdev.flatlaf.FlatLightLaf;
import contactbook.db.Database;
import contactbook.ui.MainFrame;
import contactbook.ui.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

//كلاس تشغيل وضبط البرنامج
public class MainApp {

    public static void main(String[] args) {
        // تحسين شكل المظهر
       FlatLightLaf.setup();

        //ضبط اللغة العربية كلغة افتراضية  اتجاه  من اليمين لليسار)
        Locale.setDefault(new Locale("ar"));
        UIManager.getDefaults().setDefaultLocale(new Locale("ar"));

        SwingUtilities.invokeLater(() -> {
        //تهيئة قاعدة البيانات
            try {        
                Database.init();
            } catch (Exception e) {
                showFatalError(e);
                return;
            }
         //  اعدادات اللغة العربية كافتراضية
            UIUtils.applyArabicDefaults();
            MainFrame frame = new MainFrame();
            frame.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            frame.setVisible(true);
        });
    }

    //دالة تنهي البرنامج وتعرض رسالة خطأ في حالة فشل تهيئة قاعدة البيانات
    private static void showFatalError(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(
                null,
                "حدث خطأ أثناء تشغيل البرنامج:\n" + e.getMessage(),
                "خطأ",
                JOptionPane.ERROR_MESSAGE
        );
        System.exit(1);
    }
}
