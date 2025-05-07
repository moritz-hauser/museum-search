package com.example.hci_a2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * Adapter für die Tabs in der MainActivity.
 * Steuert, welches Fragment je nach Position angezeigt wird (Search oder Results).
 */
public class TabsPagerAdapter extends FragmentStateAdapter {

    // Fester Verweis auf das ResultsFragment, damit es wiederverwendet werden kann
    private final ResultsFragment resultsFragment = new ResultsFragment();

    // Konstruktor
    public TabsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Position 0 = Search, Position 1 = Results
        if (position == 0) {
            return new SearchFragment();
        } else {
            return resultsFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Zwei Tabs
    }

    /**
     * Gibt das bereits erstellte ResultsFragment zurück.
     * Wird in MainActivity verwendet, um Ergebnisse anzuzeigen.
     */
    public ResultsFragment getResultsFragment() {
        return resultsFragment;
    }
}
