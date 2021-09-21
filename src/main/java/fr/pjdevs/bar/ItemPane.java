package fr.pjdevs.bar;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;

public class ItemPane extends VBox {
    @FXML
    private GridPane itemsBox;

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
            int currentRow = 0;
            int currentColumn = 0;
            int columnCount = 4;

            for (Item item : ItemList.getInstance().getList()) {
                itemsBox.add(new ItemView(item), currentColumn, currentRow);
                ++currentColumn;

                if (currentColumn > columnCount) {
                    currentColumn = 0;
                    ++currentRow;
                }
            }
        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Error while opening item list.\n" + e).show();
        }
    }
}
