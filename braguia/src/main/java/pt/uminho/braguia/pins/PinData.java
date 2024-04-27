package pt.uminho.braguia.pins;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PinData {
    private String name ;
    private String description;
    private String imageUrl = "";
    private int id;

    public void setItemData(JsonObject obj) {
        setId(obj.get("id").getAsInt());
        setName(obj.get("pin_name").getAsString());
        setDescription(obj.get("pin_desc").getAsString());
        JsonArray media = obj.get("media").getAsJsonArray();
        for (JsonElement file : media) {
            JsonObject mediaObject = file.getAsJsonObject();
            if (mediaObject.get("media_type").getAsString().equals("I")) {
                setImage(mediaObject.get("media_file").getAsString());
            }
        }
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
    public String getImage() {
        return imageUrl;
    }
    public void setImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

}
