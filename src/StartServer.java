import java.io.IOException;
import java.util.concurrent.TimeoutException;

import Listener.*;
import MessageServer.*;


public class StartServer {
    public static void main(final String[] args) throws IOException, TimeoutException {
        /* IMessageServer server = new MessageServer();
        IMessageEventHandler messageHandler = new MessageHandler();
        server.Listen("rpc_queue", messageHandler); */

        IRPCListener<Response, Request> server = new RPCListener();
        IMessageListener<Response, Request> messageListener = new MQMessageListener();
        server.Listen("rpc_queue", messageListener); 
    }
}