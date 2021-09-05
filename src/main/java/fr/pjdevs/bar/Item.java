package fr.pjdevs.bar;

public class Item {
    private String name;
    private double price;
    private String imagePath;

    public Item(String name, double price, String imagePath) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public String getImagePath() {
        return this.imagePath;
    }
}
