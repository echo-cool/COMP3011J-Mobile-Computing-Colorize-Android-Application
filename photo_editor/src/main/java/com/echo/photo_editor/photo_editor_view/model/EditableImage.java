package com.echo.photo_editor.photo_editor_view.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class EditableImage extends BaseObservable {
    private Bitmap current_bitmap;
    private Bitmap source_bitmap;
    private ArrayList<Bitmap> edited_bitmaps = new ArrayList<>();
    private Integer current_index;

    public EditableImage(Bitmap source_bitmap) {
        this.source_bitmap = source_bitmap;
        this.current_bitmap = Bitmap.createBitmap(source_bitmap);
        this.edited_bitmaps.add(current_bitmap);
        this.current_index = 0;
        notifyChange();

    }

    public void update_current_bitmap(Bitmap new_bitmap) {
        this.current_bitmap = Bitmap.createBitmap(new_bitmap);
        this.current_index += 1;
        if (current_index + 1 > edited_bitmaps.size())
            this.edited_bitmaps.add(current_bitmap);
        else
            this.edited_bitmaps.set(current_index, current_bitmap);

//        System.out.println("update_current_bitmap : " + current_index);
        notifyChange();
    }

    public Boolean undo() {
        if (this.current_index - 1 >= 0) {
            this.current_index -= 1;
            this.current_bitmap = edited_bitmaps.get(current_index);
//            System.out.println("undo: " + current_index);
            notifyChange();
            return true;
        } else {
            return false;
        }
    }

    public Boolean redo() {
        if (this.current_index + 1 >= edited_bitmaps.size()) {
            return false;
        } else {
            this.current_index += 1;
            this.current_bitmap = edited_bitmaps.get(current_index);
            notifyChange();
            return true;
        }
    }

    @Bindable
    public Bitmap getCurrent_bitmap() {
        return current_bitmap;
    }

    public void setCurrent_bitmap(Bitmap current_bitmap) {
        this.current_bitmap = current_bitmap;
        notifyPropertyChanged(BR.current_bitmap);
    }

    @Bindable
    public Bitmap getSource_bitmap() {
        return source_bitmap;
    }

    public void setSource_bitmap(Bitmap source_bitmap) {
        this.source_bitmap = source_bitmap;
        notifyPropertyChanged(BR.source_bitmap);
    }

    @Bindable
    public ArrayList<Bitmap> getEdited_bitmaps() {
        return edited_bitmaps;
    }

    public void setEdited_bitmaps(ArrayList<Bitmap> edited_bitmaps) {
        this.edited_bitmaps = edited_bitmaps;
        notifyPropertyChanged(BR.edited_bitmaps);
    }

    @Bindable
    public Integer getCurrent_index() {
        return current_index;
    }

    public void setCurrent_index(Integer current_index) {
        this.current_index = current_index;
        notifyPropertyChanged(BR.current_index);
    }
}
