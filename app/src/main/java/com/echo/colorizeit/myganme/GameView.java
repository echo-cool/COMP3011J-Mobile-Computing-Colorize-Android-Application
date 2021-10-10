package com.echo.colorizeit.myganme;

import static android.content.Context.VIBRATOR_SERVICE;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder holder;
    private Boolean isDrawing;
    private Canvas canvas;
    private Paint string_paint;
    private Paint string_paint1;
    public static final int FPS = 100;
    public static final int minDrawTime = 1000 / FPS;
    private Scene scene;
    public static Context context;
    private ArrayList<Integer> FPS_data = new ArrayList<>();
    private float mean_fps = 0;
    private Boolean isHit = false;


    public GameView(Context context) {
        super(context);
        this.context = context;
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
        Scene.startMove();

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        isDrawing = false;
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Scene.end();
    }

    @Override
    public void run() {
        Log.d("Draw Thread", "Draw Start");
        long last_end = System.currentTimeMillis();
        long last_start = System.currentTimeMillis();
        while (isDrawing) {
            long start = System.currentTimeMillis();
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
            canvas = holder.lockCanvas();
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
                canvas.drawText("FPS: " + String.valueOf(mean_fps), 50, 50, string_paint);
                canvas.drawText("Distance: " + String.valueOf((int) (Scene.distance / 100)), 450, 50, string_paint);
                for (RectHittableObject o : Scene.rectHittableObjects) {
                    o.draw(canvas);
                    if (!o.equals(Scene.player))
                        if (o.checkHit(Scene.player.getBorderRect())) {
                            isHit = true;
                            break;
                        }
                }
                for (Drawable o : Scene.staticObject) {
                    o.draw(canvas);
                }
                if(isHit){
                    System.out.println("HIT!!!!!!!!!!!!!!!!");
                    canvas.drawText("Game Over !" , (int) (Scene.screenWidth * 0.2), Scene.screenHeight/2, string_paint1);
                    final Vibrator vibrator=(Vibrator)context.getSystemService(VIBRATOR_SERVICE);
                    if(vibrator.hasVibrator())
                        vibrator.vibrate(300);
                    isHit = false;
                    Scene.end();
                    Scene.init_game_object(context);
                    Scene.startMove();

                }
            }
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
