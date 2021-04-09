package client;

public class BotClient extends Client{

    public class BotSocketThread extends SocketThread{

    }

    @Override
    protected SocketThread getSocketThread() { //создает новый объект класса BotSocketThread
        return new BotSocketThread();
    }

    @Override
    protected boolean shouldSendTextFromConsole() { //в данной реализации клиента мы не отправляем текст введенный в консоль
        return false;
    }

    @Override
    protected String getUserName() { //возвращает случайное имя бота
        return String.format("date_bot_%s", (int)(100*Math.random()));
    }

    public static void main(String[] args) {
        BotClient botClient = new BotClient();
        botClient.run();
    }
}
