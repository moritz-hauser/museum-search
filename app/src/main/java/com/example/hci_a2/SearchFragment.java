package com.example.hci_a2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.chip.ChipGroup;

import java.util.List;

/**
 * Fragment für die Suchseite.
 * Nutzer:innen können hier Freitext und Filter angeben,
 * um Kunstwerke über die API zu suchen.
 */
public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Leerer Konstruktor notwendig für Fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Layout laden
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Views initialisieren
        EditText etSearch = view.findViewById(R.id.etSearchBar);
        Button btnSearch = view.findViewById(R.id.btnSearch);
        EditText etTimeBegin = view.findViewById(R.id.etTimeBegin);
        EditText etTimeEnd = view.findViewById(R.id.etTimeEnd);
        ChipGroup cgIsPublicDomain = view.findViewById(R.id.chipGroupPublicDomain);
        ChipGroup cgOnView = view.findViewById(R.id.chipGroupOnView);

        // Klick auf "Search"-Button
        btnSearch.setOnClickListener(v -> {
            // Eingaben sammeln
            String queryText = etSearch.getText().toString().trim();
            String timeBegin = etTimeBegin.getText().toString().trim();
            String timeEnd = etTimeEnd.getText().toString().trim();

            // Public Domain Chip auslesen
            int selectedChipId = cgIsPublicDomain.getCheckedChipId();
            String isPublicDomain = "";
            if (selectedChipId == R.id.chipPublicDomainTrue) {
                isPublicDomain = "true";
            } else if (selectedChipId == R.id.chipPublicDomainFalse) {
                isPublicDomain = "false";
            }

            // On View Chip auslesen
            selectedChipId = cgOnView.getCheckedChipId();
            String onView = "";
            if (selectedChipId == R.id.chipOnViewTrue) {
                onView = "true";
            } else if (selectedChipId == R.id.chipOnViewFalse) {
                onView = "false";
            }

            // Nur bei vorhandener Suchanfrage fortfahren
            if (!queryText.isEmpty()) {
                Log.d("info", "Search Button clicked with text: " + queryText);

                // Query-Objekt erzeugen und an die API übergeben
                Query query = new Query(queryText, timeBegin, timeEnd, isPublicDomain, onView);
                PreviewRequester requester = new PreviewRequester(query);

                requester.fetchArtworks(artworks -> {
                    // Ergebnisse an das ResultsFragment übergeben
                    requireActivity().runOnUiThread(() -> {
                        sendToResults(artworks, query);
                    });
                });
                // Zu anderem Tab wechslen
                ((MainActivity) requireActivity()).switchToResultsTab();
            } else {
                // Toast erstellen, wenn kein Text eingegeben worden ist
                Toast.makeText(requireContext(), "Please enter something in the text-field!", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void sendToResults(List<Artwork> artworks, Query query) {
        Log.d("info", "SearchFragment::sendToResults called with artworks size: " + artworks.size());

        ResultsFragment resultsFragment = ((MainActivity) requireActivity()).getResultsFragment();
        resultsFragment.updateResults(artworks, query);
    }

}