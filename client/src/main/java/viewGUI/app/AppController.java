package viewGUI.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.LabModel;
import requests.Request;
import viewGUI.login.LoginWindow;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;

public class AppController{
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

        labModelTableView.setRowFactory( tv -> {
            TableRow<LabModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    System.out.println('1');
                }
            });
            return row;
        });
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
                Request request = new Request("remove_by_id", id.toString());
                LoginWindow.clientN.giveSessionToSent(request);
                showConfirmationAlert("Успешно", "Удаление записи", "Удаление записи прошло успешно");
            } else {
                showMistakeAlert("Ошибка", "Ошибка удаления", "Вы не можете удалять чужие работы!");
            }
        }catch (NumberFormatException e){
            showMistakeAlert("Ошибка","Ошибка удаления","Сначала выберите, какую работу удалять!");
        }
    }
    @FXML
    private void handleClearLabWorks() {
        if (showConfirmationAlert("Подтверждение", "Подтверждение удаления", "Вы уверены, что хотите удалить все свои записи?"))
            LoginWindow.clientN.giveSessionToSent(new Request("clear", ""));
    }
    @FXML
    private void handleAddLabWork() {
        showPersonAddDialog();

    }
    @FXML
    private void handleEditLabWork() {

    }
    public void showPersonAddDialog() {
        try {
            String fxmlName = "AddDialog";
            Parent root1 = FXMLLoader.load(getClass().getClassLoader().getResource(fxmlName + ".fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add Utility");
            Scene a = new Scene(root1, 380, 400);
            a.getRoot().setStyle("-fx-font-family: 'arial'");
            stage.setScene(a);
            addButton.setDisable(true);
            stage.showAndWait();
            addButton.setDisable(false);
        } catch ( IOException e) {
            e.printStackTrace();
        }
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
    public static boolean showConfirmationAlert(String title, String header, String content){
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
}
