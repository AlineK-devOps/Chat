package client;
import server.Connection;
import server.ConsoleHelper;
import server.Message;
import server.MessageType;

import java.io.IOException;

public class Client {
    protected Connection connection;
    private volatile boolean clientConnected = false; //присоединен ли клиент к серверу

    public class SocketThread extends Thread{ //Поток, устанавливающий сокетное соединение и читающий сообщения с сервера

    }

    protected String getServerAddress(){ //запрашивает ввод адреса сервера у пользователя
        ConsoleHelper.writeMessage("Введите адрес сервера:");
        return ConsoleHelper.readString();
    }

    protected int getServerPort(){ //запрашивает ввод порта сервера
        ConsoleHelper.writeMessage("Введите порт сервера:");
        return ConsoleHelper.readInt();
    }

    protected String getUserName(){ //запрашивает имя пользователя
        ConsoleHelper.writeMessage("Введите имя пользователя:");
        return ConsoleHelper.readString();
    }

    protected boolean shouldSendTextFromConsole(){ //в данной реализации клиента мы всегда отправляем текст введенный в консоль
        return true;
    }

    protected SocketThread getSocketThread(){ //создает новый объект класса SocketThread
        return new SocketThread();
    }

    protected void sendTextMessage(String text){ //отправка сообщения серверу
        try {
            connection.send(new Message(MessageType.TEXT, text));
        } catch (IOException exception) {
            ConsoleHelper.writeMessage("Не удалось отправить сообщение");
            clientConnected = false;
        }
    }
}
