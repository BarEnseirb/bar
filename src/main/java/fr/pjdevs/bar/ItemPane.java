package fr.pjdevs.bar;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;

public class ItemPane extends ScrollPane {
    @FXML
    private VBox itemsBox;

    public ItemPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/ItemPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
    
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        try {
            for (Item item : ItemList.getInstance().getList()) {
                itemsBox.getChildren().add(new ItemView(item));
            }
        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Error while opening item list.\n" + e).show();
        }
    }
}
