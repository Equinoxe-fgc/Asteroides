package com.equinoxe.asteroides;

import android.app.Activity;
import android.os.Bundle;

public class juego extends Activity {
    private VistaJuego vistaJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        vistaJuego = (VistaJuego)findViewById(R.id.VistaJuego);
    }

    @Override
    protected void onResume() {
        super.onResume();
        vistaJuego.getThread().reanudar();
    }

    @Override
    protected void onPause() {
        vistaJuego.getThread().pausar();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        vistaJuego.getThread().detener();
        super.onDestroy();
    }
}
