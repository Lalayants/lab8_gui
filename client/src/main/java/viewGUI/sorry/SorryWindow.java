package viewGUI.sorry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SorryWindow extends Application {
    public static void show() {
        launch();
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sorry.fxml"));
        primaryStage.setTitle("Простите, мы все сломали");
        Scene a = new Scene(root, 600, 400);
        a.getRoot().setStyle("-fx-font-family: 'arial'");
        primaryStage.setScene(a);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
