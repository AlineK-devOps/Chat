import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Основной класс сервера
public class Server {

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