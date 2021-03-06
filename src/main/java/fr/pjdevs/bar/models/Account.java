package fr.pjdevs.bar.models;

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
     * @return The login of this account.
     */
    public String getLogin() {
        return this.login.get();
    }

    /**
     * Sets the login of this account.
     * @param login The new login.
     */
    public void setLogin(String login) {
        this.login.set(login);
    }

    /**
     * The login of this account.
     * @return The login property.
     */
    public StringProperty loginProperty() {
        return this.login;
    }

    /**
     * The name of this account.
     * @return The name.
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
     * The name of this account.
     * @return The name property.
     */
    public StringProperty nameProperty() {
        return this.name;
    }

    /**
     * Gets the money of this account.
     * @return The money property.
     */
    public int getMoney() {
        return this.money.get();
    }

    /**
     * Sets the money of this account.
     * @param money The new money value.
     */
    public void setMoney(int money) {
        this.money.set(money);
    }

    /**
     * The money in centimes of this account.
     * @return The money property.
     */
    public IntegerProperty moneyProperty() {
        return this.money;
    }

    /**
     * Gets the year of this account.
     * @return The year property.
     */
    public int getYear() {
        return this.year.get();
    }

    /**
     * Sets the year of this account.
     * @param year The new year value.
     */
    public void setYear(int year) {
        this.year.set(year);
    }

    /**
     * The year of this account.
     * @return The year property.
     */
    public IntegerProperty yearProperty() {
        return this.year;
    }

    /**
     * Gets the sector of this account.
     * @return The sector.
     */
    public String getSector() {
        return this.sector.get();
    }

    /**
     * Sets the sector of this account.
     * @param sector The new sector value.
     */
    public void setSector(String sector) {
        this.sector.set(sector);
    }

    /**
     * The sector of this account.
     * @return The sector property.
     */
    public StringProperty sectorProperty() {
        return this.sector;
    }
}
