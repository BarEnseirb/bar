package fr.pjdevs.bar;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * This class represent an Account for students in the database.
 */
public class Account {
    /**
     * Account's login
     */
    private StringProperty login;
    /**
     * Account's name
     */
    private StringProperty name;
    /**
     * Account's money
     */
    private IntegerProperty money;
    /**
     * Account's year (1A, 2A, ...)
     */
    private IntegerProperty year;
    /**
     * Account's sector (N=None, I=Info, E=Elec, M=Matmeca, R=RSI, s=SEE, T=Telecom)
     */
    private StringProperty sector;

    /**
     * Creates a new Account instance.
     * @param login Account's login
     * @param name Account's name
     * @param money Account's money
     * @param year Account's year
     * @param sector Account's sector
     */
    public Account(String login, String name, int money, int year, String sector) {
        this.login = new SimpleStringProperty(login);
        this.name = new SimpleStringProperty(name);
        this.money = new SimpleIntegerProperty(money);
        this.year = new SimpleIntegerProperty(year);
        this.sector = new SimpleStringProperty(sector);
    }

    /**
     * The textual login of this Account.
     */
    public String getLogin() {
        return this.login.get();
    }

    /**
     * Sets the login of this account.
     * @param login
     */
    public void setLogin(String login) {
        this.login.set(login);
    }

    /**
     * The login of this account.
     * @return
     */
    public StringProperty loginProperty() {
        return this.login;
    }

    /**
     * The name of this account.
     */
    public String getName() {
        return this.name.get();
    }

    /**
     * Sets the name of this account.
     * @param name The new name
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * The name of this accont.
     */
    public StringProperty nameProperty() {
        return this.name;
    }

    /**
     * Gets the money of this account.
     */
    public int getMoney() {
        return this.money.get();
    }

    /**
     * Sets the money of this 
     * @param money
     */
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

    public String getSector() {
        return this.sector.get();
    }

    public void setSector(String sector) {
        this.sector.set(sector);
    }

    public StringProperty sectorProperty() {
        return this.sector;
    }
}
