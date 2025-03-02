package edu.prueba.doraemonjuego.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import edu.prueba.doraemonjuego.R;
import edu.prueba.doraemonjuego.controller.DoraemonGameController;

public class DoraemonGameModel  {

    public int maxX, maxY;
    public Bitmap mapa;
    public Bitmap suelo;
    public int mapa_h, mapa_w, dest_mapa_y;

    public int nivel;

    public float pos_mapa_y;  // Nueva posición para el movimiento vertical
    public float velocidadMapa = 10; // Velocidad del movimiento

    public int contadorFrames = 0;
    public static final int textoInicialx = 50;
    public static final int textoInicialy = 20;

    public DoraemonGameController bucle;

    private Context context;

    public DoraemonGameModel(Context context, int nivel) {
        this.context = context;
        this.nivel = nivel;
        velocidadMapa= nivel*velocidadMapa;
    }
    public void initializeValues(Canvas c){

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

        // Cargar suelo y mantener proporciones
        suelo = BitmapFactory.decodeResource(context.getResources(), R.drawable.suelo);

        // Escalar manteniendo la proporción
        float escalaSuelo = (float) maxX / suelo.getWidth();
        int suelo_h = (int) (suelo.getHeight() * escalaSuelo);  // Ajustar altura proporcional

        suelo = Bitmap.createScaledBitmap(suelo, maxX, suelo_h, true);
    }

    public float getEscala(){
       return (float) maxX / mapa.getWidth();
    }


}
