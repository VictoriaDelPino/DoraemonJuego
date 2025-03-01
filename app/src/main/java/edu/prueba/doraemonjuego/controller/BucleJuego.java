package edu.prueba.doraemonjuego.controller;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import edu.prueba.doraemonjuego.model.DoraemonJuego;

public class BucleJuego extends Thread {

    private DoraemonJuego juego;
    private SurfaceHolder surfaceHolder;
    private boolean JuegoEnEjecucion = true;
    public final static int MAX_FRAMES = 30;
    public final static int MAX_FRAMES_SALTADOS = 5;
    public final static int TIEMPO_FRAME = 1000 / MAX_FRAMES;

    public BucleJuego(SurfaceHolder sh, DoraemonJuego s) {
        juego = s;
        surfaceHolder = sh;
    }

    @SuppressLint("RestrictedApi")
    public void run() {
        Canvas canvas;
        Log.d(TAG, "Comienza el gameloop");
        long tiempoComienzo;
        long tiempoDiferencia;
        int tiempoDormir;
        int framesASaltar;
        while (JuegoEnEjecucion) {
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    tiempoComienzo = System.currentTimeMillis();
                    framesASaltar = 0;
                    juego.actualizar();
                    juego.renderizar(canvas);
                    tiempoDiferencia = System.currentTimeMillis() - tiempoComienzo;
                    tiempoDormir = (int) (TIEMPO_FRAME - tiempoDiferencia);
                    if (tiempoDormir > 0) {
                        try {
                            Thread.sleep(tiempoDormir);
                        } catch (InterruptedException e) {
                        }
                    }
                    while (tiempoDormir < 0 && framesASaltar > MAX_FRAMES_SALTADOS) {
                        juego.actualizar();
                        tiempoDormir += TIEMPO_FRAME;
                        framesASaltar++;
                    }
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
        Log.d(TAG, "nueva iteracion");
    }

    public void fin(){
    }
}
