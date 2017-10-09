package com.equinoxe.asteroides;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    public static AlmacenPuntuaciones almacen = new AlmacenPuntuacionesArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            lanzarPreferencias(null);
            return true;
        }
        if (id == R.id.acercaDe) {
            lanzarAcercaDe(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void lanzarPreferencias(View view) {
        Intent i = new Intent(this,Preferencias.class);
        startActivity(i);
    }

    public void lanzarAcercaDe(View view) {
        Intent i = new Intent(this,AcercaDe.class);
        startActivity(i);
    }

    public void salir(View view) {
        finish();
    }

    public void lanzarPuntuaciones(View v) {
        Intent i = new Intent(this, Puntuaciones.class);
        startActivity(i);
    }
}
