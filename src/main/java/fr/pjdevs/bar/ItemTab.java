package fr.pjdevs.bar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;

public class ItemTab extends Tab {
    private ItemList items;

    public ItemTab() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ItemTab.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
    
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void retrieveItems() throws Exception {
        String json = Files.readString(Paths.get("data/items/items.json"));
        this.items = ItemList.fromJson(json);
    }

    @FXML
    private VBox itemsBox;

    @FXML
    public void initialize() {
        try {
            this.retrieveItems();
        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Error while opening item list.\n" + e).show();
        }

        for (Item item : this.items.getList()) {
            itemsBox.getChildren().add(new ItemView(item));
        }
    }
}
