package fr.pjdevs.bar;

import java.math.BigDecimal;
import java.util.Optional;
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
    private int total;

    public CartTab() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/CartTab.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.total = 0;

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
        this.total = 0;

        Cart.getInstance().getItems().forEach((item, count) -> {
            this.cartItemsBox.getChildren().add(new CartItemView(item, count));
            this.total += item.getPrice() * count;
        });

        this.totalLbl.setText(BigDecimal.valueOf(this.total).movePointLeft(2) + "E");
    }

    @FXML
    public void clear() {
        Cart.getInstance().clear();
        this.update();
    }

    @FXML
    public void purchase() {
        if (Cart.getInstance().getItems().size() <= 0) {
            return;
        }

        try (DatabaseConnection c = new DatabaseConnection()) {
            Optional<Account> accountResult = new AccountChoiceDialog(c.getAccountList()).showAndWait();

            if (accountResult.isEmpty()) {
                return;
            }

            Account account = accountResult.get();
            
            if (account.money.get() >= this.total) {
                account.money.set(account.money.get() - this.total);
                c.updateAccount(account.login.get(), account);

                new Alert(AlertType.INFORMATION, "Purchased " + BigDecimal.valueOf(total).movePointLeft(2).toPlainString() + "E").show();

                Cart.getInstance().clear();
                this.update();
            } else {
                new Alert(AlertType.ERROR, "Not enough money.").show();
            }
                
        } catch (Exception e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();
        }
    }
}
