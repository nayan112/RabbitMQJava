package Listener;

public interface IMessageListener<T,V> {
    public T OnMessage(Object message);
    //public T OnMessage(V message);
    public T OnMessage(String message);
}