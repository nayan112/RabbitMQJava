package Listener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface IRPCListener {
void Listen(String queueName, IMessageListener messageListener) throws IOException, TimeoutException;
}
