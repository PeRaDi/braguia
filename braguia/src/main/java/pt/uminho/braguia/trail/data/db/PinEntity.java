package pt.uminho.braguia.trail.data.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(
        tableName = "pin",
        indices = @Index(value = "id", unique = true)
)
public class PinEntity {

    @PrimaryKey
    @NonNull
    Long id;

    @SerializedName("pin_name")
    String name;

    @SerializedName("pin_desc")
    String description;

    @SerializedName("pin_lat")
    Double latitude;

    @SerializedName("pin_lng")
    Double longitude;

    @SerializedName("pin_alt")
    Double altitude;

}
