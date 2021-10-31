package com.echo.photo_editor.photo_editor_view.model;

import android.graphics.Bitmap;
import android.view.View;

/**
 * @Author: WangYuyang
 * @Date: 2021/10/30-23:56
 * @Project: My Application
 * @Package: com.echo.photo_editor.photo_editor_view.model
 * @Description:
 **/
public class SuperResTool extends Tool{
    public SuperResTool(String name, Bitmap image) {
        super(name, image);
    }

    public SuperResTool(String name, Bitmap image, View.OnClickListener listener) {
        super(name, image, listener);
    }
}
