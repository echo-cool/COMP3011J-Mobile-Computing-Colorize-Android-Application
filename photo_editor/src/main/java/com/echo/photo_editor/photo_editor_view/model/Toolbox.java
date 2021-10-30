package com.echo.photo_editor.photo_editor_view.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Toolbox implements ToolBarItem{
    public String name;
    public Bitmap image;
    public ArrayList<Tool> tools = new ArrayList<>();


    public Toolbox(String name, Bitmap image) {
        this.name = name;
        this.image = image;
    }

    public Toolbox(String name, Bitmap image, ArrayList<Tool> tools) {
        this.name = name;
        this.image = image;
        this.tools = tools;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Bitmap getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public ArrayList<Tool> getTools() {
        return tools;
    }

    public void setTools(ArrayList<Tool> tools) {
        this.tools = tools;
    }
}
