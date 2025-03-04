package edu.prueba.doraemonjuego.controller;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import edu.prueba.doraemonjuego.JuegoActivity;
import edu.prueba.doraemonjuego.data.GameInstance;
import edu.prueba.doraemonjuego.data.GamePersistance;
import edu.prueba.doraemonjuego.model.DoraemonGameModel;
import edu.prueba.doraemonjuego.view.DoraemonGameView;

public class DoraemonGameController extends Thread {

    public DoraemonGameModel model;
    public DoraemonGameView view;

    public final static int MAX_FRAMES = 30;
    public final static int MAX_FRAMES_SALTADOS = 5;
    public final static int TIEMPO_FRAME = 1000 / MAX_FRAMES;
    Context context;
    private int divider;


    public DoraemonGameController(Context context, int nivel) {
        this.context = context;
        model= new DoraemonGameModel(context, nivel);
        view= new DoraemonGameView(context, this);
        if (nivel==1){
            divider=100;
        }else if (nivel==2){
            divider=75;
        }else if (nivel==3){
            divider=35;
        }
//inicializar musica

    }

    @SuppressLint("RestrictedApi")
    public void run() {
        Canvas canvas;
        Log.d(TAG, "Comienza el gameloop");
        long tiempoComienzo;
        long tiempoDiferencia;
        int tiempoDormir;
        int framesASaltar;


        while (model.isJuegoEnEjecucion()) {
            canvas = null;
            try {


                canvas = view.holder.lockCanvas();
                if(canvas != null){
                    if(model.contadorFrames%divider==0){
                        model.instantiateEntities();
                    }
                }
                synchronized (view.holder) {
                    tiempoComienzo = System.currentTimeMillis();
                    framesASaltar = 0;
                    model.updatePositions();

                    view.actualizar();
                    view.renderizar(canvas, model);

                    tiempoDiferencia = System.currentTimeMillis() - tiempoComienzo;
                    tiempoDormir = (int) (TIEMPO_FRAME - tiempoDiferencia);
                    if (tiempoDormir > 0) {
                        try {
                            Thread.sleep(tiempoDormir);
                        } catch (InterruptedException e) {
                        }
                    }
                    while (tiempoDormir < 0 && framesASaltar < MAX_FRAMES_SALTADOS) {
                        view.actualizar();
                        tiempoDormir += TIEMPO_FRAME;
                        framesASaltar++;
                    }
                }
            } finally {
                if (canvas != null) {
                    view.holder.unlockCanvasAndPost(canvas);
                }
            }
            model.checkGame();
        }
        if(model.isJuegoGanado()){
            //para musica
            view.printFinalScreen(context, true);
        }else{
            //para musica
            view.printFinalScreen(context, false);
        }
    }
}
