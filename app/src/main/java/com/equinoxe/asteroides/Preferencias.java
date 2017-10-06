package com.equinoxe.asteroides;

import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Preferencias extends AppCompatActivity {
//public class Preferencias extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_preferencias);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferenciasFragment()).commit();
        //addPreferencesFromResource(R.xml.preferencias);
    }
}
