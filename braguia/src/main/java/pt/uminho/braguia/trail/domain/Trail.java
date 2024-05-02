package pt.uminho.braguia.trail.domain;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Trail {

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

    @SerializedName("rel_trail")
    List<RelTrail> relTrails;

    @SerializedName("edges")
    List<Edge> edges;

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

    public List<RelTrail> getRelTrails() {
        return relTrails;
    }

    public void setRelTrails(List<RelTrail> relTrails) {
        this.relTrails = relTrails;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public String formatDuration() {
        int hours = (int) (getDuration() / 60);
        int minutes = (int) (getDuration() % 60);
        String h = hours < 1 ? "" : String.format("%02dh", hours);
        String m = minutes < 1 ? "" : String.format("%02dm", minutes);
        return String.format("%s%s", h, m);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trail trail = (Trail) o;
        return Objects.equals(id, trail.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
