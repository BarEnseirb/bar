package fr.pjdevs.bar;

import java.io.IOException;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class ItemView extends VBox {
    private Item item;

    public ItemView(Item item) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/ItemView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.item = item;
        this.itemNameLbl.textProperty().bind(this.item.nameProperty());
        this.itemPriceLbl.textProperty().bind(this.item.priceProperty().divide(100.0).asString("%,.2fE"));
        this.itemDescriptionLbl.textProperty().bind(this.item.descriptionProperty());
    }

    @FXML
    Label itemNameLbl;
    @FXML
    Label itemPriceLbl;
    @FXML
    Label itemDescriptionLbl;

    @FXML
    public void addToCart() {
        Cart.getInstance().add(this.item, 1);
    }

    @FXML
    public void edit() {
        Optional<Item> newItem = new ItemDialog(this.item).showAndWait();

        if (newItem.isPresent()) {
            ItemList.getInstance().update(item, newItem.get());
            new Alert(AlertType.INFORMATION, "Item updated.").show();
        } else {
            new Alert(AlertType.ERROR, "Item not updated due to wrong values.").show();
        }
    }
}