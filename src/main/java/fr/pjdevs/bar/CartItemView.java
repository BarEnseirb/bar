package fr.pjdevs.bar;

import java.io.IOException;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CartItemView extends HBox {
    private final Item item;
    private final IntegerProperty count;

    @FXML
    Label itemNameLbl;
    @FXML
    Label itemPriceLbl;
    @FXML
    Label itemCountLbl;    

    public CartItemView(Item item, int count) {
        this.item = item;
        this.count = new SimpleIntegerProperty(this, "count", count);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/CartItemView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        itemNameLbl.textProperty().bind(this.item.nameProperty());
        itemPriceLbl.textProperty().bind(this.item.priceProperty().divide(100.0).asString("%,.2fE"));
        itemCountLbl.textProperty().bind(this.count.asString("x%d"));
    }

    @FXML
    public void minus() {
        this.count.set(this.count.get()-1);

        if (this.count.get() <= 0) {
            Cart.getInstance().remove(this.item);
        } else {
            Cart.getInstance().update(this.item, this.count.get());
        }
    }
}