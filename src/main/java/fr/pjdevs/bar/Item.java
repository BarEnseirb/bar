package fr.pjdevs.bar;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Item {
    private final StringProperty name;
    private final IntegerProperty price;
    private final StringProperty description;

    public Item(String name, int price, String description) {
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleIntegerProperty(price);
        this.description = new SimpleStringProperty(description);
    }

    public String getName() {
        return this.name.get();
    }

    public int getPrice() {
        return this.price.get();
    }

    public String getDesciption() {
        return this.description.get();
    }

    public StringProperty nameProperty() {
        return this.name;
    }

    public IntegerProperty priceProperty() {
        return this.price;
    }

    public StringProperty descriptionProperty() {
        return this.description;
    }
}
