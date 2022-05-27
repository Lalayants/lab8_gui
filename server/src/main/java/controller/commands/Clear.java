package controller.commands;

import java.io.Serializable;
import model.Repository;
import requests.Response;

/**
 * Класс команды очистки коллекции
 */

/**
 * `controller.commands.Clear` is a class that implements the `Serializable` and `controller.commands.entity.Commandable` interfaces
 */
public class Clear implements Serializable, Commandable {
    @Override
    public Response execute(Object o, String login) {
        return Repository.clearUsersLabworks(login);

//        try {
//            Connection c = TableCreator.getConnection();
////            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kpop");
//            String CreateInSql = "delete from labworks\n" +
//                    "where login_id = ?;";
//            PreparedStatement stmt = c.prepareStatement(CreateInSql);
//            stmt.setInt(1, GetLoginId.getIdOfLogin(login));
//            stmt.executeUpdate();
//            return "Из базы удалены все ваши записи";
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return "что-то пошло не так";
//        }

    }

//    @Override
//    public String execute(Object o, String login) {
//        labcollection.clear();
//        return "Коллекция очищена";
//    }

    @Override
    public String getDescription() {
        return ": очищает коллекцию от элементов добавленных вами";
    }

    @Override
    public String getName() {
        return "clear";
    }
}
