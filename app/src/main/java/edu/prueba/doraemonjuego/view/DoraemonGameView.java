package edu.prueba.doraemonjuego.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import edu.prueba.doraemonjuego.R;
import edu.prueba.doraemonjuego.controller.DoraemonGameController;
import edu.prueba.doraemonjuego.model.DoraemonGameModel;

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

    public void actualizar() {
        controller.model.contadorFrames++;

        // Mover la imagen hacia abajo
        controller.model.pos_mapa_y += controller.model.velocidadMapa;

        // Resetear la posición cuando la imagen sale completamente de la pantalla
        if (controller.model.pos_mapa_y >= controller.model.mapa_h) {
            controller.model.pos_mapa_y = 0;
        }
    }


    public void renderizar(Canvas canvas) {
        if (canvas != null) {
            Paint mypaint = new Paint();
            canvas.drawColor(Color.RED);

            // Dibujar dos imágenes seguidas para un scroll fluido
            canvas.drawBitmap(controller.model.mapa, 0, controller.model.pos_mapa_y, null);
            canvas.drawBitmap(controller.model.mapa, 0, controller.model.pos_mapa_y - controller.model.mapa_h, null);

            // Dibujar la imagen del suelo manteniendo proporciones
            int suelo_y = controller.model.maxY - controller.model.suelo.getHeight(); // Asegurar que esté en la parte inferior
            canvas.drawBitmap(controller.model.suelo, 0, suelo_y, null);

            // Mostrar FPS
            mypaint.setTextSize(40);
            canvas.drawText("Frames ejecutados: " + controller.model.contadorFrames,
                    controller.model.textoInicialx, controller.model.textoInicialy + 40, mypaint);
        }
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
