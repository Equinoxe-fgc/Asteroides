package com.equinoxe.asteroides;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.Vector;


/**
 * Created by Equinoxe on 16/10/2017.
 */

public class VistaJuego extends View implements SensorEventListener{
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

    private boolean hayValorInicialX = false;
    private float valorInicialX = 0.0f;

    private boolean hayValorInicialY = false;
    private float valorInicialY = 0.0f;

    private Grafico misil;
    private static int PASO_VELOCIDAD_MISIL = 12;
    private boolean misilActivo = false;
    private int tiempoMisil;

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

            ShapeDrawable dMisil = new ShapeDrawable(new RectShape());
            dMisil.getPaint().setColor(Color.WHITE);
            dMisil.getPaint().setStyle(Paint.Style.STROKE);
            dMisil.setIntrinsicHeight(3);
            dMisil.setIntrinsicWidth(15);
            drawableMisil = dMisil;

            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        } else {
            drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
            drawableNave = context.getResources().getDrawable(R.drawable.nave);
            drawableMisil = context.getResources().getDrawable(R.drawable.misil1);

            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        nave = new Grafico(this, drawableNave);
        misil = new Grafico(this, drawableMisil);
        asteroides = new Vector<Grafico>();
        for (int i = 0; i < numAsteroides; i++) {
            Grafico asteroide = new Grafico(this, drawableAsteroide);
            asteroide.setIncX(Math.random() * 4 - 2);
            asteroide.setIncY(Math.random() * 4 - 2);
            asteroide.setAngulo((int) (Math.random() * 360));
            asteroide.setRotacion((int) (Math.random() * 8 - 4));
            asteroides.add(asteroide);
        }

        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> listSensor = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        if (!listSensor.isEmpty()) {
            Sensor orientationSensor = listSensor.get(0);
            mSensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    protected void actualizaFisica() {
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

        if (misilActivo) {
            misil.incrementaPos(factorMov);
            tiempoMisil -= factorMov;
            if (tiempoMisil < 0) {
                misilActivo = false;
            } else {
                for (int i = 0; i < asteroides.size(); i++) {
                    if (misil.verificaColision(asteroides.elementAt(i))) {
                        destruyeAsteroide(i);
                        break;
                    }
                }
            }
        }
    }

    public void destruyeAsteroide(int i) {
        synchronized (asteroides) {
            asteroides.removeElementAt(i);
            misilActivo = false;
       }
       this.postInvalidate();
    }

    public void activaMisil() {
        misil.setCentX(nave.getCentX());
        misil.setCentY(nave.getCentY());
        misil.setAngulo(nave.getAngulo());
        misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
        misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
        tiempoMisil = (int) Math.min(this.getWidth() / Math.abs(misil.getIncX()), this.getHeight() / Math.abs(misil.getIncY())) - 2;
        misilActivo = true;

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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        nave.dibujaGrafico(canvas);
        if (misilActivo)
            misil.dibujaGrafico(canvas);
        synchronized (asteroides) {
            for (Grafico asteroide: asteroides) {
                asteroide.dibujaGrafico(canvas);
            }
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
                    activaMisil();
                }
                break;
        }

        mX = x;
        mY = y;

        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float valor = event.values[2];
        if (!hayValorInicialX) {
            valorInicialX = valor;
            hayValorInicialX = true;
        }
        aceleracionNave = (int) (valor - valorInicialY) / 3;

        valor = event.values[1];
        if (!hayValorInicialY) {
            valorInicialY = valor;
            hayValorInicialY = true;
        }
        giroNave = (int) (valor - valorInicialY) / 3;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    class ThreadJuego extends Thread {
        private boolean pausa, corriendo;

        public synchronized  void pausar() {
            pausa = true;
        }

        public synchronized void reanudar() {
            pausa = false;
            notify();
        }

        public void detener() {
            corriendo = false;
            if (pausa) reanudar();
        }

        @Override
        public void run() {
            corriendo = true;
            while (corriendo) {
                actualizaFisica();
                synchronized (this) {
                    while (pausa) {
                        try {
                            wait();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }

    public ThreadJuego getThread() {
        return thread;
    }
}
