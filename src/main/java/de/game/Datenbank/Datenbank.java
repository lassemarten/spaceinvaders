package main.java.de.game.Datenbank;

import java.sql.Connection;
import java.sql.DriverManager;

    public class Datenbank {

        public static Connection getConnection() throws Exception {
            String url = "jdbc:mysql://localhost:3306/Spaceinvaders";
            String user = "root";
            String password = "Carlotta19!";

            return DriverManager.getConnection(url, user , password);
        }
    }
