package controller;

import model.Session;
import requests.Response;

import java.io.IOException;
import java.io.ObjectInputStream;

public class UpdateWaiter implements Runnable {
    ObjectInputStream inputStream;
    Session session;
    Thread sessionThread;

    public UpdateWaiter(ObjectInputStream objectInputStream, Session ses, Thread thread) {
        inputStream = objectInputStream;
        session = ses;
        sessionThread = thread;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Response response = (Response) inputStream.readObject();
                session.updateCollection(response);
            } catch (IOException | NullPointerException | ClassNotFoundException ignored) {

            }
        }
    }
}
