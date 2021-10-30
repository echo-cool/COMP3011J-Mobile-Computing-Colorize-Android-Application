package com.echo.stinger_game.myganme;

import android.graphics.Point;
import android.graphics.Rect;

public interface Hittable {
    Rect getBorderRect();

    Boolean checkHit(Rect rect);

    Boolean checkHit(int left, int top, int right, int bottom);

    Boolean checkHit(Point left_up, Point left_bottom, Point right_up, Point right_bottom);
}
