package edu.prueba.doraemonjuego.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import edu.prueba.doraemonjuego.FinalScreenActivity;
import edu.prueba.doraemonjuego.controller.DoraemonGameController;
import edu.prueba.doraemonjuego.model.DoraemonGameModel;

//Clase que define la vista del juego
public class DoraemonGameView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener{

    public SurfaceHolder holder;
    public DoraemonGameController controller;
    Context context;


    public DoraemonGameView(Context context, DoraemonGameController controller) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
        setOnTouchListener(this);
        this.controller= controller;
        this.context = context;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Canvas c = holder.lockCanvas();
        if (c != null) {
            controller.model.initializeValues(c);
            holder.unlockCanvasAndPost(c);
        }
    }

    //Metodo que actualiza la posicion de la imagen
    public void actualizar() {
        controller.model.contadorFrames++;
        // Mueve la imagen hacia abajo
        controller.model.pos_mapa_y += controller.model.velocidadMapa;
        // Resetear la posición cuando la imagen sale completamente de la pantalla
        if (controller.model.pos_mapa_y >= controller.model.mapa_h) {
            controller.model.pos_mapa_y = 0;
        }
    }

    //Metodo que dibuja la imagen
    public void renderizar(Canvas canvas, DoraemonGameModel model) {
        if (canvas != null) {


            Paint mypaint = new Paint();
            canvas.drawColor(Color.RED);

            // Dibujar dos imágenes seguidas para un scroll fluido
            canvas.drawBitmap(controller.model.mapa, 0, controller.model.pos_mapa_y, null);
            canvas.drawBitmap(controller.model.mapa, 0, controller.model.pos_mapa_y - controller.model.mapa_h, null);

            // Dibujar la imagen del suelo manteniendo proporciones
            int suelo_y = controller.model.maxY - controller.model.suelo.getHeight(); // Asegurar que esté en la parte inferior
            canvas.drawBitmap(controller.model.suelo, 0, suelo_y, null);
            model.gameInstance.player.draw(canvas);

            canvas.drawBitmap(controller.model.vida, 0, 0, null);
            canvas.drawBitmap(controller.model.puntos, controller.model.maxX-280, 0, null);

            for (int i = 0; i < controller.model.gameInstance.enemies.size(); i++) {
                controller.model.gameInstance.enemies.get(i).draw(canvas);
            }

            for (int i = 0; i < controller.model.gameInstance.points.size(); i++) {
                controller.model.gameInstance.points.get(i).draw(canvas);
            }

            for (int i = 0; i < controller.model.gameInstance.lifes.size(); i++) {
                controller.model.gameInstance.lifes.get(i).draw(canvas);
            }

            //muestra estado del juego vidas restantes, nivel y puntos
            mypaint.setTextSize(60);
            mypaint.setStyle(Paint.Style.FILL);
            mypaint.setColor(Color.BLACK); // Color principal del texto
            mypaint.setAntiAlias(true);

            // Pinta el borde blanco
            Paint borderPaint = new Paint(mypaint);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(8); // Grosor del borde
            borderPaint.setColor(Color.WHITE); // Color del borde
            // Dibuja el texto con borde blanco
            canvas.drawText("X" + controller.model.gameInstance.player.getLifes(),
                    controller.model.textoInicialx+50, controller.model.textoInicialy + 50, borderPaint);
            canvas.drawText("X" + controller.model.gameInstance.player.getLifes(),
                    controller.model.textoInicialx+50, controller.model.textoInicialy + 50, mypaint);

            canvas.drawText("X" + controller.model.gameInstance.player.getScore(),
                    controller.model.maxX-185, controller.model.textoInicialy +50, borderPaint);
            canvas.drawText("X" + controller.model.gameInstance.player.getScore(),
                    controller.model.maxX-185, controller.model.textoInicialy +50, mypaint);

            canvas.drawText("Nivel: " + controller.model.nivel,
                    controller.model.maxX-650, controller.model.textoInicialy +50, borderPaint);
            canvas.drawText("Nivel: " + controller.model.nivel,
                    controller.model.maxX-650, controller.model.textoInicialy +50, mypaint);
        }
    }

    //Metodo que muestra la pantalla final del juego
    public void printFinalScreen(Context context, boolean isWin){
        Intent intent = new Intent(context, FinalScreenActivity.class);
        intent.putExtra("isWin", isWin);
        intent.putExtra("level", controller.model.nivel);
        context.startActivity(intent);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
