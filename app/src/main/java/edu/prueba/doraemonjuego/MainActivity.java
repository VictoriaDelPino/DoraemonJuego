package edu.prueba.doraemonjuego;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ImageView imgLogo;
    private ImageButton imgBtnNivel1;
    private ImageButton imgBtnNivel2;
    private ImageButton imgBtnNivel3;
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

        imgLogo=findViewById(R.id.imageView);
        imgBtnNivel1 =findViewById(R.id.imgBtnNivel1);
        imgBtnNivel2 =findViewById(R.id.imgBtnNivel2);
        imgBtnNivel3 =findViewById(R.id.imgBtnNivel3);

        imgBtnNivel1.setAlpha(0f);
        imgBtnNivel2.setAlpha(0f);
        imgBtnNivel3.setAlpha(0f);

        AnimarLogo();
        // Animar los botones con retrasos
        handler.postDelayed(() -> AnimarBoton(imgBtnNivel1), 2500);
        handler.postDelayed(() -> AnimarBoton(imgBtnNivel2), 3000);
        handler.postDelayed(() -> AnimarBoton(imgBtnNivel3), 3500);


        imgBtnNivel1.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), JuegoActivity.class);
            i.putExtra("nivel", 1); // Enviar el valor 1
            startActivity(i);
        });

        imgBtnNivel2.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), JuegoActivity.class);
            i.putExtra("nivel", 2); // Enviar el valor 2
            startActivity(i);
        });

        imgBtnNivel3.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), JuegoActivity.class);
            i.putExtra("nivel", 3); // Enviar el valor 3
            startActivity(i);
        });
    }


    public void AnimarLogo(){
        AnimatorSet animadorBton = new AnimatorSet();

        //2ª Animación fade in de 8 segundos
        ObjectAnimator fade = ObjectAnimator.ofFloat(imgLogo, "alpha", 0f, 1f);
        fade.setDuration(3000);
        animadorBton.play(fade);
        animadorBton.start();
    };

    public void AnimarBoton(ImageButton b){
        AnimatorSet animadorBton = new AnimatorSet();


        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(b, "alpha", 0f, 1f);
        fadeIn.setDuration(1000);
        //1ª animación, trasladar desde la izquierda (800 pixeles menos hasta la posición
        //inicial (0)
        ObjectAnimator trasladar= ObjectAnimator.ofFloat(b,"translationY",1000,0);
        trasladar.setDuration(500);//duración 5 segundos




        animadorBton.playTogether(fadeIn, trasladar);
        animadorBton.start();
    };

}