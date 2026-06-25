package contactbook.dao;

import contactbook.db.Database;
import contactbook.model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//يدير عمليات إضافة وحذف وبحث الرسائل الخاصة بجهات الاتصال
public class MessageDao {

	//إضافة رسالة جديدة
    public Message insert(Message m) throws SQLException {
        String sql = "INSERT INTO messages(contact_id, direction, body) VALUES(?,?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, m.getContactId());
            ps.setString(2, m.getDirection());
            ps.setString(3, m.getBody());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) m.setId(rs.getLong(1));
            }
        }
        return m;
    }

    //حذف رسالة باستخدام id
    public void delete(long id) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM messages WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    //عرض جميع الرسائل لجهة اتصال بناء على رقم جهة الاتصال ترتيب من الاحدث
    public List<Message> findByContact(long contactId) throws SQLException {
        String sql = """
            SELECT id, contact_id, direction, body, sent_at
            FROM messages
            WHERE contact_id=?
            ORDER BY id DESC
        """;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, contactId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Message> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    // كائن object لللرسالة
    private Message map(ResultSet rs) throws SQLException {
        return new Message(
                rs.getLong("id"),
                rs.getLong("contact_id"),
                rs.getString("direction"),
                rs.getString("body"),
                rs.getString("sent_at")
        );
    }
}

