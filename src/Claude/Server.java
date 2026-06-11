package Claude;
import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Server {

    public static void main(String[] args) throws Exception {


        // Server auf Port 8080 starten
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Endpoint: /highscore
        server.createContext("/highscore", exchange -> {

            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) { //Lieber if not abfragen?!
                String body = new String(exchange.getRequestBody().readAllBytes());//Liest alles, was der Client geschickt hat, und wandel es in Text um

                //System.out.println("Empfangen: " + body);

                String name = getJsonValue(body, "name");
                int score = Integer.parseInt(getJsonValue(body, "score"));
                double quote = Double.parseDouble(getJsonValue(body, "quote"));

                if (name == null || name.isEmpty()) {
                    throw new IllegalArgumentException("Name fehlt");
                }

                try (var conn = Datenbank.getConnection()) {

                    String sql = "INSERT INTO highscore (player, score, quote) VALUES (?, ?, ?)";
                    var stmt = conn.prepareStatement(sql);

                    stmt.setString(1, name);
                    stmt.setInt(2, score);
                    stmt.setDouble(3, quote);

                    stmt.executeUpdate();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                String response = "Gespeichert!";

                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, response.length());

                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(response.getBytes(StandardCharsets.UTF_8)); //CoPilot sagt, dass ist modernen und tut das gleiche wie "UTF-8"
                outputStream.close();
            }
            });

            // GET → Scores abrufen
        server.createContext("/highscores", exchange -> {

                    if (exchange.getRequestMethod().equalsIgnoreCase("GET")) { //Lieber if not abfragen?!

                       /* String json = "[{\"name\":\"MusterMax\",\"score\":200,\"quote\":0.3}," +  //Nur zum Testen, kommt weg, wenn vollständig mit MySQL
                                "{\"name\":\"MusterMia\",\"score\":150,\"quote\":0.4}]";
*/
                        StringBuilder json = new StringBuilder();
                        json.append("[");

                        try (var conn = Datenbank.getConnection()) {

                            String sql = "SELECT player, score, quote FROM highscore ORDER BY score DESC LIMIT 10";
                            var stmt = conn.prepareStatement(sql);
                            var res = stmt.executeQuery();

                            boolean first = true;

                            while (res.next()) {

                                if (!first) {           // , soll nicht am Anfang oder Ende stehen
                                    json.append(",");
                                }

                                String name = res.getString("player");
                                int score = res.getInt("score");
                                double quote = res.getDouble("quote");

                                json.append("{");
                                json.append("\"name\":\"").append(name).append("\",");
                                json.append("\"score\":").append(score).append(",");
                                json.append("\"quote\":").append(quote);
                                json.append("}");

                                first = false;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                            String error = "Fehler beim Laden";
                            exchange.sendResponseHeaders(500, error.length());

                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(error.getBytes(StandardCharsets.UTF_8));
                            }
                            return;
                        }

                        json.append("]");


                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, json.length());

                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(json.toString().getBytes(StandardCharsets.UTF_8));
                        outputStream.close();
                    }
                });
                server.setExecutor(null); // Standard-Threadpool
                server.start();

        System.out.println("Server läuft auf http://localhost:8080");
    }

    public static String getJsonValue(String json, String key) {
        String search = "\"" + key + "\":";

        int start = json.indexOf(search);
        if (start == -1) return null;

        start += search.length();

        // Prüfen, ob String oder Zahl
        if (json.charAt(start) == '\"') {
            start++;
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        } else {
            int end = json.indexOf(",", start);
            if (end == -1) {
                end = json.indexOf("}", start);
            }
            return json.substring(start, end);
        }
    }
}