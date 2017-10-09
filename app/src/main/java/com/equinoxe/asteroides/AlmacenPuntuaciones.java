package com.equinoxe.asteroides;

import java.util.Vector;

/**
 * Created by Equinoxe on 09/10/2017.
 */

public interface AlmacenPuntuaciones {
    void guardarPuntuacion(int puntos, String nombre, long fecha);
    Vector<String> listaPuntuaciones(int cantidad);
}
