package com.echo.stinger_game.myganme;

/**
 * @author Wang Yuyang
 * @date 2021-10-11 09:47:46
 */
public interface Movable {
    void setLocation(int x, int y);

    void moveX(int distance);

    void moveY(int distance);

    void startMoveLeftX(int speed);

    void startMoveUpY(int speed);

    void startMoveXY(int speedX, int speedY);

    void moveSingle();
}
