package contactbook.model;

//كلاس لتمثيل جهة الاتصال
public class Contact extends BaseEntity {
    private String name;
    private String phone;
    private String email;
    private String notes;
    private String createdAt;

    public Contact() {}

    public Contact(Long id, String name, String phone, String email, String notes, String createdAt) {
        super(id);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        // يعبأ في ComboBox
        return name + " (" + phone + ")";
    }
}
