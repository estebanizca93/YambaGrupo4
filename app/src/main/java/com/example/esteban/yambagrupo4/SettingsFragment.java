package com.example.esteban.yambagrupo4;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Created by Esteban on 31/10/2017.
 */

public class SettingsFragment extends PreferenceFragment {
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onStart() {
            super.onStart();
            prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }
}
