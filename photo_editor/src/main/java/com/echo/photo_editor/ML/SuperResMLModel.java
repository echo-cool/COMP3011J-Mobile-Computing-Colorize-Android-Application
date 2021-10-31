package com.echo.photo_editor.ML;

import android.graphics.Bitmap;

/**
 * @Author: WangYuyang
 * @Date: 2021/10/30-23:58
 * @Project: My Application
 * @Package: com.echo.photo_editor.ML
 * @Description:
 **/
public interface SuperResMLModel {
    void process(Bitmap source, ProcessListener listener);
}
