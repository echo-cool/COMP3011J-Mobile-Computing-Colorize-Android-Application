package com.echo.colorizeit.myganme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.myapplication.R;

public class Player extends RectHittableObject {
    public static final int playerWidth = 150;
    public static final int playerHeight= 150;
    public static final float g = 9.8f;
    private static Thread thread;
    private static Boolean isJumping = false;
    private Bitmap player_image;


    public Player(int x, int y, int width, int height, Context context) {
        super(x, y, width, height, context);

    }

    @Override
    public void startMoveUpY(int speed) {
        if(!isJumping) {
            isJumping = true;
            long start_time = System.currentTimeMillis();
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    double h = 0;
                    long t = 0;
                    while (h >= 0) {
                        long start = System.currentTimeMillis();
                        long current_time = System.currentTimeMillis();
                        t = (current_time - start_time);
                        h = (speed * t - 0.5 * g * t * t) / 10000;
                        setLocation(getX(), (int) (Scene.groundY - Player.playerHeight - h));
                        long end = System.currentTimeMillis();
                        while (end - start < GameView.minDrawTime) {
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            end = System.currentTimeMillis();
                        }
                    }
                    setLocation(getX(), Scene.groundY - Player.playerHeight);
                    System.out.println(getBorder_rect());
                    isJumping = false;

                }
            });
            thread.start();
        }
    }

    @Override
    public void startMoveLeftX(int speed) {
        //stop x move
    }

    @Override
    public void setX(int x) {

    }

    @Override
    public void draw(Canvas canvas) {

        Paint paint = getDefault_paint();
        paint.setColor(Color.BLUE);
//        canvas.drawRect(getBorderRect(), paint);
        if(player_image==null)
            player_image = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.player);
        canvas.drawBitmap(player_image,null,getBorder_rect(),null);
    }

}
