package main.java.de.game.Datenbank;

import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

// Diese Klasse startet einen einfachen HTTP-Server für das Spiel "Space Invaders".
// Der Server stellt zwei Schnittstellen (Endpoints) bereit:
// 1. POST /highscore → Speichert einen neuen Highscore in der Datenbank
// 2. GET /highscores → Liefert die Top 10 Highscores als JSON zurück
// Die Kommunikation erfolgt über HTTP, und die Daten werden mit JSON (über Gson) verarbeitet.
// So kann das Spiel Highscores senden und abrufen.

public class Server {

    public static void starteServer() throws Exception {


        // Server auf Port 8080 starten
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Endpoint: /highscore
        server.createContext("/highscore", exchange -> {

            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) { //Lieber if not abfragen?!
                exchange.sendResponseHeaders(405, -1);
                return;

            }
            try {

                String body = new String(exchange.getRequestBody().readAllBytes());//Liest alles, was der Client geschickt hat, und wandel es in Text um

                Gson gson = new Gson();
                HighscoreEintrag eintrag = gson.fromJson(body, HighscoreEintrag.class);

                if( eintrag == null || eintrag.getName() == null || eintrag.getName().isEmpty() ){
                    throw new IllegalArgumentException("Name fehlt");
                }

                try (var conn = Datenbank.getConnection()){
                    String sql = "INSERT INTO highscore (player, score, quote) VALUES (?, ?, ?)";
                    var stmt = conn.prepareStatement(sql);
                    stmt.setString(1, eintrag.getName());
                    stmt.setInt(2, eintrag.getScore());
                    stmt.setDouble(3, eintrag.getQuote());
                    stmt.executeUpdate();
                }

                String response = "Gespeichert!";
                byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, bytes.length);

                try (OutputStream outputStream = exchange.getResponseBody()) {
                    outputStream.write(bytes);
                }
                } catch (Exception e) {
                    e.printStackTrace();

                    String response = "Fehler beim Speichern";
                    byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

                    exchange.sendResponseHeaders(500, bytes.length);

                    try (OutputStream outputStream = exchange.getResponseBody()) {
                        outputStream.write(bytes);
                    }
                    return;
                }

        });

        // GET → Scores abrufen
        server.createContext("/highscores", exchange -> {

            if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) { //Lieber if not abfragen?!
                exchange.sendResponseHeaders(405, -1);
                return;
            } else {

                List<HighscoreEintrag> liste = new ArrayList<>();

                try (var conn = Datenbank.getConnection()) {

                    String sql = "SELECT player, score, quote FROM highscore ORDER BY score DESC LIMIT 10";
                    var stmt = conn.prepareStatement(sql);
                    var res = stmt.executeQuery();

                    while (res.next()) {
                        String name = res.getString("player");
                        int score = res.getInt("score");
                        double quote = res.getDouble("quote");

                        liste.add(new HighscoreEintrag(name, score, quote));
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                    String error = "Fehler beim Laden";
                    exchange.sendResponseHeaders(500, error.length());

                    try (OutputStream outputStream = exchange.getResponseBody()) {
                        outputStream.write(error.getBytes(StandardCharsets.UTF_8));
                    }
                    return;
                }

                // GSON nutzen, da es in der letzten Vorlesung dran kam.
                Gson gson = new Gson();
                String json = gson.toJson(liste);

                byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, bytes.length);

                try (OutputStream outputStream = exchange.getResponseBody()) {
                    outputStream.write(bytes);
                }

            }
        });
        server.setExecutor(null); // Standard-Threadpool
        server.start();

        System.out.println("Server läuft auf http://localhost:8080");
    }

}