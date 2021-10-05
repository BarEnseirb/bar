package fr.pjdevs.bar;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

public class Item {
    private final StringProperty name;
    private final IntegerProperty price;
    private final StringProperty description;
    private final ObjectProperty<Color> color;

    public Item(String name, int price, String description) {
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleIntegerProperty(price);
        this.description = new SimpleStringProperty(description);
        this.color = new SimpleObjectProperty<Color>(new Color(0.0, 0.0, 1.0, 1.0));
    }

    public Item(String name, int price, String description, Color color) {
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleIntegerProperty(price);
        this.description = new SimpleStringProperty(description);
        this.color = new SimpleObjectProperty<Color>(color);
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

    public Color getColor() {
        return this.color.get();
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public ObjectProperty<Color> colorProperty() {
        return this.color;
    }
}
