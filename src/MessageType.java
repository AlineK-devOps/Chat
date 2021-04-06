//Enum, который отвечает за тип сообщений пересылаемых между клинетом и сервером
public enum MessageType {
    NAME_REQUEST, //Запрос имени
    USER_NAME, //Имя пользователя
    NAME_ACCEPTED, //Имя принято
    TEXT, //Текстовое сообщение
    USER_ADDED, //Пользователь добавлен
    USER_REMOVED //Пользователь удален
}
