package client;
import server.Connection;

public class Client {
    protected Connection connection;
    private volatile boolean clientConnected = false; //присоединен ли клиент к серверу

    public class SocketThread extends Thread{ //Поток, устанавливающий сокетное соединение и читающий сообщения с сервера

    }
}
