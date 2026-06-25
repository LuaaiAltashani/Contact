package contactbook.dao;

import contactbook.db.Database;
import contactbook.model.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// جدول contacts: اضافة وتعديل وحذف
public class ContactDao {
	
//ضافة جهة اتصال جديدة.
    public Contact insert(Contact c) throws SQLException {
        String sql = "INSERT INTO contacts(name, phone, email, notes) VALUES(?,?,?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getNotes());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getLong(1));
            }
        }
        return c;
    }

//تعديل بيانات جهة اتصال موجودة بناء على id
    public void update(Contact c) throws SQLException {
        String sql = "UPDATE contacts SET name=?, phone=?, email=?, notes=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getNotes());
            ps.setLong(5, c.getId());
            ps.executeUpdate();
        }
    }

//حذف جهة اتصال باستخدام id
    public void delete(long id) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM contacts WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

//بحث عن جهة اتصال باستخدام id  وترجيع object
    public Contact findById(long id) throws SQLException {
        String sql = "SELECT id, name, phone, email, notes, created_at FROM contacts WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return map(rs);
            }
        }
    }

//عرض كل جهات الاتصال
    public List<Contact> findAll() throws SQLException {
//ترتيب حسب الجديد الاحدث بالاعلى
        String sql = "SELECT id, name, phone, email, notes, created_at FROM contacts ORDER BY id DESC";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Contact> out = new ArrayList<>();
            while (rs.next()) out.add(map(rs));
            return out;
        }
    }
//البحث باستخدام الاسم او رقم الهاتف
    public List<Contact> search(String q) throws SQLException {
        String sql = """
            SELECT id, name, phone, email, notes, created_at
            FROM contacts
            WHERE name LIKE ? OR phone LIKE ?
            ORDER BY id DESC
        """;
        String like = "%" + (q == null ? "" : q.trim()) + "%";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                List<Contact> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

//object لعرض جهة اتصال
    private Contact map(ResultSet rs) throws SQLException {
        return new Contact(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("notes"),
                rs.getString("created_at")
        );
    }
}

