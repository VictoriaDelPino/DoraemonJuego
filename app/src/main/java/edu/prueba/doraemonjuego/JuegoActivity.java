package edu.prueba.doraemonjuego;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import edu.prueba.doraemonjuego.controller.DoraemonGameController;
import edu.prueba.doraemonjuego.model.DoraemonGameModel;
import edu.prueba.doraemonjuego.view.DoraemonGameView;

public class JuegoActivity extends AppCompatActivity {


    DoraemonGameController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int nivel = getIntent().getIntExtra("nivel", 1);

        controller = new DoraemonGameController(this, nivel);

// Aseguramos que el view ya ha sido inicializado antes de usarlo
        if (controller.view != null) {
            setContentView(controller.view);
            hideSystemUI();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            controller.start();
        }
    }

    private  void hideSystemUI(){
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.HONEYCOMB){
            controller.view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE //diseño estable sin cambio al ocultar la barras
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION //permite que la aplicacion de dibuje detras de la barra de navegacion
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // que se dibuje detras de la barra de estado
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar (botones virtuales)
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar (barra de estado hora y bateria..)
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //matiene el modo inmersivo incluso si el usuario toca la pantalla
            // cuando se presiona volumen, por ej, se cambia la visibilidad, hay que volver
            // a ocultar
            controller.view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    hideSystemUI();
                }
            });
        }
    }
}