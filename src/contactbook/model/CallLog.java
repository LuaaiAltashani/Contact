package contactbook.model;

//كلاس لتمثيل سجل المكالمة
public class CallLog extends BaseEntity {
    private Long contactId;
    private String type; // صادرة او واردة اوفائتة
    private int durationSeconds;
    private String callAt;

    public CallLog() {}

    public CallLog(Long id, Long contactId, String type, int durationSeconds, String callAt) {
        super(id);
        this.contactId = contactId;
        this.type = type;
        this.durationSeconds = durationSeconds;
        this.callAt = callAt;
    }

    public Long getContactId() { return contactId; }
    public void setContactId(Long contactId) { this.contactId = contactId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }
    public String getCallAt() { return callAt; }
    public void setCallAt(String callAt) { this.callAt = callAt; }
}
