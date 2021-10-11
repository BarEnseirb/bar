package fr.pjdevs.bar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class that represents a connection to the application's main SQLite database.
 * It embeds functions to add/remove/update {@link Account} and {@link HistoryEntry} in the database.
 */
public class DatabaseConnection implements AutoCloseable {
    /**
     * The SQLite database path.
     */
    private final static String DB_PATH =  "jdbc:sqlite:" + System.getProperty("user.dir") + "/data/db/bar.db";
    /**
     * The underlying JDBC connection.
     */
    private Connection connection;

    /**
     * Creates a new instance of DatabaseConnection by intializing a JDBC connection.
     * @throws SQLException If JDBC cannot init the connection to the database.
     */
    public DatabaseConnection() throws SQLException {
        this.connection = DriverManager.getConnection(DB_PATH, "", "");
    }

    /**
     * Gets an account by login (primary key) in the database.
     * @param login The login of the account to get.
     * @return An {@link Account} instance representing the account with this login in the database.
     * @throws SQLException If JDBC cannot connect to the database or if the query fails.
     */
    public Account getAccount(String login) throws SQLException {
        ResultSet r = this.connection.createStatement().executeQuery("select * from student where login = '" + login + "'");

        return new Account(r.getString("login"), r.getString("name"), r.getInt("money"), r.getInt("year"), r.getString("sector"));
    }

    /**
     * Gets the whole list of accounts in the database.
     * @return A list of {@link Account} instance representing the accounts in the database.
     * @throws SQLException If JDBC cannot connect to the database or if the query fails.
     */
    public List<Account> getAccountList() throws SQLException {
        ResultSet r = this.connection.createStatement().executeQuery("select * from student");
        List<Account> accounts = new ArrayList<Account>();

        while (r.next()) {
            accounts.add(new Account(r.getString("login"), r.getString("name"), r.getInt("money"), r.getInt("year"), r.getString("sector")));
        }

        return accounts;
    }

    /**
     * Updates an account by login (primary key) in the database.
     * @param login The login of the account to update.
     * @param account The new account properties.
     * @throws SQLException If JDBC cannot connect to the database or if the query fails.
     */
    public void updateAccount(String login, Account account) throws SQLException {
        this.connection.createStatement().executeUpdate(String.format("update student set login = '%s', name = '%s', money = %s, year = %s, sector = '%s' where login = '%s'",
            account.getLogin(), account.getName(),  String.valueOf(account.getMoney()), String.valueOf(account.getYear()), account.getSector(), login));
    }

    /**
     * Creates an account with given properties and money set to 0.
     * @param login The login of the new account.
     * @param name The name of the new account.
     * @param year The year of the new account.
     * @param sector The sector of the new account.
     * @throws SQLException If JDBC cannot connect to the database or if the query fails.
     */
    public void createAccount(String login, String name, int year, String sector) throws SQLException {
        this.connection.createStatement().executeUpdate(String.format("insert into student (login, name, money, year, sector) VALUES ('%s', '%s', %s, %s, '%s')",
            login, name, "0", String.valueOf(year), sector));   
    }

    /**
     * Removes an account by login (primary key) in the database.
     * @param login
     * @throws SQLException If JDBC cannot connect to the database or if the query fails.
     */
    public void removeAccount(String login) throws SQLException {
        this.connection.createStatement().executeUpdate("delete from student where login = '" + login + "'");
    }

    /**
     * Adds one year to all students of the database.
     * @throws SQLException If JDBC cannot connect to the database or if the query fails.
     */
    public void nextYear() throws SQLException {
        this.connection.createStatement().executeUpdate("update student set year = year+1");
    }

    /**
     * Creates a history entry in the database.
     * @param login The login of the concerned account.
     * @param product The product name.
     * @param price The price of this entry.
     * @param date The date of the history entry.
     * @param transaction The transaction name of this history entry.
     * @throws SQLException If JDBC cannot connect to the database or if the query fails.
     */
    public void createHistoryEntry(String login, String product, int price, Date date, String transaction) throws SQLException {
        this.connection.createStatement().executeUpdate(String.format("insert into history (login_student, product, price, date, \"transaction\") VALUES ('%s', '%s', %s, '%s', '%s')",
            login, product, String.valueOf(price), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date), transaction));   
    }

    /**
     * Gets the whole list of history entries in the database.
     * @return A list of {@link HistoryEntry} instance representing the history in the database.
     * @throws SQLException If JDBC cannot connect to the database or if the query fails.
     */
    public List<HistoryEntry> getHistoryEntryList() throws SQLException {
        ResultSet r = this.connection.createStatement().executeQuery("select * from history");
        List<HistoryEntry> historyEntries = new ArrayList<HistoryEntry>();

        while (r.next()) {
            historyEntries.add(new HistoryEntry(r.getString("login_student"), r.getString("product"), r.getInt("price"), r.getDate("date"), r.getString("transaction")));
        }

        return historyEntries;
    }

    @Override
    public void close() throws SQLException {
        this.connection.close();
    }
}
