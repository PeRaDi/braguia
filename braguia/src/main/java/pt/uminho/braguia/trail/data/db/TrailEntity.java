package pt.uminho.braguia.trail.data.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import pt.uminho.braguia.trail.domain.Trail;

@Entity(tableName = "trail", indices = @Index(value = "id", unique = true))
public class TrailEntity {

    @PrimaryKey
    @NonNull
    Long id;

    @SerializedName("trail_name")
    String name;

    @SerializedName("trail_desc")
    String description;

    @SerializedName("trail_img")
    String imageUrl;

    @SerializedName("trail_duration")
    Long duration;

    @SerializedName("trail_difficulty")
    String difficulty;

    public Trail toDomain() {
        Trail trail = new Trail();
        trail.setId(id);
        trail.setName(name);
        trail.setDescription(description);
        trail.setImageUrl(imageUrl);
        trail.setDuration(duration);
        trail.setDifficulty(difficulty);
        return trail;
    }

    public static TrailEntity fromDomain(Trail trail) {
        TrailEntity entity = new TrailEntity();
        entity.setId(trail.getId());
        entity.setName(trail.getName());
        entity.setDescription(trail.getDescription());
        entity.setImageUrl(trail.getImageUrl());
        entity.setDuration(trail.getDuration());
        entity.setDifficulty(trail.getDifficulty());
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
