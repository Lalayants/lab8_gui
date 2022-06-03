package view;

import java.io.Console;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Класс для чтения и выводы в консоль
 */
public class ConsoleIO {
    private static Scanner in = new Scanner(System.in);

    public static void ConsoleOut(String s) {
        System.out.println(s);
    }

    public static String ConsoleIn() {
        System.out.print("> ");
        try {
            String s = in.nextLine();
            return s.trim();
        } catch (NoSuchElementException e) {
            System.out.println("Такая комбинация завершает программу");
            System.exit(0);
            //in = new Scanner(System.in);
            return "";

        }
        //return "";
    }

    public static String ConsoleIn(Scanner a) {
        System.out.print("> ");
        try {
            String s = a.nextLine();
            if (s != null)
            return s;
            else
                return "";
        } catch (NoSuchElementException e) {
            //System.out.println("Такая комбинация завершает программу");
            //System.exit(0);

        }
        return "";
    }
    public static String ReadPassword(){
        Console console = System.console();
        char[] password = console.readPassword();
        return new String(password);
    }
}
