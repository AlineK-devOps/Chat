import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//Основной класс сервера
public class Server {
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>(); //коллекция соединений, ключ - имя клиента, значение - соединение с ним

    public static void sendBroadcastMessage(Message message){ //отправка сообщения всем соединениям
        try{
            for (Connection connection : connectionMap.values()){
                connection.send(message); //отправка сообщения в out
            }
        }
        catch (IOException ex){
            ConsoleHelper.writeMessage("Произошла ошибка! Не удалось отправить сообщение");
        }
    }

    private static class Handler extends Thread{ //поток обработчик, в котором происходит обмен сообщениями с клиентом
        private Socket socket;

        public Handler(Socket socket){
            this.socket = socket;
        }
    }

    public static void main(String[] args) {
        ConsoleHelper.writeMessage("Введите порт сервера: ");
        int port = ConsoleHelper.readInt(); //считываем порт
        Socket clientSocket;

        try (ServerSocket serverSocket = new ServerSocket(port)){ //запускаем сервер
            ConsoleHelper.writeMessage("Сервер запущен");

            while(true){ //принимаем входящие сокетные соединения и запускаем новый поток Handler, если соединение установлено
                clientSocket = serverSocket.accept(); //ждём, пока к нам не захотят присоединиться
                new Handler(clientSocket).start(); //создаём обработчик соединения
            }

        } catch (Exception exception) {
            ConsoleHelper.writeMessage("Произошла ошибка соединения");
            ConsoleHelper.writeMessage(exception.getMessage());
        }
    }
}