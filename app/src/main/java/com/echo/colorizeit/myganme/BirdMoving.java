package com.echo.colorizeit.myganme;

import android.content.Context;

import java.util.Random;

public class BirdMoving extends Bird{
    private int randomNum = 0;
    public BirdMoving(int x, int y, int width, int height, Context context) {
        super(x, y, width, height, context);
        Random random = new Random();
        randomNum = random.nextInt(2);
    }

    @Override
    public void moveSingle() {

        int x = getX() - (Scene.speed + 300*randomNum)/GameView.FPS;
//        int y = getY() - (300*randomNum)/GameView.FPS;
        if ((x > 0 - getWidth() - 100 && x <= Scene.screenWidth && getMoveStarted())) {
            setLocation(x, getY());
        } else {
            setMoveStarted(false);
        }
    }
}
