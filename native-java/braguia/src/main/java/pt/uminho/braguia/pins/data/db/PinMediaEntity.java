package pt.uminho.braguia.pins.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import pt.uminho.braguia.pins.domain.PinMedia;

@Entity(
        tableName = "media",
        indices = @Index(value = "id", unique = true)
)
public class PinMediaEntity {

    @PrimaryKey
    @NonNull
    private Long id;

    @ColumnInfo(name = "media_file")
    @SerializedName("media_file")
    private String mediaFile;

    @ColumnInfo(name = "media_type")
    @SerializedName("media_type")
    private String mediaType;

    @ColumnInfo(name = "media_pin")
    @SerializedName("media_pin")
    private Long mediaPin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMediaFile() {
        return mediaFile;
    }

    public void setMediaFile(String mediaFile) {
        this.mediaFile = mediaFile;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Long getMediaPin() {
        return mediaPin;
    }

    public void setMediaPin(Long mediaPin) {
        this.mediaPin = mediaPin;
    }

    public static PinMediaEntity fromDomain(PinMedia pinMedia) {
        PinMediaEntity entity = new PinMediaEntity();
        entity.setId(pinMedia.getId());
        entity.setMediaFile(pinMedia.getFileUrl());
        entity.setMediaType(pinMedia.getType());
        entity.setMediaPin(pinMedia.getPinId());
        return entity;
    }

    public PinMedia toDomain() {
        PinMedia pinMedia = new PinMedia();
        pinMedia.setId(id);
        pinMedia.setFileUrl(mediaFile);
        pinMedia.setType(mediaType);
        pinMedia.setPinId(mediaPin);
        return pinMedia;
    }
}