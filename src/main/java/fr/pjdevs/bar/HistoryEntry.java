package fr.pjdevs.bar;

import javafx.beans.property.SimpleStringProperty;

import java.time.Instant;
import java.util.Date;

import javafx.beans.property.SimpleIntegerProperty;

public class HistoryEntry {
    public enum Transaction {
        PURCHASE,
        MOVEMENT
    };

    public SimpleIntegerProperty studentid;
    public SimpleStringProperty product;
    public SimpleIntegerProperty price;
    public SimpleStringProperty date;
    public SimpleStringProperty transaction;

    private HistoryEntry(int studentid, String product, int price, String date, String transaction) {
        this.studentid = new SimpleIntegerProperty(studentid);
        this.product = new SimpleStringProperty(product);
        this.price = new SimpleIntegerProperty(price);
        this.date = new SimpleStringProperty(date);
        this.transaction = new SimpleStringProperty(transaction);
    }

    public static HistoryEntry PurchaseEntry(Account account) {
        Cart cart = Cart.getInstance();
        int total = 0;

        return new HistoryEntry(account.getId(), "Cart", total, Date.from(Instant.now()).toString(), "Achat");
    }
}
