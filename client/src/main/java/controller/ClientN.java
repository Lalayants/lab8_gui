package controller;

import model.Session;
import view.ConsoleIO;
import entity.LabWorkDTOCreator;
import request.Request;


import java.io.*;
import java.util.Arrays;
import java.util.Iterator;


public class ClientN {
    private static Request request;
    private static final Integer PORT = 8085;
    private static final String[] commands_lab = {"add", "add_if_min", "remove_lower", "update"};
    private static final String[] commands_str = {"remove_by_id", "count_less_than_minimal_point", "execute_script"};
    private static final String[] commands_noarg = {"help", "info", "show", "clear", "remove_first", "print_descending", "print_unique_minimal_point"};

    public static void main(String[] args) {
        ConsoleIO.ConsoleOut("Client app for Lab8 v53490 by Kirill Lalayants R3137 2022 \nApp Started");
        ConsoleIO.ConsoleOut("Postavte ball, pls");
        try {
            Session.prepareSession(PORT);
            login();
            process();
            shutDown();
        } catch (IOException e) {
            ConsoleIO.ConsoleOut("Сервер недоступен");
        }
    }

    static void login() {
        while (true) {
            try {
                ConsoleIO.ConsoleOut("login / register  ?");
                String com;
                do {
                    com = ConsoleIO.ConsoleIn();
                } while (!com.equals("register") && !com.equals("login"));
                ConsoleIO.ConsoleOut("Введите логин:");
                String login = ConsoleIO.ConsoleIn();
                ConsoleIO.ConsoleOut("Введите пароль:");
                String password = ConsoleIO.ConsoleIn();

                Session.sentRequest(new Request(com, login + " " + password, login));
                String answer = Session.receiveAnswer();
                if (answer.toLowerCase().contains("успешно")) {
                    ConsoleIO.ConsoleOut(answer);
                    ConsoleIO.ConsoleOut("Вход в систему выполнен");
                    Session.setLogin(login);
                    break;
                } else {
                    System.out.println(answer);
                }
            } catch (IndexOutOfBoundsException e) {
                ConsoleIO.ConsoleOut("Формат ввода: команда + логин + пароль");
                ConsoleIO.ConsoleOut("Попробуйте снова");
            } catch (IOException | NullPointerException e) {
                ConsoleIO.ConsoleOut("Сервер не доступен, перезапустите программу позже");
                Session.shutDown();
                shutDown();
            }
        }
    }

    public static void process() {
        while (true) {
//            checkLog();
            System.out.print(">");
            String[] raw = ConsoleIO.ConsoleIn().trim().split(" ");
            checkLog();
            String command = raw[0];
            try {
                if (Arrays.asList(commands_lab).contains(command)) {
                    request = new Request(command, new LabWorkDTOCreator().create());
                    if (raw.length > 1)
                        try {
                            request.setId(Integer.parseInt(raw[1]));
                        } catch (ClassCastException e) {
                            ConsoleIO.ConsoleOut("id - Целое число");
                        }
                    sentToServer();
                } else if (Arrays.asList(commands_str).contains(command)) {
                    try {
                        request = new Request(command, raw[1]);
                        sentToServer();
                    } catch (IndexOutOfBoundsException e) {
                        ConsoleIO.ConsoleOut("У этой команды должен быть аргумент");
                    }
                } else if (Arrays.asList(commands_noarg).contains(command)) {
                    request = new Request(command, null, Session.getLogin());
                    sentToServer();
                } else if (command.equals("exit")) {
                    break;
                } else {
                    ConsoleIO.ConsoleOut("Такой команды нет, воспользуйтесь help для ознакомления с перечнем доступных команд");
                }
            } catch (IOException e) {
                log();
            }
        }
    }

    private static void checkLog() {
        if (!Session.getLog().isEmpty()) {
            try {
                Session.prepareSession(PORT);
                Iterator<Request> logIterator = Session.getLog().iterator();
                while (logIterator.hasNext()) {
                    try {
                        request = logIterator.next();
                        sentToServerLog();
                        String s = Session.receiveAnswer();
                        logIterator.remove();
                        System.out.println("++++++++++++++++++++++++++++\n" + "Из лога выполнена команда " + request.getCommand_name() + ". Результат:\n" + s + "\n++++++++++++++++++++++++++++");
                    } catch (IOException ignored) {}
                }
            } catch (IOException e) {
                ConsoleIO.ConsoleOut("Сервер пока не ожил");
            }
        }
    }

    private static void log() {
        if (Session.getLogSize() == 0)
            ConsoleIO.ConsoleOut("Сервер умер, 3 команды сохранятся в лог и выполнятся после восстановления подключения");
        if (Session.getLogSize() > 3) {
            ConsoleIO.ConsoleOut("Сервер так и не ожил. Лог заполнен, выключение");
            shutDown();
        } else {
            Session.addToLog(request);
            ConsoleIO.ConsoleOut("В логе осталось мест: " + (3 - Session.getLogSize()));
        }

    }

    private static void sentToServer() throws IOException {
        request.setLogin(Session.getLogin());
        Session.sentRequest(request);
        ConsoleIO.ConsoleOut(Session.receiveAnswer());
    }

    private static void sentToServerLog() throws IOException {
        request.setLogin(Session.getLogin());
        Session.sentRequest(request);
    }

    static void shutDown() {
        System.out.println("Завершение работы приложения\n");
        System.exit(0);
    }
}

