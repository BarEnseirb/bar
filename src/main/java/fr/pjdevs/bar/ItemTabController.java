package fr.pjdevs.bar;

import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;

public class ItemTabController {
    private ObservableList<Item> items;

    public void retrieveItems() throws Exception {
        String json = Files.readString(Paths.get("data/items/items.json"));
        ItemList list = ItemList.fromJson(json);
        this.items = FXCollections.observableArrayList(list.getList());
    }

    @FXML
    private ListView<Item> itemListView;

    @FXML
    public void initialize() {
        try {
            this.retrieveItems();
        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Error while opening item list.\n" + e).show();
        }

        itemListView.setCellFactory(new Callback<ListView<Item>, ListCell<Item>>() {
            @Override
            public ListCell<Item> call(ListView<Item> param) {
                return new ListCell<Item>() {
                    @Override
                    protected void updateItem(Item item, boolean empty) {
                        if (item == null || empty) {
                            this.setGraphic(null);
                        } else {
                            this.setGraphic(new ItemView(item));
                        }
                    }
                };
            }
        });
        itemListView.setItems(items);
    }
}
