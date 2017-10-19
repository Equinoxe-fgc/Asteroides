package com.equinoxe.asteroides;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Vector;


/**
 * Created by Equinoxe on 16/10/2017.
 */

public class VistaJuego extends View {
    private Grafico nave;
    private int giroNave;
    private double aceleracionNave;
    private static final int MAX_VELOCIDAD_NAVE = 20;
    private static final int PASO_GIRO_NAVE = 5;
    private static final float PASO_ACELERACION_NAVE = 0.5f;

    private Vector<Grafico> asteroides;
    private int numAsteroides = 5;
    private int numFragmentos = 3;

    private ThreadJuego thread = new ThreadJuego();
    private static int PERIODO_PROCESO = 50;
    private long ultimoProceso = 0;

    private float mX=0.0f, mY=0.0f;
    private boolean disparo = false;

    public VistaJuego(Context context, AttributeSet attrs) {
        super(context, attrs);

        Drawable drawableNave, drawableAsteroide, drawableMisil;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (pref.getString("graficos", "1").equals("0")) {
            Path pathAsteroide = new Path();
            pathAsteroide.moveTo(0.3f, 0.0f);
            pathAsteroide.lineTo(0.6f, 0.0f);
            pathAsteroide.lineTo(0.6f, 0.3f);
            pathAsteroide.lineTo(0.8f, 0.2f);
            pathAsteroide.lineTo(1.0f, 0.4f);
            pathAsteroide.lineTo(0.8f, 0.6f);
            pathAsteroide.lineTo(0.9f, 0.9f);
            pathAsteroide.lineTo(0.8f, 1.0f);
            pathAsteroide.lineTo(0.4f, 1.0f);
            pathAsteroide.lineTo(0.0f, 0.6f);
            pathAsteroide.lineTo(0.0f, 0.2f);
            pathAsteroide.lineTo(0.3f, 0.0f);

            ShapeDrawable dAsteroide = new ShapeDrawable(new PathShape(pathAsteroide, 1, 1));
            dAsteroide.getPaint().setColor(Color.WHITE);
            dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
            dAsteroide.setIntrinsicWidth(50);
            dAsteroide.setIntrinsicHeight(50);
            drawableAsteroide = dAsteroide;
            setBackgroundColor(Color.BLACK);

            Path pathNave = new Path();
            pathNave.moveTo(0.0f, 0.0f);
            pathNave.lineTo(1.0f, 0.5f);
            pathNave.lineTo(0.0f, 1.0f);
            pathNave.lineTo(0.0f, 0.0f);

            ShapeDrawable dNave = new ShapeDrawable(new PathShape(pathNave, 1, 1));
            dNave.getPaint().setColor(Color.WHITE);
            dNave.getPaint().setStyle(Paint.Style.STROKE);
            dNave.setIntrinsicWidth(20);
            dNave.setIntrinsicHeight(15);
            drawableNave = dNave;

            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        } else {
            drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
            drawableNave = context.getResources().getDrawable(R.drawable.nave);

            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        nave = new Grafico(this, drawableNave);
        asteroides = new Vector<Grafico>();
        for (int i = 0; i < numAsteroides; i++) {
            Grafico asteroide = new Grafico(this, drawableAsteroide);
            asteroide.setIncX(Math.random() * 4 - 2);
            asteroide.setIncY(Math.random() * 4 - 2);
            asteroide.setAngulo((int) (Math.random() * 360));
            asteroide.setRotacion((int) (Math.random() * 8 - 4));
            asteroides.add(asteroide);
        }
    }

    synchronized protected void actualizaFisica() {
        long ahora = System.currentTimeMillis();
        if (ultimoProceso + PERIODO_PROCESO > ahora) {
            return;
        }

        double factorMov = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora;

        nave.setAngulo((int)(nave.getAngulo() + giroNave * factorMov));
        double nIncX = nave.getIncX() + aceleracionNave * Math.cos(Math.toRadians(nave.getAngulo())) * factorMov;
        double nIncY = nave.getIncY() + aceleracionNave * Math.sin(Math.toRadians(nave.getAngulo())) * factorMov;

        if (Math.hypot(nIncX, nIncY) <= MAX_VELOCIDAD_NAVE) {
            nave.setIncX(nIncX);
            nave.setIncY(nIncY);
        }
        nave.incrementaPos(factorMov);

        for (Grafico asteroide: asteroides) {
            asteroide.incrementaPos(factorMov);
        }
    }

    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anter, int alto_anter) {
        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);

        nave.setCentX(ancho / 2);
        nave.setCentY(alto / 2);

        for (Grafico asteroide: asteroides) {
            do {
                asteroide.setCentX((int) (Math.random() * ancho));
                asteroide.setCentY((int) (Math.random() * alto));
            } while (asteroide.distancia(nave) < (ancho+alto)/5);
        }

        ultimoProceso = System.currentTimeMillis();
        thread.start();
    }

    @Override
    synchronized protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        nave.dibujaGrafico(canvas);
        for (Grafico asteroide: asteroides) {
            asteroide.dibujaGrafico(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                disparo = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dy < 6 && dx > 6) {
                    giroNave = Math.round((x - mX) / 2);
                    disparo = false;
                } else if (dx < 6 && dy > 6) {
                    if (mY - y > 0)
                        aceleracionNave = Math.round((mY - y) / 25);
                    disparo = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                giroNave = 0;
                aceleracionNave = 0;
                if (disparo) {
                    //activaMisil();
                }
                break;
        }

        mX = x;
        mY = y;

        return true;
    }

    class ThreadJuego extends Thread {
        @Override
        public void run() {
            while (true) {
                actualizaFisica();
            }
        }
    }
}
