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
import edu.prueba.doraemonjuego.data.GamePersistance;
import edu.prueba.doraemonjuego.data.Player;
import edu.prueba.doraemonjuego.data.Point;
import edu.prueba.doraemonjuego.data.PowerUp;

public class DoraemonGameModel  {

    public int maxX, maxY;
    public Bitmap mapa;
    public Bitmap suelo;
    public Bitmap vida;
    public Bitmap puntos;
    public int mapa_h, mapa_w, dest_mapa_y;

    public int nivel;

    public int maxEnemies, maxPoints,maxPowerUps;
    public float pos_mapa_y;  // Nueva posición para el movimiento vertical
    public float velocidadMapa; // Velocidad del movimiento

    public int contadorFrames = 0;
    public static final int textoInicialx = 50;
    public static final int textoInicialy = 20;

    public GameInstance gameInstance;


    public DoraemonGameController bucle;

    private Context context;

    private float multiplier = 100f;

    public DoraemonGameModel(Context context, int nivel) {
        this.context = context;
        this.nivel = nivel;
        velocidadMapa= 5;
        //if por nivel


        if(nivel==1){
            multiplier = 100f;
            maxEnemies = 20;
            maxPoints = 30;
            maxPowerUps = 5;
        }else if(nivel==2){
            multiplier = 150f;
            maxEnemies = 40;
            maxPoints = 40;
            maxPowerUps = 4;
        }else if(nivel==3){
            multiplier = 200f;
            maxEnemies = 60;
            maxPoints = 50;
            maxPowerUps = 3;
        }else{
            multiplier = 100f;
            maxEnemies = 20;
            maxPoints = 30;
        }

    }

    public void updatePositions() {
        // Mover enemigos y verificar colisiones
        for (int i = 0; i < gameInstance.enemies.size(); i++) {
            Enemy enemy = gameInstance.enemies.get(i);
            try {
                enemy.moveDown(multiplier);
                if (checkCollision(gameInstance.player, enemy)) {
                    multiplier+=10;
                    enemy.collide(gameInstance.player);
                    gameInstance.enemies.remove(i);
                    i--; // Ajustar índice tras eliminación
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        // Mover puntos y verificar colisiones
        for (int i = 0; i < gameInstance.points.size(); i++) {
            Point point = gameInstance.points.get(i);
            try {
                point.moveDown(multiplier);
                if (checkCollision(gameInstance.player, point)) {
                    multiplier+=10;
                    point.collide(gameInstance.player);
                    gameInstance.points.remove(i);
                    i--; // Ajustar índice tras eliminación
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i < gameInstance.lifes.size(); i++) {
            PowerUp powerUp = gameInstance.lifes.get(i);
            try {
                powerUp.moveDown(multiplier);
                if (checkCollision(gameInstance.player, powerUp)) {
                    multiplier+=10;
                    powerUp.collide(gameInstance.player);
                    gameInstance.lifes.remove(i);
                    i--; // Ajustar índice tras eliminación
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
    }

    private boolean checkCollision(Player player, PowerUp powerUp) {
        return player.getX() < powerUp.getX() + powerUp.getWidth() &&
                player.getX() + 350 > powerUp.getX() &&
                player.getY() < powerUp.getY() + powerUp.getHeight() &&
                player.getY() + 300 > powerUp.getY();
    }

    // Método para verificar colisiones
    private boolean checkCollision(Player player, Enemy enemy) {
        return player.getX() < enemy.getX() + enemy.getWidth() &&
                player.getX() + 350 > enemy.getX() &&
                player.getY() < enemy.getY() + enemy.getHeight() &&
                player.getY() + 300 > enemy.getY();
    }

    private boolean checkCollision(Player player, Point point) {
        return player.getX() < point.getX() + point.getWidth() &&
                player.getX() + 350 > point.getX() &&
                player.getY() < point.getY() + point.getHeight() &&
                player.getY() + 300 > point.getY();
    }


    public void instantiateEntities(){
        int randomEnemies = (int) (Math.random() * maxEnemies);
        int randomPoints = (int) (Math.random() * maxPoints);
        int randomPowerUps = (int) (Math.random() * 11);
        if(randomPoints%2==0){
            if(gameInstance.points.size()<maxPoints){
                int posX = (int) (Math.random() * (maxX - 160)); // Corrección aquí
                gameInstance.points.add(new Point(2, 2, posX, maxY-2500));
                Log.d("Instanciar_entidad", "Instanciado punto");
            }
        }
        if (randomEnemies % 2 == 0) {
            if (gameInstance.enemies.size() < maxEnemies) {
                int posX = (int) (Math.random() * (maxX - 126)); // Corrección aquí
                gameInstance.enemies.add(new Enemy(2, 2, posX, maxY - 2500));
                Log.d("Instanciar_entidad", "Instanciado enemigo en X: " + posX);
            }
        }
        if(randomPowerUps%10==0){
            if(gameInstance.lifes.size()<maxPowerUps){
                int posX = (int) (Math.random() * (maxX - 160)); // Corrección aquí
                gameInstance.lifes.add(new PowerUp(2, 2, posX, maxY-2500));

            }

        }

    }

    public void initializeValues(Canvas c){

        maxX = c.getWidth();
        maxY = c.getHeight();
        gameInstance= new GameInstance(nivel, maxX, maxY);

        mapa = GamePersistance.background;
        // Escalar la imagen para que tenga el mismo ancho que la pantalla
        mapa_w = maxX;
        mapa_h = (int) (mapa.getHeight() * getEscala());

        // Crear un nuevo bitmap escalado
        mapa = Bitmap.createScaledBitmap(mapa, mapa_w, mapa_h, true);

        dest_mapa_y = (maxY - mapa_h) / 2;
        pos_mapa_y = 0;  // Inicializa en la parte superior

        // Cargar suelo y mantener proporciones
        suelo = GamePersistance.ground;

        // Escalar manteniendo la proporción
        float escalaSuelo = (float) maxX / suelo.getWidth();
        int suelo_h = (int) (suelo.getHeight() * escalaSuelo);  // Ajustar altura proporcional

        suelo = Bitmap.createScaledBitmap(suelo, maxX, suelo_h, true);

        vida =GamePersistance.life; // Carga el bitmap de la vida
        vida = Bitmap.createScaledBitmap(vida, 100, 100, true);

        puntos = GamePersistance.point;
        puntos = Bitmap.createScaledBitmap(puntos, 80, 80, true);


    }

    public float getEscala(){
       return (float) maxX / mapa.getWidth();
    }


}
