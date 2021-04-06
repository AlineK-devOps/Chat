import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//Вспомогательный класс, для чтения и записи в консоль
public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message){ //Выводит сообщение в консоль
        System.out.println(message);
    }

    public static String readString(){ //Считывает строку с консоли
        while (true){
            try{
                return reader.readLine();
            }
            catch (IOException ex){
                System.out.println("Произошла ошибка при попытке ввода текста.\nПопробуйте еще раз.");
            }
        }
    }

    public static int readInt(){ //Считывает число с консоли
        while (true){
            try{
                return Integer.parseInt(readString());
            }
            catch (NumberFormatException ex){
                System.out.println("Произошла ошибка при попытке ввода числа.\nПопробуйте еще раз.");
            }
        }
    }
}
