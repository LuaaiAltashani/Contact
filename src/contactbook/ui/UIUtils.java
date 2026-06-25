package contactbook.ui;

import javax.swing.*;
import java.awt.*;
//كلاس نهائي final يحتوي على دوال مساعدة
public final class UIUtils {
    private UIUtils() {}  // حماية من انشاء كائنات

    //تحديد نوع وحجم الخط العربي
    public static void applyArabicDefaults() {
        Font base = new Font("SansSerif", Font.PLAIN, 14);
        for (Object key : UIManager.getDefaults().keySet()) {
            Object val = UIManager.get(key);
            if (val instanceof Font) UIManager.put(key, base);
        }
    }

    //رسايل الخطأ
    public static void showError(Component parent, Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(
                parent,
                "حدث خطأ:\n" + e.getMessage(),
                "خطأ",
                JOptionPane.ERROR_MESSAGE
        );
    }

    //رسايل التاكيد
    public static boolean confirm(Component parent, String message) {
        int r = JOptionPane.showConfirmDialog(parent, message, "تأكيد", JOptionPane.YES_NO_OPTION);
        return r == JOptionPane.YES_OPTION;
    }
}

