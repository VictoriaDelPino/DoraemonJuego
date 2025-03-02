package edu.prueba.doraemonjuego.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import edu.prueba.doraemonjuego.R;
import edu.prueba.doraemonjuego.controller.DoraemonGameController;
import edu.prueba.doraemonjuego.data.Enemy;
import edu.prueba.doraemonjuego.data.GameInstance;
import edu.prueba.doraemonjuego.data.Point;

public class DoraemonGameModel  {

    public int maxX, maxY;
    public Bitmap mapa;
    public Bitmap suelo;
    public int mapa_h, mapa_w, dest_mapa_y;

    public int nivel;

    public int maxEnemies, maxPoints;
    public float pos_mapa_y;  // Nueva posición para el movimiento vertical
    public float velocidadMapa = 10; // Velocidad del movimiento

    public int contadorFrames = 0;
    public static final int textoInicialx = 50;
    public static final int textoInicialy = 20;

    public GameInstance gameInstance;


    public DoraemonGameController bucle;

    private Context context;

    public DoraemonGameModel(Context context, int nivel) {
        this.context = context;
        this.nivel = nivel;
        velocidadMapa= nivel*velocidadMapa;
        //if por nivel
        maxEnemies = 20;
        maxPoints = 30;

    }

    public void updatePositions(){
        for(int i=0; i<gameInstance.enemies.size(); i++){
            try {
                gameInstance.enemies.get(i).moveDown(100f);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        for(int i=0; i<gameInstance.points.size(); i++){
            try {
                gameInstance.points.get(i).moveDown(100f);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void instantiateEntities(){
        int randomEnemies = (int) (Math.random() * 11);
        int randomPoints = (int) (Math.random() * 11);
        int randomPowerUps = (int) (Math.random() * 11);
        if(randomPoints%2==0){
            if(gameInstance.points.size()<maxPoints){
                int posX = (int) (Math.random() * (maxX - 150)); // Corrección aquí
                gameInstance.points.add(new Point(2, 2, posX, maxY-2500));
                Log.d("Instanciar_entidad", "Instanciado punto");
            }
        }
        if (randomEnemies % 3 == 0) {
            if (gameInstance.enemies.size() < maxEnemies) {
                int posX = (int) (Math.random() * (maxX - 126)); // Corrección aquí
                gameInstance.enemies.add(new Enemy(2, 2, posX, maxY - 2500));
                Log.d("Instanciar_entidad", "Instanciado enemigo en X: " + posX);
            }
        }
        if(randomPowerUps%5==0){

        }

    }

    public void initializeValues(Canvas c){

        maxX = c.getWidth();
        maxY = c.getHeight();
        gameInstance= new GameInstance(nivel, maxX, maxY);

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
