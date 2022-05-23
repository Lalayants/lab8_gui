package controller.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetterFromSql {
    public static String getCollectionFromSql(Connection c, String createInSql) throws SQLException {
        PreparedStatement stmt = c.prepareStatement(createInSql);
        ResultSet res = stmt.executeQuery();
        String anwser = "";
        boolean isEmpty = true;
        while (res.next()) {
            isEmpty = false;
            anwser += "\n========================" +
                    "\nЛабораторная работа:" +
                    "\n   Название: " + res.getString("name") +
                    "\n   id: " + res.getInt("id") +
                    "\n   Координаты: x = " + res.getFloat("x") + " y = " + res.getInt("y") +
                    "\n   Дата создания: " + res.getTimestamp("creation_date") +
                    "\n   minimalPoint: " + res.getFloat("minimalpoint") +
                    "\n   personalQualitiesMinimum: " + res.getInt("personalqualitiesminimum") +
                    "\n   Сложность: " + res.getString("difficulty") +
                    "\nАвтор: "  +
                    "\n   Имя: " + res.getString("author_name") +
                    "\n   Вес: " + res.getFloat("weight") +
                    "\n   Дата рождения: ";
            try {
                anwser += res.getDate("author_birthday");
            } catch (SQLException e) {
                anwser += "не указана";
            } finally {
                anwser += "\n   Цвет глаз: ";
            }
            try {
                String eye_color = res.getString("eye_color");
                if (eye_color != null)
                    anwser += res.getString("eye_color");
                else
                    anwser += "не указан";
            } catch (SQLException e) {
                anwser += "не указан";
            } finally {
                anwser += "\n========================";
            }
        }
        if (!isEmpty)
            return anwser;
        else
            return "========================\n\n" + "Коллекция пуста\n\n" + "========================\n";
    }
}
