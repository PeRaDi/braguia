package pt.uminho.braguia.trail.data.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(
        tableName = "edge",
        indices = @Index(value = "id", unique = true),
        foreignKeys = {
                @ForeignKey(entity = TrailEntity.class, parentColumns = "id", childColumns = "trailId", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = PinEntity.class, parentColumns = "id", childColumns = "startId", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = PinEntity.class, parentColumns = "id", childColumns = "endId", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
        }
)
public class EdgeEntity {

    @PrimaryKey
    @NonNull
    Long id;

    @SerializedName("edge_transport")
    String transport;

    @SerializedName("edge_desc")
    String description;

    @SerializedName("edge_duration")
    Long duration;

    @SerializedName("trail")
    Long trailId;

    @SerializedName("edge_start")
    Long startId;

    @SerializedName("edge_end")
    Long endId;

}
