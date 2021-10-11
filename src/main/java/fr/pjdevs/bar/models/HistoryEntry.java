package fr.pjdevs.bar.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Class that represents an history entry in the database.
 */
public class HistoryEntry {
    /**
     * Login of the student concerned by the entry.
     */
    private StringProperty studentLogin;
    /**
     * The product name of the entry.
     */
    private StringProperty product;
    /**
     * The price of the entry.
     */
    private IntegerProperty price;
    /**
     * The date of the entry.
     */
    private ObjectProperty<Date> date;
    /**
     * The transaction type of the entry.
     */
    private StringProperty transaction;

    /**
     * Creates a new HistoryEntry instance.
     * @param login Login of the student concerned by the entry.
     * @param product The product name of the entry.
     * @param price The price of the entry.
     * @param date The date of the entry.
     * @param transaction The transaction type of the entry.
     */
    public HistoryEntry(String login, String product, int price, Date date, String transaction) {
        this.studentLogin = new SimpleStringProperty(login);
        this.product = new SimpleStringProperty(product);
        this.price = new SimpleIntegerProperty(price);
        this.date = new SimpleObjectProperty<Date>();
        this.date.set(date);
        this.transaction = new SimpleStringProperty(transaction);
    }

    /**
     * Gets the login of the student concerned by this entry.
     */
    public String getStudentLogin() {
        return this.studentLogin.get();
    }

    /**
     * The login of the student concerned by this entry.
     */
    public StringProperty studentLoginProperty() {
        return this.studentLogin;
    }

    /**
     * Gets the product name of this entry.
     */
    public String getProduct() {
        return this.product.get();
    }

    /**
     * The product name of this entry.
     */
    public StringProperty productProperty() {
        return this.product;
    }

    /**
     * Gets the price of this entry.
     */
    public int getPrice() {
        return this.price.get();
    }

    /**
     * The price of this entry.
     */
    public IntegerProperty priceProperty() {
        return this.price;
    }

    /**
     * Gets the date of this entry.
     */
    public Date getDate() {
        return this.date.get();
    }

    /**
     * The date of this entry.
     */
    public ObjectProperty<Date> dateProperty() {
        return this.date;
    }

    /**
     * Gets the transaction type of this entry.
     */
    public String getTransaction() {
        return this.transaction.get();
    }

    /**
     * The transaction type of this entry.
     */
    public StringProperty transactionProperty() {
        return this.transaction;
    }
}
