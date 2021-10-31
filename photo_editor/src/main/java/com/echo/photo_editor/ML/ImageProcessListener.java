package com.echo.photo_editor.ML;

import android.graphics.Bitmap;

/**
 * @author WangYuyang
 * @date 2021-09-22 13:54:24
 */
public interface ImageProcessListener {
    void start();
    void success(Bitmap FinishedBitmap);
    void failed(String message);
    void complete();
}
