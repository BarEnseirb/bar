package fr.pjdevs.bar;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;

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

        this.totalLbl.textProperty().bind(CustomStringBindings.createIntegerMoneyStringBinding(this.total));
        this.givenTotalLbl.textProperty().bind(CustomStringBindings.createIntegerMoneyStringBinding(this.givenTotal));
        this.leftTotalLbl.textProperty().bind(CustomStringBindings.createPositiveIntegerMoneyStringBinding(this.total.subtract(this.givenTotal)));

        this.backTotalLbl.textProperty().bind(CustomStringBindings.createPositiveIntegerMoneyStringBinding(this.givenTotal.subtract(this.total)));

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
        if (Cart.getInstance().getItems().size() <= 0 || this.total.get() <= 0) {
            return;
        }

        try (DatabaseConnection c = new DatabaseConnection()) {
            Optional<Account> accountResult = new AccountChoiceDialog(c.getAccountList()).showAndWait();

            if (accountResult.isEmpty()) {
                return;
            }

            Account account = accountResult.get();
            
            if (account.getMoney() >= this.total.get()) {
                account.setMoney(account.getMoney() - this.total.get());
                
                c.updateAccount(account.getLogin(), account);
                for (Map.Entry<Item, Integer> e : Cart.getInstance().getItems().entrySet()) {
                    Item item = e.getKey();
                    Integer count = e.getValue();

                    if (item.getPrice() > 0) {
                        c.createHistoryEntry(account.getLogin(), item.getName() + (count > 0 ? " x" + count : ""), this.total.get(), Date.from(Instant.now()), "Achat");
                    }
                }

                new Alert(AlertType.INFORMATION, String.format("Achat de %s effectue.", CustomStringBindings.createIntegerMoneyStringBinding(this.total).get())).show();

                Cart.getInstance().clear();
                this.update();
            } else {
                new Alert(AlertType.ERROR, "Pas assez d'argent sur ce compte.").show();
            }
        } catch (SQLException e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    public void purchaseMoney() {
        if (Cart.getInstance().getItems().size() <= 0 || this.total.get() <= 0) {
            return;
        }

        if (this.givenTotal.get() >= this.total.get()) {
            this.clear();
            this.clearMoney();
        } else {
            new Alert(AlertType.ERROR, "Pas assez d'argent.").show();
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
