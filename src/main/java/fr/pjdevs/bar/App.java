package fr.pjdevs.bar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Exception;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;

/**
 * Main JavaFX class that extends {@code Application} base class.
 * The main layout {@code App.fxml} loaded here and the window is created.
 */
public class App extends Application {
    /**
     * The lock file
     */
    private FileLock lock;

    /**
     * Try to acquire a lock to avoid multiple instance running simultaneously.
     */
    private boolean acquireLock() {
        File file = new File(System.getProperty("user.dir"), "bar.lock");

        try {
            FileChannel fc = FileChannel.open(file.toPath(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE);
            this.lock = fc.tryLock();
            
            if (this.lock != null) {
                file.deleteOnExit();
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        boolean lock;

        // Try to acquire lock
        try {
            lock = acquireLock();
        } catch(Error e) {
            lock = false;
        }

        if (lock) {
            final Image ekip = new Image(getClass().getResourceAsStream("images/ekip.jpg"));
            final ImageView splash = new ImageView(ekip);
            final AnchorPane splashRoot = new AnchorPane(splash);
            final Scene splashScene = new Scene(splashRoot);

            //Load splash screen with fade in effect
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), splashRoot);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);
    
            //Finish splash with fade out effect
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), splashRoot);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);

            final Stage splashStage = new Stage(StageStyle.TRANSPARENT);
            splashStage.getIcons().add(ekip);
            splashStage.setScene(splashScene);
            splashStage.show();

            fadeIn.play();

            final Parent root = FXMLLoader.load(getClass().getResource("fxml/App.fxml"));
            root.getStylesheets().add(getClass().getResource("css/bar.css").toExternalForm());
            final Scene scene = new Scene(root);
            
            stage.getIcons().add(ekip);
            stage.setTitle("Bar");
            stage.setScene(scene);
            stage.setMaximized(true);

            fadeIn.setOnFinished(e -> {
                fadeOut.play();
                fadeOut.setOnFinished(f -> {
                    stage.show();
                    splashStage.close();
                });
            });
        } else {
            new Alert(AlertType.ERROR, "Impossble d'acquerir le lock. Une instance de l'application doit deja etre lance.").show();
            stage.close();
        }
    }
}
