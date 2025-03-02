package edu.prueba.doraemonjuego.data;

import android.graphics.Bitmap;

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


    public Player(int height, int width, float x, float y) {
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
        score=0;
        left= (Bitmap[]) GamePersistance.playerMovements.get("l");
        right= (Bitmap[]) GamePersistance.playerMovements.get("r");
        actualBitmap=right[0];
        lifes=3;
        actualBitmapIndex=0;

    }


    public void removeLife(){
        if(lifes>0) lifes--;
    }
    public void addLife(){
        if(lifes<3) lifes++;
        else score+=100;
    }

    public void addPoints(){
        score+=10;
    }

    public void moveLeft(){
        if (x<=10) x=10;
        else x--;
        if(isDirectionRight()) actualBitmap=left[0];
        else {
            actualBitmapIndex++;
            if(actualBitmapIndex>=left.length) actualBitmapIndex=0;
            actualBitmap=left[actualBitmapIndex];
        }
    }

    public void moveRight(int maxX){
        if (x>= maxX-10) x=maxX-10;
        else x++;
        if(!isDirectionRight()) actualBitmap=right[0];
        else {
            actualBitmapIndex++;
            if(actualBitmapIndex>=right.length) actualBitmapIndex=0;
            actualBitmap=right[actualBitmapIndex];
        }
    }

    private boolean isDirectionRight(){
        boolean isRight=false;
        for (Bitmap b: right) {
            if(actualBitmap==b) isRight=true;

        }
        return isRight;
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

}
