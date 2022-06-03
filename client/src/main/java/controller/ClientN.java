package controller;


import model.Session;
import view.ConsoleIO;
import requests.Request;
import viewGUI.login.LoginWindow;
import viewGUI.sorry.SorryWindow;

import java.io.*;


public class ClientN {
    Request request;
    private Session session = new Session();
    public static final Integer PORT = 8085;

    public static void main(String[] args) {
        ClientN cl = new ClientN();
        cl.st();
        System.exit(0);
    }

    public void st() {
        try {
            session.prepareSession(PORT);
            LoginWindow.show(this, session);
        } catch (IOException e) {
            ConsoleIO.ConsoleOut("Сервер недоступен");
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
        } catch (IOException e) {
            ConsoleIO.ConsoleOut("Сервер не доступен, перезапустите программу позже");
            SorryWindow.show();
            session.shutDown();
            shutDown();
        }
    }

    public void giveSessionToSent(Request req) throws IOException {
        request = req;
        session.sentRequest(req);
    }

    void shutDown() {
        System.out.println("Завершение работы приложения\n");
        System.exit(0);
    }
}
