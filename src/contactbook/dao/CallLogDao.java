package contactbook.dao;

import contactbook.db.Database;
import contactbook.model.CallLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//ادارة عمليات الاضافةوالتعديل والحذف لسجلات المكالمات
public class CallLogDao {

	// إنشاء سجل مكالمة جديد
    public CallLog insert(CallLog c) throws SQLException {
        String sql = "INSERT INTO calls(contact_id, type, duration_seconds) VALUES(?,?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, c.getContactId());
            ps.setString(2, c.getType());
            ps.setInt(3, c.getDurationSeconds());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getLong(1));
            }
        }
        return c;
    }

    //حذف سجل مكالمة باستخدام id
    public void delete(long id) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM calls WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    // عرض جميع المكالمات لجهة اتصال معينة
    public List<CallLog> findByContact(long contactId) throws SQLException {
    	//ترتيب حسب الاحدث
        String sql = """
            SELECT id, contact_id, type, duration_seconds, call_at
            FROM calls
            WHERE contact_id=?
            ORDER BY id DESC
        """;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, contactId);
            try (ResultSet rs = ps.executeQuery()) {
                List<CallLog> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    // الكائن object للمكالمة
    private CallLog map(ResultSet rs) throws SQLException {
        return new CallLog(
                rs.getLong("id"),
                rs.getLong("contact_id"),
                rs.getString("type"),
                rs.getInt("duration_seconds"),
                rs.getString("call_at")
        );
    }
}

