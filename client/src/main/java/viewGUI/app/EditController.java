package viewGUI.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.LabModel;
import requests.Request;
import viewGUI.login.LoginWindow;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditController implements Initializable {
    private static LabModel labModel;
    ObservableList<String> difficulties = FXCollections.observableArrayList("HARD", "TERRIBLE", "INSANE");
    ObservableList<String> colors = FXCollections.observableArrayList("BLACK", "BLUE", "YELLOW", "ORANGE", "BROWN");
    public LabModel enteredLabModel;
    @FXML
    private Button editButton;
    @FXML
    private TextField nameField, xField, yField, weightField, authorField,
            personalPointField, minimalPointField;
    @FXML
    private ChoiceBox<String> difficultyChoice;
    @FXML
    private ChoiceBox<String> eyeColorChoice;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        difficultyChoice.setValue(labModel.getDifficulty());
        eyeColorChoice.setValue(labModel.getEyeColor());
        setPerson();
        editButton.setDefaultButton(true);
    }

    public void cancelButtonOnAction() {
        Stage stage = (Stage) difficultyChoice.getScene().getWindow();
        stage.close();
    }

    public void editButtonOnAction() {
        float xPrevious = labModel.getX();
        long yPrevious = labModel.getY();
        float weightPrevious = labModel.getWeight();
        int idPrevious = labModel.getId();
        int creatorsIdPrevious = labModel.getCreators_id();
        String mistake = "";
        String name = nameField.getText();
        String x = xField.getText();
        String y = yField.getText();
        String weight = weightField.getText();
        String author = authorField.getText();
        String personalPoint = personalPointField.getText();
        String minimalPoint = minimalPointField.getText();
        String difficulty = difficultyChoice.getValue();
        String eyeColor = eyeColorChoice.getValue();
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
//                float xx = (float) LoginWindow.formater.parse(x);
                labModel.setX(xx);
            } catch (NumberFormatException  e) {
                mistake += LoginWindow.resourceBundle.getString("xFillError");
            }
        }
        if (weight == null|| weight.isEmpty()) {
            mistake += LoginWindow.resourceBundle.getString("weightFillError");
        } else {
            try {
                float wght = Float.parseFloat(weight);
//                float wght = (float) LoginWindow.formater.parse(weight);
                labModel.setWeight(wght);
            } catch (NumberFormatException  e) {
                mistake += LoginWindow.resourceBundle.getString("weightFillError");
            }
        }
        if (y == null || y.isEmpty()) {
            mistake += LoginWindow.resourceBundle.getString("yFillError");
        } else {
            try {
                Long yy = Long.parseLong(y);
//                Long yy = (Long) LoginWindow.formater.parse(y);
                labModel.setY(yy);
            } catch (NumberFormatException  e) {
                mistake += LoginWindow.resourceBundle.getString("yFillError");
            }
        }
        if (personalPoint == null || personalPoint.isEmpty()) {
            mistake += LoginWindow.resourceBundle.getString("personalPointFillError");
        } else {
            try {
                int persPoint = Integer.parseInt(personalPoint);
//                int persPoint = (int) LoginWindow.formater.parse(personalPoint);
                labModel.setPersonalQualitiesMinimum(persPoint);
            } catch (NumberFormatException  e) {
                mistake += LoginWindow.resourceBundle.getString("personalPointFillError");
            }
        }
        if (minimalPoint == null || minimalPoint.isEmpty()) {
            mistake += LoginWindow.resourceBundle.getString("minimalPointFillError");
        } else {
            try {
                float minPoint = Float.parseFloat(minimalPoint);
                labModel.setMinimalPoint(minPoint);
            } catch (NumberFormatException  e) {
                mistake += LoginWindow.resourceBundle.getString("minimalPointFillError");
            }
        }

        if (mistake.isEmpty()) {
            int index = AppController.listOfId.indexOf(idPrevious);
            System.out.println(index);
            AppController.listOfX.remove(index);
            AppController.listOfY.remove(index);
            AppController.listOfId.remove(index);
            AppController.listOfWeight.remove(index);
            AppController.listOfCreatorsId.remove(index);
            enteredLabModel = labModel;
            try {
                LoginWindow.clientN.giveSessionToSent(new Request("update", enteredLabModel.toDTO()));
                cancelButtonOnAction();
                AppController.showInfoAlert(LoginWindow.resourceBundle.getString("success"), LoginWindow.resourceBundle.getString("successOfEdit"), LoginWindow.resourceBundle.getString("successOfEditMessage"));

            } catch (IOException e) {
                cancelButtonOnAction();
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
        } else {
            AppController.showErrorAlert(LoginWindow.resourceBundle.getString("error"), LoginWindow.resourceBundle.getString("LabWorkFillError"), mistake);
        }
    }

    public void setPerson() {
        difficultyChoice.setItems(difficulties);
        eyeColorChoice.setItems(colors);
        nameField.setText(labModel.getName());
        xField.setText(String.valueOf(labModel.getX()));
        yField.setText(String.valueOf(labModel.yProperty().get()));
        minimalPointField.setText(String.valueOf(labModel.getMinimalPoint()));
        personalPointField.setText(String.valueOf(labModel.getPersonalQualitiesMinimum()));
        authorField.setText(labModel.getAuthor());
        weightField.setText(String.valueOf(labModel.getWeight()));
        eyeColorChoice.setValue(labModel.getEyeColor());
        difficultyChoice.setValue(labModel.getDifficulty());
    }

    public static void setLab(LabModel l) {
        labModel = l;
    }
}
