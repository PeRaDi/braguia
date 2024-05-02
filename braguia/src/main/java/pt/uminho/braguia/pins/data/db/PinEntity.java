package pt.uminho.braguia.pins.data.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import pt.uminho.braguia.pins.domain.Pin;

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
}
