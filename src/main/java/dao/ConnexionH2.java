package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionH2 {
    private static final String URL = "jdbc:h2:./xoudouqiDB";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Erreur H2 : " + e.getMessage());
            return null;
        }
    }
}