package pt.uminho.braguia.pins.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import pt.uminho.braguia.pins.domain.Pin;
import pt.uminho.braguia.pins.domain.PinMedia;
import pt.uminho.braguia.pins.domain.RelPin;

@Entity(
        tableName = "pin",
        indices = @Index(value = "id", unique = true)
)
public class PinEntity {

    @PrimaryKey
    @NonNull
    Long id;

    @ColumnInfo(name = "pin_name")
    @SerializedName("pin_name")
    String name;

    @ColumnInfo(name = "pin_desc")
    @SerializedName("pin_desc")
    String description;

    @ColumnInfo(name = "pin_lat")
    @SerializedName("pin_lat")
    Double latitude;

    @ColumnInfo(name = "pin_lng")
    @SerializedName("pin_lng")
    Double longitude;

    @ColumnInfo(name = "pin_alt")
    @SerializedName("pin_alt")
    Double altitude;

    public Pin toDomain() {
        Pin pin = new Pin();
        pin.setId(id);
        pin.setName(name);
        pin.setDescription(description);
        pin.setLatitude(latitude);
        pin.setLongitude(longitude);
        pin.setAltitude(altitude);
        return pin;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public static PinEntity fromDomain(Pin pin) {
        PinEntity entity = new PinEntity();
        entity.setId(pin.getId());
        entity.setName(pin.getName());
        entity.setDescription(pin.getDescription());
        entity.setLatitude(pin.getLatitude());
        entity.setLongitude(pin.getLongitude());
        entity.setAltitude(pin.getAltitude());
        return entity;
    }
}
