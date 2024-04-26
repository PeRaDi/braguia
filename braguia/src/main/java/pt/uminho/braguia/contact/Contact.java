package pt.uminho.braguia.contact;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

    public Contact() {
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
}

