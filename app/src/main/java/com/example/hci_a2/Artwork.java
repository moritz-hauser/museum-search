package com.example.hci_a2;

import java.io.Serializable;

/*
Die Klasse Artwork dient der Speicherung von
relevanten Daten über gefundene Kunstwerke
 */
public class Artwork implements Serializable {
    String title;
    String artist;
    String year;
    String imageUrl;
    public String description;
    public boolean isPublicDomain;
    public boolean isOnView;
    public String department;
    public String artistTitle;
    public String styleTitle;

    // Konstruktor mit Fehlerbehandlung - leere Felder werden als "-" gespeichert
    public Artwork(String title, String artist, String year, String imageUrl,
                   String description, boolean isPublicDomain, boolean isOnView,
                   String department, String artistTitle, String styleTitle) {

        this.title = (title == null || title.equals("null") || title.isEmpty()) ? "-" : title;
        this.artist = (artist == null || artist.equals("null") || artist.isEmpty()) ? "-" : artist;
        this.year = (year == null || year.equals("null") || year.isEmpty()) ? "-" : year;
        this.imageUrl = imageUrl; // Bild-URL sollte valide sein, sonst vorher prüfen
        this.description = (description == null || description.equals("null") || description.isEmpty()) ? "-" : description;
        this.isPublicDomain = isPublicDomain;
        this.isOnView = isOnView;
        this.department = (department == null || department.equals("null") || department.isEmpty()) ? "-" : department;
        this.artistTitle = (artistTitle == null || artistTitle.equals("null") || artistTitle.isEmpty()) ? "-" : artistTitle;
        this.styleTitle = (styleTitle == null || styleTitle.equals("null") || styleTitle.isEmpty()) ? "-" : styleTitle;
    }

    // Getter:
    public String getDescription() {
        return description;
    }

    public boolean isPublicDomain() {
        return isPublicDomain;
    }

    public boolean isOnView() {
        return isOnView;
    }

    public String getDepartment() {
        return department;
    }

    public String getArtistTitle() {
        return artistTitle;
    }

    public String getStyleTitle() {
        return styleTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getYear() {
        return year;
    }

    public String getImageURL() {
        return imageUrl;
    }
}
