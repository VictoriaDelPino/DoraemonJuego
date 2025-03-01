package edu.prueba.doraemonjuego.model;


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

public class DoraemonGameModel  {

    public int maxX, maxY;
    public Bitmap mapa;
    public int mapa_h, mapa_w, dest_mapa_y;


    public float pos_mapa_y;  // Nueva posición para el movimiento vertical
    public float velocidadMapa = 50; // Velocidad del movimiento

    public int contadorFrames = 0;
    public static final int textoInicialx = 50;
    public static final int textoInicialy = 20;

    public DoraemonGameController bucle;

    public DoraemonGameModel() {

    }
    public void initializeValues(Context context,Canvas c){

        maxX = c.getWidth();
        maxY = c.getHeight();


        mapa = BitmapFactory.decodeResource(context.getResources(), R.drawable.cielo);
        // Escalar la imagen para que tenga el mismo ancho que la pantalla
        mapa_w = maxX;
        mapa_h = (int) (mapa.getHeight() * getEscala());

        // Crear un nuevo bitmap escalado
        mapa = Bitmap.createScaledBitmap(mapa, mapa_w, mapa_h, true);

        dest_mapa_y = (maxY - mapa_h) / 2;
        pos_mapa_y = 0;  // Inicializa en la parte superior
    }

    public float getEscala(){
       return (float) maxX / mapa.getWidth();
    }


}
