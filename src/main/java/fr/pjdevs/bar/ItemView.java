package fr.pjdevs.bar;

import java.io.IOException;

import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
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
        this.itemPriceLbl.setText(item.getPrice() + "E");
        this.item = item;
    }

    @FXML
    ImageView itemImage;
    @FXML
    Spinner<Integer> countSpinner;
    @FXML
    Label itemNameLbl;
    @FXML
    Label itemPriceLbl;

    @FXML
    public void addToCart() {
        int count = countSpinner.getValue();

        Cart.getInstance().add(this.item, count);
        new Alert(AlertType.INFORMATION, "Added " + count + " " + this.item.getName() + " to the cart.").show();
    }
}