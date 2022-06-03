package viewGUI.login;

import controller.ClientN;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import model.Session;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginWindow extends Application {

    public static ClientN clientN;
    public static Session session;
    public static int id;
    public static Stage prStage;
    public static ResourceBundle resourceBundle;
    public static Locale locale;
    public static NumberFormat formatter;

    public static void show(ClientN clientN, Session s) {
        LoginWindow.clientN = clientN;
        LoginWindow.session = s;
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        locale = new Locale("en", "AU");
        resourceBundle = ResourceBundle.getBundle("viewGUI.app.res.Bundle", locale);
        prStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"), resourceBundle);
        primaryStage.setTitle("Authorisation");
        Scene a = new Scene(root, 520, 308);
        a.getRoot().setStyle("-fx-font-family: 'arial'");
        primaryStage.setScene(a);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
