package pt.uminho.braguia.pins.domain;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class RelPin {

    @NonNull
    Long id;

    @SerializedName("value")
    String value;

    @SerializedName("attrib")
    String attribute;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelPin relPin = (RelPin) o;
        return Objects.equals(id, relPin.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
