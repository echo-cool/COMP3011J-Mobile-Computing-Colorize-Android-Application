package com.echo.photo_editor.photo_editor_view.model;

import android.graphics.Bitmap;
import android.view.View;

public class Tool implements ToolBarItem {
    public String name;
    public Bitmap image;
    public View.OnClickListener listener;


    public Tool(String name, Bitmap image) {
        this.name = name;
        this.image = image;
    }
    public Tool(String name, Bitmap image, View.OnClickListener listener) {
        this.name = name;
        this.image = image;
        this.listener = listener;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Bitmap getImage() {
        return image;
    }
}
