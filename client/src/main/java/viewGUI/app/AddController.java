package viewGUI.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.LabModel;
import requests.Request;
import viewGUI.login.LoginWindow;

import java.net.URL;
import java.util.ResourceBundle;

public class AddController implements Initializable {
    ObservableList<String> difficulties = FXCollections.observableArrayList("Hard", "Terrible", "Insane");
    ObservableList<String> colors = FXCollections.observableArrayList("BLACK", "BLUE", "YELLOW", "ORANGE", "BROWN");
    public LabModel enteredLabModel;
    @FXML
    private TextField nameField, xField, yField, weightField, authorField,
            personalPointField, minimalPointField;
    @FXML
    private ChoiceBox<String> difficultyChoice;
    @FXML
    private ChoiceBox<String> eyeColorChoice;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        difficultyChoice.setItems(difficulties);
        eyeColorChoice.setItems(colors);
    }
    public void cancelButtonOnAction(){
        Stage stage = (Stage) difficultyChoice.getScene().getWindow();
        stage.close();
    }

    public void getLabWork() {
        String mistake = "";
        LabModel labModel = new LabModel();
        String name = nameField.getText();
        String x = xField.getText();
        String y = yField.getText();
        String weight = weightField.getText();
        String author = authorField.getText();
        String personalPoint = personalPointField.getText();
        String minimalPoint = minimalPointField.getText();
        String difficulty = (String) difficultyChoice.getValue();
        String eyeColor = (String) eyeColorChoice.getValue();
        if (name == null) {
            mistake += "Имя должно быть заполнено\n";
        } else {
            labModel.setName(name);
        }
        if (difficulty == null) {
            mistake += "Сложность должна быть заполнено\n";
        } else {
            labModel.setDifficulty(name);
        }
        if (eyeColor  != null) {
            labModel.setEyeColor(eyeColor);
        }
        if (author == null) {
            mistake += "Автор должен быть заполнен\n";
        } else {
            labModel.setAuthor(author);
        }
        if (x  == null) {
            mistake += "X должен быть заполнен\n";
        } else {
            try {
                float xx = Float.parseFloat(x);
                labModel.setX(xx);
            } catch (NumberFormatException e) {
                mistake += "X должен быть десятичной дробью с точкой разделителем\n";
            }
        }
        if (weight == null) {
            mistake += "Вес должен быть заполнен\n";
        } else {
            try {
                float wght = Float.parseFloat(weight);
                labModel.setWeight(wght);
            } catch (NumberFormatException e) {
                mistake += "Вес должен быть десятичной дробью с точкой разделителем\n";
            }
        }
        if (y == null) {
            mistake += "Y должен быть заполнен\n";
        } else {
            try {
                Long yy = Long.parseLong(y);
                labModel.setY(yy);
            } catch (NumberFormatException e) {
                mistake += "Y должен быть целым числом\n";
            }
        }
        if (personalPoint == null) {
            mistake += "Личный балл должен быть заполнен\n";
        } else {
            try {
                int persPoint = Integer.parseInt(personalPoint);
                labModel.setPersonalQualitiesMinimum(persPoint);
            } catch (NumberFormatException e) {
                mistake += "Личный балл должен быть целым числом\n";
            }
        }
        if (minimalPoint == null) {
            mistake += "Минимальный балл должен быть заполнен\n";
        } else {
            try {
                float minPoint = Float.parseFloat(minimalPoint);
                labModel.setMinimalPoint(minPoint);
            } catch (NumberFormatException e) {
                mistake += "Минимальный балл должен быть десятичной дробью с точкой разделителем\n";
            }
        }

        if (mistake.isEmpty()) {
            enteredLabModel = labModel;
            LoginWindow.clientN.giveSessionToSent(new Request("add", enteredLabModel.toDTO()));
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            Scene scene = alert.getDialogPane().getScene();
            scene.getRoot().setStyle("-fx-font-family: 'arial'");
            alert.initOwner(LoginWindow.prStage);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка заполнения");
            alert.setContentText(mistake);
            alert.showAndWait();
        }
    }

}
