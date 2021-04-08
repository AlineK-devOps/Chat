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

        @Override
        public void run() { //метод, отвечающий за обработку сообщений
            String userName = null;
            try (Connection connection = new Connection(socket)){
                ConsoleHelper.writeMessage(String.format("Соединение с %s установлено.", socket.getRemoteSocketAddress()));

                userName = serverHandshake(connection); //знакомимся с пользователем
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, userName)); //рассылаем всем пользователям имя присоединившегося участника
                notifyUsers(connection, userName); //сообщаем новому участнику о существуюших
                serverMainLoop(connection, userName); //запускаем главный цикл обработки сообщений сервером

            } catch (IOException | ClassNotFoundException exception) {
                ConsoleHelper.writeMessage(String.format("Произошла ошибка при обмене данными с удаленным сервером %s", socket.getRemoteSocketAddress()));
            }
            finally { //соединение разорвано
                if (userName != null){
                    connectionMap.remove(userName); //удаляем пользовательское соединение из connectionMap
                    sendBroadcastMessage(new Message(MessageType.USER_REMOVED, userName)); //рассылаем всем пользователям, что пользователь удален
                }
                ConsoleHelper.writeMessage(String.format("Соединение с удалённым адресом %s закрыто.", socket.getRemoteSocketAddress()));
            }
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException { //Знакомство сервера с клиентом
            while (true){
                connection.send(new Message(MessageType.NAME_REQUEST)); //сообщение с запросом имени клиента

                Message clientMessage = connection.receive(); //получаем соощение с именем от клиента
                if (clientMessage.getType() != MessageType.USER_NAME){
                    ConsoleHelper.writeMessage(String.format("Получено сообщение от %s. Тип сообщения не соответсвует протоколу.", connection.getRemoteSocketAddress()));
                    continue;
                }

                String userName = clientMessage.getData(); //получаем имя пользователя
                if (userName.isEmpty()){
                    ConsoleHelper.writeMessage(String.format("Попытка подключения к серверу с пустым именем от %s.", connection.getRemoteSocketAddress()));
                    continue;
                }

                if (connectionMap.containsKey(userName)){
                    ConsoleHelper.writeMessage(String.format("Попытка подключения к серверу с уже используемым именем от %s.", connection.getRemoteSocketAddress()));
                    continue;
                }

                connectionMap.put(userName, connection); //добавляем нового пользователя
                connection.send(new Message(MessageType.NAME_ACCEPTED)); //отправить, что имя принято
                return userName;
            }
        }

        private void notifyUsers(Connection connection, String userName) throws IOException{ //Отправка новому участнику информации об остальных участниках
            for (String otherName : connectionMap.keySet()){
                if (!userName.equals(otherName))
                connection.send(new Message(MessageType.USER_ADDED, otherName));
            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException { //Главный цикл обработки сообщений сервером
            while (true){
                Message message = connection.receive();
                if (message.getType() == MessageType.TEXT)
                    sendBroadcastMessage(new Message(MessageType.TEXT, String.format("%s: %s", userName, message.getData())));
                else
                    ConsoleHelper.writeMessage(String.format("Получено сообщение от %s. Тип сообщения не соответсвует протоколу.", connection.getRemoteSocketAddress()));
            }
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