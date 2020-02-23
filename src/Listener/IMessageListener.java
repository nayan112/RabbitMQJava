package Listener;

public interface IMessageListener<T,V> {
    public T OnMessage(String message);
}