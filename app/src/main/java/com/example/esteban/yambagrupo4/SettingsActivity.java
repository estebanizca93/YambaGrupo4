package com.example.esteban.yambagrupo4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Esteban on 31/10/2017.
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

// Check whether this activity was created before
        if (savedInstanceState == null) {
// Create a fragment
            SettingsFragment fragment = new SettingsFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragment, fragment.getClass().getSimpleName())
                    .commit();

        }
    }
}
