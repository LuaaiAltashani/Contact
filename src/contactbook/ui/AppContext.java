package contactbook.ui;

import contactbook.dao.CallLogDao;
import contactbook.dao.ContactDao;
import contactbook.dao.MessageDao;

import java.util.ArrayList;
import java.util.List;

// كلاس لعمليات التحديث 
public class AppContext {
	private ContactDao contactDao;     // كائن جهات الاتصال
	private MessageDao messageDao;     // كائن الرسائل
	private CallLogDao callLogDao;     // كاين المكالمات

    private final List<Runnable> contactsChangedListeners = new ArrayList<>();

    public AppContext() {
        setContactDao(new ContactDao());
        setMessageDao(new MessageDao());
        setCallLogDao(new CallLogDao());
    }

    public ContactDao getContactDao() {
        return contactDao;
    }

    public void setContactDao(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    public MessageDao getMessageDao() {
        return messageDao;
    }

    public void setMessageDao(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public CallLogDao getCallLogDao() {
        return callLogDao;
    }

    public void setCallLogDao(CallLogDao callLogDao) {
        this.callLogDao = callLogDao;
    }
// في حال اي تغيير في جهات الاتصال
    public void onContactsChanged(Runnable r) {
        contactsChangedListeners.add(r);
    }

    public void fireContactsChanged() {
        for (Runnable r : contactsChangedListeners) r.run();
    }
}
