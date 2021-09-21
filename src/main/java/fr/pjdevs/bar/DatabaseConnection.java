package fr.pjdevs.bar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection implements AutoCloseable {
    private final static String DB_PATH =  "jdbc:sqlite:" + System.getProperty("user.dir") + "/data/db/bar.db";
    private Connection connection;

    public DatabaseConnection() throws SQLException {
        this.connection = DriverManager.getConnection(DB_PATH, "", "");
    }

    public Account getAccount(String login) throws SQLException {
        ResultSet r = this.connection.createStatement().executeQuery("select * from student where login = '" + login + "'");

        return new Account(r.getInt("oid"), r.getString("login"), r.getString("name"), r.getInt("money"), r.getInt("year"), r.getInt("class"));
    }

    public List<Account> getAccountList() throws SQLException {
        ResultSet r = this.connection.createStatement().executeQuery("select * from student");
        List<Account> accounts = new ArrayList<Account>();

        while (r.next()) {
            accounts.add(new Account(r.getInt("oid"), r.getString("login"), r.getString("name"), r.getInt("money"), r.getInt("year"), r.getInt("class")));
        }

        return accounts;
    }

    public void updateAccount(String login, Account account) throws SQLException{
        this.connection.createStatement().executeUpdate(String.format("update student set login = '%s', name = '%s', money = %s, year = %s where login = '%s'",
            account.login.get(), account.name.get(),  String.valueOf(account.money.get()), String.valueOf(account.year.get()), login));
    }

    public void createAccount(String login, String name, int year, int sector) throws SQLException{
        this.connection.createStatement().executeUpdate(String.format("insert into student (login, name, money, threshold, class, year) VALUES ('%s', '%s', %s, 0, %s, %s)",
            login, name, "0", String.valueOf(sector), String.valueOf(year)));   
    }

    public void removeAccount(String login) throws SQLException {
        this.connection.createStatement().executeUpdate("delete from student where login = '" + login + "'");
    }

    public void nextYear() throws SQLException {
        this.connection.createStatement().executeUpdate("update student set year = year+1");
    }

    public void close() throws SQLException {
        this.connection.close();
    }
}
