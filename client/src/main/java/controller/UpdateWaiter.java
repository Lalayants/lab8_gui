package controller;

import model.Session;
import requests.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.sleep;

public class UpdateWaiter implements Runnable{
    ObjectInputStream inputStream;
    Session session;
    Thread sessionThread;
    CountDownLatch countDownLatch;
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
                        System.out.println(response.getAnswer());
                        System.out.println(response.getCommand());
                        session.update(response);

            } catch (IOException | NullPointerException | ClassNotFoundException  e) {
                e.printStackTrace();
            }
        }
    }
}
