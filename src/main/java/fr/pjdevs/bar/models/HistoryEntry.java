package fr.pjdevs.bar.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class HistoryEntry {
    private StringProperty studentLogin;
    private StringProperty product;
    private IntegerProperty price;
    private ObjectProperty<Date> date;
    private StringProperty transaction;

    public HistoryEntry(String login, String product, int price, Date date, String transaction) {
        this.studentLogin = new SimpleStringProperty(login);
        this.product = new SimpleStringProperty(product);
        this.price = new SimpleIntegerProperty(price);
        this.date = new SimpleObjectProperty<Date>();
        this.date.set(date);
        this.transaction = new SimpleStringProperty(transaction);
    }

    public String getStudentLogin() {
        return this.studentLogin.get();
    }

    public StringProperty studentLoginProperty() {
        return this.studentLogin;
    }

    public String getProduct() {
        return this.product.get();
    }

    public StringProperty productProperty() {
        return this.product;
    }

    public int getPrice() {
        return this.price.get();
    }

    public IntegerProperty priceProperty() {
        return this.price;
    }

    public Date getDate() {
        return this.date.get();
    }

    public ObjectProperty<Date> dateProperty() {
        return this.date;
    }

    public String getTransaction() {
        return this.transaction.get();
    }

    public StringProperty transactionProperty() {
        return this.transaction;
    }
}
