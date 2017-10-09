package com.equinoxe.asteroides;

import java.util.Vector;

/**
 * Created by Equinoxe on 09/10/2017.
 */

public class AlmacenPuntuacionesArray implements AlmacenPuntuaciones {
    private Vector<String> puntuaciones;

    public AlmacenPuntuacionesArray() {
        puntuaciones = new Vector<String>();
        puntuaciones.add("123000 Pepito Domínguez");
        puntuaciones.add("111000 Pedro Martínez");
        puntuaciones.add("011000 Paco Pérez");
    }

    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        puntuaciones.add(0, puntos + " " + nombre);
    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {
        return puntuaciones;
    }
}
