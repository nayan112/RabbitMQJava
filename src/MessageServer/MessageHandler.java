package MessageServer;

import java.time.LocalDateTime;

public class MessageHandler implements IMessageEventHandler
{

    @Override
    public String OnMessage(String message) {
        return "Message received: " + message + " at " + LocalDateTime.now();

    }
    
}