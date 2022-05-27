package viewGUI.login;

import controller.ClientN;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import model.Session;
import viewGUI.app.AppController;

import java.io.IOException;

public class LoginWindow extends Application {

    private Stage primaryStage;
    public static ClientN clientN;
    public static Session session;
    public static int id;
    public static Stage prStage;
//    public static Stage prStage;

    public static void show(ClientN clientN, Session s) {
        LoginWindow.clientN = clientN;
        LoginWindow.session = s;
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        prStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        primaryStage.setTitle("Авторизация");
        Scene a = new Scene(root, 520, 308);
        a.getRoot().setStyle("-fx-font-family: 'arial'");
        primaryStage.setScene(a);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


}
