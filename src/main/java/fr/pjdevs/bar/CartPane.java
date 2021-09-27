package fr.pjdevs.bar;

import java.util.Optional;
import java.io.IOException;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;

public class CartPane extends VBox {
    private IntegerProperty total;
    private IntegerProperty givenTotal;

    @FXML
    private VBox cartItemsBox;
    @FXML
    private Button clearBtn;
    @FXML
    private Button purchaseBtn;
    @FXML
    private Label totalLbl;
    @FXML
    private Label givenTotalLbl;
    @FXML
    private Label backTotalLbl;
    @FXML
    private Label leftTotalLbl;

    public CartPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/CartPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.total = new SimpleIntegerProperty(0);
        this.givenTotal = new SimpleIntegerProperty(0);

        this.totalLbl.textProperty().bind(this.total.divide(100.0).asString("%.2fE"));
        this.givenTotalLbl.textProperty().bind(this.givenTotal.divide(100.0).asString("%.2fE"));
        this.leftTotalLbl.textProperty().bind(Bindings.createStringBinding(() -> {
            int left = total.get() - givenTotal.get();
            if (left < 0) return "0,00E";
            else return String.format("%,.2fE", left/100.0);
        }, this.total, this.givenTotal));
        this.backTotalLbl.textProperty().bind(Bindings.createStringBinding(() -> {
            int back = givenTotal.get() - total.get();
            if (back < 0) return "0,00E";
            else return String.format("%,.2fE", back/100.0);
        }, this.total, this.givenTotal));

        Cart.getInstance().addListenner(new ChangedListenner() {
            @Override
            public void onChanged() {
                update();
            }
        });
    }

    private void update() {
        this.cartItemsBox.getChildren().clear();
        this.total.set(0);

        Cart.getInstance().getItems().forEach((item, count) -> {
            this.cartItemsBox.getChildren().add(new CartItemView(item, count));
            this.total.set(this.total.get() + item.getPrice() * count);
        });
    }

    @FXML
    public void clear() {
        Cart.getInstance().clear();
    }

    @FXML
    public void clearMoney() {
        this.givenTotal.set(0);
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
            
            if (account.money.get() >= this.total.get()) {
                account.money.set(account.money.get() - this.total.get());
                c.updateAccount(account.login.get(), account);

                new Alert(AlertType.INFORMATION, String.format("Purschased %,.2fE", this.total.get()/100.0)).show();

                Cart.getInstance().clear();
                this.update();
            } else {
                new Alert(AlertType.ERROR, "Not enough money.").show();
            }
                
        } catch (Exception e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    public void purchaseMoney() {
        if (this.givenTotal.get() >= this.total.get()) {
            this.clear();
            this.clearMoney();

            new Alert(AlertType.INFORMATION, String.format("Purschased %,.2fE", this.total.get()/100.0)).show();
        } else {
            new Alert(AlertType.ERROR, "Not enough money.").show();
        }
    }

    private void add(int amount) {
        this.givenTotal.set(this.givenTotal.get()+amount);
    }

    @FXML public void add1Cent()   { this.add(1);     }
    @FXML public void add2Cent()   { this.add(2);     }
    @FXML public void add5Cent()   { this.add(5);     }
    @FXML public void add10Cent()  { this.add(10);    }
    @FXML public void add20Cent()  { this.add(20);    }
    @FXML public void add50Cent()  { this.add(50);    }
    @FXML public void add1Euro()   { this.add(100);   }
    @FXML public void add2Euro()   { this.add(200);   }
    @FXML public void add5Euro()   { this.add(500);   }
    @FXML public void add10Euro()  { this.add(1000);  }
    @FXML public void add20Euro()  { this.add(2000);  }
    @FXML public void add50Euro()  { this.add(5000);  }
    @FXML public void add100Euro() { this.add(10000); }
    @FXML public void add200Euro() { this.add(20000); }
    @FXML public void add500Euro() { this.add(50000); }
    
}
