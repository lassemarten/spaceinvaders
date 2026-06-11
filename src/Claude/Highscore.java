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
            String json = response.toString();

        // [ ] entfernen
            json = json.substring(1, json.length() - 1);

            // einzelne Einträge trennen
            String[] entries = json.split("\\},\\{");

            System.out.println("Highscores:");

            int rank = 1;

            for (String entry : entries) {

                // Klammern wieder sauber machen
                entry = entry.replace("{", "").replace("}", "");

                String name = getJsonValue(entry, "name");
                int score = Integer.parseInt(getJsonValue(entry, "score"));

                System.out.println(rank + ". " + name + " - " + score + " Punkte");

                rank++;
            }
    }  catch (IOException e){
        throw new RuntimeException(e);
    }
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
