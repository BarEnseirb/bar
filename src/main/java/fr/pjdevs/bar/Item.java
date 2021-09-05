package fr.pjdevs.bar;

import java.math.BigDecimal;

public class Item {
    private String name;
    private BigDecimal price;
    private String imagePath;

    public Item(String name, BigDecimal price, String imagePath) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public String getImagePath() {
        return this.imagePath;
    }
}
