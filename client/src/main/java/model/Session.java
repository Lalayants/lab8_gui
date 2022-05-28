package model;

import controller.UpdateWaiter;
import entity.LabWorkDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import requests.Request;
import requests.Response;
import viewGUI.login.LoginController;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class Session {
    public String messageForClient;
    private ObjectOutputStream oos;
    private ObjectInputStream in;
    private String login = "sync";
    private Thread updater;
    private int id = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
    }

    private Socket socket;
    private final Vector<Request> log = new Vector<>();

    public ObservableList<LabModel> getLabModels() {
        return labModels;
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLabModels(ArrayList<LabWorkDTO> labModels) {
        ArrayList<LabModel> list = new ArrayList<>();
        for (LabWorkDTO a : labModels) {
            LabModel model = new LabModel();
            model.setId(a.getId());
            model.setName(a.getName());
            model.setX(a.getX());
            model.setY(a.getY());
            model.setCreationDate(a.getCreationDate());
            model.setMinimalPoint(a.getMinimalPoint());
            model.setPersonalQualitiesMinimum(a.getPersonalQualitiesMinimum());
            model.setDifficulty(a.getDifficulty());
            model.setAuthor(a.getAuthor());
//            model.setBirthday(a.getBirthday());
            model.setWeight(a.getWeight());
            model.setEyeColor(a.getEyeColor());
            model.setCreators_id(a.getCreators_id());
            list.add(model);
            System.out.println(model);
        }

        this.labModels = FXCollections.observableArrayList(list);
        System.out.println(labModels);
    }

    private ObservableList<LabModel> labModels = FXCollections.observableArrayList();

    public void prepareSession(int port) throws IOException {
        InetAddress address = InetAddress.getByName(null);
        socket = new Socket(address, port);
        setStreams(socket);
        System.out.println("Ставлю лабы");
        setLabModels();
    }

    public void setStreams(Socket socket) {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            UpdateWaiter waiter = new UpdateWaiter(in, this, Thread.currentThread());
            updater = new Thread(waiter);
            updater.setName("updater");
            updater.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sentRequest(Request req) throws IOException {
        req.setLogin(login);
        oos.writeObject(req);
        oos.flush();
    }

    public Object receiveAnswer() throws IOException {
        try {
            return in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void shutDown() {
        System.exit(0);
    }

    public Vector<Request> getLog() {
        return log;
    }

    public int getLogSize() {
        return log.size();
    }

    public void addToLog(Request r) {
        log.add(r);
    }

    public void setLogin(String lgn) {
        login = lgn;
    }

    public String getLogin() {
        return login;
    }

    public void setLabModels() throws IOException {
        sentRequest(new Request("show", null));
//        setLabModels((ArrayList<LabWorkDTO>) receiveAnswer());
    }

    public void update(Response response) {
        System.out.println("upd");
        try {
            String command = response.getCommand();
            System.out.println(command);
            if (command.equals("show")) {
                setLabModels((ArrayList<LabWorkDTO>) response.getObjectAnswer());
            } else if (command.equals("login")) {
                try {
                    System.out.println(response.getAnswer());
                    id = Integer.parseInt(response.getAnswer());
                    setId(id);
                    setLogin(Integer.toString(id));
                } catch (NumberFormatException e) {
                    messageForClient = response.getAnswer();
                }
            } else if (command.equals("register")) {
                try {
                    System.out.println(response.getAnswer());
                    id = Integer.parseInt(response.getAnswer());
                    setId(id);
                } catch (NumberFormatException e) {
                    messageForClient = response.getAnswer();
                }
            } else if (command.equals("remove_by_id")) {
                int dId = Integer.parseInt(response.getAnswer());
                LabModel d = null;
                for (LabModel a : getLabModels()) {
                    if (a.getId() == dId) {
                        d = a;
                        break;
                    }
                }
                getLabModels().remove(d);
            } else if (command.equals("clear")) {
                ArrayList<Integer> idsToDelete = (ArrayList<Integer>) response.getObjectAnswer();
                ArrayList<LabModel> modelsToDelete = new ArrayList<>();
                for (LabModel a : getLabModels()) {
                    System.out.println(a.getId() + " " + idsToDelete.contains(a.getId()));
                    if (idsToDelete.contains(a.getId())) {
                        modelsToDelete.add(a);
                    }
                }
                getLabModels().removeAll(modelsToDelete);
            } else if (command.equals("add")){
                LabWorkDTO dto = (LabWorkDTO) response.getObjectAnswer();
                getLabModels().add(new LabModel(dto));
            } else if (command.equals("update")){
                LabWorkDTO labWorkDTO = (LabWorkDTO) response.getObjectAnswer();
                int dId = labWorkDTO.getId();
                LabModel d = null;
                for (int i = 0; i < getLabModels().size(); i++) {
                    if (getLabModels().get(i).getId() == dId) {
                        getLabModels().remove(i);
                        getLabModels().add(new LabModel(labWorkDTO));
                        break;
                    }
                }
                getLabModels().remove(d);
            }
        } catch (NullPointerException ignored) {
        }
    }

}

//public class Session {
//    private static ObjectOutputStream oos;
//    private static ObjectInputStream in;
//    private static String login = "sync";
//    private static int id = -1;
//
//    public static int getId() {
//        return id;
//    }
//
//    public static void setId(int id) {
//        Session.id = id;
//    }
//
//    private static Socket socket;
//    private static final Vector<Request> log = new Vector<>();
//
//    public static ObservableList<LabModel> getLabModels() {
//        return labModels;
//    }
//    public static void close(){
//        try {
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void setLabModels(ArrayList<LabWorkDTO> labModels) {
//        ArrayList<LabModel> list =  new ArrayList<>();
//        for (LabWorkDTO a : labModels){
//            LabModel model = new LabModel();
//            model.setId(a.getId());
//            model.setName(a.getName());
//            model.setX(a.getX());
//            model.setY(a.getY());
//            model.setCreationDate(a.getCreationDate());
//            model.setMinimalPoint(a.getMinimalPoint());
//            model.setPersonalQualitiesMinimum(a.getPersonalQualitiesMinimum());
//            model.setDifficulty(a.getDifficulty());
//            model.setAuthor(a.getAuthor());
//            model.setBirthday(a.getBirthday());
//            model.setWeight(a.getWeight());
//            model.setEyeColor(a.getEyeColor());
//            model.setCreators_id(a.getCreators_id());
//            System.out.println(a.getBirthday() + " " + a.getCreators_id());
//            list.add(model);
//            System.out.println(model);
//        }
//
//        Session.labModels = FXCollections.observableArrayList(list);
//        System.out.println(labModels);
//    }
//
//    private static ObservableList<LabModel> labModels = FXCollections.observableArrayList();
//
//    public static void prepareSession(int port) throws IOException {
//        InetAddress address = InetAddress.getByName(null);
//        socket = new Socket(address, port);
//        setStreams(socket);
//        System.out.println("Ставлю лабы");
//        setLabModels();
//    }
//
//    public static void setStreams(Socket socket) {
//        try {
//            oos = new ObjectOutputStream(socket.getOutputStream());
//            in = new ObjectInputStream(socket.getInputStream());
//            UpdateWaiter waiter = new UpdateWaiter(in);
//            Thread updaiter = new Thread(waiter);
//            updaiter.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public static void sentRequest(Request req) throws IOException {
//        req.setLogin(login);
//        oos.writeObject(req);
//        oos.flush();
//    }
//
//    public static Object receiveAnswer() throws IOException {
//        try {
//            return in.readObject();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static void shutDown() {
//        System.exit(0);}
//
//    public static Vector<Request> getLog() {
//        return log;
//    }
//
//    public static int getLogSize() {
//        return log.size();
//    }
//
//    public static void addToLog(Request r) {
//        log.add(r);
//    }
//
//    public static void setLogin(String lgn) {
//        login = lgn;}
//
//    public static String getLogin() {
//        return login;
//    }
//
//    public static void setLabModels() throws IOException {
//        sentRequest(new Request("show", null));
////        setLabModels((ArrayList<LabWorkDTO>) receiveAnswer());
//    }
//    public static void update(Response response){
//        String command = response.getCommand();
//        if (command.equals("show")){
//            setLabModels((ArrayList<LabWorkDTO>) response.getObjectAnswer());
//        }else if(command.equals("login")){
//            try{
//                System.out.println(response.getAnswer());
//                id = Integer.parseInt(response.getAnswer());
//                new LoginController().loadSecond();
//            } catch (ClassCastException e){
//                new LoginController().setText("Неправильный логин или пароль");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//}
