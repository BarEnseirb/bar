package fr.pjdevs.bar;

import java.lang.Exception;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        final Parent root = FXMLLoader.load(getClass().getResource("App.fxml"));
        final Scene scene = new Scene(root, 600, 800);

        stage.setTitle("Bar");
        stage.setScene(scene);
        stage.show();
    }
}
