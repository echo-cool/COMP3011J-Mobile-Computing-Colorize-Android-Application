package com.echo.stinger_game.myganme;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Wang Yuyang
 * @date 2021-10-11 10:40:25
 */
public class GameActivity extends AppCompatActivity {
    private long last_time = 0;
    private int counter = 0;

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
                long delta = event.getDownTime() - last_time;
                if (delta < 200) {
                    counter += 1;
                    if (counter > 20) {
                        counter -= 20;
                    }
                }
                System.out.println(delta);
                if (counter >= 10) {
                    GameView.DEBUG = true;
                } else {
                    GameView.DEBUG = false;
                }
                last_time = event.getDownTime();
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