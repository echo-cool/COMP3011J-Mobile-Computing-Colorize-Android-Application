package com.echo.colorizeit.ML;

import android.graphics.Bitmap;

public interface ProcessListener {
    void success(String data);
    void failure(String info);
    void success(Bitmap FinishedBitmap);
}
