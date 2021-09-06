package com.example.myapplication.ui.ImageColorize;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.R;

public class ImageColorizeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Bitmap> sourceImageBitmap;
    private MutableLiveData<Bitmap> colorizedImageBitmap;
    private MutableLiveData<Boolean> showSaveButtonVisbility;

    public ImageColorizeViewModel() {
        mText = new MutableLiveData<>();
        sourceImageBitmap = new MutableLiveData<>();
        colorizedImageBitmap = new MutableLiveData<>();
        showSaveButtonVisbility = new MutableLiveData<>();

        mText.setValue("");
        showSaveButtonVisbility.setValue(false);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setmText(String data) {
        this.mText.setValue(data);
    }


    public LiveData<Bitmap> getSourceImageBitmap() {
        return sourceImageBitmap;
    }
    public LiveData<Bitmap> getColorizedImageBitmap() {
        return colorizedImageBitmap;
    }


    public void setColorizedImageBitmap(Bitmap sourceImageBitmap) {
        this.colorizedImageBitmap.postValue(sourceImageBitmap);
    }

    public void setSourceImageBitmap(Bitmap sourceImageBitmap) {
        this.sourceImageBitmap.postValue(sourceImageBitmap);
    }

    public LiveData<Boolean> getSaveButtonVisbility(){
        return showSaveButtonVisbility;
    }
    public void enableSaveButton(){
        showSaveButtonVisbility.postValue(true);
    }
    public void disableSaveButton(){
        showSaveButtonVisbility.postValue(false);
    }
}