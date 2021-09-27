package fr.pjdevs.bar;

public class Item {
    private String name;
    private int price;
    private String description;

    public Item(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    public String getDesciption() {
        return this.description;
    }
}
