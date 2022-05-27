package viewGUI.login;

import controller.ClientN;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import viewGUI.app.App;
import viewGUI.app.AppController;
//import viewGUI.app.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class LoginController implements Initializable {

    @FXML
    private BorderPane rootPane;
    @FXML
    private Button registerButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;


    public void registerButtonOnAction(ActionEvent event) throws InterruptedException, IOException {
        LoginWindow.session.setId(-1);
        getAnwser(usernameTextField.getText(), passwordTextField.getText(), "register");
        LoginWindow.id = LoginWindow.session.getId();
        loginMessageLabel.setText(LoginWindow.session.messageForClient);
    }

    public void loginButtonOnAction(ActionEvent event) throws InterruptedException, IOException {
        LoginWindow.session.setId(-1);
        getAnwser(usernameTextField.getText(), passwordTextField.getText(), "login");
        loginMessageLabel.setText(LoginWindow.session.messageForClient);

    }

    void getAnwser(String login, String password, String command) throws IOException {
        if (!usernameTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty()) {
            LoginWindow.clientN.checkLogin(command + " " + login + " " + password);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (LoginWindow.session.getId() != -1) {
                LoginWindow.id = LoginWindow.session.getId();
                loadSecond();
            }
        } else
            loginMessageLabel.setText("Оба поля должны быть заполнены");
    }

    public void loadSecond() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("app.fxml"));
        Parent root1 = FXMLLoader.load(getClass().getClassLoader().getResource("app.fxml"));
        Stage stage = LoginWindow.prStage;

        stage.setTitle("App");
        Scene a = new Scene(root1, 1000, 500);
        a.getRoot().setStyle("-fx-font-family: 'arial'");
        stage.setScene(a);
        stage.show();
//        new AppController().setItems();
    }

    public void setText(String s) {
        loginMessageLabel.setText(s);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
