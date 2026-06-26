package main.java.de.game.Datenbank;

// Die Methode getConnection stellt eine Verbindung zur MySQL-Datenbank her, was die ganze Aufgabe dieser Klasse ist.

import java.sql.Connection;
import java.sql.DriverManager;

    public class Datenbank {

        public static Connection getConnection() throws Exception {
            String url = "jdbc:mysql://localhost:3306/Spaceinvaders";
            String user = "spieler";
            String password = "Carlotta19!";

            return DriverManager.getConnection(url, user , password);
        }
    }
