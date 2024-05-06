package pt.uminho.braguia.pins.domain;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class PinMedia implements Serializable {

    public enum Type {
        IMAGE, VIDEO, RECORD;

        public static Type of(String type) {
            switch (type) {
                case "I":
                    return IMAGE;
                case "V":
                    return VIDEO;
                case "R":
                    return RECORD;
                default:
                    throw new IllegalArgumentException("Invalid media type: " + type);
            }
        }

        public static Type of(Integer type) {
            if (type.equals(IMAGE.ordinal())) {
                return IMAGE;
            }
            if (type.equals(VIDEO.ordinal())) {
                return VIDEO;
            }
            if (type.equals(RECORD.ordinal())) {
                return RECORD;
            }
            throw new IllegalArgumentException("Invalid media type: " + type);
        }
    }

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

    public Type type() {
        return Type.of(type);
    }

    public boolean isImage() {
        return type().equals(Type.IMAGE);
    }

    public boolean isVideo() {
        return type().equals(Type.VIDEO);
    }

    public boolean isRecord() {
        return type().equals(Type.RECORD);
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
