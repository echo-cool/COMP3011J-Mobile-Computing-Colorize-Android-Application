package com.echo.stinger_game.myganme;

import android.content.Context;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;

public class Scene {
    public static int speed = 420;
    public static int distance = 0; //from right of the screen
    public static int screenWidth;
    public static int screenHeight;
    public static int groundY;
    public static ArrayList<Drawable> staticObject = new ArrayList<>();
    public static ArrayList<RectHittableObject> rectHittableObjects = new ArrayList<>();
    public static Boolean running = true;
    public static Player player;
    private static final ArrayList<Thread> threads = new ArrayList<>();
    private static Context context;
    private static final Random random = new Random();


    public Scene(int screenWidth, int screenHeight) {
        Scene.screenWidth = screenWidth;
        Scene.screenHeight = screenHeight;
        groundY = (int) (screenHeight * 0.8);
        distance = 0;
    }

    public static void startMove() {
//        Log.d("Scene","startMove");
//        running = true;

        long start = System.currentTimeMillis();
        distance += speed / GameView.FPS;
        if (distance % 200 == 0) {
            genNewObj();
        }
//                    Log.d("Scene","Distance:" + distance);
        try {
            for (int i = 0; i < rectHittableObjects.size(); i++) {
                RectHittableObject o = rectHittableObjects.get(i);
                if (o.getX() - distance < screenWidth && !o.getMoveStarted()) {
                    o.setMoveStarted(true);
                    o.setX(o.getX() - distance);
                }
                if (o.getMoveStarted()) {
                    o.moveSingle();
                }
                //Cheat
                if (GameView.DEBUG) {
                    if (!o.equals(player) && o instanceof Box) {
                        if (Math.abs(o.getRight_bottom().x - player.getRight_bottom().x) < 200) {
                            System.out.println("JUMP");
                            Scene.player.startMoveUpY(6400);
                        }
                    }
                }

            }
        } catch (ConcurrentModificationException e) {
            System.out.println("ConcurrentModificationException");
        }

        long end = System.currentTimeMillis();
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(running){
//                    long start = System.currentTimeMillis();
//                    distance += speed/GameView.FPS;
////                    Log.d("Scene","Distance:" + distance);
//                    for(RectHittableObject o: rectHittableObjects){
//                        if(o.getX() - distance < screenWidth && !o.getMoveStarted()){
//                            o.setX(o.getX() - distance);
//                            o.startMoveLeftX(speed);
//                        }
//                    }
//                    long end = System.currentTimeMillis();
//                    while(end - start < GameView.minDrawTime){
//                        try {
//                            Thread.sleep(1);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        end = System.currentTimeMillis();
//                    }
//                }
//
//            }
//        });
//        threads.add(thread);
//        thread.start();
    }

    public static void genNewObj() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                rectHittableObjects.add(new Box(Scene.screenWidth + distance + 2000 + random.nextInt(500), Scene.groundY - 100, 100, 100, context));
                rectHittableObjects.add(new Cloud(Scene.screenWidth + distance + 500 + random.nextInt(2000), 300, 150, 100, context));
                rectHittableObjects.add(new Cloud(Scene.screenWidth + distance + 0 + random.nextInt(2000), 100, 150, 100, context));
                rectHittableObjects.add(new Cloud(Scene.screenWidth + distance + 1000 + random.nextInt(500), 100, 150, 100, context));
                rectHittableObjects.add(new Bird(Scene.screenWidth + distance + 1200 + random.nextInt(600), 200, 100, 100, context));
                rectHittableObjects.add(new BirdMoving(Scene.screenWidth + distance + 600 + random.nextInt(300), 400, 100, 100, context));
            }
        }).start();

    }

    public static void init_game_object(Context context) {
        distance = 0;
        Random random = new Random();
        Scene.context = context;
        if (rectHittableObjects != null)
            rectHittableObjects.clear();
        staticObject.add(new Ground());
        System.out.println(rectHittableObjects.size());
        player = new Player(300, groundY - Player.playerHeight, Player.playerWidth, Player.playerHeight, context);
        rectHittableObjects.add(player);
        rectHittableObjects.add(new Box(distance + 3000 + random.nextInt(500), Scene.groundY - 100, 100, 100, context));
        rectHittableObjects.add(new Cloud(distance + 800 + random.nextInt(400), 300, 150, 100, context));
        rectHittableObjects.add(new Cloud(distance + 1000 + random.nextInt(800), 100, 150, 100, context));
        rectHittableObjects.add(new Bird(distance + 900 + random.nextInt(800), 200, 100, 100, context));
//        for (int i = 3; i < 10; i++) {
//            rectHittableObjects.add(new Box(i * 1000 + random.nextInt(500), Scene.groundY - 100, 100, 100, context));
//        }
//        for (int i = 2; i < 10; i++) {
//            rectHittableObjects.add(new Cloud(i * 800 + random.nextInt(400), 300, 150, 100, context));
//            rectHittableObjects.add(new Cloud(i * 1000 + random.nextInt(800), 100, 150, 100, context));
//            rectHittableObjects.add(new Bird(i * 900 + random.nextInt(800), 200, 100, 100, context));
//        }


    }

    public static void end() {
        running = false;
//        for(RectHittableObject o: rectHittableObjects){
//            Thread thread = o.getThread();
//            if(thread != null) {
//                try {
//                    thread.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        for(Thread t: threads){
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        rectHittableObjects.clear();

    }

}
