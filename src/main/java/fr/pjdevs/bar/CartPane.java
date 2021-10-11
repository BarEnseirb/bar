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

/**
 * Cart component where {@link Cart}, prices, purchase/clear buttons are displayed.
 */
public class CartPane extends VBox {
    /**
     * Property for the total price of the {@link Cart}.
     */
    private IntegerProperty total;
    /**
     * Property for the given total with real money.
     */
    private IntegerProperty givenTotal;

    /**
     * Box where all the {@link CartItemView} are displayed.
     */
    @FXML
    private VBox cartItemsBox;
    /**
     * Button to clear the {@link Cart}.
     */
    @FXML
    private Button clearBtn;
    /**
     * Button to purchase with an account.
     */
    @FXML
    private Button purchaseBtn;
    /**
     * Label for {@link #total}.
     */
    @FXML
    private Label totalLbl;
    /**
     * Label for {@link #givenTotal}.
     */
    @FXML
    private Label givenTotalLbl;
    /**
     * Label for the money to get back.
     */
    @FXML
    private Label backTotalLbl;
    /**
     * Label for the money left to pay.
     */
    @FXML
    private Label leftTotalLbl;

    /**
     * Creates a new CartPane instance.
     */
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

        this.totalLbl.textProperty().bind(MoneyStringBindings.createIntegerMoneyStringBinding(this.total));
        this.givenTotalLbl.textProperty().bind(MoneyStringBindings.createIntegerMoneyStringBinding(this.givenTotal));
        this.leftTotalLbl.textProperty().bind(MoneyStringBindings.createPositiveIntegerMoneyStringBinding(this.total.subtract(this.givenTotal)));

        this.backTotalLbl.textProperty().bind(MoneyStringBindings.createPositiveIntegerMoneyStringBinding(this.givenTotal.subtract(this.total)));

        Cart.getInstance().addListenner(new ChangedListenner() {
            @Override
            public void onChanged() {
                update();
            }
        });
    }

    /**
     * Update this component accordig to the {@link Cart} content.
     */
    private void update() {
        this.cartItemsBox.getChildren().clear();
        this.total.set(0);

        Cart.getInstance().getItems().forEach((item, count) -> {
            this.cartItemsBox.getChildren().add(new CartItemView(item, count));
            this.total.set(this.total.get() + item.getPrice() * count);
        });
    }

    /**
     * FXML accessible method to clear the {@link Cart}.
     */
    @FXML
    public void clear() {
        Cart.getInstance().clear();
    }

    /**
     * FXML accessible method to clear the money.
     */
    @FXML
    public void clearMoney() {
        this.givenTotal.set(0);
    }

    /**
     * FXML accessible method to purchase with an account.
     */
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

                new Alert(AlertType.INFORMATION, String.format("Achat de %s effectue.", MoneyStringBindings.createIntegerMoneyStringBinding(this.total).get())).show();

                Cart.getInstance().clear();
                this.update();
            } else {
                new Alert(AlertType.ERROR, "Pas assez d'argent sur ce compte.").show();
            }
        } catch (SQLException e) {
            new Alert(AlertType.ERROR, e.getMessage()).show();
        }
    }

    /**
     * FXML accessible method to purchase with given money.
     */
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

    /**
     * Convenient method to add money.
     * @param amount The amount of money to add.
     */
    private void add(int amount) {
        this.givenTotal.set(this.givenTotal.get()+amount);
    }

    /**
     * FXML accesible method to add 1 cent to the given money.
     */
    @FXML public void add1Cent() { this.add(1); }
    /**
     * FXML accesible method to add 2 cent to the given money.
     */
    @FXML public void add2Cent() { this.add(2); }
    /**
     * FXML accesible method to add 5 cent to the given money.
     */
    @FXML public void add5Cent() { this.add(5); }
    /**
     * FXML accesible method to add 10 cent to the given money.
     */
    @FXML public void add10Cent() { this.add(10); }
    /**
     * FXML accesible method to add 20 cent to the given money.
     */
    @FXML public void add20Cent() { this.add(20); }
    /**
     * FXML accesible method to add 50 cent to the given money.
     */
    @FXML public void add50Cent() { this.add(50); }
    /**
     * FXML accesible method to add 1 euro to the given money.
     */
    @FXML public void add1Euro() { this.add(100); }
    /**
     * FXML accesible method to add 2 euro to the given money.
     */
    @FXML public void add2Euro() { this.add(200); }
    /**
     * FXML accesible method to add 5 euros to the given money.
     */
    @FXML public void add5Euro() { this.add(500); }
    /**
     * FXML accesible method to add 10 euros to the given money.
     */
    @FXML public void add10Euro() { this.add(1000); }
    /**
     * FXML accesible method to add 20 euros to the given money.
     */
    @FXML public void add20Euro() { this.add(2000); }
    /**
     * FXML accesible method to add 50 euros to the given money.
     */
    @FXML public void add50Euro()  { this.add(5000);  }
    /**
     * FXML accesible method to add 100 euros to the given money.
     */
    @FXML public void add100Euro() { this.add(10000); }
    /**
     * FXML accesible method to add 200 euros to the given money.
     */
    @FXML public void add200Euro() { this.add(20000); }
    /**
     * FXML accesible method to add 500 euros to the given money.
     */
    @FXML public void add500Euro() { this.add(50000); }
    
}
