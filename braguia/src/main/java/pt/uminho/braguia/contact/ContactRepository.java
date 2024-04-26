package pt.uminho.braguia.contact;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.contact.Contact;
import pt.uminho.braguia.database.BraGuiaDatabase;
import pt.uminho.braguia.database.daos.ContactDao;

public class ContactRepository {

    BraGuiaDatabase braGuiaDatabase;
    ContactDao contactDao;

    public MediatorLiveData<List<Contact>> allContacts;

    @Inject
    public ContactRepository(BraGuiaDatabase braGuiaDatabase) {
        this.braGuiaDatabase = braGuiaDatabase;
        this.contactDao = braGuiaDatabase.contactDao();

        allContacts = new MediatorLiveData<>();
        allContacts.addSource(
                contactDao.getAll(), localTrails -> {
                    if (localTrails != null && localTrails.size() > 0) {
                        allContacts.setValue(localTrails);
                    } else {
                        allContacts.setValue(null);
                    }
                }
        );
    }

    public void insertContacts(List<Contact> contact) {
        new InsertAsyncTask(contactDao).execute(contact);
    }

    public void deleteAllContacts() {
        contactDao.deleteAll();
    }

    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }

    public static class InsertAsyncTask extends AsyncTask<List<Contact>, Void, Void> {
        private ContactDao contactDao;

        public InsertAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(List<Contact>... contacts) {
            contactDao.insert(contacts[0]);
            List<Contact> contactList = contactDao.getAll().getValue();
            return null;
        }
    }
}
