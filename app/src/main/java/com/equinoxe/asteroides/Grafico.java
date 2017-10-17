package com.equinoxe.asteroides;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by Equinoxe on 16/10/2017.
 */

public class Grafico {
    private Drawable drawable;
    private int centX, centY;
    private int ancho, alto;
    private double incX, incY;
    private double angulo, rotacion;
    private int radioColision;
    private int xAnterior, yAnterior;
    private int radioInval;
    private View view;

    public Grafico(View view, Drawable drawable) {
        this.view = view;
        this.drawable = drawable;
        ancho = drawable.getIntrinsicWidth();
        alto = drawable.getIntrinsicHeight();
        radioColision = (ancho + alto) / 4;
        radioInval = (int) Math.hypot(ancho/2, alto/2);
    }

    public void dibujaGrafico(Canvas canvas) {
        int x = centX - ancho/2;
        int y = centY - alto/2;

        drawable.setBounds(x, y, x+ancho, y+alto);

        canvas.save();
        canvas.rotate((float)angulo, centX, centY);
        drawable.draw(canvas);
        canvas.restore();

        view.invalidate(centX-radioInval, centY-radioInval, centX+radioInval, centY+radioInval);
        view.invalidate(xAnterior-radioInval, yAnterior-radioInval, xAnterior+radioInval, yAnterior+radioInval);

        xAnterior = centX;
        yAnterior = centY;
    }

    public void incrementaPos(double factor) {
        centX += incX * factor;
        centY += incY * factor;
        angulo += rotacion * factor;

        if (centX < 0)                centX = view.getWidth();
        if (centX > view.getWidth())  centX = 0;
        if (centY < 0)                centY = view.getHeight();
        if (centY > view.getHeight()) centY = 0;
    }

    public double distancia (Grafico g) {
        return Math.hypot(centX - g.centX, centY - g.centY);
    }

    public boolean verificaColision(Grafico g) {
        return (distancia(g) < (radioColision + g.radioColision));
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public int getCentX() {
        return centX;
    }

    public void setCentX(int centX) {
        this.centX = centX;
    }

    public int getCentY() {
        return centY;
    }

    public void setCentY(int centY) {
        this.centY = centY;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public double getIncX() {
        return incX;
    }

    public void setIncX(double incX) {
        this.incX = incX;
    }

    public double getIncY() {
        return incY;
    }

    public void setIncY(double incY) {
        this.incY = incY;
    }

    public double getAngulo() {
        return angulo;
    }

    public void setAngulo(double angulo) {
        this.angulo = angulo;
    }

    public double getRotacion() {
        return rotacion;
    }

    public void setRotacion(double rotacion) {
        this.rotacion = rotacion;
    }

    public int getRadioColision() {
        return radioColision;
    }

    public void setRadioColision(int radioColision) {
        this.radioColision = radioColision;
    }

    public int getxAnterior() {
        return xAnterior;
    }

    public void setxAnterior(int xAnterior) {
        this.xAnterior = xAnterior;
    }

    public int getyAnterior() {
        return yAnterior;
    }

    public void setyAnterior(int yAnterior) {
        this.yAnterior = yAnterior;
    }

    public int getRadioInval() {
        return radioInval;
    }

    public void setRadioInval(int radioInval) {
        this.radioInval = radioInval;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
