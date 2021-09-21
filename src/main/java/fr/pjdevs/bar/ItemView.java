package fr.pjdevs.bar;

import java.io.IOException;
import java.math.BigDecimal;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

public class ItemView extends HBox {
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

        Image img = new Image(item.getImagePath(), 100, 100, false, false);
        this.itemImage.setImage(img);
        this.itemNameLbl.setText(item.getName());
        this.itemPriceLbl.setText(BigDecimal.valueOf(item.getPrice()).movePointLeft(2).toPlainString() + "E");
        this.item = item;
    }

    @FXML
    ImageView itemImage;
    @FXML
    Label itemNameLbl;
    @FXML
    Label itemPriceLbl;

    @FXML
    public void addToCart() {
        Cart.getInstance().add(this.item, 1);
    }
}