package Claude;
import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Server {

    public static void main(String[] args) throws Exception {

        // Server auf Port 8080 starten
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Endpoint: /highscore
        server.createContext("/highscore", exchange -> {

            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) { //Lieber if not abfragen?!
                String body = new String(exchange.getRequestBody().readAllBytes());//Liest alles, was der Client geschickt hat, und wandel es in Text um
                System.out.println("Empfangen: " + body);

                String response = "Highscore empfangen!";

                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, response.length());

                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(response.getBytes("UTF-8"));
                outputStream.close();
            }
        });


        // GET → Scores abrufen
        server.createContext("/highscores", exchange -> {

                    if (exchange.getRequestMethod().equalsIgnoreCase("GET")) { //Lieber if not abfragen?!

                        String json = "[{\"name\":\"MusterMax\",\"score\":200,\"quote\":0.3}," +  //Nur zum Testen, kommt weg, wenn vollständig mit MySQL
                                "{\"name\":\"MusterMia\",\"score\":150,\"quote\":0.4}]";

                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, json.length());

                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(json.getBytes("UTF-8"));
                        outputStream.close();
                    }
                });
                server.setExecutor(null); // Standard-Threadpool
                server.start();

        System.out.println("Server läuft auf http://localhost:8080");
    }
}