package pt.uminho.braguia.pins.domain;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Pin {

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

    @SerializedName("rel_pin")
    List<RelPin> relPins;

    @SerializedName("media")
    List<PinMedia> medias;

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

    public List<RelPin> getRelPins() {
        return relPins;
    }

    public void setRelPins(List<RelPin> relPins) {
        this.relPins = relPins;
    }

    public List<PinMedia> getPinMedias() {
        return medias;
    }

    public void setMedias(List<PinMedia> medias) {
        this.medias = medias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pin pin = (Pin) o;
        return Objects.equals(id, pin.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
