package fr.pjdevs.bar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Accounts {
    /**
     * Demo method
     */
    public void getAccountList() {
        try (Connection conn = DriverManager.getConnection(
            "jdbc:sqlite:" + System.getProperty("user.dir") + "/data/db/bar.db",
            "",
            "")) {
            
            try (Statement stmt = conn.createStatement()) {
                ResultSet r = stmt.executeQuery("SELECT * from student");

                while (r.next()) {
                    System.out.println(r.getString("NAME"));
                }
            }
            
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
}
