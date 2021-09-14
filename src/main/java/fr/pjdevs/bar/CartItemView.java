package fr.pjdevs.bar;

import java.io.IOException;
import java.math.BigDecimal;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

public class CartItemView extends HBox {
    private Item item;

    public CartItemView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/CartItemView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public CartItemView(Item item, int count) {
        this();

        this.setItem(item);
        this.updateCountLabel(count);
    }

    private void setItem(Item item) {
        this.item = item;

        Image img = new Image(item.getImagePath(), 100, 100, false, false);
        this.itemImage.setImage(img);
        this.itemNameLbl.setText(item.getName());
        this.itemPriceLbl.setText(BigDecimal.valueOf(item.getPrice()).movePointLeft(2).toPlainString() + "E");
    }

    private void setCount(int count) {
        if (count <= 0) {
            Cart.getInstance().remove(this.item);
            return;
        }

        Cart.getInstance().update(this.item, count);
        this.updateCountLabel(count);
    }

    private int getCount() {
        return Cart.getInstance().getCount(this.item);
    }

    private void updateCountLabel(int count) {
        this.itemCountLbl.setText("x " + count);
    }

    @FXML
    ImageView itemImage;
    @FXML
    Label itemNameLbl;
    @FXML
    Label itemPriceLbl;
    @FXML
    Label itemCountLbl;
    @FXML
    Button plusBtn;
    @FXML
    Button minusBtn;

    @FXML
    public void plus() {
        this.setCount(this.getCount()+1);
    }

    @FXML
    public void minus() {
        this.setCount(this.getCount()-1);
    }
}