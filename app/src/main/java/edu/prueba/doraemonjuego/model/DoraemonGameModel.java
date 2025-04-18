package edu.prueba.doraemonjuego.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.util.Log;

import edu.prueba.doraemonjuego.R;
import edu.prueba.doraemonjuego.controller.DoraemonGameController;
import edu.prueba.doraemonjuego.data.Enemy;
import edu.prueba.doraemonjuego.data.GameInstance;
import edu.prueba.doraemonjuego.data.GamePersistance;
import edu.prueba.doraemonjuego.data.Point;
import edu.prueba.doraemonjuego.data.PowerUp;

//clase que define el modelo del juego
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
    private boolean JuegoEnEjecucion = true;
    private boolean juegoGanado=false;
    public MediaPlayer musicaFondo;
    public MediaPlayer punto;
    public MediaPlayer enemigo;
    public DoraemonGameController bucle;
    private Context context;
    private float multiplier = 100f;
    public static int MAX_PT_LVL1=500;
    public static int MAX_PT_LVL2=1000;
    public static int MAX_PT_LVL3=1500;

    //Constructor
    public DoraemonGameModel(Context context, int nivel) {
        this.context = context;
        this.nivel = nivel;
        velocidadMapa= 7;

        //Inicializa los mediaPlayers del juego
        musicaFondo = MediaPlayer.create(context, R.raw.musicafondo);
        punto = MediaPlayer.create(this.context, R.raw.ganar2);
        enemigo = MediaPlayer.create(this.context, R.raw.perder2);

        //Inicializa los valores del juego
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

    public DoraemonGameController getBucle() {
        return bucle;
    }

    public boolean isJuegoEnEjecucion() {
        return JuegoEnEjecucion;
    }

    public void setJuegoEnEjecucion(boolean juegoEnEjecucion) {
        JuegoEnEjecucion = juegoEnEjecucion;
    }

    public boolean isJuegoGanado() {
        return juegoGanado;
    }

    //Metodo que comprueba si el juego ha terminado y si es asi si ha ganado o perdido
    public void checkGame(){

       // Log.d("checkGame",DoraemonGameModel.MAX_PT_LVL1+" | "+nivel+" | "+gameInstance.player.getScore() );
        if(gameInstance.player.getLifes()<=0){
            JuegoEnEjecucion=false;
            juegoGanado=false;
        }else {
            if (gameInstance.player.getScore() >= DoraemonGameModel.MAX_PT_LVL1 && nivel == 1) {
                //Log.d("checkGame", "1");
                JuegoEnEjecucion = false;
                juegoGanado = true;
            } else if (gameInstance.player.getScore() >= DoraemonGameModel.MAX_PT_LVL2 && nivel == 2) {
               // Log.d("checkGame", "2");
                JuegoEnEjecucion = false;
                juegoGanado = true;
            } else if (gameInstance.player.getScore() >= DoraemonGameModel.MAX_PT_LVL3 && nivel == 3) {
               // Log.d("checkGame", "3");
                JuegoEnEjecucion = false;
                juegoGanado = true;
            }

        }
    }

    //Metodo que actualiza las posiciones de los objetos
    // aumenta el multiplicado de la velocidad cuando colisiona
    //reproduce los sonidos de puntos y enemigos
    public void updatePositions() {
        // Mueve enemigos y verificar colisiones
        for (int i = 0; i < gameInstance.enemies.size(); i++) {
            Enemy enemy = gameInstance.enemies.get(i);
            try {
                enemy.moveDown(multiplier);
                if(enemy.collide(gameInstance.player)){
                    enemigo.start();
                    gameInstance.enemies.remove(i);
                    multiplier+=15;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        // Mueve puntos y verificar colisiones
        for (int i = 0; i < gameInstance.points.size(); i++) {
            Point point = gameInstance.points.get(i);
            try {
                point.moveDown(multiplier);
                if(point.collide(gameInstance.player)){
                    punto.start();
                    gameInstance.points.remove(i);
                    multiplier+=10;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i < gameInstance.lifes.size(); i++) {
            PowerUp powerUp = gameInstance.lifes.get(i);
            try {
                powerUp.moveDown(multiplier);
                if(powerUp.collide(gameInstance.player)){
                    punto.start();
                    gameInstance.lifes.remove(i);
                    multiplier+=5;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
    }

    //Metodo que instancia los objetos
    public void instantiateEntities(){
        int randomEnemies = (int) (Math.random() * maxEnemies);
        int randomPoints = (int) (Math.random() * maxPoints);
        int randomPowerUps = (int) (Math.random() * 11);
        if(randomPoints%2==0){
            if(gameInstance.points.size()<maxPoints){
                int posX = (int) (Math.random() * (maxX - 250));
                gameInstance.points.add(new Point(200, 250, posX, -250));
                Log.d("Instanciar_entidad", "Instanciado punto");
            }
        }
        if (randomEnemies % 2 == 0) {
            if (gameInstance.enemies.size() < maxEnemies) {
                int posX = (int) (Math.random() * (maxX - 110)); // Corrección aquí
                gameInstance.enemies.add(new Enemy(110, 110, posX, -250));
                Log.d("Instanciar_entidad", "Instanciado enemigo en X: " + posX);
            }
        }
        if(randomPowerUps%10==0){
            if(gameInstance.lifes.size()<maxPowerUps){
                int posX = (int) (Math.random() * (maxX - 250)); // Corrección aquí
                gameInstance.lifes.add(new PowerUp(225, 250, posX, -250));

            }
        }
    }

    //Metodo que inicializa imagenes que se van a usar en el juego
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
        int suelo_h = (int) (suelo.getHeight() * escalaSuelo);  // Ajusta altura proporcional

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
