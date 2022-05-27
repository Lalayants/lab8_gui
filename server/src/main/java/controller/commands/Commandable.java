package controller.commands;

// Creating an interface called controller.commands.entity.Commandable.
public interface Commandable{

    Object execute(Object o, String login);
    String getDescription();
    String getName();

}
