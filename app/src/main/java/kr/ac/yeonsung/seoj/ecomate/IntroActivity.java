package kr.ac.yeonsung.seoj.ecomate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        moveMain(1); //1초 후 Main으로 넘어감


        ImageView loading_gif = findViewById(R.id.loading_gif);


        Glide.with(this)
                .load(R.raw.loading)
                .override(1000, 1000)
                .into(loading_gif);

        Handler handler = new Handler();
    }


    private void moveMain(int sec) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);

                finish();
            }
        },2000*sec);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}