package edu.prueba.doraemonjuego.data;

import android.graphics.Bitmap;

public class Enemy {

    private float x;
    private float y;
    private int width;
    private int height;
    private Bitmap bitmap;

    public Enemy(int height, int width, float x, float y) {
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
        bitmap=GamePersistance.enemy;
    }

    public void moveDown(Float multiplier) throws Throwable {
        y-=0.1*multiplier;
        if(y<0) autoDestroy();
    }


    public void collide(Player player) throws Throwable {
        player.removeLife();
        autoDestroy();
    }

    public void autoDestroy() throws Throwable {
        finalize();
    }
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
