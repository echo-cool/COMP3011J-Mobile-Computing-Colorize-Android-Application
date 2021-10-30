package com.echo.photo_editor.photo_editor_view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.echo.photo_editor.photo_editor_view.model.CustomMutableLiveData;
import com.echo.photo_editor.photo_editor_view.model.EditableImage;

import java.util.ArrayList;

public class PhotoEditorViewModel extends ViewModel {
    private MutableLiveData<String> sourceFilePath = new MutableLiveData<>("");
    private CustomMutableLiveData<EditableImage> editableImage = new CustomMutableLiveData<>();

    public MutableLiveData<String> getSourceFilePath() {
        return sourceFilePath;
    }

    public void set_sourceFilePath(String sourceFilePath) {
        this.sourceFilePath.postValue(sourceFilePath);
        this.editableImage.postValue(new EditableImage(BitmapFactory.decodeFile(sourceFilePath)));
    }

    public CustomMutableLiveData<EditableImage> getEditableImage() {
        return editableImage;
    }

}
