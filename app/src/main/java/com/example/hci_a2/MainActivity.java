package com.example.hci_a2;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * MainActivity steuert die Navigation zwischen den Tabs
 * (Suche und Ergebnisse) sowie den Übergang zur Detailansicht.
 */
public class MainActivity extends AppCompatActivity {
    private TabsPagerAdapter adapter;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Tabs und Viewpager2 initialisieren
        TabLayout tabLayout = findViewById(R.id.tlTabLayout);
        viewPager = findViewById(R.id.vpViewPager2);
        adapter = new TabsPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Tabs mit Viewpager verbinden und Titel setzen
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) tab.setText("Search");
                    else tab.setText("Results");
                }
        ).attach();
    }

    /**
     * Zugriff auf das ResultsFragment (zB. zum Aktualisieren der Ergebnisse)
     */
    public ResultsFragment getResultsFragment() {
        return adapter.getResultsFragment();
    }

    /**
     * Wechsel zum Tab "Results".
     */
    public void switchToResultsTab() {
        viewPager.setCurrentItem(1, true); // Index 1 = Results-Tab
    }

    /**
     * Öffnet das Detail-Fragment für ein bestimmtes Kunstwerk.
     * @param artwork Das ausgewählte Kunstwerk.
     * @param query Die zuletzt verwendete Suchanfrage.
     */
    public void openDetailFragment(Artwork artwork, Query query) {
        // Übergabe Daten vorbereiten
        Bundle bundle = new Bundle();
        bundle.putSerializable("artwork", artwork);
        bundle.putSerializable("query", query);

        // Fragment initialisieren
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);

        findViewById(R.id.detailContainer).setVisibility(View.VISIBLE);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detailContainer, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}