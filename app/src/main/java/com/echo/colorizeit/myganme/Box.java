package com.echo.colorizeit.myganme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.myapplication.R;

public class Box extends RectHittableObject {
    private Bitmap obstacle;

    public Box(int x, int y, int width, int height, Context context) {
        super(x, y, width, height, context);


    }


    @Override
    public void draw(Canvas canvas) {
//        canvas.drawRect(getBorderRect(), getDefault_paint());

        if(obstacle== null)
            obstacle = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.obstacle);
        canvas.drawBitmap(obstacle, null, getBorder_rect(), null);

    }

}
