package com.example.hci_a2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Adapter für RecyclerView, um Kunstwerke in einer Liste darzustellen.
 */
public class ArtworkAdapter extends RecyclerView.Adapter<ArtworkAdapter.ArtworkViewHolder> {
    private final List<Artwork> artworkList;
    private final OnArtworkClickListener clickListener;

    /**
     * Konstruktor für den Adapter.
     * @param artworkList Liste von Kunstwerken.
     * @param clickListener Callback für Klicks auf einzelne Listenelemente.
     */
    public ArtworkAdapter(List<Artwork> artworkList, OnArtworkClickListener clickListener) {
        this.artworkList = artworkList;
        this.clickListener = clickListener;
    }

    /**
     * Interface für Klick-Events auf einzelne Kunstwerke.
     */
    public interface OnArtworkClickListener {
        void onArtworkClick(Artwork artwork);
    }

    @NonNull
    @Override
    public ArtworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Layout für ein einzelnes Kunstwerk laden
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artwork, parent, false);
        return new ArtworkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtworkViewHolder holder, int position) {
        // Aktuelles Kunstwerk holen
        Artwork artwork = artworkList.get(position);

        // Titel, Künstler und Jahr setzen
        holder.tvTitle.setText(artwork.title);
        String artist = "Artist: " + artwork.artistTitle;
        holder.tvArtist.setText(artist);
        String year = "Year: " + artwork.year;
        holder.tvYear.setText(year);

        // Bild laden
        new Thread(() -> {
            try {
                InputStream input = new URL(artwork.imageUrl).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                holder.imgArtwork.post(() -> holder.imgArtwork.setImageBitmap(bitmap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Click-Listener für Detailansicht
        holder.itemView.setOnClickListener(v -> clickListener.onArtworkClick(artwork));
    }

    @Override
    public int getItemCount() {
        return artworkList.size();
    }

    /**
     * ViewHolder-Klasse für die Darstellung eines Kunstwerks.
     */
    public static class ArtworkViewHolder extends RecyclerView.ViewHolder {
        ImageView imgArtwork;
        TextView tvTitle, tvArtist, tvYear;

        public ArtworkViewHolder(@NonNull View itemView) {
            super(itemView);
            imgArtwork = itemView.findViewById(R.id.imgArtwork);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            tvYear = itemView.findViewById(R.id.tvYear);
        }
    }
}