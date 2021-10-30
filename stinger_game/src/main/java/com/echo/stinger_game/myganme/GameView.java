package com.echo.stinger_game.myganme;

import static android.content.Context.VIBRATOR_SERVICE;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 * @author Wang Yuyang
 * @date 2021-10-11 11:26:47
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    public static final Boolean DEBUG = true;
    public static int FPS = 60;
    public static int minDrawTime = 1000 / FPS;
    public static Context context;
    private SurfaceHolder holder;
    private Boolean isDrawing;
    private Canvas canvas;
    private Paint string_paint;
    private Paint string_paint1;
    private Scene scene;
    private final ArrayList<Integer> FPS_data = new ArrayList<>();
    private float mean_fps = 0;
    private Boolean isHit = false;
    private Boolean isGameOver = false;


    public GameView(Context context) {
        super(context);
        GameView.context = context;
        init();

    }

    private void init() {
        holder = getHolder();//得到SurfaceHolder对象
        holder.addCallback(this);//注册SurfaceHolder
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);//保持屏幕长亮
        //画笔
        string_paint = new Paint();
        string_paint.setColor(Color.BLACK);
        string_paint.setTextSize(50);

        string_paint1 = new Paint();
        string_paint1.setColor(Color.BLACK);
        string_paint1.setTextSize(250);

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        isDrawing = true;
        new Thread(this).start();
        int ScreenWidth = Math.abs(holder.getSurfaceFrame().right - holder.getSurfaceFrame().left);
        int ScreenHeight = Math.abs(holder.getSurfaceFrame().top - holder.getSurfaceFrame().bottom);
        scene = new Scene(ScreenWidth, ScreenHeight);
        Scene.init_game_object(context);
//        Scene.startMove();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            holder.getSurface().setFrameRate(120, Surface.FRAME_RATE_COMPATIBILITY_DEFAULT);
//        }

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        isDrawing = false;
        //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Scene.end();
    }

    @Override
    public void run() {
        Log.d("Draw Thread", "Draw Start");
        long last_end = System.currentTimeMillis();
        long last_start = System.currentTimeMillis();
        while (isDrawing) {
            long start = System.currentTimeMillis();
            Scene.startMove();
            drawing(last_start, last_end);
            long end = System.currentTimeMillis();
            while (end - start < minDrawTime) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                end = System.currentTimeMillis();
            }
            last_end = end;
            last_start = start;
        }
    }

    private void drawing(long start, long end) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas = holder.lockHardwareCanvas();
            } else {
                canvas = holder.lockCanvas();
            }
            if (canvas != null) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                canvas.drawColor(Color.WHITE, PorterDuff.Mode.ADD);
                int real_FPS = (int) (1000 / ((end - start) == 0 ? 1 : (end - start)));
                FPS_data.add(real_FPS);
                if (FPS_data.size() >= 5) {
                    int sum = 0;
                    for (int i : FPS_data) {
                        sum += i;
                    }
                    mean_fps = sum / FPS_data.size();
                    FPS_data.clear();
                }
                canvas.drawText("FPS: " + mean_fps, 50, 50, string_paint);
                canvas.drawText("Distance: " + (int) (Scene.distance / 100), 450, 50, string_paint);
                try {
                    for (int i = 0; i < Scene.rectHittableObjects.size(); i++) {
                        RectHittableObject o = Scene.rectHittableObjects.get(i);
                        if (o.getX() > 0 - o.getWidth() - 100)
                            o.draw(canvas);
                        else {
                            Scene.rectHittableObjects.remove(o);
                        }
                        if (!o.equals(Scene.player))
                            if (o.checkHit(Scene.player.getBorderRect())) {
                                isHit = true;
                                break;
                            }
                    }
                } catch (ConcurrentModificationException e) {
                    System.out.println("ConcurrentModificationException");
                }
                if (DEBUG) {
                    canvas.drawText("DrawTime: " + (end - start) + " ms", 50, 100, string_paint);
                    canvas.drawText("Width: " + canvas.getWidth(), 50, 150, string_paint);
                    canvas.drawText("Height: " + canvas.getHeight(), 50, 200, string_paint);
                    canvas.drawText("HardwareAccelerated: " + canvas.isHardwareAccelerated(), 50, 250, string_paint);
                    canvas.drawText("ClipBounds: " + canvas.getClipBounds().toString(), 50, 300, string_paint);
                    int count = 0;
                    for (RectHittableObject o : Scene.rectHittableObjects) {
                        if (o.getMoveStarted()) {
                            count += 1;
                        }
                        if (!o.equals(Scene.player) && o.getMoveStarted()) {
                            int x1 = Scene.player.getX();
                            int y1 = Scene.player.getY();
                            int x2 = o.getX();
                            int y2 = o.getY();
                            int distance = (int) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
                            Paint paint = new Paint();
                            paint.setStrokeWidth(5f);
                            paint.setColor(Color.CYAN);

                            if (distance < 450) {
                                string_paint.setColor(Color.RED);
                                string_paint.setTextSize(80);
                                paint.setColor(Color.RED);
                                canvas.drawLine(Scene.player.getX(), Scene.player.getY(), o.getX(), o.getY(), paint);
                                canvas.drawText(String.valueOf(distance), (x1 + x2) / 2, (y1 + y2) / 2, string_paint);
                                string_paint.setColor(Color.BLACK);
                                paint.setColor(Color.BLACK);
                                string_paint.setTextSize(50);
                            } else {
                                canvas.drawLine(Scene.player.getX(), Scene.player.getY(), o.getX(), o.getY(), paint);
                                canvas.drawText(String.valueOf(distance), (x1 + x2) / 2, (y1 + y2) / 2, string_paint);
                            }

                        }
                    }
                    if (DEBUG) {
                        Paint paint = new Paint();
                        paint.setTextSize(100);
                        paint.setColor(Color.RED);
                        canvas.drawText("DEBUG MODE", (int) (Scene.screenWidth * 0.6), 200, paint);
                    }
                    if (Player.getIsJumping() && !isHit) {
                        canvas.drawText("Need Jump !", (int) (Scene.screenWidth * 0.2), Scene.screenHeight / 2, string_paint1);
                    }
                    canvas.drawText("Moving Obj: " + count, 50, 350, string_paint);
                    canvas.drawText("Jump H: " + Scene.player.getJump_h(), 50, 400, string_paint);
                    canvas.drawText("Jump: " + Player.getIsJumping(), 50, 450, string_paint);
                    canvas.drawText("Draw Obj Count: " + Scene.rectHittableObjects.size(), 50, 500, string_paint);
                    canvas.drawText("Left up: " + Scene.player.getLeft_up(), Scene.player.getX() - 100, Scene.player.getY() + Player.playerHeight + 100, string_paint);

                }
                for (Drawable o : Scene.staticObject) {
                    o.draw(canvas);
                }
                if (isHit) {
                    System.out.println("HIT!!!!!!!!!!!!!!!!");
                    canvas.drawText("Game Over !", (int) (Scene.screenWidth * 0.2), Scene.screenHeight / 2, string_paint1);
                    final Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
                    if (vibrator.hasVibrator())
                        vibrator.vibrate(300);
                    isHit = false;
                    isGameOver = true;
                    Scene.end();
                    Scene.init_game_object(context);
                }
            }
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
                if (isGameOver) {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    isGameOver = false;
                }
            }
        }
    }
}
