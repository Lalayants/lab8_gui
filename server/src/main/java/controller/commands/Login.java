package controller.commands;

import controller.utilities.PasswordEncryptor;
import controller.utilities.TableCreator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login implements Serializable, Commandable {
    @Override
    public String execute(Object o, String login) {
        try {
            String[] data = ((String) o).split(" ");
//            Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kpop");
            Connection c = TableCreator.getConnection();
            String registration = "select * from Users where login = ? AND password = ?";
            PreparedStatement stmt = c.prepareStatement(registration);
            stmt.setString(1, data[0]);
            stmt.setString(2, PasswordEncryptor.generate(data[1]));
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                return "Успешно";
            } else {
                return "Неверный логин или пароль";
            }
        } catch (SQLException e) {
            return "Неверный логин или пароль";
        }
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getName() {
        return "login";
    }
}
