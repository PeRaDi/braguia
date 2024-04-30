package pt.uminho.braguia.pins;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import pt.uminho.braguia.user.User;
import pt.uminho.braguia.util.ResultConsumer;

public interface PinsService {

    void pins(ResultConsumer<JsonArray> result);

    void pin(int pinNumber, ResultConsumer<JsonObject> result);
}
