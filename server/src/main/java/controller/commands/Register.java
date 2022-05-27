package controller.commands;

import controller.utilities.PasswordEncryptor;
import controller.utilities.TableCreator;
import requests.Response;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * It implements the controller.commands.entity.Commandable interface, which means that it has a method called execute, which takes an object and a
 * string as parameters and returns a string
 */

public class Register implements Serializable, Commandable {
    @Override
    public Object execute(Object o, String login) {
        Response response =  new Response("");
        response.setCommand("register");
        try {
            String [] data = ((String) o) .split(" ");
            Connection c = TableCreator.getConnection();
            //Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kpop");
            String registration = "INSERT INTO Users(login, password)\n" +
                    "VALUES (?, ?);";
            PreparedStatement stmt =  c.prepareStatement(registration);
            stmt.setString(1, data[0]);
            stmt.setString(2, PasswordEncryptor.generate(data[1]));
            stmt.executeUpdate();
            String CreateInSql = "select id from users where login = ?";
            stmt = c.prepareStatement(CreateInSql);
            stmt.setString(1, data[0]);
            ResultSet res = stmt.executeQuery();
            res.next();
            Integer id = res.getInt("id");
             response.setAnswer(id.toString());
            return response;
        } catch (SQLException e) {
            response.setAnswer("Такой пользователь уже есть в системе, попробуйте другой логин");
            return response;
        }
    }

    @Override
    public String getDescription() {
        return " : зарегистрироваться в системе(логин и пароль вводятся через пробелы";
    }

    @Override
    public String getName() {
        return "register";
    }

}

