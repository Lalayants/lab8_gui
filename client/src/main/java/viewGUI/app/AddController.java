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
    ObservableList<String> difficulties = FXCollections.observableArrayList("HARD", "TERRIBLE", "INSANE");
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
        if (name == null || name.isEmpty()) {
            mistake += LoginWindow.resourceBundle.getString("nameFillError");
        } else {
            labModel.setName(name);
        }
        if (difficulty == null||difficulty.isEmpty()) {
            mistake += LoginWindow.resourceBundle.getString("difficultyFillError");
        } else {
            labModel.setDifficulty(difficulty);
        }
        if (eyeColor  != null) {
            labModel.setEyeColor(eyeColor);
        }
        if (author == null || author.isEmpty()) {
            mistake += LoginWindow.resourceBundle.getString("authorFillError");
        } else {
            labModel.setAuthor(author);
        }
        if (x  == null || x.isEmpty()) {
            mistake += LoginWindow.resourceBundle.getString("xFillError");
        } else {
            try {
                float xx = Float.parseFloat(x);
                labModel.setX(xx);
            } catch (NumberFormatException e) {
                mistake += LoginWindow.resourceBundle.getString("xFillError");
            }
        }
        if (weight == null|| weight.isEmpty()) {
            mistake += LoginWindow.resourceBundle.getString("weightFillError");
        } else {
            try {
                float wght = Float.parseFloat(weight);
                labModel.setWeight(wght);
            } catch (NumberFormatException e) {
                mistake += LoginWindow.resourceBundle.getString("weightFillError");
            }
        }
        if (y == null || y.isEmpty()) {
            mistake += LoginWindow.resourceBundle.getString("yFillError");
        } else {
            try {
                Long yy = Long.parseLong(y);
                labModel.setY(yy);
            } catch (NumberFormatException e) {
                mistake += LoginWindow.resourceBundle.getString("yFillError");
            }
        }
        if (personalPoint == null || personalPoint.isEmpty()) {
            mistake += LoginWindow.resourceBundle.getString("personalPointFillError");
        } else {
            try {
                int persPoint = Integer.parseInt(personalPoint);
                labModel.setPersonalQualitiesMinimum(persPoint);
            } catch (NumberFormatException e) {
                mistake += LoginWindow.resourceBundle.getString("personalPointFillError");
            }
        }
        if (minimalPoint == null || minimalPoint.isEmpty()) {
            mistake += LoginWindow.resourceBundle.getString("minimalPointFillError");
        } else {
            try {
                float minPoint = Float.parseFloat(minimalPoint);
                labModel.setMinimalPoint(minPoint);
            } catch (NumberFormatException e) {
                mistake += LoginWindow.resourceBundle.getString("minimalPointFillError");
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
            alert.setTitle(LoginWindow.resourceBundle.getString("error"));
            alert.setHeaderText(LoginWindow.resourceBundle.getString("LabWorkFillError"));
            alert.setContentText(mistake);
            alert.showAndWait();
        }
    }

}
