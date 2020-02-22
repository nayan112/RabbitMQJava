package Listener;

import java.time.LocalDateTime;

public class MessageListener implements IMessageListener
{

    @Override
    public String OnMessage(String message) {
        return "Message received: " + message + " at " + LocalDateTime.now();

    }
    
}