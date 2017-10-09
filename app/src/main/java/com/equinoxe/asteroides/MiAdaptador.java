package com.equinoxe.asteroides;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by Equinoxe on 09/10/2017.
 */

public class MiAdaptador extends BaseAdapter {
    private final Activity actividad;
    private final Vector<String> lista;

    public MiAdaptador(Activity actividad, Vector<String> lista) {
        super();
        this.actividad = actividad;
        this.lista = lista;
    }

    public View getView(int posicion, View convertView, ViewGroup parent) {
        LayoutInflater inflater = actividad.getLayoutInflater();
        View view = inflater.inflate(R.layout.elemento_lista, null, true);

        TextView textView = (TextView) view.findViewById(R.id.titulo);
        textView.setText(lista.elementAt(posicion));

        ImageView imageView = (ImageView) view.findViewById(R.id.icono);
        switch (Math.round((float)Math.random()*3)) {
            case 0:
                imageView.setImageResource(R.drawable.asteroide1);
                break;
            case 1:
                imageView.setImageResource(R.drawable.asteroide2);
                break;
            case 2:
                imageView.setImageResource(R.drawable.asteroide3);
                break;
            default:
                imageView.setImageResource(R.drawable.asteroide3);
                break;
        }

        return view;
    }

    public int getCount() {
        return lista.size();
    }

    public Object getItem(int arg0) {
        return lista.elementAt(arg0);
    }

    public long getItemId(int posicion) {
        return posicion;
    }
}
