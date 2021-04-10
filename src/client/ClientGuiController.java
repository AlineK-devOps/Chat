package client;

public class ClientGuiController extends Client{ //Controller в MVC
    private ClientGuiModel model = new ClientGuiModel();
    private ClientGuiView view = new ClientGuiView(this);

    public class GuiSocketThread extends SocketThread{ //Поток, устанавливающий сокетное соединение и читающий сообщения с сервера
        @Override
        protected void processIncomingMessage(String message) { //Выводит текст message
            model.setNewMessage(message);
            view.refreshMessages();
        }

        @Override
        protected void informAboutAddingNewUser(String userName) { //выводит информацию о том. что участник userName присоединился к чату
            model.addUser(userName);
            view.refreshUsers();
        }

        @Override
        protected void informAboutDeletingNewUser(String userName) { //выводит информацию о том. что участник userName покинул чат
            model.deleteUser(userName);
            view.refreshUsers();
        }

        @Override
        protected void notifyConnectionStatusChanged(boolean clientConnected) { //оповещает основной поток о соединении
            view.notifyConnectionStatusChanged(clientConnected);
        }
    }

    @Override
    public void run() {
        getSocketThread().run();
    }

    @Override
    protected SocketThread getSocketThread() { //создает новый объект класса GuiSocketThread
        return new GuiSocketThread();
    }

    @Override
    protected String getServerAddress() { //запрашивает ввод адреса сервера у пользователя
        return view.getServerAddress();
    }

    @Override
    protected int getServerPort() { //запрашивает ввод порта сервера у пользователя
        return view.getServerPort();
    }

    @Override
    protected String getUserName() { //запрашивает ввод имени у пользователя
        return view.getUserName();
    }

    public ClientGuiModel getModel(){ //возращает модель
        return model;
    }

    public static void main(String[] args) {
        ClientGuiController controller = new ClientGuiController();
        controller.run();
    }
}
