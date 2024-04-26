package pt.uminho.braguia.contact;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class Contact {

    @PrimaryKey
    public final int id;

    @ColumnInfo(name = "contact_name")
    public final String contactName;

    @ColumnInfo(name = "contact_number")
    public final String contactNumber;

    public Contact(int id, String contactName, String contactNumber) {
        this.id = id;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }
}

