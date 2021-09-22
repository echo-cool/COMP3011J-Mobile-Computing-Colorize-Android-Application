package com.echo.myapplication.ML;

import android.graphics.Bitmap;

public interface ProcessListener {
    void success(String data);
    void failure(String info);
    void success(Bitmap FinishedBitmap);
}
