package controller.server;

import controller.commands.Invoker;
import view.ConsoleIO;
import request.Request;
import controller.utilities.TableCreator;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("InfiniteLoopStatement")
public class ServerN {
    private ServerSocket serverSocket;
    private final ReentrantLock lock = new ReentrantLock();
    public static int PORT = 8085;


    /**
     * This executor service has 10 threads.
     * So it means your server can process max 10 concurrent requests.
     */
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final ExecutorService processorService = Executors.newFixedThreadPool(10);
    private final ExecutorService answerService = Executors.newCachedThreadPool();


    public static void main(String[] args) {
        System.out.println("Server for Lab7 v11249 by Kirill Lalayants R3137 2022 \n\nServer Started");
        ServerN server = new ServerN();
        TableCreator.TableCreator();
        server.runServer();
    }

    private void runServer() {
        int serverPort = PORT;
        try {
            serverSocket = new ServerSocket(serverPort);
            Connector();
        } catch (IOException e) {
            ConsoleIO.ConsoleOut("Error starting Server on " + serverPort);
        }
    }

    private void Connector() {
        while (true) {
            System.out.println("Waiting for new connections");
            try {
                Socket s = serverSocket.accept();
                System.out.println("Connecting...");
                executorService.submit(new ServiceRequest(s));
            } catch (IOException ioe) {
                System.out.println("Error accepting connection");
                ioe.printStackTrace();
            }
        }
    }

    class ServiceRequest implements Runnable {
        private final Socket socket;
        private ObjectInputStream inn;
        private DataOutputStream dout;
        private String login;

        public ServiceRequest(Socket connection) {
            this.socket = connection;
            try {
                dout = new DataOutputStream(socket.getOutputStream());
                inn = new ObjectInputStream(socket.getInputStream());
//                ObjectOutputStream outt = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                ConsoleIO.ConsoleOut("Running " + socket);
                while (true) {
                    Request req = (Request) inn.readObject();
                    login = req.getLogin();                                                     //FutureTask a = new FutureTask(Callable)
                    Future<String> answer = processorService.submit(new RequestProcessor(req));//можно просто через FutureTask, но тогда бещ executors
                    answerService.submit(new RequestAnswer(dout, answer.get()));
                }
            } catch (ClassCastException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException | InterruptedException | ExecutionException e) {
                ConsoleIO.ConsoleOut(login + " disconnected");

            } finally {
                System.out.println("close# " + login);
                try {
                    socket.close();
                } catch (SocketException e) {
                    ConsoleIO.ConsoleOut("close# " + login);
                } catch (IOException e) {
                    ConsoleIO.ConsoleOut("Socket not closed");
                }
            }
        }
    }

    class RequestProcessor implements Callable<String> {
        Request r;

        public RequestProcessor(Request in) {
            r = in;
        }

        @Override
        public String call() {
            lock.lock();
            ConsoleIO.ConsoleOut("Executing " + r.getCommand_name() + " from " + r.getLogin());
            String a = Invoker.getInvoker().commands.get(r.getCommand_name()).execute(r.getArgs(), r.getLogin());
            lock.unlock();
            return a;

        }
    }

    static class RequestAnswer implements Runnable {
        DataOutputStream out;
        String answer;

        public RequestAnswer(DataOutputStream out, String answer) {
            this.out = out;
            this.answer = answer;
        }

        @Override
        public void run() {
            try {
                out.writeUTF(answer);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

