package edu.prueba.doraemonjuego;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import edu.prueba.doraemonjuego.controller.DoraemonGameController;

//Clase que maneja la actividad del juego.

public class JuegoActivity extends AppCompatActivity {

    private DoraemonGameController controller;
    private GestureDetector gestureDetector;
    private boolean isTouching = false; // Para detectar si el dedo sigue presionado
    private Handler movementHandler; // Manejar el movimiento continuo
    private Runnable movementRunnable; // Ejecutar movimiento repetido

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int nivel = getIntent().getIntExtra("nivel", 1); // Obtiene nivel del intent

        // Crea el controlador del juego
        controller = new DoraemonGameController(this, nivel);

        if (controller.view != null) {
            setContentView(controller.view);
            hideSystemUI();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            // Inicia el juego después de 1 segundo
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> controller.start(), 1000);
            movementHandler = new Handler(Looper.getMainLooper());

            // Configura el detector de gestos
            gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
                // Maneja el gesto de toque
                @Override
                public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
                    handlePlayerMovement(e);
                    return true;
                }
                // Maneja el gesto de pulsación larga
                @Override
                public void onLongPress(@NonNull MotionEvent e) {
                    startContinuousMovement(e);
                }
            });

            // Configura el detector de gestos para la vista
            controller.view.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {

                    // Maneja los eventos de toque
                    case MotionEvent.ACTION_DOWN:
                        isTouching = true;
                        startContinuousMovement(event);
                        return true;

                    // Maneja los eventos de movimiento
                    case MotionEvent.ACTION_MOVE:
                        if (!isTouching) {
                            isTouching = true;
                            startContinuousMovement(event);
                        }
                        return true;

                    // Maneja los eventos de soltado
                    case MotionEvent.ACTION_UP:
                        isTouching = false;
                        stopContinuousMovement();
                        return true;
                }
                return false;
            });
        }
    }


    // Oculta la barra de notificaciones
    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            controller.view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

            controller.view.setOnSystemUiVisibilityChangeListener(visibility -> hideSystemUI());
        }
    }

    // Maneja el movimiento del jugador
    private void handlePlayerMovement(MotionEvent e) {
        int actualX = (int) e.getX();
        if (actualX < controller.model.maxX / 2) {
            controller.model.gameInstance.player.moveLeft();
        } else {
            controller.model.gameInstance.player.moveRight(controller.model.maxX);
        }
    }


    //Mueve el jugador de forma continua
    private void startContinuousMovement(MotionEvent e) {
        stopContinuousMovement();
        movementRunnable = new Runnable() {
            @Override
            public void run() {
                if (isTouching) {
                    handlePlayerMovement(e);
                    movementHandler.postDelayed(this, 50); // Repetir cada 50ms
                }
            }
        };

        movementHandler.post(movementRunnable);
    }

    //Maneja la parada del movimiento
    private void stopContinuousMovement() {
        if (movementRunnable != null) {
            movementHandler.removeCallbacks(movementRunnable);
        }
    }

    //Maneja la deteccion de gestos de toque de la pantalla
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    //Al pausar la activida vuelve al menu principal
    @Override
    protected void onPause() {
        super.onPause();
        if(controller.model.isJuegoEnEjecucion()) {
            if (controller.model.musicaFondo != null) {
                controller.model.musicaFondo.stop();
                controller.model.musicaFondo.release();
                controller.model.musicaFondo = null;
            }

            Intent i = new Intent(this, LauncherActivity.class);
            startActivity(i);
            controller.model.setJuegoEnEjecucion(false);
            finish();
        }
    }

    //Al dar atras vuelve al menu principal
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (controller.model.musicaFondo != null) {
            controller.model.musicaFondo.stop();
            controller.model.musicaFondo.release();
            controller.model.musicaFondo = null;
        }

        Intent i = new Intent(this, LauncherActivity.class);
        startActivity(i);
        controller.model.setJuegoEnEjecucion(false);
        finish();
    }
}
