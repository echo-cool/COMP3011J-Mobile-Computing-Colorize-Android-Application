package com.echo.photo_editor.ML;

import android.graphics.Bitmap;

public interface ProcessListener {
    void start();
    void success(Bitmap FinishedBitmap);
    void failed(String message);
    void complete();
}
