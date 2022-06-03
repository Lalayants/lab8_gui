package model;

import controller.commands.Add;
import entity.LabWorkDTO;
import controller.utilities.GetLoginId;
import controller.utilities.TableCreator;
import requests.Response;

import java.sql.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class Repository {
    public static Response add(LabWorkDTO a, String login) {
        try {
            LabWorkDTO lab = a;
            a.setCreators_id(Integer.parseInt(login));
            a.setCreationDate(Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
            Connection c = TableCreator.getConnection();
            String CreateInSql = "INSERT INTO LabWorks(name, x, y, creation_date, minimalPoint, personalQualitiesMinimum, difficulty, author_name,\n" +
                    "                     birthday, weight, eye_color, login_id)\n" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,\n" +
                    "        ?, ?);";
            PreparedStatement stmt = c.prepareStatement(CreateInSql);
            stmt.setString(1, lab.getName());
            stmt.setDouble(2, Double.parseDouble(String.valueOf(lab.getX())));
            stmt.setLong(3, lab.getY());
            stmt.setTimestamp(4, a.getCreationDate());
            stmt.setDouble(5, lab.getMinimalPoint());
            stmt.setInt(6, lab.getPersonalQualitiesMinimum());
            stmt.setString(7, lab.getDifficulty());
            stmt.setString(8, lab.getAuthor());
            try {
                stmt.setDate(9, Date.valueOf(lab.getBirthday()));
            } catch (NullPointerException e) {
                stmt.setDate(9, null);
            }
            stmt.setFloat(10, lab.getWeight());
            try {
                stmt.setString(11, lab.getEyeColor());
            } catch (NullPointerException e) {
                stmt.setDate(11, null);
            }
            stmt.setInt(12, Integer.parseInt(login));
            stmt.executeUpdate();

            stmt = c.prepareStatement("SELECT id\n" +
                    "FROM labworks\n" +
                    "WHERE creation_date = (\n" +
                    "    SELECT MAX(creation_date)\n" +
                    "    FROM labworks\n" +
                    ");");
            ResultSet res = stmt.executeQuery();
            res.next();
            a.setId(res.getInt("id"));
            Response r = new Response("AddSuccess");
            r.setForUpdate();
            r.setCommand("add");
            r.setObjectAnswer(a);
            return r;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Response r = new Response("Что-то пошло не так");
        return r;
    }

    public static Response addIfMin(LabWorkDTO a, String login) {
        Response response = new Response("AddIfMinAnswerNotMin");
        response.setCommand("add_if_min");
        try {
            Connection c = TableCreator.getConnection();
            String CreateInSql = "select name from labworks order by name limit 1";
            PreparedStatement stmt = c.prepareStatement(CreateInSql);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                if (res.getString("name").compareTo(a.getName()) <= 0)
                    return response;
            }
            return new Add().execute(a, login);

        } catch (SQLException e) {
            e.printStackTrace();
            return new Response("что-то пошло не так");
        }

    }

    public static Response clearUsersLabworks(String login) {
        Response response = new Response("");
        response.setCommand("clear");
        try {
            Connection c = TableCreator.getConnection();
            ArrayList<Integer> toDelete = new ArrayList<>();
            String CreateInSql = "select id from labworks\n" +
                    "where login_id = ?;";
            PreparedStatement stmt = c.prepareStatement(CreateInSql);
            stmt.setInt(1, Integer.parseInt(login));
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                toDelete.add(res.getInt("id"));
            }
            if (!toDelete.isEmpty())
                response.setForUpdate();
            response.setObjectAnswer(toDelete);
            CreateInSql = "delete from labworks\n" +
                    "where login_id = ?;";
            c = TableCreator.getConnection();
            stmt = null;
            try {
                stmt = c.prepareStatement(CreateInSql);
                stmt.setInt(1, Integer.parseInt(login));
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return response;
        } catch (SQLException e) {
            e.printStackTrace();
            response.setAnswer("что-то пошло не так");
            return response;
        }
    }

    public static Response CountLessThanMinimalPoint(double min) {
        Response r = new Response("");
        r.setCommand("count_less_than_minimal_point");
        try {
            Connection c = TableCreator.getConnection();
//            Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kpop");
            String CreateInSql = "select minimalpoint from labworks " +
                    "where minimalpoint<?;";
            PreparedStatement stmt = c.prepareStatement(CreateInSql);
            stmt.setDouble(1, min);
            ResultSet res = stmt.executeQuery();
            Integer counter = 0;
            while (res.next()) {
                counter += 1;
            }
            r.setAnswer(counter.toString());
            return r;
        } catch (SQLException e) {
            return new Response("Что-то пошло не так");
        }
    }

    public static Response getCollectionFromSql() throws SQLException {
        ArrayList<LabWorkDTO> list = new ArrayList<>();
        String CreateInSql = "select * from labworks order by name";
        PreparedStatement stmt = null;
        try {
            stmt = TableCreator.getConnection().prepareStatement(CreateInSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet res = null;
        try {
            res = stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (true) {
            assert res != null;
            if (!res.next()) break;
            LabWorkDTO lab = new LabWorkDTO();
            lab.setName(res.getString("name"));
            lab.setId(res.getInt("id"));
            lab.setX(res.getFloat("x"));
            lab.setY(res.getLong("y"));
            lab.setCreationDate(res.getTimestamp("creation_date"));
            lab.setMinimalPoint(res.getFloat("minimalpoint"));
            lab.setPersonalQualitiesMinimum(res.getInt("personalqualitiesminimum"));
            lab.setDifficulty(res.getString("difficulty"));
            lab.setAuthor(res.getString("author_name"));
            lab.setWeight(res.getFloat("weight"));
            lab.setCreators_id(res.getInt("login_id"));
            try {
                System.out.println(res.getDate("author_birthday"));
                lab.setBirthday(res.getDate("author_birthday").toLocalDate());
            } catch (SQLException ignored) {
            }
            try {
                lab.setEyeColor(res.getString("eye_color"));
            } catch (SQLException ignored) {
            }
            list.add(lab);
        }
        Response response = new Response("Успешно");
        response.setCommand("show");
        response.setObjectAnswer(list);
        return response;
    }

    public static String getCollectionFromSqlDescending() throws SQLException {
        Connection с = TableCreator.getConnection();
        String CreateInSql = "select * from labworks order by name desc";
        PreparedStatement stmt = null;
        try {
            stmt = TableCreator.getConnection().prepareStatement(CreateInSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet res = null;
        try {
            res = stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                    "\nАвтор: " +
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

    public static Response getUniqueMinimalPoint() {
        Response response = new Response("getUniqueMinimalPointAnswerEmpty");
        response.setCommand("print_unique_minimal_point");
        try {
            Connection c = TableCreator.getConnection();
            String CreateInSql = "select minimalpoint from labworks"; //distinct
            PreparedStatement stmt = c.prepareStatement(CreateInSql);
            ResultSet res = stmt.executeQuery();
            ArrayList<Integer> a = new ArrayList();
            while (res.next()) {
                int k = res.getInt(1);
                if (!a.contains(k))
                    a.add(k);
            }
            if (a.size() != 0) {
                response.setAnswer(a.toString().replace("[", "").replace("]", ""));
                return response;
            }
            else
                return response;
        } catch (SQLException e) {
            return response;
        }
    }

    public static Response removeById(int id, String login) {
        try {
            Response response;
            Connection c = TableCreator.getConnection();
            String CreateInSql = "select id from labworks " +
                    "where id = ? and login_id = ?";
            PreparedStatement stmt = c.prepareStatement(CreateInSql);
            stmt.setInt(1, id);
            stmt.setInt(2, Integer.parseInt(login));
            ResultSet res = stmt.executeQuery();
            res.next();
            if (res.getInt("id") > 0) {
                CreateInSql = "delete from labworks\n" +
                        "where id = ?;";
                stmt = c.prepareStatement(CreateInSql);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                response = new Response(Integer.toString(id));
                response.setForUpdate();
                response.setCommand("remove_by_id");
                response.setObjectAnswer(Integer.toString(id));
                return response;

            } else
                return new Response("Нет элемента с = " + id);
        } catch (SQLException a) {
            return new Response("Элемента с таким id нет или он создан не вами");
        }
    }

    public static String removeFirst(String login) {
        try {
            Connection c = TableCreator.getConnection();
            String CreateInSql = "select id from labworks  \n" +
                    "where login_id = ?";
            PreparedStatement stmt = c.prepareStatement(CreateInSql);
            stmt.setInt(1, GetLoginId.getIdOfLogin(login));
            ResultSet res = stmt.executeQuery();
            ArrayList<Integer> a = new ArrayList();
            while (res.next()) {
                a.add(res.getInt(1));
            }
            a.sort(null);
            if (a.isEmpty())
                return "Коллекция пуста или в ней нет элементов доступных для удаления";
            CreateInSql = "delete from labworks" +
                    " where id = " + a.get(0) + " ;";
            stmt = c.prepareStatement(CreateInSql);
            stmt.executeUpdate();
            return "Элемент удален";
        } catch (SQLException e) {
            return "В коллекции уже не осталось элементов, созданных вами";
        }
    }

    public static Response removeLower(LabWorkDTO labWork, String login) {
        Response response = new Response("removeLowerAnswerNoElements");
        response.setCommand("remove_lower");
        try {
            Connection c = TableCreator.getConnection();
            String CreateInSql = "select id from labworks " +
                    "where name < ? and login_id = ?";
            PreparedStatement stmt = c.prepareStatement(CreateInSql);
            stmt.setString(1, labWork.getName());
            stmt.setInt(2, Integer.parseInt(login));
            ResultSet res = stmt.executeQuery();
            ArrayList<Integer> ids = new ArrayList<>();
            while(res.next()){
                int id = res.getInt("id");
                ids.add(id);
                CreateInSql = "delete from labworks\n" +
                        "where id = ?;";
                stmt = c.prepareStatement(CreateInSql);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                response.setAnswer("removeLowerAnswerSuccess");
                response.setForUpdate();
            }
            response.setObjectAnswer(ids);
            return response;
        } catch (SQLException a) {
            return new Response("Элемента с таким id нет или он создан не вами");
        }
    }

    public static Response updateID(LabWorkDTO lab, String login) {
        try {
            Response response;
            Connection c = TableCreator.getConnection();
            String CreateInSql = "UPDATE labworks SET name = ?, x= ?, y= ?, creation_date= ?, minimalPoint= ?, personalQualitiesMinimum= ?, difficulty= ?, author_name= ?, weight= ?, eye_color= ? WHERE id = ? and login_id = ?;;";
            PreparedStatement stmt = c.prepareStatement(CreateInSql);
            stmt.setString(1, lab.getName());
            stmt.setDouble(2, Double.parseDouble(String.valueOf(lab.getX())));
            stmt.setLong(3, lab.getY());
            stmt.setTimestamp(4, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
            stmt.setDouble(5, lab.getMinimalPoint());
            stmt.setInt(6, lab.getPersonalQualitiesMinimum());
            stmt.setString(7, lab.getDifficulty().toString());
            stmt.setString(8, lab.getName());
//            try {
//                stmt.setDate(9, Date.valueOf(lab.getBirthday()));
//            } catch (NullPointerException e) {
//                stmt.setDate(9, null);
//            }
            stmt.setFloat(9, lab.getWeight());
            try {
                stmt.setString(10, lab.getEyeColor());
            } catch (NullPointerException e) {
                stmt.setDate(10, null);
            }
            stmt.setInt(11, lab.getId());
            stmt.setInt(12, Integer.parseInt(login));
            stmt.executeUpdate();
            response = new Response("Подходящий элемент обновлен");
            response.setForUpdate();
            lab.setCreators_id(Integer.parseInt(login));
            response.setCommand("update");
            response.setObjectAnswer(lab);
            return response;
        } catch (SQLException e) {
            return new Response("not your element");
        }
    }
}


