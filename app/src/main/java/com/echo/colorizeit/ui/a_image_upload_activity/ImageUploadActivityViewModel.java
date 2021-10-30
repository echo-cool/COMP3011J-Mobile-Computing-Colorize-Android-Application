package com.echo.colorizeit.ui.a_image_upload_activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.echo.colorizeit.BaiduAPI.BaiduImageAPI;
import com.echo.colorizeit.ImageUtil.PhotoLib;
import com.echo.colorizeit.Interfaces.RequestsListener;

/**
 * @author Wang Yuyang
 * @date 2021-09-22 13:52:43
 */
public class ImageUploadActivityViewModel extends ViewModel {
    private BaiduImageAPI baiduImageAPI;
    private String imagePath;
    private Bitmap sourceImageBitmap;
    private Bitmap colorizedImageBitmap;
    private MutableLiveData<Bitmap> imageViewData;
    public MutableLiveData<Integer> progress;

    public ImageUploadActivityViewModel() {
        this.imageViewData = new MutableLiveData<>();
        this.progress = new MutableLiveData<>();
    }



    public void setImageViewDataByBitmap(Bitmap data){
        this.imageViewData.postValue(data);
    }
    public void setImageViewDataByPath(String Path) {
        imagePath = Path;
        sourceImageBitmap = BitmapFactory.decodeFile(Path);
        this.imageViewData.postValue(sourceImageBitmap);
    }
    public MutableLiveData getImageViewData(){
        return this.imageViewData;
    }

    public void processImage(RequestsListener listener) {
        baiduImageAPI = new BaiduImageAPI();
        baiduImageAPI.colourize(new RequestsListener() {
            @Override
            public void success(String data) {
//                imageColorizeViewModel.setColorizedImageBitmap(PhotoLib.Base64ToBitmap(data));
//                hideLoading();
//                imageColorizeViewModel.enableSaveButton();
                colorizedImageBitmap = PhotoLib.Base64ToBitmap(data);
                imageViewData.postValue(colorizedImageBitmap);
                listener.success("Success");
            }
            @Override
            public void failure(String info) {


            }
        }, imagePath);

    }

    public Bitmap getSourceImageBitmap() {
        return sourceImageBitmap;
    }

    public Bitmap getColorizedImageBitmap() {
        return colorizedImageBitmap;
    }
}
