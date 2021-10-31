package com.echo.photo_editor.ML;

import android.graphics.Bitmap;

/**
 * @author WangYuyang
 * @date 2021-09-22 13:54:24
 */
public interface StyleMLModel {
    void process(String path, ImageProcessListener listener);
    void process(Bitmap img_style, Bitmap img_to_be_process, ImageProcessListener listener);
}
