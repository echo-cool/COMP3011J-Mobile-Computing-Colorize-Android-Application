package com.example.myapplication.ui.slideshow;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.R;

public class SlideshowViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    public MutableLiveData<Bitmap> ImageLayer1_Right_Bitmap;
    public MutableLiveData<Bitmap> ImageLayer2_Right_Bitmap;
    public MutableLiveData<Integer> sliderX;
    public Bitmap Right_Bitmap_Source;

    public SlideshowViewModel() {
        mText = new MutableLiveData<>();
        ImageLayer1_Right_Bitmap = new MutableLiveData<>();
        ImageLayer2_Right_Bitmap = new MutableLiveData<>();
        sliderX = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");

//        ImageLayer1_Right_Bitmap.setValue(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.main_background_black));
//        ImageLayer2_Right_Bitmap.setValue(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.main_background_colorized));
    }

    public void process_touch(int screenWidth, int imageLayer2_Right_Width, int imageLayer2_Right_Height){
        int fX = ScaleFunction(sliderX.getValue()+50, 0, screenWidth, 0, Right_Bitmap_Source.getWidth());
        final Bitmap outputBitmap = Bitmap.createBitmap(imageLayer2_Right_Width, imageLayer2_Right_Height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(outputBitmap);
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(0);
        final Rect rect = new Rect(fX, 0, Right_Bitmap_Source.getWidth(), Right_Bitmap_Source.getHeight());
        final double imageWidth = Right_Bitmap_Source.getWidth();
        final double imageHeight = Right_Bitmap_Source.getHeight();
        final double imageAspect = imageWidth / imageHeight;
        final double finalImageHeight = imageLayer2_Right_Width / imageAspect;
        final double startY = (imageLayer2_Right_Height / 2.0) - (finalImageHeight / 2.0);
        final RectF rectF = new RectF(sliderX.getValue()+50, (int) startY, imageLayer2_Right_Width, (int) startY + (int) finalImageHeight);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        canvas.drawRect(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(Right_Bitmap_Source, rect, rectF, paint);
        ImageLayer2_Right_Bitmap.setValue(outputBitmap);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public int ScaleFunction(int xCoordinate, double A, double B, double C, double D) {
        double resultData = 0.0;
        double data1 = xCoordinate - A;
        double data2 = B - A;
        if (data2 != 0) {
            resultData = data1 / data2;
        }
        double start = Math.round((C * (1.0 - resultData)));
        double end = D * resultData;
        double finalResult = (start + end);
        return (int) finalResult;
    }


}