package controller.server;

import controller.commands.Invoker;
import requests.Response;
import view.ConsoleIO;
import requests.Request;
import controller.utilities.TableCreator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("InfiniteLoopStatement")
public class ServerN {
    private ServerSocket serverSocket;
    private final ReentrantLock lock = new ReentrantLock();
    public static int PORT = 8085;
    private static HashMap<Socket, ObjectOutputStream> activeConnections= new HashMap<>();


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
        private ObjectOutputStream dout;
        private String login;

        public ServiceRequest(Socket connection) {
            this.socket = connection;
            try {
                dout = new ObjectOutputStream(socket.getOutputStream());
                inn = new ObjectInputStream(socket.getInputStream());
                activeConnections.put(socket, dout);
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
                    login = req.getLogin();
                    Future<Object> response = processorService.submit(new RequestProcessor(req));
                    Response r = (Response) response.get();
                    if (!r.isForUpdate())
                        answerService.submit(new RequestAnswer(dout, response.get()));
                    if(r.getCommand().equals("login") || r.getCommand().equals("register"))
                            login = r.getAnswer();
                }
            } catch (ClassCastException | ClassNotFoundException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            } catch (IOException  e) {
                ConsoleIO.ConsoleOut(login + " disconnected");
            } finally {
                ConsoleIO.ConsoleOut("close# " + login);
                activeConnections.remove(socket);
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

    class RequestProcessor implements Callable<Object> {
        Request r;

        public RequestProcessor(Request in) {
            r = in;
        }

        @Override
        public Object call() {
            lock.lock();
            ConsoleIO.ConsoleOut("Executing " + r.getCommand_name() + " from " + r.getLogin());
            Response a = (Response) Invoker.getInvoker().commands.get(r.getCommand_name()).execute(r.getArgs(), r.getLogin());
            if (a.isForUpdate()){
                ObjectOutputStream out;
                Set<Socket> connections = activeConnections.keySet();
                for (Socket s: connections){
                    try {
                        out = activeConnections.get(s);
                        out.writeObject(a);
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            lock.unlock();
            return a;
        }
    }

    static class RequestAnswer implements Runnable {
        ObjectOutputStream out;
        Object response;

        public RequestAnswer(ObjectOutputStream out, Object answer) {
            this.out = out;
            this.response = answer;
        }

        @Override
        public void run() {
            try {
                out.writeObject(response);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

