package fr.pjdevs.bar;

import java.io.File;
import java.io.IOException;
import java.lang.Exception;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
// import javafx.scene.image.Image;
import javafx.scene.Parent;
import javafx.stage.Stage;
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
            // Create window
            // stage.getIcons().add(new Image(getClass().getResourceAsStream("images/ekip.jpg")));

            final Parent root = FXMLLoader.load(getClass().getResource("fxml/App.fxml"));
            root.getStylesheets().add(getClass().getResource("css/bar.css").toExternalForm());
            final Scene scene = new Scene(root, 600, 800);

            stage.setTitle("Bar");
            stage.setScene(scene);
            stage.show();
        } else {
            new Alert(AlertType.ERROR, "Cannot acquiere lock. An instance may already be running.").show();
            stage.close();
        }
    }
}
