package com.example.formadmission;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {

    // This method is used to establish a connection to the PostgreSQL database
    public Connection connect_to_db(String dbName, String user, String password) {
        Connection connection = null;
        try {
            // Load the PostgreSQL driver
            Class.forName("org.postgresql.Driver");

            // Define the connection string (you may need to modify this based on your setup)
            String connectionUrl = "jdbc:postgresql://192.168.1.2:5432/" + dbName;

            // Establish the connection
            connection = DriverManager.getConnection(connectionUrl, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
