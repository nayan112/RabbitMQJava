package MessageServer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface IMessageServer {
void Listen() throws IOException,TimeoutException;
void Listen(String queueName, IMessageEventHandler messageEventHandler) throws IOException, TimeoutException;
}
