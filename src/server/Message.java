package server;

import java.io.Serializable;

//Класс, отвечающий за пересылаемые сообщения
public class Message implements Serializable {
    private final MessageType type; //Тип сообщения
    private final String data; //Данные сообщения

    public Message(MessageType type){
        this.type = type;
        this.data = null;
    }

    public Message(MessageType type, String data){
        this.type = type;
        this.data = data;
    }

    public MessageType getType() { //Получить тип сообщения
        return type;
    }

    public String getData() { //Получить данные сообщения
        return data;
    }
}
