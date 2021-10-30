package com.echo.stinger_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.echo.stinger_game.myganme.GameActivity;

/**
 * @author Wang Yuyang
 * @date 2021-10-30 16:08:27
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}