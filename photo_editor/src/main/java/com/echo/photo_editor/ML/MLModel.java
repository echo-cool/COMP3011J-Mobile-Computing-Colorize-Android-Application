package com.echo.photo_editor.ML;

import android.graphics.Bitmap;

public interface MLModel {
    void process(String path, ProcessListener listener);
    void process(Bitmap img_style, Bitmap img_to_be_process, ProcessListener listener);
}
