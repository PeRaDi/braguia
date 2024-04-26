package pt.uminho.braguia.contact;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "contacts")
public class Contact {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "contact_name")
    public String contactName;

    @ColumnInfo(name = "contact_number")
    public String contactNumber;

    public Contact(int id, String contactName, String contactNumber) {
        this.id = id;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return id == contact.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

