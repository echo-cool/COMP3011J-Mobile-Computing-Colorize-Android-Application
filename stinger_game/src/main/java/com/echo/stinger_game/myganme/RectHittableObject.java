package com.echo.stinger_game.myganme;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public abstract class RectHittableObject implements Hittable, Movable, Drawable {
    private int x;
    private int y;
    private int width;
    private int height;
    private Point left_up;
    private Point left_bottom;
    private Point right_up;
    private Point right_bottom;
    private Rect border_rect;
    private Boolean moveStarted = false;
    private Paint default_paint;
    private Context context;
    private Thread thread;
    private Boolean blocking = false;


    public RectHittableObject(int x, int y, int width, int height, Context context) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.context = context;
        left_up = new Point(x, y);
        left_bottom = new Point(x, y + height);
        right_up = new Point(x + width, y);
        right_bottom = new Point(x + width, y + height);
        border_rect = new Rect(x, y, x + width, y + height);

        default_paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        default_paint.setStrokeWidth(10f);
        default_paint.setColor(Color.parseColor("#FF4081"));
        default_paint.setStyle(Paint.Style.STROKE);
        default_paint.setStrokeJoin(Paint.Join.ROUND);
        default_paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public RectHittableObject(Point left_up, Point left_bottom, Point right_up, Point right_bottom, Context context) {
        this.left_up = left_up;
        this.left_bottom = left_bottom;
        this.right_up = right_up;
        this.right_bottom = right_bottom;
        this.context = context;
        x = left_up.x;
        y = left_up.y;
        width = Math.abs(left_up.x - right_up.x);
        height = Math.abs(left_up.y - left_bottom.y);
        border_rect = new Rect(x, y, x + width, y + height);

        default_paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        default_paint.setStrokeWidth(10f);
        default_paint.setColor(Color.parseColor("#FF4081"));
        default_paint.setStyle(Paint.Style.STROKE);
        default_paint.setStrokeJoin(Paint.Join.ROUND);
        default_paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Point getLeft_up() {
        return left_up;
    }

    public void setLeft_up(Point left_up) {
        this.left_up = left_up;
    }

    public Point getLeft_bottom() {
        return left_bottom;
    }

    public void setLeft_bottom(Point left_bottom) {
        this.left_bottom = left_bottom;
    }

    public Point getRight_up() {
        return right_up;
    }

    public void setRight_up(Point right_up) {
        this.right_up = right_up;
    }

    public Point getRight_bottom() {
        return right_bottom;
    }

    public void setRight_bottom(Point right_bottom) {
        this.right_bottom = right_bottom;
    }

    public Rect getBorder_rect() {
        return border_rect;
    }

    public void setBorder_rect(Rect border_rect) {
        this.border_rect = border_rect;
    }

    public Paint getDefault_paint() {
        return default_paint;
    }

    public void setDefault_paint(Paint default_paint) {
        this.default_paint = default_paint;
    }

    @Override
    public Rect getBorderRect() {
        return border_rect;
    }

    @Override
    public Boolean checkHit(Rect rect) {
        return getBorderRect().intersect(rect);
    }

    @Override
    public Boolean checkHit(int left, int top, int right, int bottom) {
        return getBorderRect().intersect(new Rect(left, top, right, bottom));
    }

    @Override
    public Boolean checkHit(Point left_up, Point left_bottom, Point right_up, Point right_bottom) {
        return getBorderRect().intersect(new Rect(left_up.x, left_up.y, right_up.x, right_bottom.y));
    }

    @Override
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        left_up = new Point(x, y);
        left_bottom = new Point(x, y + height);
        right_up = new Point(x + width, y);
        right_bottom = new Point(x + width, y + height);
        border_rect = new Rect(x, y, x + width, y + height);
    }

    @Override
    public void moveX(int distance) {
        this.x = this.x + distance;
        left_up = new Point(x, y);
        left_bottom = new Point(x, y + height);
        right_up = new Point(x + width, y);
        right_bottom = new Point(x + width, y + height);
        border_rect = new Rect(x, y, x + width, y + height);
    }

    @Override
    public void moveY(int distance) {
        this.y = this.y + distance;
        left_up = new Point(x, y);
        left_bottom = new Point(x, y + height);
        right_up = new Point(x + width, y);
        right_bottom = new Point(x + width, y + height);
        border_rect = new Rect(x, y, x + width, y + height);
    }

    @Override
    public void startMoveLeftX(int speed) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (x > 0 - Scene.screenWidth && x < Scene.screenWidth && Scene.running) {
                    long start = System.currentTimeMillis();
                    x = x - speed / GameView.FPS;
                    setLocation(x, y);
                    long end = System.currentTimeMillis();
                    while (end - start < GameView.minDrawTime) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            //e.printStackTrace();
                        }
                        end = System.currentTimeMillis();
                    }
                }
            }
        });
        thread.start();
        moveStarted = true;
    }

    @Override
    public void startMoveUpY(int speed) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (y > 0 - Scene.screenHeight && y < Scene.screenHeight && Scene.running) {
                    long start = System.currentTimeMillis();
                    y = y - speed / GameView.FPS;
                    setLocation(x, y);
                    long end = System.currentTimeMillis();
                    while (end - start < GameView.minDrawTime) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            //e.printStackTrace();
                        }
                        end = System.currentTimeMillis();
                    }
                }
            }
        });
        thread.start();
        moveStarted = true;
    }

    @Override
    public void startMoveXY(int speedX, int speedY) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while ((y > 0 - Scene.screenHeight || x > 0 - Scene.screenWidth) && (y < Scene.screenHeight || x < Scene.screenWidth)) {
                    long start = System.currentTimeMillis();
                    y = y - speedY / GameView.FPS;
                    x = x - speedX / GameView.FPS;
                    setLocation(x, y);
                    long end = System.currentTimeMillis();
                    while (end - start < GameView.minDrawTime) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            //e.printStackTrace();
                        }
                        end = System.currentTimeMillis();
                    }
                }
            }
        });
        thread.start();
        moveStarted = true;
    }

    public Boolean getMoveStarted() {
        return moveStarted;
    }

    public void setMoveStarted(Boolean moveStarted) {
        this.moveStarted = moveStarted;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Boolean getBlocking() {
        return blocking;
    }

    public void setBlocking(Boolean blocking) {
        this.blocking = blocking;
    }
}
