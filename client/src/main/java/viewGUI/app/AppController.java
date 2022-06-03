package viewGUI.app;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.LabModel;
import requests.Request;
import viewGUI.login.LoginWindow;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.Vector;

public class AppController {
    private int creators_id;
    private static String login;
    private final Vector<Color> colors = new Vector<>();
    private final ArrayList<Integer> drawnIDs = new ArrayList<>();
    public static ObservableList<Float> listOfX = FXCollections.observableArrayList();
    public static ObservableList<Long> listOfY = FXCollections.observableArrayList();
    public static ObservableList<Integer> listOfId = FXCollections.observableArrayList();
    public static ObservableList<Float> listOfWeight = FXCollections.observableArrayList();
    public static ObservableList<Integer> listOfCreatorsId = FXCollections.observableArrayList();

    public static void setLogin(String login) {
        AppController.login = login;
    }

    private boolean editIsClosed = true;
    private Stage editStage;
    private LabModel activeLabWork;
    private GraphicsContext graphicsContext;
    @FXML
    private AnchorPane tabMap;
    @FXML
    private Canvas objectCanvas;
    @FXML
    private Label usersLogin;
    @FXML
    private TableView<LabModel> labModelTableView;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private TextField idTextField, nameTextField, creationDateTextField, xTextField, yTextField, minimalPointTextField,
            personalPointTextField, difficultyTextField, authorTextField, weightTextField, eyeColorTextField;
    @FXML
    private TableColumn<LabModel, Integer> idColumn;
    @FXML
    private TableColumn<LabModel, String> nameColumn;
    @FXML
    private TableColumn<LabModel, Timestamp> dateColumn;
    @FXML
    private TableColumn<LabModel, Float> xColumn;
    @FXML
    private TableColumn<LabModel, Long> yColumn;
    @FXML
    private TableColumn<LabModel, Float> minimalPointColumn;
    @FXML
    private TableColumn<LabModel, Integer> personalPointColumn;
    @FXML
    private TableColumn<LabModel, String> difficultyColumn;
    @FXML
    private TableColumn<LabModel, String> authorColumn;
    @FXML
    private TableColumn<LabModel, Float> weightColumn;
    @FXML
    private TableColumn<LabModel, String> eyeColorColumn;
    @FXML
    private TableColumn<LabModel, Integer> creatorsIdColumn;

    @FXML
    private void initialize() {
        usersLogin.setText(login);

        LoginWindow.formatter = NumberFormat.getInstance(LoginWindow.locale);

        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().creationDateProperty());
        xColumn.setCellValueFactory(cellData -> cellData.getValue().xProperty().asObject());
        yColumn.setCellValueFactory(cellData -> cellData.getValue().yProperty().asObject());
        minimalPointColumn.setCellValueFactory(cellData -> cellData.getValue().minimalPointProperty().asObject());
        personalPointColumn.setCellValueFactory(cellData -> cellData.getValue().personalQualitiesMinimumProperty().asObject());
        difficultyColumn.setCellValueFactory(cellData -> cellData.getValue().difficultyProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        weightColumn.setCellValueFactory(cellData -> cellData.getValue().weightProperty().asObject());
        eyeColorColumn.setCellValueFactory(cellData -> cellData.getValue().eyeColorProperty());
        creatorsIdColumn.setCellValueFactory(cellData -> cellData.getValue().creators_idProperty().asObject());

        labModelTableView.setItems(LoginWindow.session.getLabModels());

        LoginWindow.session.getLabModels().addListener((ListChangeListener<LabModel>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    for (LabModel removedLabModel : c.getRemoved()) {
                        if (listOfX.contains(removedLabModel.getX()) && listOfX.contains(removedLabModel.getX()) && listOfY.contains(removedLabModel.getY()) && listOfId.contains(removedLabModel.getId())) {
                            int index = listOfId.indexOf(removedLabModel.getId());
                            listOfX.remove(index);
                            listOfY.remove(index);
                            listOfId.remove(index);
                            listOfWeight.remove(index);
                            listOfCreatorsId.remove(index);
                        }
                    }
                }
                if (c.wasAdded()) {
                    for (LabModel addedLabModel : c.getAddedSubList()) {
                        listOfX.add(addedLabModel.getX());
                        listOfY.add(addedLabModel.getY());
                        listOfWeight.add(addedLabModel.getWeight());
                        listOfId.add(addedLabModel.getId());
                        listOfCreatorsId.add(addedLabModel.getCreators_id());
                    }
                }
                redrawObjects();
            }
        }); //listener for observable list

        for (LabModel item : labModelTableView.getItems()) {
            drawnIDs.add(item.getId());
            listOfX.add(item.getX());
            listOfY.add(item.getY());
            listOfId.add(item.getId());
            listOfWeight.add(item.getWeight());
            listOfCreatorsId.add(item.getCreators_id());
        } //all current labs info needed for drawing

        labModelTableView.getSelectionModel().selectedItemProperty().addListener(   //shows data in Details
                (observable, oldValue, newValue) -> showPersonDetails(newValue));

        labModelTableView.setRowFactory(tv -> {
            TableRow<LabModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    if (!editIsClosed) {
                        editStage.close();
                        editIsClosed = true;
                    } else {
                        handleEditLabWork();
                    }
                }
            });
            return row;
        }); // double click reaction

        tabMap.widthProperty().addListener((obs, oldVal, newVal) -> redrawObjects()); //listener that changes canvas size
        tabMap.heightProperty().addListener((obs, oldVal, newVal) -> redrawObjects());
        graphicsContext = objectCanvas.getGraphicsContext2D();
        graphicsContext.setFont(Font.font("Arial"));

        Field[] colorsField = Color.class.getDeclaredFields(); //all colors
        for (int i = 7; i < colorsField.length - 10; i++) {
            try {
                this.colors.add((Color) colorsField[i].get(Color.class));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        objectCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, //mouse click on map
                e -> {
//                        System.out.println(e.getX() + " " + e.getY());
                    ObservableList<LabModel> list = labModelTableView.getItems();
                    for (LabModel lab : list) {
                        if (e.getX() < tabMap.getWidth() / 2 + lab.getX() + lab.getWeight() / 2 && e.getX() > tabMap.getWidth() / 2 + lab.getX() - lab.getWeight() / 2)
                            if (e.getY() > tabMap.getHeight() / 2 - lab.getY() - lab.getWeight() / 2 && e.getY() < tabMap.getHeight() / 2 - lab.getY() + lab.getWeight() / 2) {
                                showPersonDetails(lab);
                                if (e.getButton().equals(MouseButton.PRIMARY)) {
                                    handleEditLabWork();
                                    break;
                                } else if (e.getButton().equals(MouseButton.SECONDARY)) {
                                    handleDeleteLabWork();
                                    break;
                                }
                            }
                    }
                });
    }


    private void redrawObjects() {
        redrawPlain();
        drawnBefore();
        Integer id = null;
        for (int i = 0; i < listOfX.size(); i++) {
            if (!drawnIDs.contains(listOfId.get(i))) {
                double sizeHalf = listOfWeight.get(i) / 2;
                double x = tabMap.getWidth() / 2 + listOfX.get(i) - sizeHalf;
                double y = tabMap.getHeight() / 2 - listOfY.get(i) - sizeHalf;
                int creatorsID = listOfCreatorsId.get(i);
                id = listOfId.get(i);
                DoubleProperty xx = new SimpleDoubleProperty();
                DoubleProperty yy = new SimpleDoubleProperty();
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.seconds(0),
                                new KeyValue(xx, tabMap.getWidth() / 2),
                                new KeyValue(yy, tabMap.getHeight() / 2)
                        ),
                        new KeyFrame(Duration.seconds(0.25),
                                new KeyValue(xx, x),
                                new KeyValue(yy, y)
                        ),
                        new KeyFrame(Duration.seconds(0.26),
                                new KeyValue(xx, 3000),
                                new KeyValue(yy, 3000))
                );
                AnimationTimer timer = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        GraphicsContext gc = graphicsContext;
                        redrawPlain();
                        drawnBefore();
                        gc.setFill(colors.get(creatorsID % colors.size()));
                        gc.fillRect(xx.doubleValue(), yy.doubleValue(), sizeHalf * 2, sizeHalf * 2);
                    }
                };
                timer.start();
                timeline.play();

                graphicsContext.strokeText(String.valueOf(id), x, y + sizeHalf, sizeHalf * 2);
                break;
            }
        }
        if (id != null)
            drawnIDs.add(id);
        redrawPlain();
        drawnBefore();
    }

    private void drawnBefore() {
        for (int i = 0; i < listOfX.size(); i++) {
            if (drawnIDs.contains(listOfId.get(i))) {
                double sizeHalf = listOfWeight.get(i) / 2;
                double x = tabMap.getWidth() / 2 + listOfX.get(i) - sizeHalf;
                double y = tabMap.getHeight() / 2 - listOfY.get(i) - sizeHalf;
                Rect a = new Rect(x, y, sizeHalf, listOfId.get(i), listOfCreatorsId.get(i));
                graphicsContext.setFill(colors.get(a.creators_id % colors.size()));
                graphicsContext.fillRect(a.x, a.y, a.halfSize * 2, a.halfSize * 2);
                graphicsContext.strokeText(String.valueOf(a.id), a.x, a.y + a.halfSize, a.halfSize * 2);
            }
        }
    }

    private void redrawPlain() {
        objectCanvas.setHeight(tabMap.getHeight());
        objectCanvas.setWidth(tabMap.getWidth());
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0, 0, tabMap.getWidth(), tabMap.getHeight());
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.strokeLine(0, tabMap.getHeight() / 2, tabMap.getWidth(), tabMap.getHeight() / 2);
        graphicsContext.strokeLine(tabMap.getWidth() / 2, 0, tabMap.getWidth() / 2, tabMap.getHeight());
        graphicsContext.strokeLine(tabMap.getWidth() / 2 + 10, tabMap.getHeight() / 2 - 5, tabMap.getWidth() / 2 + 10, tabMap.getHeight() / 2 + 5);
        graphicsContext.strokeText("10", tabMap.getWidth() / 2 + 5, tabMap.getHeight() / 2 + 18, 10);
        graphicsContext.strokeLine(tabMap.getWidth() / 2 - 10, 10, tabMap.getWidth() / 2, 0);
        graphicsContext.strokeLine(tabMap.getWidth() / 2 + 10, 10, tabMap.getWidth() / 2, 0);
        graphicsContext.strokeLine(tabMap.getWidth() - 10, tabMap.getHeight() / 2 + 10, tabMap.getWidth(), tabMap.getHeight() / 2);
        graphicsContext.strokeLine(tabMap.getWidth() - 10, tabMap.getHeight() / 2 - 10, tabMap.getWidth(), tabMap.getHeight() / 2);
    }

    @FXML
    private void handleEditLabWork() {
        try {
            Integer id = Integer.parseInt(idTextField.getText());
            if (creators_id == LoginWindow.id) {
                EditController.setLab(activeLabWork);
                Parent root1 = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("EditDialog.fxml")), LoginWindow.resourceBundle);
                Stage stage = new Stage();
                editStage = stage;
                stage.setTitle(LoginWindow.resourceBundle.getString("editUtility"));
                Scene a = new Scene(root1, 380, 400);
                a.getRoot().setStyle("-fx-font-family: 'arial'");
                stage.setScene(a);
                editButton.setDisable(true);
                editIsClosed = false;
                stage.showAndWait();
                editButton.setDisable(false);
                editIsClosed = true;
            } else {
                showErrorAlert(LoginWindow.resourceBundle.getString("error"), LoginWindow.resourceBundle.getString("LabWorkEditError"), LoginWindow.resourceBundle.getString("LabWorkEditErrorNotYours"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            showErrorAlert(LoginWindow.resourceBundle.getString("error"), LoginWindow.resourceBundle.getString("LabWorkEditError"), LoginWindow.resourceBundle.getString("LabWorkEditErrorNotSpecified"));
        }
    }

    @FXML
    private void showPersonDetails(LabModel labWork) {
        activeLabWork = labWork;
        if (labWork != null) {
            // Заполняем метки информацией из объекта person.
            idTextField.setText(String.valueOf(labWork.getId()));
            nameTextField.setText(labWork.getName());
            creationDateTextField.setText(labWork.getCreationDate().toString());
            xTextField.setText(LoginWindow.formatter.format(labWork.getX()));
            yTextField.setText(String.valueOf(labWork.yProperty().get()));
            minimalPointTextField.setText(LoginWindow.formatter.format(labWork.getMinimalPoint()));
            personalPointTextField.setText(String.valueOf(labWork.getPersonalQualitiesMinimum()));
            difficultyTextField.setText(labWork.getDifficulty());
            authorTextField.setText(labWork.getAuthor());
            eyeColorTextField.setText(labWork.getEyeColor());
            weightTextField.setText(LoginWindow.formatter.format(labWork.getWeight()));
            creators_id = labWork.getCreators_id();
        } else {
            idTextField.setText("");
            nameTextField.setText("");
            creationDateTextField.setText("");
            xTextField.setText("");
            yTextField.setText("");
            minimalPointTextField.setText("");
            personalPointTextField.setText("");
            difficultyTextField.setText("");
            authorTextField.setText("");
            eyeColorTextField.setText("");
            weightTextField.setText("");
        }
    }

    @FXML
    private void handleDeleteLabWork() {
        try {
            int id = Integer.parseInt(idTextField.getText());
            if (creators_id == LoginWindow.id) {
                Request request = new Request("remove_by_id", Integer.toString(id));
                try {
                    LoginWindow.clientN.giveSessionToSent(request);
                } catch (IOException e) {
                    Parent root;
                    try {
                        root = FXMLLoader.load(getClass().getClassLoader().getResource("sorry.fxml"));
                        LoginWindow.prStage.setTitle("Простите, мы все сломали");
                        Scene a = new Scene(root, 600, 400);
                        a.getRoot().setStyle("-fx-font-family: 'arial'");
                        LoginWindow.prStage.setScene(a);
                        LoginWindow.prStage.setResizable(false);
                        LoginWindow.prStage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                showInfoAlert(LoginWindow.resourceBundle.getString("success"), LoginWindow.resourceBundle.getString("labWorkRemove"), LoginWindow.resourceBundle.getString("labWorkRemoveSuccess"));
            } else {
                showErrorAlert(LoginWindow.resourceBundle.getString("error"), LoginWindow.resourceBundle.getString("labWorkRemove"), LoginWindow.resourceBundle.getString("labWorkRemoveError"));
            }
        } catch (NumberFormatException e) {
            showErrorAlert(LoginWindow.resourceBundle.getString("error"), LoginWindow.resourceBundle.getString("labWorkRemove"), LoginWindow.resourceBundle.getString("labWorkRemoveErrorNotSpecified"));
        }
    }

    @FXML
    private void handleClearLabWorks() {
        if (showConfirmationAlert(LoginWindow.resourceBundle.getString("confirmation"), LoginWindow.resourceBundle.getString("confirmationOfClear"), LoginWindow.resourceBundle.getString("confirmationOfClearMessage"))) {
            try {
                LoginWindow.clientN.giveSessionToSent(new Request("clear", ""));
            } catch (IOException e) {
                Parent root;
                try {
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("sorry.fxml"));
                    LoginWindow.prStage.setTitle("Простите, мы все сломали");
                    Scene a = new Scene(root, 600, 400);
                    a.getRoot().setStyle("-fx-font-family: 'arial'");
                    LoginWindow.prStage.setScene(a);
                    LoginWindow.prStage.setResizable(false);
                    LoginWindow.prStage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleAddLabWork() {
        showPersonAddDialog();
    }

    public void showPersonAddDialog() {
        try {
            String fxmlName = "AddDialog";
            Parent root1 = FXMLLoader.load(getClass().getClassLoader().getResource(fxmlName + ".fxml"), LoginWindow.resourceBundle);
            Stage stage = new Stage();
            stage.setTitle(LoginWindow.resourceBundle.getString("addUtility"));
            Scene a = new Scene(root1, 380, 400);
            a.getRoot().setStyle("-fx-font-family: 'arial'");
            stage.setScene(a);
            addButton.setDisable(true);
            stage.showAndWait();
            addButton.setDisable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUniMinimalPoint() {
        try {
            LoginWindow.clientN.giveSessionToSent(new Request("print_unique_minimal_point", ""));
        } catch (IOException e) {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("sorry.fxml"));
                LoginWindow.prStage.setTitle("Простите, мы все сломали");
                Scene a = new Scene(root, 600, 400);
                a.getRoot().setStyle("-fx-font-family: 'arial'");
                LoginWindow.prStage.setScene(a);
                LoginWindow.prStage.setResizable(false);
                LoginWindow.prStage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (LoginWindow.session.messageForClient.equals("getUniqueMinimalPointAnswerEmpty"))
            AppController.showInfoAlert(LoginWindow.resourceBundle.getString("success"), LoginWindow.resourceBundle.getString("uniqueMinimalPointButton"), LoginWindow.resourceBundle.getString(LoginWindow.session.messageForClient));
        else
            AppController.showInfoAlert(LoginWindow.resourceBundle.getString("success"), LoginWindow.resourceBundle.getString("uniqueMinimalPointButton"), LoginWindow.session.messageForClient);


    }

    public void countLessThanMinimalPoint() {
        try {
            Double minP = Double.valueOf(minimalPointTextField.getText());
            try {
                LoginWindow.clientN.giveSessionToSent(new Request("count_less_than_minimal_point", minP));
            } catch (IOException e) {
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("sorry.fxml"));
                    LoginWindow.prStage.setTitle("Простите, мы все сломали");
                    Scene a = new Scene(root, 600, 400);
                    a.getRoot().setStyle("-fx-font-family: 'arial'");
                    LoginWindow.prStage.setScene(a);
                    LoginWindow.prStage.setResizable(false);
                    LoginWindow.prStage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AppController.showInfoAlert(LoginWindow.resourceBundle.getString("success"), LoginWindow.resourceBundle.getString("countLessThanMinimalPointButton"), LoginWindow.session.messageForClient);
        } catch (NumberFormatException e) {
            showErrorAlert(LoginWindow.resourceBundle.getString("error"), LoginWindow.resourceBundle.getString("countLessThanMinimalPointButton"), LoginWindow.resourceBundle.getString("minimalPointFillError"));
        }

    }

    public static void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Scene scene = alert.getDialogPane().getScene();
        scene.getRoot().setStyle("-fx-font-family: 'arial'");
        alert.initOwner(LoginWindow.prStage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static boolean showConfirmationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Scene scene = alert.getDialogPane().getScene();
        scene.getRoot().setStyle("-fx-font-family: 'arial'");
        alert.initOwner(LoginWindow.prStage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        Optional<ButtonType> res = alert.showAndWait();
        return res.get() == ButtonType.OK;
    }

    public static void showInfoAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Scene scene = alert.getDialogPane().getScene();
        scene.getRoot().setStyle("-fx-font-family: 'arial'");
        alert.initOwner(LoginWindow.prStage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private class Rect {
        //        public Rectangle rectangle;
        public Double x;
        public Double y;
        public Double halfSize;
        public int id;
        public int creators_id;

        @Override
        public boolean equals(Object o) {
            Rect rect = (Rect) o;
            return id == rect.id && creators_id == rect.creators_id && x.equals(rect.x) && y.equals(rect.y) && halfSize.equals(rect.halfSize);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, halfSize, id, creators_id);
        }

        public Rect(Double x, Double y, Double halfSize, int id, int creators_id) {
            this.x = x;
            this.y = y;
            this.halfSize = halfSize;
            this.id = id;
            this.creators_id = creators_id;
        }
    }
}
