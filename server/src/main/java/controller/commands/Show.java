package controller.commands;

import model.Repository;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * Класс команды, выводящей команду в консоль
 */

public class Show implements Commandable, Serializable {
    @Override
    public String execute(Object o, String login) {
        try {
            return Repository.getCollectionFromSql();
        } catch (SQLException e) {
            e.printStackTrace();
            return "smth went wrong";
        }
    }

    @Override
    public String getDescription() {
        return ": показать все элементы коллекции";
    }

    @Override
    public String getName() {
        return "show";
    }
}
