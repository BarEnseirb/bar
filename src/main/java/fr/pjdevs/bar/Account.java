package fr.pjdevs.bar;

import java.math.BigDecimal;

public class Account {
    private String login;
    private String name;
    private BigDecimal money;
    private int year;

    public Account(String login, String name, BigDecimal money, int year) {
        this.login = login;
        this.name = name;
        this.money = money;
        this.year = year;
    }

    public String getLogin() {
        return this.login;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getMoney() {
        return this.money;
    }

    public int getYear() {
        return this.year;
    }
}
