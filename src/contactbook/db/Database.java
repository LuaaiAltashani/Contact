package contactbook.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//الكلاس مختص بالاتصال بقاعدة البيانات وانشاء الجداول
// كلاس نهائي final
public final class Database {
    private static final String DB_URL = "jdbc:sqlite:contact_book.db";

    private Database() {}
//انشاء اتصال بقاعدة البيانات contact_book.db
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    //انشاء جدول جهات الاتصال
    public static void init() throws SQLException {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS contacts (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  name TEXT NOT NULL,
                  phone TEXT NOT NULL UNIQUE,
                  email TEXT,
                  notes TEXT,
                  created_at TEXT NOT NULL DEFAULT (datetime('now'))
                );
            """);
       // تم تعريف حقل phone unique لمنع تكرار ارقام الهاتف
            
// انشاء جدول الرسايل
            st.execute("""
                CREATE TABLE IF NOT EXISTS messages (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  contact_id INTEGER NOT NULL,
                  direction TEXT NOT NULL, -- SENT / RECEIVED
                  body TEXT NOT NULL,
                  sent_at TEXT NOT NULL DEFAULT (datetime('now')),
                  FOREIGN KEY (contact_id) REFERENCES contacts(id) ON DELETE CASCADE
                );
            """);
//انشاء جدول المكالمات
            st.execute("""
                CREATE TABLE IF NOT EXISTS calls (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  contact_id INTEGER NOT NULL,
                  type TEXT NOT NULL, -- INCOMING / OUTGOING / MISSED
                  duration_seconds INTEGER NOT NULL DEFAULT 0,
                  call_at TEXT NOT NULL DEFAULT (datetime('now')),
                  FOREIGN KEY (contact_id) REFERENCES contacts(id) ON DELETE CASCADE
                );
            """);
            //عمود call_at يعبأ تلقائيا خاص بوقت المكالمة

            st.execute("CREATE INDEX IF NOT EXISTS idx_messages_contact ON messages(contact_id);");
            st.execute("CREATE INDEX IF NOT EXISTS idx_calls_contact ON calls(contact_id);");
        }
    }
}

