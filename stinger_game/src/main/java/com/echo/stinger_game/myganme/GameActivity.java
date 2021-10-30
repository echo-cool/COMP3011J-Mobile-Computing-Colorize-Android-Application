package com.echo.stinger_game.myganme;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        makeFullScreen();
        super.onCreate(savedInstanceState);
        GameView gameView = new GameView(this);
//        getSupportActionBar().hide();

        setContentView(gameView);
//        gameView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Scene.player.startMoveUpY(6400);
//            }
//        });
        gameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Scene.player.startMoveUpY(6400);
                return false;
            }
        });

    }

    public void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}