package client;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ClientGuiModel { //модель MVC
    private final Set<String> allUserNames = new HashSet<>(); //список всех участников чата
    private String newMessage; //новое сообщение, которое получил клиент

    public void addUser(String newUserName){ //добавляет имя участника
        allUserNames.add(newUserName);
    }

    public void deleteUser(String userName){ //удаляет имя участника
        allUserNames.remove(userName);
    }

    public Set<String> getAllUserNames() { //запрещает модифицировать возвращаемое множество
        return Collections.unmodifiableSet(allUserNames);
    }

    public String getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }
}
