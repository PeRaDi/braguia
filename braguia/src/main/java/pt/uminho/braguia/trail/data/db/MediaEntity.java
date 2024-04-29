package pt.uminho.braguia.trail.data.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(
        tableName = "rel_pin",
        indices = @Index(value = "id", unique = true),
        foreignKeys = {
                @ForeignKey(entity = PinEntity.class, parentColumns = "id", childColumns = "pinId", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
        }
)
public class MediaEntity {

    @PrimaryKey
    @NonNull
    Long id;

    @SerializedName("media_file")
    String fileUrl;

    @SerializedName("media_type")
    String type;

    @SerializedName("media_pin")
    Long pinId;

}
