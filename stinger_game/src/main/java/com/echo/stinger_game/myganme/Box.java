package com.echo.stinger_game.myganme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.echo.stinger_game.R;

public class Box extends RectHittableObject {
    private Bitmap obstacle;
    private final int speed = Scene.speed;

    public Box(int x, int y, int width, int height, Context context) {
        super(x, y, width, height, context);


    }


    @Override
    public void draw(Canvas canvas) {
        if (GameView.DEBUG)
            canvas.drawRect(getBorderRect(), getDefault_paint());

        if (obstacle == null)
            obstacle = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.obstacle);
        canvas.drawBitmap(obstacle, null, getBorder_rect(), null);

    }

    @Override
    public void moveSingle() {
        int x = getX() - speed / GameView.FPS;
        if ((x > 0 - getWidth() - 100 && x <= Scene.screenWidth && getMoveStarted())) {
            setLocation(x, getY());
        } else {
            setMoveStarted(false);

        }
    }
}
