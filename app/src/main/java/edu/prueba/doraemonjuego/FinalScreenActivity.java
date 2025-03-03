package edu.prueba.doraemonjuego;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FinalScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isWin = getIntent().getBooleanExtra("isWin", false);
        int level = getIntent().getIntExtra("level", 1);
        if(isWin){
            setContentView(R.layout.win_layout);
        }else{
            setContentView(R.layout.game_over_layout);
        }

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

        }
    }

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
}