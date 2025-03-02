package edu.prueba.doraemonjuego.data;

import java.util.ArrayList;
import java.util.List;

public class GameInstance {
    public Player player;
    public List<Enemy> enemies;
    public List<Point> points;
    public int score;
    public int level;
    public List<PowerUp> lifes;

    public GameInstance(int level,int maxX, int maxY) {
        enemies = new ArrayList<>();
        this.level = level;
        lifes = new ArrayList<>();
        player = new Player(10,10,maxX/2-200, maxY-maxY/4);
        points = new ArrayList<>();
        score = 0;
    }
}
