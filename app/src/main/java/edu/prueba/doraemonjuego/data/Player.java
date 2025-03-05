package edu.prueba.doraemonjuego.data;

import android.graphics.Bitmap;
import android.graphics.Canvas;

//Clase que define al jugador
public class Player {
    private float x;
    private final float y;
    private int width;
    private int height;
    private int lifes;
    private int score;
    private Bitmap actualBitmap;
    private Bitmap[] left;
    private Bitmap[] right;
    private int actualBitmapIndex;
    private boolean isMoving = false;
    private int direction = 0; // -1 = izquierda, 1 = derecha, 0 = detenido
    private float velocityX = 25; // Velocidad de movimiento constante

    public Player(int height, int width, float x, float y) {
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
        score = 0;
        left = (Bitmap[]) GamePersistance.playerMovements.get("l");
        right = (Bitmap[]) GamePersistance.playerMovements.get("r");
        actualBitmap = right[0];
        lifes = 3;
        actualBitmapIndex = 0;
    }

    //Metodo para dibujar al jugador
    public void draw(Canvas canvas) {
        canvas.drawBitmap(actualBitmap, x, y, null);

    }

    //Metodo para actualizar la posicion del jugador
    public void update(int maxX) {
        if (isMoving) {
            if (direction == -1) moveLeft();
            else if (direction == 1) moveRight(maxX);
        }
    }
    //Metodos para mover al jugador a la izquierda
    public void moveLeft() {
        isMoving = true;
        direction = -1;
        x = Math.max(-50, x - velocityX);
        actualizarBitmap(left);
    }

    //Metodo para mover al jugador a la derecha
    public void moveRight(int maxX) {
        isMoving = true;
        direction = 1;
        x = Math.min(maxX - 350, x + velocityX);
        actualizarBitmap(right);
    }

    //Metodo para detener al jugador
    public void stopMoving() {
        isMoving = false;
        direction = 0;
    }

    //Metodo para actualizar la imagen del jugador
    private void actualizarBitmap(Bitmap[] bitmaps) {
        actualBitmapIndex = (actualBitmapIndex + 1) % bitmaps.length;
        actualBitmap = bitmaps[actualBitmapIndex];
    }

    //Metodo para eliminar una vida del jugador
    public void removeLife() {
        if (lifes > 0) lifes--;
    }

    //Metodo para obtener añadir una vida al jugador o si esta completa añadir 100 puntos
    public void addLife() {
        if (lifes < 3) lifes++;
        else score += 100;
    }

    //Metodo para añadir puntos al jugador
    public void addPoints() {
        score += 10;
    }

    public int getLifes() {
        return lifes;
    }

    public int getScore() {
        return score;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
