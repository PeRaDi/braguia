package pt.uminho.braguia.trail.domain;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class RelTrail {

    @NonNull
    Long id;

    @SerializedName("value")
    String value;

    @SerializedName("attrib")
    String attribute;

    @SerializedName("trail")
    Long trailId;

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

    public Long getTrailId() {
        return trailId;
    }

    public void setTrailId(Long trailId) {
        this.trailId = trailId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelTrail relTrail = (RelTrail) o;
        return Objects.equals(id, relTrail.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
