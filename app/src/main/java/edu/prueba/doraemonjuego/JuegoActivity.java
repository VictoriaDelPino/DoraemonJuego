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

/**
 * Clase que maneja la actividad del juego.
 * Controla la vista, gestos y eventos táctiles para mover al personaje.
 */
public class JuegoActivity extends AppCompatActivity {

    private DoraemonGameController controller;
    private GestureDetector gestureDetector;
    private boolean isTouching = false; // Para detectar si el dedo sigue presionado
    private Handler movementHandler; // Manejar el movimiento continuo
    private Runnable movementRunnable; // Ejecutar movimiento repetido

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int nivel = getIntent().getIntExtra("nivel", 1); // Obtener nivel del intent

        controller = new DoraemonGameController(this, nivel);

        if (controller.view != null) {
            setContentView(controller.view);
            hideSystemUI();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> controller.start(), 1000);

            movementHandler = new Handler(Looper.getMainLooper());


            gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
                    handlePlayerMovement(e);
                    return true;
                }

                @Override
                public void onLongPress(@NonNull MotionEvent e) {
                    startContinuousMovement(e);
                }
            });

            controller.view.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isTouching = true;
                        startContinuousMovement(event);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        if (!isTouching) {
                            isTouching = true;
                            startContinuousMovement(event);
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                        isTouching = false;
                        stopContinuousMovement();
                        return true;
                }
                return false;
            });
        }
    }



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


    private void handlePlayerMovement(MotionEvent e) {
        int actualX = (int) e.getX();
        if (actualX < controller.model.maxX / 2) {
            controller.model.gameInstance.player.moveLeft();
        } else {
            controller.model.gameInstance.player.moveRight(controller.model.maxX);
        }
    }


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


    private void stopContinuousMovement() {
        if (movementRunnable != null) {
            movementHandler.removeCallbacks(movementRunnable);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (controller.model.musicaFondo != null) {
            controller.model.musicaFondo.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (controller.model.musicaFondo != null) {
            controller.model.musicaFondo.start();
        }
    }

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
