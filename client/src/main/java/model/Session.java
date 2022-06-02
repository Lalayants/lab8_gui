package model;

import controller.ClientN;
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
    public String messageForClient = "";
    private ObjectOutputStream oos;
    private ObjectInputStream in;
    private String login = "sync";
    private Thread updater;
    private int id = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
            model.setWeight(a.getWeight());
            model.setEyeColor(a.getEyeColor());
            model.setCreators_id(a.getCreators_id());
            list.add(model);
        }
        this.labModels = FXCollections.observableArrayList(list);
    }

    private ObservableList<LabModel> labModels = FXCollections.observableArrayList();

    public void prepareSession(int port) throws IOException {
        InetAddress address = InetAddress.getByName(null);
        socket = new Socket(address, port);
        setStreams(socket);
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
//    public void sentRequest(Request req)  {
//        try {
//            req.setLogin(login);
//            oos.writeObject(req);
//            oos.flush();
//        }catch (IOException e){
//            addToLog(req);
//            if lo
//            try {
//            setStreams(socket);
//                setLabModels();
//            } catch (IOException ignored) {}
//        }
//    }

    public void sentRequest(Request req) throws IOException {
        req.setLogin(login);
        oos.writeObject(req);
        oos.flush();
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
    }

    public void updateCollection(Response response) {
        try {
            String command = response.getCommand();
            if (command.equals("show")) {
                setLabModels((ArrayList<LabWorkDTO>) response.getObjectAnswer());
            } else if (command.equals("login")) {
                try {
                    id = Integer.parseInt(response.getAnswer());
                    setId(id);
                    setLogin(Integer.toString(id));
                } catch (NumberFormatException e) {
                    messageForClient = response.getAnswer();
                }
            } else if (command.equals("register")) {
                try {
                    id = Integer.parseInt(response.getAnswer());
                    setId(id);
                    setLogin(Integer.toString(id));
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
                    if (idsToDelete.contains(a.getId())) {
                        modelsToDelete.add(a);
                    }
                }
                getLabModels().removeAll(modelsToDelete);
            } else if (command.equals("add")){
                LabWorkDTO dto = (LabWorkDTO) response.getObjectAnswer();
                getLabModels().add(new LabModel(dto));
                messageForClient = response.getAnswer();
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
            } else if (command.equals("remove_lower")){
                messageForClient = response.getAnswer();
                LabModel toRemove = null;
                ArrayList<Integer> ids = (ArrayList<Integer>) response.getObjectAnswer();
                if (ids.size()>0){
                    for (Integer id: ids){
                        for (LabModel lab: getLabModels()){
                            if (id.equals(lab.getId())){
                                toRemove = lab;
                                break;
                            }
                        }
                        if(toRemove != null){
                            getLabModels().remove(toRemove);
                            toRemove = null;
                        }
                    }
                }
            }else if(command.equals("add_if_min")){
                messageForClient = response.getAnswer();
            }else if(command.equals("print_unique_minimal_point")){
                messageForClient = response.getAnswer();
            }else if(command.equals("count_less_than_minimal_point")){
                messageForClient = response.getAnswer();
            }
        } catch (NullPointerException  ignored) {
        }
    }

}
