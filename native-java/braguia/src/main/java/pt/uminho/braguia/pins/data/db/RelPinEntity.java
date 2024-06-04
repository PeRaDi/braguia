package pt.uminho.braguia.pins.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import pt.uminho.braguia.pins.domain.RelPin;

@Entity(tableName = "rel_pin",
        indices = @Index(value = "id", unique = true)
)
public class RelPinEntity {

    @PrimaryKey
    @NonNull
    Long id;

    @ColumnInfo(name = "value")
    @SerializedName("value")
    String value;

    @ColumnInfo(name = "attrib")
    @SerializedName("attrib")
    String attribute;

    @ColumnInfo(name = "pin")
    @SerializedName("pin")
    Long pinId;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Long getPinId() {
        return pinId;
    }

    public void setPinId(Long pinId) {
        this.pinId = pinId;
    }

    public static RelPinEntity fromDomain(RelPin relPin) {
        RelPinEntity relPinEntity = new RelPinEntity();
        relPinEntity.setId(relPin.getId());
        relPinEntity.setValue(relPin.getValue());
        relPinEntity.setAttribute(relPin.getAttribute());
        relPinEntity.setPinId(relPin.getPinId());
        return relPinEntity;
    }

    public RelPin toDomain() {
        RelPin relPin = new RelPin();
        relPin.setId(id);
        relPin.setValue(value);
        relPin.setAttribute(attribute);
        relPin.setPinId(pinId);
        return relPin;
    }
}
