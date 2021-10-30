package com.echo.colorizeit.ui.f_ImageProcessTEST_view;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author Wang Yuyang
 * @date 2021-09-22 13:52:43
 */
public class ImageProcessTESTViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Bitmap> sourceImageBitmap;
    private MutableLiveData<Bitmap> colorizedImageBitmap;
    private MutableLiveData<Boolean> showSaveButtonVisbility;

    public ImageProcessTESTViewModel() {
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