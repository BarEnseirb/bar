package fr.pjdevs.bar;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Account {
    private SimpleStringProperty login;
    private SimpleStringProperty name;
    private SimpleIntegerProperty money;
    private SimpleIntegerProperty year;
    private SimpleIntegerProperty sector;

    public Account(String login, String name, int money, int year, int sector) {
        this.login = new SimpleStringProperty(login);
        this.name = new SimpleStringProperty(name);
        this.money = new SimpleIntegerProperty(money);
        this.year = new SimpleIntegerProperty(year);
        this.sector = new SimpleIntegerProperty(sector);
    }

    public String getLogin() {
        return this.login.get();
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public StringProperty loginProperty() {
        return this.login;
    }

    public String getName() {
        return this.name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return this.name;
    }

    public int getMoney() {
        return this.money.get();
    }

    public void setMoney(int money) {
        this.money.set(money);
    }

    public IntegerProperty moneyProperty() {
        return this.money;
    }

    public int getYear() {
        return this.year.get();
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public IntegerProperty yearProperty() {
        return this.year;
    }

    public int getSector() {
        return this.sector.get();
    }

    public void setSector(int sector) {
        this.sector.set(sector);
    }

    public IntegerProperty sectorProperty() {
        return this.sector;
    }
}
