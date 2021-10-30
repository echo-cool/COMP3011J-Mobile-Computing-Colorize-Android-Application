package com.echo.stinger_game.myganme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.echo.stinger_game.R;


/**
 * @author Wang Yuyang
 * @date 2021-10-11 10:48:52
 */
public class Player extends RectHittableObject {
    public static final int playerWidth = 150;
    public static final int playerHeight = 150;
    public static final float g = 9.8f;
    private static Thread thread;
    private static Boolean isJumping = false;
    private static Boolean canNewJumping = true;
    private Bitmap player_image;
    private Bitmap player_image_all;
    private long jump_start_time;
    private long jump_current_time;
    private int jump_up_v0 = 6400;
    private double jump_h = 0;
    private final Rect[] crop_array = new Rect[6];


    public Player(int x, int y, int width, int height, Context context) {
        super(x, y, width, height, context);

    }

    public static int getPlayerWidth() {
        return playerWidth;
    }

    public static int getPlayerHeight() {
        return playerHeight;
    }

    public static float getG() {
        return g;
    }

    public static Boolean getIsJumping() {
        return isJumping;
    }

    public static void setIsJumping(Boolean isJumping) {
        Player.isJumping = isJumping;
    }

    public static Boolean getCanNewJumping() {
        return canNewJumping;
    }

    public static void setCanNewJumping(Boolean canNewJumping) {
        Player.canNewJumping = canNewJumping;
    }

    @Override
    public void startMoveUpY(int speed) {
        if (!isJumping) {
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
                        jump_h = h;
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
    public void moveSingle() {
//        if(canNewJumping){
//            jump_start_time = System.currentTimeMillis();
//            canNewJumping = false;
//            isJumping = true;
//        }
//        if(isJumping) {
//            jump_current_time = System.currentTimeMillis();
//            int x = getX() - jump_up_v0 / GameView.FPS;
//            long t = (jump_current_time - jump_start_time);
//            long h = (long) ((jump_up_v0 * t - 0.5 * g * t * t) / 10000);
//            if (h <= 0) {
//                isJumping = false;
//                canNewJumping = true;
//            }
//            else {
//                setLocation(getX(), (int) (Scene.groundY - Player.playerHeight - h));
//            }
//        }

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
        if (GameView.DEBUG)
            canvas.drawRect(getBorderRect(), getDefault_paint());
        Paint paint = getDefault_paint();
        paint.setColor(Color.BLUE);
        if (player_image == null) {
//            player_image = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.player);
            player_image_all = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.playerall);
            int single_width = player_image_all.getWidth() / 6;
            int single_height = player_image_all.getHeight();
            for (int i = 0; i < 6; i++) {
                int x = single_width * i;
                crop_array[i] = new Rect(x, 0, x + single_width, single_height);
            }
        }

        canvas.drawBitmap(player_image_all, crop_array[(Scene.distance / 20) % 4], getBorder_rect(), null);

    }

    public Bitmap getPlayer_image() {
        return player_image;
    }

    public void setPlayer_image(Bitmap player_image) {
        this.player_image = player_image;
    }

    public long getJump_start_time() {
        return jump_start_time;
    }

    public void setJump_start_time(long jump_start_time) {
        this.jump_start_time = jump_start_time;
    }

    public long getJump_current_time() {
        return jump_current_time;
    }

    public void setJump_current_time(long jump_current_time) {
        this.jump_current_time = jump_current_time;
    }

    public int getJump_up_v0() {
        return jump_up_v0;
    }

    public void setJump_up_v0(int jump_up_v0) {
        this.jump_up_v0 = jump_up_v0;
    }


    public double getJump_h() {
        return jump_h;
    }

    public void setJump_h(double jump_h) {
        this.jump_h = jump_h;
    }
}
