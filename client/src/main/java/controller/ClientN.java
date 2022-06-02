package controller;

import entity.LabWorkDTO;
import model.Session;
import view.ConsoleIO;
import requests.Request;
//import viewGUI.app.App;
import viewGUI.login.LoginWindow;
import viewGUI.sorry.SorryWindow;


import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ClientN {
    private Request request;
    private Session session = new Session();
    private final Integer PORT = 8085;
    private final String[] commands_lab = {"add", "add_if_min", "remove_lower", "update"};
    private final String[] commands_str = {"remove_by_id", "count_less_than_minimal_point", "execute_script"};
    private final String[] commands_noarg = {"help", "info", "show", "clear", "remove_first", "print_descending", "print_unique_minimal_point"};

    public static void main(String[] args) {
        ClientN cl= new ClientN();
        cl.st();
        System.exit(0);
    }

    public void st(){
        try {
            session.prepareSession(PORT);
            LoginWindow.show(this, session);
        } catch (IOException  e) {
            ConsoleIO.ConsoleOut("Сервер недоступен");
            session.close();
            SorryWindow.show();
            System.exit(0);
        }
    }

    public void checkLogin(String login) {
        String[] cup = login.split(" ");
        String command = cup[0];
        String username = cup[1];
        String password = cup[2];
        try {
            session.sentRequest(new Request(command, username + " " + password, username));
        }catch (IOException e){
            ConsoleIO.ConsoleOut("Сервер не доступен, перезапустите программу позже");
            SorryWindow.show();
            session.shutDown();
            shutDown();
        }
    }
//    public void add(){
//
//    }
//    public void remove(Request req){
//        try {
//            session.sentRequest(req);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public void giveSessionToSent(Request req){
        try {
            request = req;
            session.sentRequest(req);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  void checkLog() {
        if (!session.getLog().isEmpty()) {
            try {
                session.prepareSession(PORT);
                Iterator<Request> logIterator = session.getLog().iterator();
                while (logIterator.hasNext()) {
                    try {
                        request = logIterator.next();
                        sentToServerLog();
//                        String s = (String) session.receiveAnswer();
                        logIterator.remove();
                        System.out.println("++++++++++++++++++++++++++++\n" + "Из лога выполнена команда " + request.getCommand_name());
                    } catch (IOException ignored) {
                    }
                }
            } catch (IOException e) {
                ConsoleIO.ConsoleOut("Сервер пока не ожил");
            }
        }
    }

    private  void log() {
        if (session.getLogSize() == 0)
            ConsoleIO.ConsoleOut("Сервер умер, 3 команды сохранятся в лог и выполнятся после восстановления подключения");
        if (session.getLogSize() > 3) {
            ConsoleIO.ConsoleOut("Сервер так и не ожил. Лог заполнен, выключение");
            shutDown();
        } else {
            session.addToLog(request);
            ConsoleIO.ConsoleOut("В логе осталось мест: " + (3 - session.getLogSize()));
        }

    }

    private void sentToServer() throws IOException {
        request.setLogin(session.getLogin());
        session.sentRequest(request);
    }

    private void sentToServerLog() throws IOException {
        request.setLogin(session.getLogin());
        session.sentRequest(request);
    }

     void shutDown() {
        System.out.println("Завершение работы приложения\n");
        System.exit(0);
    }
}

//public class ClientN {
//    private static Request request;
//    private static final Integer PORT = 8085;
//    private static final String[] commands_lab = {"add", "add_if_min", "remove_lower", "update"};
//    private static final String[] commands_str = {"remove_by_id", "count_less_than_minimal_point", "execute_script"};
//    private static final String[] commands_noarg = {"help", "info", "show", "clear", "remove_first", "print_descending", "print_unique_minimal_point"};
//
//    public static void main(String[] args) {
//        ConsoleIO.ConsoleOut("Client app for Lab8 v53490 by Kirill Lalayants R3137 2022 \nApp Started");
//        ConsoleIO.ConsoleOut("Postavte ball, pls");
//        try {
//            Session.prepareSession(PORT);
//            LoginWindow.show();
//            ConsoleIO.ConsoleOut("Приступайте");
////            process();
//        } catch (IOException e) {
//            ConsoleIO.ConsoleOut("Сервер недоступен");
//            Session.close();
//            SorryWindow.show();
//            System.exit(0);
//        }
//    }
//
//    public static void checkLogin(String login) {
//        String[] cup = login.split(" ");
//        String command = cup[0];
//        String username = cup[1];
//        String password = cup[2];
//        try {
//            Session.sentRequest(new Request(command, username + " " + password, username));
//        }catch (IOException e){
//            ConsoleIO.ConsoleOut("Сервер не доступен, перезапустите программу позже");
//            SorryWindow.show();
//            Session.shutDown();
//            shutDown();
//        }
//    }
////    static void checkLogin() {
////        while (true) {
////            try {
////                ConsoleIO.ConsoleOut("login / register  ?");
////                String com;
////                do {
////                    com = ConsoleIO.ConsoleIn();
////                } while (!com.equals("register") && !com.equals("login"));
////                ConsoleIO.ConsoleOut("Введите логин:");
////                String login = ConsoleIO.ConsoleIn();
////                ConsoleIO.ConsoleOut("Введите пароль:");
////                String password = ConsoleIO.ConsoleIn();
////
////                Session.sentRequest(new Request(com, login + " " + password, login));
////                String answer = Session.receiveAnswer();
////                if (answer.toLowerCase().contains("успешно")) {
////                    ConsoleIO.ConsoleOut(answer);
////                    ConsoleIO.ConsoleOut("Вход в систему выполнен");
////                    Session.setLogin(login);
////                    break;
////                } else {
////                    System.out.println(answer);
////                }
////            } catch (IndexOutOfBoundsException e) {
////                ConsoleIO.ConsoleOut("Формат ввода: команда + логин + пароль");
////                ConsoleIO.ConsoleOut("Попробуйте снова");
////            } catch (IOException | NullPointerException e) {
////                ConsoleIO.ConsoleOut("Сервер не доступен, перезапустите программу позже");
////                Session.shutDown();
////                shutDown();
////            }
////        }
////    }
//
////    public static void process() {
////        while (true) {
//////            checkLog();
////            System.out.print(">");
////            String[] raw = ConsoleIO.ConsoleIn().trim().split(" ");
////            checkLog();
////            String command = raw[0];
////            try {
////                if (Arrays.asList(commands_lab).contains(command)) {
////                    request = new Request(command, new LabWorkDTOCreator().create());
////                    if (raw.length > 1)
////                        try {
////                            request.setId(Integer.parseInt(raw[1]));
////                        } catch (ClassCastException e) {
////                            ConsoleIO.ConsoleOut("id - Целое число");
////                        }
////                    sentToServer();
////                } else if (Arrays.asList(commands_str).contains(command)) {
////                    try {
////                        request = new Request(command, raw[1]);
////                        sentToServer();
////                    } catch (IndexOutOfBoundsException e) {
////                        ConsoleIO.ConsoleOut("У этой команды должен быть аргумент");
////                    }
////                } else if (Arrays.asList(commands_noarg).contains(command)) {
////                    request = new Request(command, null, Session.getLogin());
////                    sentToServer();
////                } else if (command.equals("exit")) {
////                    break;
////                } else {
////                    ConsoleIO.ConsoleOut("Такой команды нет, воспользуйтесь help для ознакомления с перечнем доступных команд");
////                }
////            } catch (IOException e) {
////                log();
////            }
////        }
////    }
//
//    private static void checkLog() {
//        if (!Session.getLog().isEmpty()) {
//            try {
//                Session.prepareSession(PORT);
//                Iterator<Request> logIterator = Session.getLog().iterator();
//                while (logIterator.hasNext()) {
//                    try {
//                        request = logIterator.next();
//                        sentToServerLog();
//                        String s = (String) Session.receiveAnswer();
//                        logIterator.remove();
//                        System.out.println("++++++++++++++++++++++++++++\n" + "Из лога выполнена команда " + request.getCommand_name() + ". Результат:\n" + s + "\n++++++++++++++++++++++++++++");
//                    } catch (IOException ignored) {
//                    }
//                }
//            } catch (IOException e) {
//                ConsoleIO.ConsoleOut("Сервер пока не ожил");
//            }
//        }
//    }
//
//    private static void log() {
//        if (Session.getLogSize() == 0)
//            ConsoleIO.ConsoleOut("Сервер умер, 3 команды сохранятся в лог и выполнятся после восстановления подключения");
//        if (Session.getLogSize() > 3) {
//            ConsoleIO.ConsoleOut("Сервер так и не ожил. Лог заполнен, выключение");
//            shutDown();
//        } else {
//            Session.addToLog(request);
//            ConsoleIO.ConsoleOut("В логе осталось мест: " + (3 - Session.getLogSize()));
//        }
//
//    }
//
//    private static void sentToServer() throws IOException {
//        request.setLogin(Session.getLogin());
//        Session.sentRequest(request);
//        if (request.getCommand_name().equals("show")){
//            Session.setLabModels((ArrayList<LabWorkDTO>) Session.receiveAnswer());
//        }
//        ConsoleIO.ConsoleOut((String)Session.receiveAnswer());
//    }
//
//    private static void sentToServerLog() throws IOException {
//        request.setLogin(Session.getLogin());
//        Session.sentRequest(request);
//    }
//
//    static void shutDown() {
//        System.out.println("Завершение работы приложения\n");
//        System.exit(0);
//    }
//}

