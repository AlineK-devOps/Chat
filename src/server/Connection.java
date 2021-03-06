package server;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;

//Класс соединения между клиентом и сервером
public class Connection implements Closeable{
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public void send(Message message) throws IOException{ //сериализует сообщение message в out
        synchronized (out){
            out.writeObject(message);
        }
    }

    public Message receive() throws IOException, ClassNotFoundException{ //десериализует данные из in
        synchronized (in){
            return (Message)in.readObject();
        }
    }

    public SocketAddress getRemoteSocketAddress(){ //возвращает удаленный адрес сокетного соединения
        return socket.getRemoteSocketAddress();
    }

    public void close() throws IOException{ //закрывает все ресурсы
        socket.close();
        in.close();
        out.close();
    }
}
