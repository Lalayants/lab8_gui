package controller.commands;

import model.Repository;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * Класс команды, выводящий коллекцию в порядке уменьшения
 */

public class PrintDescending implements Serializable, Commandable {
    @Override
    public String execute(Object o, String login) {
        try {
            return Repository.getCollectionFromSqlDescending();
        } catch (SQLException e) {
            e.printStackTrace();
            return "smth went wrong";
        }
    }


//        try {
//            Connection c = TableCreator.getConnection();
//            //c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kpop");
//            String CreateInSql = "select * from labworks order by name desc";
//            return GetterFromSql.getCollectionFromSql(c, CreateInSql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return "ZZZZZZ пуста";
//        }
//    }

    @Override
    public String getDescription() {
        return ": вывести элементы коллекции в порядке убывания";
    }

    @Override
    public String getName() {
        return "print_descending";
    }
}
