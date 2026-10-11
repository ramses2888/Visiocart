package org.example.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/visiocart_db";
    private static final String USER = "root"; // tu usuario
    private static final String PASSWORD = "Rspviqldo2027@2"; // tu clave
    public static Connection getConnection() throws SQLException {
        try {
            // Fuerza la carga del driver JDBC de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("No se encontró la clase del driver MySQL: " + e.getMessage());
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
