package fr.pjdevs.bar;

import java.io.IOException;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Component which represents an item of the {@link Cart} in the {@link CartPane}.
 */
public class CartItemView extends HBox {
    /**
     * The item represented by this view.
     */
    private final Item item;
    /**
     * The count property for the represented item.
     */
    private final IntegerProperty count;

    /**
     * Label for the item's name.
     */
    @FXML
    private Label itemNameLbl;
    /**
     * Label for the item's price.
     */
    @FXML
    private Label itemPriceLbl;
    /**
     * Label for the item's count.
     */
    @FXML
    private Label itemCountLbl;    

    /**
     * Creates a new CartItemView instance with an item and a count.
     * @param item The item represented by this CartItemView.
     * @param count The count of this item in the {@link Cart}.
     */
    public CartItemView(Item item, int count) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/CartItemView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.item = item;
        this.count = new SimpleIntegerProperty(count);

        this.itemNameLbl.textProperty().bind(this.item.nameProperty());
        this.itemPriceLbl.textProperty().bind(CustomStringBindings.createIntegerMoneyStringBinding(this.item.priceProperty()));
        this.itemCountLbl.textProperty().bind(this.count.asString("x%d"));
    }

    /**
     * FXML accessible method to decrease teh count of this item by one in the {@link Cart}.
     */
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