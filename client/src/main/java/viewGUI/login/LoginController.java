package viewGUI.login;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import viewGUI.app.AppController;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private static String login;
    @FXML
    private BorderPane rootPane;
    @FXML
    private Button registerButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private Label loginPaneText;
    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private ChoiceBox<String> languageChoice;
    ObservableList<String> languages;


    public void registerButtonOnAction(ActionEvent event) throws InterruptedException, IOException {
        LoginWindow.session.setId(-1);
        if (!usernameTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty()) {
            getAnwser(usernameTextField.getText(), passwordTextField.getText(), "register");
            LoginWindow.id = LoginWindow.session.getId();
            loginMessageLabel.setText(LoginWindow.resourceBundle.getString(LoginWindow.session.messageForClient));
        } else
            loginMessageLabel.setText(LoginWindow.resourceBundle.getString("loginErrorEmptyField"));
    }

    public void loginButtonOnAction(ActionEvent event) throws IOException {
        LoginWindow.session.setId(-1);
        if (getAnwser(usernameTextField.getText(), passwordTextField.getText(), "login")) {
            try {
                loginMessageLabel.setText(LoginWindow.resourceBundle.getString(LoginWindow.session.messageForClient));
            } catch (NullPointerException ignored) {
            }
        } else {
            loginMessageLabel.setText(LoginWindow.resourceBundle.getString("loginErrorEmptyField"));
        }

    }

    boolean getAnwser(String login, String password, String command) throws IOException {
        if (!usernameTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty()) {
            LoginWindow.clientN.checkLogin(command + " " + login + " " + password);
            try {
                Thread.sleep(100);
                if (LoginWindow.session.getId() != -1) {
                    LoginWindow.id = LoginWindow.session.getId();
                    loadSecond();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        } else
            return false;
    }

    public void loadSecond() throws IOException {
        login = LoginWindow.resourceBundle.getString("userNameField") + ": " + usernameTextField.getText();
        login += ", " + LoginWindow.resourceBundle.getString("creatorsIdColumn") + " = " + String.valueOf(LoginWindow.id);
        AppController.setLogin(login);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("app.fxml"));
        Parent root1 = FXMLLoader.load(getClass().getClassLoader().getResource("app.fxml"), LoginWindow.resourceBundle);
        Stage stage = LoginWindow.prStage;
        stage.setResizable(true);
        stage.setTitle(LoginWindow.resourceBundle.getString("app"));
        Scene a = new Scene(root1, 1000, 500);
        a.getRoot().setStyle("-fx-font-family: 'arial'");
        stage.setScene(a);
        stage.show();
    }

    public void setText(String s) {
        loginMessageLabel.setText(s);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        languages = FXCollections.observableArrayList("English", "Russian", "Sweden", "Belorussian", "Language");
        languageChoice.setItems(languages);
        languageChoice.setValue("Language");
        languageChoice.setOnAction(this::changeLanguage);
        loginButton.setDefaultButton(true);
    }

    public void changeLanguage(ActionEvent event) {
        if (languageChoice.getValue().equals("Russian")) {
            LoginWindow.locale = new Locale("ru", "RU");
            LoginWindow.resourceBundle = ResourceBundle.getBundle("viewGUI.app.res.Bundle", LoginWindow.locale);
        } else if (languageChoice.getValue().equals("English")) {
            LoginWindow.locale =new Locale("en", "AU");
            LoginWindow.resourceBundle = ResourceBundle.getBundle("viewGUI.app.res.Bundle", LoginWindow.locale);
        }else if (languageChoice.getValue().equals("Sweden")) {
            LoginWindow.locale =new Locale("se", "SE");
            LoginWindow.resourceBundle = ResourceBundle.getBundle("viewGUI.app.res.Bundle", LoginWindow.locale);
        }else if (languageChoice.getValue().equals("Belorussian")) {
            LoginWindow.locale =new Locale("by", "BY");
            LoginWindow.resourceBundle = ResourceBundle.getBundle("viewGUI.app.res.Bundle", LoginWindow.locale);
        }

        try {
            LoginWindow.prStage.setTitle(LoginWindow.resourceBundle.getString("authorization"));
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"), LoginWindow.resourceBundle);
            Scene a = new Scene(root, 520, 308);
            a.getRoot().setStyle("-fx-font-family: 'arial'");
            LoginWindow.prStage.setScene(a);
            LoginWindow.prStage.setResizable(false);
            LoginWindow.prStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
