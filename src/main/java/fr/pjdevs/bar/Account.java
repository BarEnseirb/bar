package fr.pjdevs.bar;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Account {
    public SimpleStringProperty login;
    public SimpleStringProperty name;
    public SimpleIntegerProperty money;
    public SimpleIntegerProperty year;
    public SimpleIntegerProperty sector;

    public Account(String login, String name, int money, int year, int sector) {
        this.login = new SimpleStringProperty(login);
        this.name = new SimpleStringProperty(name);
        this.money = new SimpleIntegerProperty(money);
        this.year = new SimpleIntegerProperty(year);
        this.sector = new SimpleIntegerProperty(sector);
    }
}
