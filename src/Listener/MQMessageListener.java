package Listener;

import java.time.LocalDateTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MQMessageListener implements IMessageListener<Response,Request> {
    @Override
    public Response OnMessage(String message) {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.create();
        Request req = gson.fromJson(message.toLowerCase(), Request.class);
        return new Response(req, LocalDateTime.now());
    }
}