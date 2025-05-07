package com.example.hci_a2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment zur Anzeige der Suchergebnisse als Liste.
 * Nutzt einen RecyclerView und den ArtworkAdapter.
 */
public class ResultsFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArtworkAdapter adapter;
    private final List<Artwork> artworks = new ArrayList<>();
    private Query lastQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Layout laden
        View view = inflater.inflate(R.layout.fragment_results, container, false);

        // RecyclerView vorbereiten
        recyclerView = view.findViewById(R.id.rvArtworks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Adapter setzen, mit Click-Listener für Detailansicht
        adapter = new ArtworkAdapter(artworks, artwork -> {
            if (lastQuery != null) Log.d("info", "adapter query != null");
            ((MainActivity) requireActivity()).openDetailFragment(artwork, lastQuery);
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * Aktualisiert die Liste der Suchergebnisse im UI.
     * @param newResults Die neue Ergebnisliste
     * @param query Die dazugehörige Suchanfrage (wird für Detailansicht gespeichert)
     */
    public void updateResults(List<Artwork> newResults, Query query) {
        this.lastQuery = query;

        // Debugging
        if (this.lastQuery != null) {
            Log.d("info", "updateResults this.query != null");
        }
        if (adapter == null || artworks == null) {
            Log.e("ResultsFragment", "Adapter oder artworks ist noch null!");
            return;
        }

        Log.d("info", "ResultsFragment::updateResults called with List size: " + newResults.size());

        artworks.clear();
        artworks.addAll(newResults);
        adapter.notifyDataSetChanged();

        Log.d("info", "ResultsFragment updated successfully.");
    }
}