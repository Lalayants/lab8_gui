package controller.commands;

import controller.utilities.PasswordEncryptor;
import controller.utilities.TableCreator;
import requests.Response;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login implements Serializable, Commandable {
    @Override
    public Object execute(Object o, String login) {
        Response response = new Response("");
        response.setCommand("login");
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
                String CreateInSql = "select id from users where login = ?";
                stmt = c.prepareStatement(CreateInSql);
                stmt.setString(1, data[0]);
                res = stmt.executeQuery();
                res.next();
                Integer id = res.getInt("id");
                response.setAnswer(id.toString());
                return response;
            } else {
                response.setAnswer("Неверный логин или пароль");
                return response;
            }
        } catch (SQLException e) {
            return new Response("Неверный логин или пароль");
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
