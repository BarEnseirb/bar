package fr.pjdevs.bar;

import java.io.IOException;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
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
        this.backgroundProperty().bind(Bindings.createObjectBinding((() -> {
            return new Background(new BackgroundFill(this.item.getColor(), null, null));
        }), this.item.colorProperty()));;
    }

    @FXML
    private Label itemNameLbl;
    @FXML
    private Label itemPriceLbl;
    @FXML
    private Label itemDescriptionLbl;

    @FXML
    public void addToCart() {
        Cart.getInstance().add(this.item, 1);
    }

    @FXML
    public void edit() {
        Optional<Item> newItem = new ItemDialog(this.item).showAndWait();

        if (newItem.isPresent()) {
            try {
                ItemList.getInstance().update(this.item, newItem.get());
            } catch (IOException e) {
                new Alert(AlertType.ERROR, "Cannot open item list :\n" + e.getMessage()).show();
            }
        } else {
            new Alert(AlertType.ERROR, "Item not updated due to wrong values.").show();
        }
    }

    @FXML
    public void remove() {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure to want to delete this item ?");
        Optional<ButtonType> res = alert.showAndWait();
        if (res.isEmpty() || res.get() == ButtonType.CANCEL) {
            return;
        }
        
        try {
            ItemList.getInstance().remove(this.item);
        } catch (IOException e) {
            new Alert(AlertType.ERROR, "Cannot open item list :\n" + e.getMessage()).show();
        }
    }
}