package pt.uminho.braguia.trail.domain;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Edge {

    @NonNull
    Long id;

    @SerializedName("edge_transport")
    String transport;

    @SerializedName("edge_desc")
    String description;

    @SerializedName("edge_duration")
    Long duration;

    @SerializedName("trail")
    Long trailId;

    @SerializedName("edge_start")
    Pin startPin;

    @SerializedName("edge_end")
    Pin endPin;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getTrailId() {
        return trailId;
    }

    public void setTrailId(Long trailId) {
        this.trailId = trailId;
    }

    public Pin getStartPin() {
        return startPin;
    }

    public void setStartPin(Pin startPin) {
        this.startPin = startPin;
    }

    public Pin getEndPin() {
        return endPin;
    }

    public void setEndPin(Pin endPin) {
        this.endPin = endPin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(id, edge.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
