import java.io.IOException;
import java.util.concurrent.TimeoutException;

import Listener.*;
import MessageServer.*;


public class StartServer {
    public static void main(final String[] args) throws IOException, TimeoutException {
        /* IMessageServer server = new MessageServer();
        IMessageEventHandler messageHandler = new MessageHandler();
        server.Listen("rpc_queue", messageHandler); */

        IRPCListener server = new RPCListener();
        IMessageListener messageListener = new MessageListener();
        server.Listen("rpc_queue", messageListener); 
    }
}