package com.echo.colorizeit.myganme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.myapplication.R;

public class Cloud extends RectHittableObject{
    private Bitmap cloud;
    public Cloud(int x, int y, int width, int height, Context context) {
        super(x, y, width, height, context);
    }

    @Override
    public void draw(Canvas canvas) {

        if(cloud== null)
            cloud = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.cloud);
        canvas.drawBitmap(cloud, null, getBorder_rect(), null);

    }
    @Override
    public Boolean checkHit(Rect rect) {
        return false;
    }
}
