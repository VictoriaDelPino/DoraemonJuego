package edu.prueba.doraemonjuego.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.Map;

import edu.prueba.doraemonjuego.R;

//clase que almacena las imagenes del juego
public class GamePersistance {

    public static Bitmap enemy, point,  background, ground, life;
    public static Map playerMovements = new HashMap();


    public static void initialize(Context context){
        enemy= BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy );
        point= BitmapFactory.decodeResource(context.getResources(), R.drawable.dorayaki );

        Bitmap[] playerR = new Bitmap[]{BitmapFactory.decodeResource(context.getResources(), R.drawable.playerr0 ),
              BitmapFactory.decodeResource(context.getResources(), R.drawable.playerr1 ),
              BitmapFactory.decodeResource(context.getResources(), R.drawable.playerr2 ),
              BitmapFactory.decodeResource(context.getResources(), R.drawable.playerr3 ),
              BitmapFactory.decodeResource(context.getResources(), R.drawable.playerr4 ),
              BitmapFactory.decodeResource(context.getResources(), R.drawable.playerr5 )};

        Bitmap[] playerL = new Bitmap[]{BitmapFactory.decodeResource(context.getResources(), R.drawable.playeri0),
              BitmapFactory.decodeResource(context.getResources(), R.drawable.playeri1),
              BitmapFactory.decodeResource(context.getResources(), R.drawable.playeri2),
              BitmapFactory.decodeResource(context.getResources(), R.drawable.playeri3),
              BitmapFactory.decodeResource(context.getResources(), R.drawable.playeri4),
              BitmapFactory.decodeResource(context.getResources(), R.drawable.playeri5)};

        playerMovements.put("r", playerR);
        playerMovements.put("l", playerL);

        background= BitmapFactory.decodeResource(context.getResources(), R.drawable.cielo );
        ground= BitmapFactory.decodeResource(context.getResources(), R.drawable.suelo );
            life= BitmapFactory.decodeResource(context.getResources(), R.drawable.vida );
    }
}
