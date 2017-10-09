package com.equinoxe.asteroides;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class Puntuaciones extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuaciones);

        //setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MainActivity.almacen.listaPuntuaciones(10)));
        /*setListAdapter(new ArrayAdapter<String>(this,
                                                R.layout.elemento_lista,
                                                R.id.titulo,
                                                MainActivity.almacen.listaPuntuaciones(10)));*/
        setListAdapter(new MiAdaptador(this, MainActivity.almacen.listaPuntuaciones(10)));
    }
}
