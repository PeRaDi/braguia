package pt.uminho.braguia.contact;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

import javax.inject.Inject;

import pt.uminho.braguia.database.BraGuiaDatabase;

public class ContactRepository {

    private final BraGuiaDatabase braGuiaDatabase;
    private final ContactDao contactDao;
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
        new DeleteAllAsyncTask(contactDao).execute();
    }

    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }

    private static class InsertAsyncTask extends AsyncTask<List<Contact>, Void, Void> {
        private ContactDao contactDao;

        public InsertAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Contact>... contacts) {
            contactDao.insert(contacts[0]);
            Log.i("ContactRepository", "Inserted " + contacts[0].size() + " contacts");
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private ContactDao contactDao;

        public DeleteAllAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            contactDao.deleteAll();
            Log.i("ContactRepository", "Deleted all contacts");
            return null;
        }
    }
}
