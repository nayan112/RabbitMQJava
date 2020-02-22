package MessageServer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.*;

public class MessageServer implements IMessageServer {

    private ConnectionFactory _connection;
    private String QUEUE_NAME;

    public MessageServer() {
        super();
        _connection = new ConnectionFactory();
        _connection.setHost("localhost");
        _connection.setUsername("guest");
        _connection.setPassword("guest");
        _connection.setVirtualHost("/");
        _connection.setPort(5672);
        QUEUE_NAME = "rpc_queue";
    }

    @Override
    public void Listen() throws IOException, TimeoutException {
        try (Connection connection = _connection.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare("rpc_queue", false, false, false, null);
            channel.basicQos(0, 1, false);
            System.out.println(" [x] Awaiting RPC requests");
            // var consumer = new EventingBasicConsumer(channel);
            // channel.BasicConsume("rpc_queue", false, consumer);
            Object monitor = new Object();
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String body = new String(delivery.getBody(), "UTF-8");
                // BasicProperties props = delivery.getProperties();
                // var replyProps = channel.CreateBasicProperties();
                String CorrelationId = delivery.getProperties().getCorrelationId();
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder().correlationId(CorrelationId)
                        .build();
                // replyProps.CorrelationId = props.getCorrelationId();

                String response = "";

                try {
                    // String message = new String(delivery.getBody(), "UTF-8");
                    // int n = Integer.parseInt(message);

                    System.out.println(" [.] Received Message " + body + ")");
                    response += "Message received: " + body + " at " + LocalDateTime.now();
                } catch (RuntimeException e) {
                    System.out.println(" [.] " + e.toString());
                } finally {
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps,
                            response.getBytes("UTF-8"));
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    // RabbitMq consumer worker thread notifies the RPC server owner thread
                    synchronized (monitor) {
                        monitor.notify();
                    }
                }
            };
            channel.basicConsume(QUEUE_NAME, false, deliverCallback, (consumerTag -> {
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

    @Override
    public void Listen(String queueName, IMessageEventHandler messageEventHandler)
            throws IOException, TimeoutException {
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
                    response = messageEventHandler.OnMessage(body);
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
