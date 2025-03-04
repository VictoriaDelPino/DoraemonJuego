package edu.prueba.doraemonjuego.data;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;

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

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, x, y, null);
        Log.d("Instanciar_entidad", "Pintado enemigo");
    }

    public void moveDown(Float multiplier) throws Throwable {
        y+=0.1*multiplier;
        if(y<0) autoDestroy();
    }


    public boolean collide(Player player) throws Throwable {
        float playerL, playerR, playerT, playerB;
        float otherL, otherR, otherT, otherB;
        playerL = player.getX()+50;
        playerR = player.getX() + player.getWidth();
        playerT = player.getY()+50;
        playerB = player.getY() + player.getHeight();
        otherL = x;
        otherR = x + width;
        otherT = y;
        otherB = y + height;
        boolean isCollide = !(otherT > playerB || otherB < playerT || otherL > playerR || otherR < playerL);


        if( isCollide){
            player.removeLife();
            autoDestroy();
        }
        return isCollide;
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
