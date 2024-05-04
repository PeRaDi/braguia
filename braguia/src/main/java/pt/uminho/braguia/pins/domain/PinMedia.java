package pt.uminho.braguia.pins.domain;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class PinMedia {

    @NonNull
    Long id;

    @SerializedName("media_file")
    String fileUrl;

    @SerializedName("media_type")
    String type;

    @SerializedName("media_pin")
    Long pinId;

    PinMediaType getMediaType() {
        return PinMediaType.of(type);
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getPinId() {
        return pinId;
    }

    public void setPinId(Long pinId) {
        this.pinId = pinId;
    }

    public boolean isImage() {
        return type != null && type.equals("I");
    }

    public boolean isVideo() {
        return type != null && type.equals("V");
    }

    public boolean isRecord() {
        return type != null && type.equals("R");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PinMedia media = (PinMedia) o;
        return Objects.equals(id, media.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
