package com.echo.colorizeit.myganme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.myapplication.R;

public class Bird extends RectHittableObject {
    private Bitmap bird;
    private int speed = Scene.speed;

    public Bird(int x, int y, int width, int height, Context context) {
        super(x, y, width, height, context);
    }

    @Override
    public void draw(Canvas canvas) {
        if (GameView.DEBUG)
            canvas.drawRect(getBorderRect(), getDefault_paint());
        if (bird == null)
            bird = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.bird2);
        canvas.drawBitmap(bird, null, getBorder_rect(), null);

    }

    @Override
    public Boolean checkHit(Rect rect) {
        return false;
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
