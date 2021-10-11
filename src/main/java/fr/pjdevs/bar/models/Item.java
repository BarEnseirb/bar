package fr.pjdevs.bar.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

/**
 * Class that represents items available for sale.
 */
public class Item {
    /**
     * The name of the item.
     */
    private final StringProperty name;
    /**
     * The price of the item.
     */
    private final IntegerProperty price;
    /**
     * The description of the item.
     */
    private final StringProperty description;
    /**
     * The color of the item.
     */
    private final ObjectProperty<Color> color;

    /**
     * Creates a new intance Item with given name, price, description and default color.
     * @param name The name of the item.
     * @param price The price of the item.
     * @param description The description of the item.
     */
    public Item(String name, int price, String description) {
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleIntegerProperty(price);
        this.description = new SimpleStringProperty(description);
        this.color = new SimpleObjectProperty<Color>(new Color(0.0, 0.0, 1.0, 1.0));
    }

    /**
     * Creates a new instance of Item with given name, price, description and color.
     * @param name The name of the item.
     * @param price The price of the item.
     * @param description The description of the item.
     * @param color The color of the item.
     */
    public Item(String name, int price, String description, Color color) {
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleIntegerProperty(price);
        this.description = new SimpleStringProperty(description);
        this.color = new SimpleObjectProperty<Color>(color);
    }

    /**
     * Gets the name of this item.
     */
    public String getName() {
        return this.name.get();
    }

    /**
     * Gets the price of this item.
     */
    public int getPrice() {
        return this.price.get();
    }

    /**
     * Gets the description of this item.
     */
    public String getDesciption() {
        return this.description.get();
    }

    /**
     * The name of this item.
     */
    public StringProperty nameProperty() {
        return this.name;
    }

    /**
     * The price of this item.
     */
    public IntegerProperty priceProperty() {
        return this.price;
    }

    /**
     * The description of this item.
     */
    public StringProperty descriptionProperty() {
        return this.description;
    }

    /**
     * Gets the color of this item.
     */
    public Color getColor() {
        return this.color.get();
    }

    /**
     * Sets the color of this item.
     * @param color The new color.
     */
    public void setColor(Color color) {
        this.color.set(color);
    }

    /**
     * The color of this item.
     */
    public ObjectProperty<Color> colorProperty() {
        return this.color;
    }
}
