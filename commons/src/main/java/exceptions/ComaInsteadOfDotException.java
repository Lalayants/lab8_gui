package exceptions;

public class ComaInsteadOfDotException extends Exception{
    public ComaInsteadOfDotException(){
        super("Try again");
        System.out.println("Для ввода десятичных чисел используйте \'.\', а не \',\'");
    }
}
