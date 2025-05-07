package com.example.hci_a2;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse ist für die Kommunikation mit der API zuständig.
 * Sie stellt die Verbindung her, holt die Daten und wandelt sie in Artwork-Objekte um.
 */
public class PreviewRequester {
    private final Query query;

    /**
     * Interface für Callback, sobald Daten geladen wurden
     */
    public interface PreviewCallback {
        void onResult(List<Artwork> artworks);
    }

    public PreviewRequester(Query query) {
        this.query = query;
    }

    /**
     * Führt die API-Anfrage aus und ruft das Callback mit der Ergebnisliste auf
     */
    public void fetchArtworks(PreviewCallback callback) {
        Log.d("info", "PreviewRequester::fetchArtworks called");

        new Thread(() -> {
            try {
                // URL vom Query-Objekt holen
                String urlString = query.buildPreviewUrl();

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("info", "HTTP_OK in fetchArtworks");

                    // Antwort lesen
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // JSON-Daten parsen
                    JSONObject json = new JSONObject(response.toString());
                    JSONArray data = json.getJSONArray("data");
                    JSONObject config = json.getJSONObject("config");
                    String iiifUrl = config.getString("iiif_url");

                    List<Artwork> artworks = new ArrayList<>();

                    // Einträge durchlaufen
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.getJSONObject(i);

                        // Nur Einträge mit Bild anzeigen
                        if (obj.has("image_id") && !obj.isNull("image_id")) {
                            String title = obj.optString("title", "Untitled");
                            String artist = obj.optString("artist_display", "Unknown");
                            String year = obj.optString("date_start", "n/a");
                            String imageId = obj.optString("image_id", "");
                            String imageUrl = iiifUrl + "/" + imageId + "/full/200,/0/default.jpg";
                            String description = obj.optString("description", "none");
                            boolean isPublicDomain = obj.optBoolean("is_public_domain", false);
                            boolean isOnView = obj.optBoolean("is_on_view", false);
                            String department = obj.optString("department_title", "Unknown");
                            String artistTitle = obj.optString("artist_title", "Unknown");
                            String styleTitle = obj.optString("style_title", "Unknown");

                            artworks.add(new Artwork(title, artist, year, imageUrl,
                                    description, isPublicDomain, isOnView,
                                    department, artistTitle, styleTitle));
                        }
                    }

                    Log.d("info", "fetched Artworks List size: " + artworks.size());
                    callback.onResult(artworks);

                } else {
                    Log.d("info", "HTTP Fehler: " + responseCode);
                    callback.onResult(new ArrayList<>()); // Leere Liste bei Fehler
                }

            } catch (Exception e) {
                e.printStackTrace();
                callback.onResult(new ArrayList<>()); // Leere Liste bei Exception
            }
        }).start();
    }
}

