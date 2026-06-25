package contactbook.model;

//كلاس لتمثيل الرسائل
public class Message extends BaseEntity {
    private Long contactId;
    private String direction; // مستلمة او مرسلة
    private String body;
    private String sentAt;

    public Message() {}

    public Message(Long id, Long contactId, String direction, String body, String sentAt) {
        super(id);
        this.contactId = contactId;
        this.direction = direction;
        this.body = body;
        this.sentAt = sentAt;
    }

    public Long getContactId() { return contactId; }
    public void setContactId(Long contactId) { this.contactId = contactId; }
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public String getSentAt() { return sentAt; }
    public void setSentAt(String sentAt) { this.sentAt = sentAt; }
}
