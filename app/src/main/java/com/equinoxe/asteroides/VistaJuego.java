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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        nave.dibujaGrafico(canvas);
        for (Grafico asteroide: asteroides) {
            asteroide.dibujaGrafico(canvas);
        }
    }
}
