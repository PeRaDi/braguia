package pt.uminho.braguia.repositories;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.contact.Contact;
import pt.uminho.braguia.database.BraGuiaDatabase;
import pt.uminho.braguia.database.daos.ContactDao;

public class ContactRepository {

    BraGuiaDatabase braGuiaDatabase;
    ContactDao contactDao;

    @Inject
    public ContactRepository(BraGuiaDatabase braGuiaDatabase) {
        this.braGuiaDatabase = braGuiaDatabase;
        this.contactDao = braGuiaDatabase.contactDao();
    }

    public void insertContact(Contact contact) {
        contactDao.insert(contact);
    }

    public void deleteContact(Contact contact) {
        contactDao.delete(contact);
    }

    public List<Contact> getAllContacts() {
        return contactDao.getAll();
    }
}
