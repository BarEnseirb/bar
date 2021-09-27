package fr.pjdevs.bar;

import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ItemPane extends VBox {
    @FXML
    private GridPane itemsBox;
    private Button addBtn;

    public ItemPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/ItemPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.addBtn = new Button("Add");
        this.addBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e) {
                addItem();
            } 
        });
        this.addBtn.setPrefWidth(200);
        this.addBtn.setPrefHeight(200);

        ItemList.getInstance().addListenner(new ChangedListenner(){
            @Override
            public void onChanged() {
                update();
            }
        });

        this.update();
    }

    private void addItem() {
        Optional<Item> newItem = new ItemDialog(null).showAndWait();

        if (newItem.isPresent()) {
            ItemList.getInstance().add(newItem.get());
            new Alert(AlertType.INFORMATION, "Item added.").show();
        } else {
            new Alert(AlertType.ERROR, "Item not added due to wrong values.").show();
        }
    }

    private void update() {
        int currentRow = 0;
        int currentColumn = 0;
        int columnCount = 4;

        for (Item item : ItemList.getInstance().getList()) {
            this.itemsBox.add(new ItemView(item), currentColumn, currentRow);
            ++currentColumn;

            if (currentColumn > columnCount) {
                currentColumn = 0;
                ++currentRow;
            }
        }

        this.itemsBox.add(this.addBtn, currentColumn, currentRow);
        GridPane.setFillWidth(this.addBtn, true);
        GridPane.setFillHeight(this.addBtn, true);
    }
}
