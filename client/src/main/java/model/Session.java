package model;

import request.Request;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;

public class Session {
    private static ObjectOutputStream oos;
    private static DataInputStream in;
    private static String login;
    private static final Vector<Request> log = new Vector<>();

    public static void prepareSession(int port) throws IOException {
        InetAddress address = InetAddress.getByName(null);
        Socket socket = new Socket(address, port);
        setStreams(socket);
    }

    public static void setStreams(Socket socket) {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
//            ois = new ObjectInputStream(socket.getInputStream()); //kill it
            InputStream inFromServer = socket.getInputStream();
            in = new DataInputStream(inFromServer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void sentRequest(Request req) throws IOException {
        oos.writeObject(req);
        oos.flush();
    }

    public static String receiveAnswer() throws IOException {
        return in.readUTF();
    }

    public static void shutDown() {
        System.exit(0);}

    public static Vector<Request> getLog() {
        return log;
    }

    public static int getLogSize() {
        return log.size();
    }

    public static void addToLog(Request r) {
        log.add(r);
    }

    public static void setLogin(String lgn) {
        login = lgn;}

    public static String getLogin() {
        return login;
    }

}
