package pt.uminho.braguia.pins;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PinAdditionalData extends PinData{
    private String audioUrl = "";
    private String videoUrl = "";
    private float pin_lat;
    private float pin_lng;
    private float pin_alt;
    @Override
    public void setItemData(JsonObject obj) {
        super.setItemData(obj);
        setPinLat(obj.get("pin_lat").getAsFloat());
        setPinLng(obj.get("pin_lng").getAsFloat());
        setPinAlt(obj.get("pin_alt").getAsFloat());
        JsonArray media = obj.get("media").getAsJsonArray();
        for (JsonElement file : media) {
            JsonObject mediaObject = file.getAsJsonObject();
            if (mediaObject.get("media_type").getAsString().equals("R")) {
                setAudio(mediaObject.get("media_file").getAsString());
            } else if (mediaObject.get("media_type").getAsString().equals("V")) {
                setVideo(mediaObject.get("media_file").getAsString());
            }
        }
    }


    public String getAudio() {
        return audioUrl;
    }
    public void setAudio(String audioUrl) {
        this.audioUrl = audioUrl;
    }
    public String getVideo() {
        return videoUrl;
    }
    public void setVideo(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public float getPinLat() {
        return pin_lat;
    }
    public void setPinLat(float pin_lat) { this.pin_lat = pin_lat; }
    public float getPinLng() {
        return pin_lng;
    }
    public void setPinLng(float pin_lng) {
        this.pin_lng = pin_lng;
    }
    public float getPinAlt() {
        return pin_alt;
    }
    public void setPinAlt(float pin_alt) {
        this.pin_alt = pin_alt;
    }

}
