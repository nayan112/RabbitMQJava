package Listener;

public abstract class MessageListener<T,V> implements IMessageListener<T,V> {
    T t;
    @Override
    public T OnMessage(Object message) {
        return t;//"Message received: at " + LocalDateTime.now();

    }

}