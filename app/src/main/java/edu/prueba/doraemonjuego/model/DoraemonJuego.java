package edu.prueba.doraemonjuego.model;

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
import edu.prueba.doraemonjuego.controller.BucleJuego;

public class DoraemonJuego extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private SurfaceHolder holder;
    private AppCompatActivity micontexto;

    private int maxX, maxY;
    private Bitmap mapa;
    private int mapa_h, mapa_w, dest_mapa_y;

    //private float pos_mapa_x;  // Posición de desplazamiento del mapa
    private float pos_mapa_y;  // Nueva posición para el movimiento vertical
    private float velocidadMapa = 50; // Velocidad del movimiento

    private int contadorFrames = 0;
    private static final int textoInicialx = 50;
    private static final int textoInicialy = 20;

    private BucleJuego bucle;

    public DoraemonJuego(AppCompatActivity context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        micontexto = context;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Canvas c = holder.lockCanvas();
        maxX = c.getWidth();
        maxY = c.getHeight();
        holder.unlockCanvasAndPost(c);

        mapa = BitmapFactory.decodeResource(getResources(), R.drawable.cielo);
        // Escalar la imagen para que tenga el mismo ancho que la pantalla
        float escala = (float) maxX / mapa.getWidth();
        mapa_w = maxX;
        mapa_h = (int) (mapa.getHeight() * escala);

        // Crear un nuevo bitmap escalado
        mapa = Bitmap.createScaledBitmap(mapa, mapa_w, mapa_h, true);

        dest_mapa_y = (maxY - mapa_h) / 2;
        pos_mapa_y = 0;  // Inicializa en la parte superior


        bucle = new BucleJuego(getHolder(), this);
        setFocusable(true);
        bucle.start();
    }

    public void actualizar() {
        contadorFrames++;

        // Mover la imagen hacia abajo
        pos_mapa_y += velocidadMapa;

        // Resetear la posición cuando la imagen sale completamente de la pantalla
        if (pos_mapa_y >= mapa_h) {
            pos_mapa_y = 0;
        }
    }


    public void renderizar(Canvas canvas) {
        if (canvas != null) {
            Paint mypaint = new Paint();
            canvas.drawColor(Color.RED);

            // Dibujar dos imágenes seguidas para un scroll fluido
            canvas.drawBitmap(mapa, 0, pos_mapa_y, null);
            canvas.drawBitmap(mapa, 0, pos_mapa_y - mapa_h, null);

            // Mostrar FPS
            mypaint.setTextSize(40);
            canvas.drawText("Frames ejecutados: " + contadorFrames, textoInicialx, textoInicialy + 40, mypaint);
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
