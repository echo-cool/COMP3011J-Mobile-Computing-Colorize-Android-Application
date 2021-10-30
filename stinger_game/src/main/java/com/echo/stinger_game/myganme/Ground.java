package com.echo.stinger_game.myganme;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ground implements Drawable {
    private final Paint paint;

    public Ground() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setStrokeWidth(10f);
        paint.setColor(Color.GRAY);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine(0, Scene.groundY, Scene.screenWidth, Scene.groundY, paint);
    }

}
