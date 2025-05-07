package com.example.hci_a2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

import io.getstream.photoview.PhotoView;


/**
 * Zeigt Details zu einem ausgewählten Kunstwerk an.
 * Inklusive Bild, Titel, Jahr, Beschreibung und weiteren Metadaten.
 * Felder, nach denen gefiltert wurden, werden hervorgehoben.
 */
public class DetailFragment extends Fragment {

    public DetailFragment() {
        // Leerer Konstruktor erforderlich für Fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Layout laden
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // Kunstwerk und Query aus dem Bundle holen
        Artwork artwork = (Artwork) getArguments().getSerializable("artwork");
        Query query = (Query) getArguments().getSerializable("query");

        // UI-Elemente initialisieren
        TextView tvTitle = view.findViewById(R.id.tvDetailTitle);
        TextView tvYear = view.findViewById(R.id.tvDetailYear);
        PhotoView img = view.findViewById(R.id.imgDetail);
        Button btnBack = view.findViewById(R.id.btnBack);
        TextView tvDescription = view.findViewById(R.id.tvDetailDescription);
        TextView tvOnView = view.findViewById(R.id.tvDetailOnView);
        TextView tvPublicDomain = view.findViewById(R.id.tvDetailPublicDomain);
        TextView tvDepartment = view.findViewById(R.id.tvDetailDepartment);
        TextView tvArtistTitle = view.findViewById(R.id.tvDetailArtistTitle);
        TextView tvStyleTitle = view.findViewById(R.id.tvDetailStyleTitle);

        // Default Bild setzen
        img.setImageResource(R.drawable.loading_cat);

        // Textfelder befüllen
        tvTitle.setText(artwork.title);
        String year = artwork.year;
        tvYear.setText(year);
        tvOnView.setText((artwork.isOnView ? "Yes" : "No"));
        tvPublicDomain.setText((artwork.isPublicDomain ? "Yes" : "No"));
        tvDepartment.setText((artwork.department == null ? "n/a" : artwork.department));
        tvArtistTitle.setText((artwork.artistTitle == null ? "n/a" : artwork.artistTitle));
        tvStyleTitle.setText((artwork.styleTitle == null ? "n/a" : artwork.styleTitle));

        // Beschreibung als HTML parsen
        if (artwork.description == null || artwork.description.equals("null") || artwork.description.isEmpty()) {
            tvDescription.setText("n/a");
        } else {
            tvDescription.setText(Html.fromHtml(artwork.description, Html.FROM_HTML_MODE_LEGACY));
            tvDescription.setMovementMethod(LinkMovementMethod.getInstance());
        }

        // Aktive Filter hervorheben (Farbe, Fett)
        Log.d("info", "reached Query Felder hervorheben");
        if (query != null) {
            Log.d("info", "query != null confirmed");
            int highlightColor = ContextCompat.getColor(requireContext(), R.color.secondary);
            if (!query.getOnView().isEmpty()) {
                tvOnView.setTextColor(highlightColor);
                tvOnView.setTypeface(null, Typeface.BOLD);
            }
            if (!query.getIsPublicDomain().isEmpty()) {
                tvPublicDomain.setTextColor(highlightColor);
                tvPublicDomain.setTypeface(null, Typeface.BOLD);
            }
            if (!query.getTimeStart().isEmpty() || !query.getTimeEnd().isEmpty()) {
                tvYear.setTextColor(highlightColor);
                tvYear.setTypeface(null, Typeface.BOLD);
            }
        }

        // Bild aus dem Internet laden
        new Thread(() -> {
            try {
                String highResUrl = artwork.imageUrl
                        .replaceFirst("/full/\\d+,/", "/full/max/");
                String resizedUrl = artwork.imageUrl.replaceFirst("/full/\\d+,/", "/full/!2000,2000/");
                InputStream input = new URL(resizedUrl).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                requireActivity().runOnUiThread(() -> img.setImageBitmap(bitmap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Zurück Button schließt das Detail-Fragment
        btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
            requireActivity().findViewById(R.id.detailContainer).setVisibility(View.GONE);
        });

        return view;
    }
}