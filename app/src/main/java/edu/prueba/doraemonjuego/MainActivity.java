package edu.prueba.doraemonjuego;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


//Actividad main del juego que muestra la pantalla de inicio
public class MainActivity extends AppCompatActivity {

    private ImageView imgLogo;
    private ImageButton imgBtnNivel1;
    private ImageButton imgBtnNivel2;
    private ImageButton imgBtnNivel3;
    private ImageButton imgBtnInfo;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Inicializa los botones
        imgLogo=findViewById(R.id.imageView);
        imgBtnNivel1 =findViewById(R.id.imgBtnNivel1);
        imgBtnNivel2 =findViewById(R.id.imgBtnNivel2);
        imgBtnNivel3 =findViewById(R.id.imgBtnNivel3);
        imgBtnInfo =findViewById(R.id.imageBtnInfo);

        //Inicializa el alpha de los botones para que no se vean inicialmente
        imgBtnNivel1.setAlpha(0f);
        imgBtnNivel2.setAlpha(0f);
        imgBtnNivel3.setAlpha(0f);

        AnimarLogo();
        // Animar los botones con retrasos
        handler.postDelayed(() -> AnimarBoton(imgBtnNivel1), 2500);
        handler.postDelayed(() -> AnimarBoton(imgBtnNivel2), 3000);
        handler.postDelayed(() -> AnimarBoton(imgBtnNivel3), 3500);

        //Inicia el juego al nivel 1
        imgBtnNivel1.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), JuegoActivity.class);
            i.putExtra("nivel", 1); // Enviar el valor 1
            startActivity(i);
            finish();
        });

        //Inicia el juego al nivel 2
        imgBtnNivel2.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), JuegoActivity.class);
            i.putExtra("nivel", 2); // Enviar el valor 2
            startActivity(i);
            finish();
        });

        //Inicia el juego al nivel 3
        imgBtnNivel3.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), JuegoActivity.class);
            i.putExtra("nivel", 3); // Enviar el valor 3
            startActivity(i);
            finish();
        });

        imgBtnInfo.setOnClickListener(v -> {
            showDialog();
        });
    }

    //Animacion del logo
    public void AnimarLogo(){
        AnimatorSet animadorBton = new AnimatorSet();

        ObjectAnimator fade = ObjectAnimator.ofFloat(imgLogo, "alpha", 0f, 1f);
        fade.setDuration(3000);
        animadorBton.play(fade);
        animadorBton.start();
    };

    //Animacion del boton
    public void AnimarBoton(ImageButton b){
        AnimatorSet animadorBton = new AnimatorSet();

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(b, "alpha", 0f, 1f);
        fadeIn.setDuration(1000);

        ObjectAnimator trasladar= ObjectAnimator.ofFloat(b,"translationY",1000,0);
        trasladar.setDuration(500);


        animadorBton.playTogether(fadeIn, trasladar);
        animadorBton.start();
    };

    //Muestra un dialogo con informacion del juego
    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Información del juego");
        builder.setMessage("¡Bienvenido al juego de Doraemon!\n\n" +
                "Eres Doraemon y tu misión es atrapar tantos dorayakis como puedas. ¡Son tu comida favorita!\n\n" +
                "Debes evitar a Nobita llorando, ya que si lo atrapas, perderás una vida.\n\n" +
                "Comienzas con 3 vidas. Si pierdes todas, el juego terminará.\n\n" +
                "Cada dorayaki que atrapes te dará 10 puntos.\n\n" +
                "Los corazones te devuelven una vida si tienes menos de 3. Si ya tienes las 3 vidas, te otorgan 1000 puntos.\n\n" +
                "La velocidad de caída de los objetos aumentará cada vez que atrapes uno.\n\n" +
                "Objetivo para ganar:\n" +
                "   - Nivel 1: 500 puntos\n" +
                "   - Nivel 2: 1000 puntos\n" +
                "   - Nivel 3: 1500 puntos\n\n" +
                "¡Buena suerte y que consigas muchos dorayakis! 🎉");

        builder.setNeutralButton("Cerrar", (dialog, which) -> {

        });
        builder.setOnDismissListener(v->{

        });
        builder.create().show();
    }

    //Al dar atras cierra la aplicacion
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}