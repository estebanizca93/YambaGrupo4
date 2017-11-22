package com.example.esteban.yambagrupo4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class StatusActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Comprobad si la actividad ya ha sido creada con anterioridad
        if (savedInstanceState == null) {
            // Crear un fragment
            StatusFragment fragment = new StatusFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragment, fragment.getClass().getSimpleName())
                    .commit();
        }
    }
}