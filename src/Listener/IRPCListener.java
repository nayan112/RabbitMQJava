package Listener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface IRPCListener<T,V> {
    void Listen(String queueName, IMessageListener<T,V> messageListener) throws IOException, TimeoutException;
}
