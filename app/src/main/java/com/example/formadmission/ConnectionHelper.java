package com.example.formadmission;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.sql.*;

public class ConnectionHelper {

    public Connection connect_to_db(String dbname, String user, String pass) {
        Connection conn = null;
        try {
            // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");

            // Use 10.0.2.2 for Emulator, replace with server IP for a real device
            String url = "jdbc:postgresql://192.168.1.2:5432/" + dbname;
            conn = DriverManager.getConnection(url, user, pass);

            if (conn != null) {
                Log.i("DB Connection", "Connected Successfully");
            } else {
                Log.e("DB Connection", "Connection Failed");
            }
        } catch (Exception e) {
            Log.e("DB Error", e.getMessage());
        }
        return conn;
    }
}
