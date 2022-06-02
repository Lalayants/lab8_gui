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
