package entity;

// Creating an interface called entity.Commandable.
public interface Commandable {

    String execute(Object o, String login);
    String getDescription();
    String getName();

}
