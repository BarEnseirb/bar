package fr.pjdevs.bar;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class AccountList {

    private final static String DB_PATH =  "jdbc:sqlite:" + System.getProperty("user.dir") + "/data/db/bar.db";
    private static AccountList instance;

    private AccountList() {
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

    public List<Account> queryList() throws SQLException {
        List<Account> accountList = new ArrayList<Account>();
        SQLException ex = null;

        try {
            makeStatement((stmt) -> {
                try {
                    ResultSet r =  stmt.executeQuery("select * from student");

                    while (r.next()) {
                        String login = r.getString("login");
                        String name = r.getString("name");
                        BigDecimal money = r.getBigDecimal("money");
                        int year = r.getInt("year");
                    
                        accountList.add(new Account(login, name, money, year));
                    }
                } catch (SQLException e) {
                    
                }
            });
        } catch (SQLException e) {
            ex = e;
        }

        if (ex == null)
            return accountList;
        else throw ex;
    }
}
