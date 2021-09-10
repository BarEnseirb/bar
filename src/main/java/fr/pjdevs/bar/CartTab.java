package fr.pjdevs.bar;

import java.math.BigDecimal;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;

public class CartTab extends Tab {
    private BigDecimal total;

    public CartTab() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/CartTab.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.getStyleClass().add("tab");
        
        this.total = new BigDecimal("0.00");

        Cart.getInstance().addListenner(new CartChangedListenner() {
            @Override
            public void onCartChanged() {
                update();
            } 
        });
    }

    @FXML
    VBox cartItemsBox;
    @FXML
    Label totalLbl;
    @FXML
    Button clearBtn;
    @FXML
    Button purchaseBtn;
    
    @FXML
    public void update() {
        this.cartItemsBox.getChildren().clear();
        this.total = new BigDecimal("0.00");

        Cart.getInstance().getItems().forEach((item, count) -> {
            this.cartItemsBox.getChildren().add(new CartItemView(item, count));
            this.total = this.total.add(item.getPrice().multiply(BigDecimal.valueOf(count)));
        });

        this.totalLbl.setText(this.total + "E");
    }

    @FXML
    public void clear() {
        Cart.getInstance().clear();
        this.update();
    }

    @FXML
    public void purchase() {
        new Alert(AlertType.INFORMATION, "Purchased " + total + "E").show();
    }
}
