package edu.prueba.doraemonjuego;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

//Activity de la pantalla final del juego
public class FinalScreenActivity extends AppCompatActivity {

    private MediaPlayer musicaVictoria;
    private MediaPlayer musicaDerrota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        boolean isWin = getIntent().getBooleanExtra("isWin", false);
        int level = getIntent().getIntExtra("level", 1);
        musicaVictoria = MediaPlayer.create(this, R.raw.ganar1);
        musicaDerrota = MediaPlayer.create(this, R.raw.perder1);
        //Comprueba si ha ganado o perdido para poner distinito layout y sonido
        if(isWin){
            setContentView(R.layout.win_layout);
            musicaVictoria.start();
        }else{
            setContentView(R.layout.game_over_layout);
            musicaDerrota.start();
        }

        //inicializa los botones y les da su funcion dependiendo de si ha ganado o perdido
        if(isWin){
            findViewById(R.id.imgBtnInicio_win).setOnClickListener(v->{
                Intent i = new Intent(this, LauncherActivity.class);
                startActivity(i);
                finish();
            });
            findViewById(R.id.imgBtnSigNivel).setOnClickListener(v->{
                if(level<3){
                    Intent i = new Intent(this, JuegoActivity.class);
                    i.putExtra("nivel", level+1);
                    startActivity(i);
                    finish();
                }else{
                    showDialog();
                }
            });
        }else{
            findViewById(R.id.imgBtnInicio_gameOver).setOnClickListener(v->{
                Intent i = new Intent(this, LauncherActivity.class);
                startActivity(i);
                finish();
            });
            findViewById(R.id.imgBtnVolverIntentar).setOnClickListener(v->{
                    Intent i = new Intent(this, JuegoActivity.class);
                    i.putExtra("nivel", level);
                    startActivity(i);
                    finish();
            });
        }
    }

    //Si se supera el ultimo nivel al dar al boton de siguiente muestra un dialogo
    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Juego completado");
        builder.setMessage("Vas a volver al menú");
        builder.setPositiveButton("Llevame", (dialog, which) -> {
            Intent i = new Intent(this, LauncherActivity.class);
            startActivity(i);
            finish();
        });
        builder.setNegativeButton("Llevame, no me queda otra :(", (dialog, which) -> {
            Intent i = new Intent(this, LauncherActivity.class);
            startActivity(i);
            finish();
        });
        builder.setOnDismissListener(v->{

        });
        builder.create().show();
    }

    //Al dar atras vuelve al menu principal
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, LauncherActivity.class);
        startActivity(i);
        finish();
    }
}