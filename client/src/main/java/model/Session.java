package model;

import controller.UpdateWaiter;
import entity.LabWorkDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import requests.Request;
import requests.Response;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Session {
    public String messageForClient = "";
    private ObjectOutputStream oos;
    private String login = "sync";
    private int id = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ObservableList<LabModel> getLabModels() {
        return labModels;
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
        Socket socket = new Socket(address, port);
        setStreams(socket);
        setLabModels();
    }

    public void setStreams(Socket socket) {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            UpdateWaiter waiter = new UpdateWaiter(in, this, Thread.currentThread());
            Thread updater = new Thread(waiter);
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

    public void shutDown() {
        System.exit(0);
    }

    public void setLogin(String lgn) {
        login = lgn;
    }

    public void setLabModels() throws IOException {
        sentRequest(new Request("show", null));
    }

    public void updateCollection(Response response) {
        try {
            String command = response.getCommand();
            switch (command) {
                case "show":
                    setLabModels((ArrayList<LabWorkDTO>) response.getObjectAnswer());
                    break;
                case "login":
                case "register":
                    try {
                        id = Integer.parseInt(response.getAnswer());
                        setId(id);
                        setLogin(Integer.toString(id));
                    } catch (NumberFormatException e) {
                        messageForClient = response.getAnswer();
                    }
                    break;
                case "remove_by_id": {
                    int dId = Integer.parseInt(response.getAnswer());
                    LabModel d = null;
                    for (LabModel a : getLabModels()) {
                        if (a.getId() == dId) {
                            d = a;
                            break;
                        }
                    }
                    getLabModels().remove(d);
                    break;
                }
                case "clear":
                    ArrayList<Integer> idsToDelete = (ArrayList<Integer>) response.getObjectAnswer();
                    ArrayList<LabModel> modelsToDelete = new ArrayList<>();
                    for (LabModel a : getLabModels()) {
                        if (idsToDelete.contains(a.getId())) {
                            modelsToDelete.add(a);
                        }
                    }
                    getLabModels().removeAll(modelsToDelete);
                    break;
                case "add":
                    LabWorkDTO dto = (LabWorkDTO) response.getObjectAnswer();
                    getLabModels().add(new LabModel(dto));
                    messageForClient = response.getAnswer();
                    break;
                case "update": {
                    LabWorkDTO labWorkDTO = (LabWorkDTO) response.getObjectAnswer();
                    int dId = labWorkDTO.getId();
                    for (int i = 0; i < getLabModels().size(); i++) {
                        if (getLabModels().get(i).getId() == dId) {
                            getLabModels().remove(i);
                            getLabModels().add(new LabModel(labWorkDTO));
                            break;
                        }
                    }
                    break;
                }
                case "remove_lower":
                    messageForClient = response.getAnswer();
                    LabModel toRemove = null;
                    ArrayList<Integer> ids = (ArrayList<Integer>) response.getObjectAnswer();
                    if (ids.size() > 0) {
                        for (Integer id : ids) {
                            for (LabModel lab : getLabModels()) {
                                if (id.equals(lab.getId())) {
                                    toRemove = lab;
                                    break;
                                }
                            }
                            if (toRemove != null) {
                                getLabModels().remove(toRemove);
                                toRemove = null;
                            }
                        }
                    }
                    break;
                case "add_if_min":
                case "print_unique_minimal_point":
                case "count_less_than_minimal_point":
                    messageForClient = response.getAnswer();
                    break;
            }
        } catch (NullPointerException  ignored) {
        }
    }

}
