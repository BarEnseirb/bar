package fr.pjdevs.bar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseConnection implements AutoCloseable {
    private final static String DB_PATH =  "jdbc:sqlite:" + System.getProperty("user.dir") + "/data/db/bar.db";
    private Connection connection;

    public DatabaseConnection() throws SQLException {
        this.connection = DriverManager.getConnection(DB_PATH, "", "");
    }

    public Account getAccount(String login) throws SQLException {
        ResultSet r = this.connection.createStatement().executeQuery("select * from student where login = '" + login + "'");

        return new Account(r.getString("login"), r.getString("name"), r.getInt("money"), r.getInt("year"), r.getString("sector"));
    }

    public List<Account> getAccountList() throws SQLException {
        ResultSet r = this.connection.createStatement().executeQuery("select * from student");
        List<Account> accounts = new ArrayList<Account>();

        while (r.next()) {
            accounts.add(new Account(r.getString("login"), r.getString("name"), r.getInt("money"), r.getInt("year"), r.getString("sector")));
        }

        return accounts;
    }

    public void updateAccount(String login, Account account) throws SQLException {
        this.connection.createStatement().executeUpdate(String.format("update student set login = '%s', name = '%s', money = %s, year = %s, sector = '%s' where login = '%s'",
            account.getLogin(), account.getName(),  String.valueOf(account.getMoney()), String.valueOf(account.getYear()), account.getSector(), login));
    }

    public void createAccount(String login, String name, int year, String sector) throws SQLException {
        this.connection.createStatement().executeUpdate(String.format("insert into student (login, name, money, year, sector) VALUES ('%s', '%s', %s, %s, '%s')",
            login, name, "0", String.valueOf(year), sector));   
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

    public void createHistoryEntry(String login, String product, int price, Date date, String transaction) throws SQLException {
        this.connection.createStatement().executeUpdate(String.format("insert into history (login_student, product, price, date, \"transaction\") VALUES ('%s', '%s', %s, '%s', '%s')",
            login, product, String.valueOf(price), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date), transaction));   
    }

    public List<HistoryEntry> getHistoryEntryList() throws SQLException {
        ResultSet r = this.connection.createStatement().executeQuery("select * from history");
        List<HistoryEntry> historyEntries = new ArrayList<HistoryEntry>();

        while (r.next()) {
            historyEntries.add(new HistoryEntry(r.getString("login_student"), r.getString("product"), r.getInt("price"), r.getDate("date"), r.getString("transaction")));
        }

        return historyEntries;
    }
}
