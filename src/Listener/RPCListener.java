package Listener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.*;

public class RPCListener<T, V> implements IRPCListener<T, V> {

    private ConnectionFactory _connection;
    public RPCListener() {
        super();
        _connection = new ConnectionFactory();
        _connection.setHost("localhost");
        _connection.setUsername("guest");
        _connection.setPassword("guest");
        _connection.setVirtualHost("/");
        _connection.setPort(5672);
    }

    @Override
    public void Listen(String queueName, IMessageListener<T,V> messageListener) throws IOException, TimeoutException {
        try (Connection connection = _connection.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(queueName, false, false, false, null);
            channel.basicQos(0, 1, false);
            System.out.println(" [x] Awaiting RPC requests");
            Object monitor = new Object();
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String body = new String(delivery.getBody(), "UTF-8");
                String CorrelationId = delivery.getProperties().getCorrelationId();
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder().correlationId(CorrelationId).build();

                String response = "";

                try {
                    System.out.println(" [.] Received Message " + body + ")");
                    Gson gson = new Gson();
                    //V req = gson.fromJson(body,V.class);
                    T res = messageListener.OnMessage(body);
                    response = gson.toJson(res);
                } catch (RuntimeException e) {
                    System.out.println(" [.] " + e.toString());
                } finally {
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes("UTF-8"));
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    // RabbitMq consumer worker thread notifies the RPC server owner thread
                    synchronized (monitor) {
                        monitor.notify();
                    }
                }
            };
            channel.basicConsume(queueName, false, deliverCallback, (consumerTag -> {
            }));
            // Wait and be prepared to consume the message from RPC client.
            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
