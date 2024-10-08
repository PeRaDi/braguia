package pt.uminho.braguia.trail.data.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(
        tableName = "rel_trail",
        indices = @Index(value = "id", unique = true),
        foreignKeys = {
                @ForeignKey(entity = TrailEntity.class, parentColumns = "id", childColumns = "trailId", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
        }
)
public class RelTrailEntity {

    @PrimaryKey
    @NonNull
    Long id;

    @SerializedName("value")
    String value;

    @SerializedName("attrib")
    String attribute;

    @SerializedName("trail")
    Long trailId;

}
