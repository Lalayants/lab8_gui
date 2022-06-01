package viewGUI.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class App extends Application {
    Stage primaryStage;

    public static void show() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("../../../resources/app.fxml"));
        primaryStage.setTitle("Авторизация");
//        new AppController().setItems();
        Scene a = new Scene(root, 520, 308);
        a.getRoot().setStyle("-fx-font-family: 'arial'");
        primaryStage.setScene(a);
        primaryStage.setResizable(false);
        primaryStage.show();

    }
    public void startAnother() throws IOException{

    }
}
