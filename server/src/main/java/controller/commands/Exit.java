package controller.commands;

import java.io.Serializable;

public class Exit implements Serializable, Commandable {
    @Override
    public String execute(Object o, String login){
        System.out.println("Завершение работы...");
        System.exit(0);
        return null;
    }

    @Override
    public String getDescription() {
        return ": завершить программу";
    }

    @Override
    public String getName() {
        return "exit";
    }
}
