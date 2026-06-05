package Claude;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Highscore {
    public  static void sendScore(String player, int score, double quote){
        try {
            URL url = new URL("http://localhost:8080/highscore");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            //JSON erstellen
            String json = String.format("{\"name\":\"%s\",\"score\":%d,\"quote\":%f}", player, score, quote);

            //Daten senden
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(json.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // Sicherstellen, dass der Score tatsächlich gesendet wird, um Fehler zu vermeiden.
                System.out.println("Der Punktestand etc. wurde erfolgreich gesendet.");
            } else {
                System.out.println("Ein Fehler ist beim Senden aufgetreten: " + responseCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadScore() {
        try {
           URL url2 = new URL("http://localhost:8080/highscores");

        HttpURLConnection conn = (HttpURLConnection) url2.openConnection();

        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();

        System.out.println("Highscores:");
        System.out.println(response);
    }  catch (IOException e){
        throw new RuntimeException(e);
    }
    }
}
