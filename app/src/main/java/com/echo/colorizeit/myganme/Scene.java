package com.echo.colorizeit.myganme;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class Scene {
    private static int speed = 420;
    public static int distance = 0; //from right of the screen
    public static int screenWidth;
    public static int screenHeight;
    public static int groundY;
    public static ArrayList<Drawable> staticObject = new ArrayList<>();
    public static ArrayList<RectHittableObject> rectHittableObjects = new ArrayList<>();
    public static Boolean running = true;
    private static ArrayList<Thread> threads = new ArrayList<>();
    public static Player player;



    public Scene(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        groundY = (int) (screenHeight * 0.8);
    }

    public static void startMove(){
        Log.d("Scene","startMove");
        running = true;
        distance = 0;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(running){
                    long start = System.currentTimeMillis();
                    distance += speed/GameView.FPS;
//                    Log.d("Scene","Distance:" + distance);
                    for(RectHittableObject o: rectHittableObjects){
                        if(o.getX() - distance < screenWidth && !o.getMoveStarted()){
                            o.setX(o.getX() - distance);
                            o.startMoveLeftX(speed);
                        }
                    }
                    long end = System.currentTimeMillis();
                    while(end - start < GameView.minDrawTime){
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        end = System.currentTimeMillis();
                    }
                }

            }
        });
        threads.add(thread);
        thread.start();
    }

    public static void init_game_object(Context context){
        Random random = new Random();
        if(rectHittableObjects != null)
            rectHittableObjects.clear();
        staticObject.add(new Ground());
        System.out.println(rectHittableObjects.size());
        player = new Player(300,groundY - Player.playerHeight, Player.playerWidth, Player.playerHeight,context);
        rectHittableObjects.add(player);
        for(int i = 5; i < 100; i++){
            rectHittableObjects.add(new Box(i * 800 + random.nextInt(500),Scene.groundY - 100, 100,100,context));
        }
        for(int i = 2; i < 100; i++){
            rectHittableObjects.add(new Cloud(i * 800 + random.nextInt(400), 300, 150,100,context));
            rectHittableObjects.add(new Cloud(i * 1000 + random.nextInt(800), 100, 150,100,context));
            rectHittableObjects.add(new Bird(i * 900 + random.nextInt(800), 200, 100,100,context));
        }


    }

    public static void end(){
        running = false;
        for(RectHittableObject o: rectHittableObjects){
            Thread thread = o.getThread();
            if(thread != null) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        for(Thread t: threads){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        rectHittableObjects.clear();

    }

}
