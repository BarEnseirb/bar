package fr.pjdevs.bar;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;

public final class AccountList {

    private final static String DB_PATH =  "jdbc:sqlite:" + System.getProperty("user.dir") + "/data/db/bar.db";
    private static AccountList instance;

    private List<Account> accountList;

    private AccountList() {
        try {
            accountList = this.queryList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Main method to access the singleton.
     * @return Returns the unique instance of the Singleton class.
     */
    public static AccountList getInstance() {
        if (instance == null) {
            instance = new AccountList();
        }
        return instance;
    }

    /**
     * Query the list of account in the database.
     * @return The list of {@link Account} in the db.
     */
    private void makeStatement(Consumer<Statement> statementCallBack) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_PATH, "", "")) {
            try (Statement stmt = conn.createStatement()) {
                statementCallBack.accept(stmt);
            }
        }
    }

    private List<Account> queryList() throws SQLException {
        List<Account> accountList = new ArrayList<Account>();
        SQLException ex = null;

        try {
            makeStatement((stmt) -> {
                try {
                    ResultSet r =  stmt.executeQuery("select * from student");

                    while (r.next()) {
                        String login = r.getString("login");
                        String name = r.getString("name");
                        BigDecimal money = r.getBigDecimal("money").movePointLeft(2);
                        int year = r.getInt("year");
                    
                        accountList.add(new Account(login, name, money, year));
                    }
                } catch (SQLException e) {
                    new Alert(AlertType.ERROR, "Could not fetch an account correctly" + e.getMessage()).show();
                }
            });
        } catch (SQLException e) {
            ex = e;
        }

        if (ex == null)
            return accountList;
        else throw ex;
    }

    /**
     * Retrieves the list of all student account.
     * @return Returns the list of all {@link Account}.
     */
    public List<Account> getList() {
        return this.accountList;
    }

    public void updateMoney(String login, BigDecimal money) {
        try {
            makeStatement((stmt) -> {
                try {
                    stmt.executeUpdate("update student set money = " + money.unscaledValue() + "where login = " +  login);
                } catch (SQLException e) {
                    new Alert(AlertType.ERROR, "Could not execute a statement :\n" + e.getMessage()).show();
                }
            });
        } catch (SQLException e) {
            new Alert(AlertType.ERROR, "Could not update money of " + login).show();
        }
    }
}
