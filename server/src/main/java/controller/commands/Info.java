package controller.commands;

import controller.utilities.TableCreator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс команды, выводящей справку по коллекции
 */
public class Info implements Serializable, Commandable {

    @Override
    public String execute(Object o, String login) {

        try {
            Connection c = TableCreator.getConnection();
            String CreateInSql = "select count(*) from labworks";
            PreparedStatement stmt = c.prepareStatement(CreateInSql);
            ResultSet res = stmt.executeQuery();
            res.next();
            return "В коллекции " + Integer.toString(res.getInt(1)) + " элементов";
        } catch (SQLException e) {
            e.printStackTrace();
            return "что-то пошло не так";
        }

    }

    @Override
    public String getDescription() {
        return ": вывести в стандартный поток вывода информацию о коллекции";
    }

    @Override
    public String getName() {
        return "info";
    }
}
