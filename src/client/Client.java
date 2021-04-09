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
        protected void processIncomingMessage(String message){ //Выводит текст message в консоль
            ConsoleHelper.writeMessage(message);
        }

        protected void informAboutAddingNewUser(String userName){ //выводит информацию о том. что участник userName присоединился к чату
            ConsoleHelper.writeMessage(String.format("%s присоединился к чату", userName));
        }

        protected void informAboutDeletingNewUser(String userName){ //выводит информацию о том. что участник userName покинул чат
            ConsoleHelper.writeMessage(String.format("%s покинул чат", userName));
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected){ //оповещает основной поток о соединении
            Client.this.clientConnected = clientConnected;
            synchronized (Client.this){
                Client.this.notify();
            }
        }
    }

    public void run(){
        SocketThread socketThread = getSocketThread();
        socketThread.setDaemon(true); //при выходе из программы вспомогательный поток прервется автоматически
        socketThread.start();

        synchronized (this){
            try {
                wait(); //ожидаем, пока вспомогательный поток установит соединение с сервером
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage("Не удалось установить соединение с сервером.");
                return;
            }
        }

        if (clientConnected)
            ConsoleHelper.writeMessage("Соединение установлено.\nДля выхода наберить команду 'exit'.");
        else
            ConsoleHelper.writeMessage("Произошла ошибка во время работы клиента.");

        while (clientConnected){
            String message = ConsoleHelper.readString();//Считываем сообщения с консоли, пока не введено слово "exit" и подключение стабильно
            if (message.equals("exit")) break;
            if (shouldSendTextFromConsole())
                sendTextMessage(message); //отправляем сообщение серверу
        }
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

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}
