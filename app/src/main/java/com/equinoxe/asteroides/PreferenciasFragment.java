package com.equinoxe.asteroides;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Equinoxe on 05/10/2017.
 */

public class PreferenciasFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
