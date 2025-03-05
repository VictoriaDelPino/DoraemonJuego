package edu.prueba.doraemonjuego.controller;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
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

    //Constructor
    public DoraemonGameController(Context context, int nivel) {
        this.context = context;
        //Inicializa el modelo y la vista
        model= new DoraemonGameModel(context, nivel);
        view= new DoraemonGameView(context, this);
        //inicializa la probavilidad de instaciar objetosinicial dependiendo del nivel
        if (nivel==1){
            divider=100;
        }else if (nivel==2){
            divider=65;
        }else if (nivel==3){
            divider=25;
        }

        //inicia el bucle de la musica de fondo
        if (model.musicaFondo != null) {
            model.musicaFondo.setLooping(true);
            model.musicaFondo.setVolume(0.04f, 0.04f);
            model.musicaFondo.start();
        }
    }

    @SuppressLint("RestrictedApi")
    public void run() {
        Canvas canvas;
        Log.d(TAG, "Comienza el gameloop");
        long tiempoComienzo;
        long tiempoDiferencia;
        int tiempoDormir;
        int framesASaltar;


        //comienza el bucle del juego
        while (model.isJuegoEnEjecucion()) {
            canvas = null;
            try {

                //instancia los objetos que caen
                canvas = view.holder.lockCanvas();
                if(canvas != null){
                    if(model.contadorFrames%divider==0){
                        model.instantiateEntities();
                    }
                }

                //sincroniza el canva
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
            //comprueba si el juego ha terminado
            model.checkGame();
        }

        //apaga el bucle de la musica de fondo
        if (model.musicaFondo != null) {
            model.musicaFondo.stop();
            model.musicaFondo.release();
            model.musicaFondo = null;
        }
        // comprueba si se ha ganado o perdido
        if(model.isJuegoGanado()){
            view.printFinalScreen(context, true);
        }else if(!model.isJuegoGanado() && model.gameInstance.player.getLifes()<=0){
            view.printFinalScreen(context, false);
        }
    }
}
