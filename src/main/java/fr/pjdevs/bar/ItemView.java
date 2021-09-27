package fr.pjdevs.bar;

import java.io.IOException;
import java.math.BigDecimal;

import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class ItemView extends VBox {
    private Item item;

    public ItemView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/ItemView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public ItemView(Item item) {
        this();

        this.itemNameLbl.setText(item.getName());
        this.itemPriceLbl.setText(BigDecimal.valueOf(item.getPrice()).movePointLeft(2).toPlainString() + "E");
        this.itemDescriptionLbl.setText(item.getDesciption());
        this.item = item;
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
}