package client;

import server.ConsoleHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BotClient extends Client{

    public class BotSocketThread extends SocketThread{
        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException { //отправка сообщения серверу
            BotClient.this.sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды.");
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(String message){ //Выводит текст message в консоль
            ConsoleHelper.writeMessage(message); // Выводим текст сообщения в консоль

            String[] split = message.split(": ");// Отделяем отправителя от текста сообщения
            if (split.length != 2) return;

            String messageWithoutUserName = split[1];

            String format = null; // Подготавливаем формат для отправки даты согласно запросу
            switch (messageWithoutUserName) {
                case "дата":
                    format = "d.MM.YYYY";
                    break;
                case "день":
                    format = "d";
                    break;
                case "месяц":
                    format = "MMMM";
                    break;
                case "год":
                    format = "YYYY";
                    break;
                case "время":
                    format = "H:mm:ss";
                    break;
                case "час":
                    format = "H";
                    break;
                case "минуты":
                    format = "m";
                    break;
                case "секунды":
                    format = "s";
                    break;
            }

            if (format != null) {
                String answer = new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
                BotClient.this.sendTextMessage(String.format("Информация для %s: %s", split[0], answer));
            }
        }
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
