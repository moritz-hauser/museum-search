package com.example.hci_a2;

import android.util.Log;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Die Query-Klasse speichert die Filtereinstellungen für eine Kunstwerksuche
 * und kann daraus eine URL für die Art Institute of Chicago API erzeugen.
 */
public class Query implements Serializable {
    private final String content;
    private final String timeStart;
    private final String timeEnd;
    private final String isPublicDomain;
    private final String onView;

    // Konstruktor
    public Query(String content, String timeStart, String timeEnd, String isPublicDomain, String onView) {
        this.content = content;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.isPublicDomain = isPublicDomain;
        this.onView = onView;

        Log.d("info", "Query created with filters: content=" + this.content
                + ", timeStart=" + this.timeStart
                + ", timeEnd=" + this.timeEnd
                + ", isPublicDomain=" + this.isPublicDomain
                + ", onView=" + this.onView);
    }

    /**
     * Erstellt eine URL für die Suchanfrage unter Verwendung der gesetzten Filter.
     * Nutzt einen JSON-String im URL-Parameter `params`, der Elasticsearch-Syntax verwendet.
     */
    public String buildPreviewUrl() {
        try {
            StringBuilder paramsJson = new StringBuilder();
            paramsJson.append("{");
            paramsJson.append("\"q\":\"").append(content).append("\",");
            paramsJson.append("\"query\":{\"bool\":{\"must\":[");

            boolean firstCondition = true;

            // Zeitfilter: Erstellungsjahr
            if (!timeStart.isEmpty()) {
                // Falls kein timeEnd angegeben wurde, auf 2025 setzen
                String end = timeEnd.isEmpty() ? "2025" : timeEnd;
                paramsJson.append("{\"range\":{\"date_start\":{\"gte\":")
                        .append(timeStart).append(",\"lte\":").append(end).append("}}}");
                firstCondition = false;
            } else if (!timeEnd.isEmpty()) {
                paramsJson.append("{\"range\":{\"date_start\":{\"lte\":")
                        .append(timeEnd).append("}}}");
                firstCondition = false;
            }

            // Filter: Public Domain
            if (!isPublicDomain.isEmpty()) {
                if (!firstCondition) paramsJson.append(",");
                paramsJson.append("{\"term\":{\"is_public_domain\":")
                        .append(isPublicDomain).append("}}");
                firstCondition = false;
            }

            // Filter: On View
            if (!onView.isEmpty()) {
                if (!firstCondition) paramsJson.append(",");
                paramsJson.append("{\"term\":{\"is_on_view\":")
                        .append(onView).append("}}");
            }

            paramsJson.append("]}}"); // Ende von bool/must

            // Felder angeben, die zurückgegeben werden sollen
            paramsJson.append(",\"fields\":[")
                    .append("\"id\",")
                    .append("\"title\",")
                    .append("\"image_id\",")
                    .append("\"artist_display\",")
                    .append("\"artist_title\",")
                    .append("\"date_start\",")
                    .append("\"description\",")
                    .append("\"is_public_domain\",")
                    .append("\"is_on_view\",")
                    .append("\"department_title\",")
                    .append("\"style_title\"")
                    .append("]");
            paramsJson.append(",\"limit\":100");
            paramsJson.append("}");

            // URL-Encodierung für den Parameter
            String encodedParams = URLEncoder.encode(paramsJson.toString(), "UTF-8");
            String urlString = "https://api.artic.edu/api/v1/artworks/search?params=" + encodedParams;
            Log.d("info", "URL created (must only): " + urlString);
            return urlString;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Getter:
    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public String getIsPublicDomain() {
        return isPublicDomain;
    }

    public String getOnView() {
        return onView;
    }

    public String getContent() {
        return content;
    }
}