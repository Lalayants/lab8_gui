package viewGUI.app;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import model.LabModel;
import requests.Request;
import viewGUI.login.LoginWindow;

import java.sql.Timestamp;

public class AppController {
    private int creators_id;
    @FXML
    private TableView<LabModel> labModelTableView;
    @FXML
    private Button addButton, clearButton, editButton, removeButton;
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
        // Инициализация таблицы адресатов с двумя столбцами.
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

        labModelTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    private void showPersonDetails(LabModel labWork) {
        if (labWork != null) {
            // Заполняем метки информацией из объекта person.
            idTextField.setText(String.valueOf(labWork.getId()));
            nameTextField.setText(labWork.getName());
            creationDateTextField.setText(labWork.getCreationDate().toString());
            xTextField.setText(String.valueOf(labWork.getX()));
            yTextField.setText(String.valueOf(labWork.yProperty().get()));
            minimalPointTextField.setText(String.valueOf(labWork.getMinimalPoint()));
            personalPointTextField.setText(String.valueOf(labWork.getPersonalQualitiesMinimum()));
            difficultyTextField.setText(labWork.getDifficulty());
            authorTextField.setText(labWork.getAuthor());
            eyeColorTextField.setText(labWork.getEyeColor());
            weightTextField.setText(String.valueOf(labWork.getWeight()));
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
            Integer id = Integer.parseInt(idTextField.getText());
            System.out.println(id);
            if (creators_id == LoginWindow.id) {
//            System.out.println(creators_id == LoginWindow.id);
                Request request = new Request("remove_by_id", id.toString());
                LoginWindow.clientN.giveSessionToSent(request);
            } else {
                showMistakeAlert("Ошибка", "Ошибка удаления", "Вы не можете удалять чужие работы!");
            }
        }catch (NumberFormatException e){
            showMistakeAlert("Ошибка","Ошибка удаления","Сначала выберите, какую работу удалять!");
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            Scene scene = alert.getDialogPane().getScene();
//            scene.getRoot().setStyle("-fx-font-family: 'arial'");
//            alert.initOwner(LoginWindow.prStage);
//            alert.setTitle("Ошибка");
//            alert.setHeaderText("Ошибка удаления");
//            alert.setContentText("Сначала выберите, какую работу удалять!");
//            alert.showAndWait();
        }
    }
    @FXML
    private void handleClearLabWorks() {
        LoginWindow.clientN.giveSessionToSent(new Request("clear", ""));


    }
    @FXML
    private void handleAddLabWork() {

    }
    @FXML
    private void handleEditLabWork() {

    }

    public static void showMistakeAlert(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Scene scene = alert.getDialogPane().getScene();
        scene.getRoot().setStyle("-fx-font-family: 'arial'");
        alert.initOwner(LoginWindow.prStage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
