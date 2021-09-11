package com.example.myapplication.ui.image_upload;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.BaiduAPI.BaiduImageAPI;
import com.example.myapplication.ImageUtil.PhotoLib;
import com.example.myapplication.Interfaces.RequestsListener;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.databinding.ImageColorizeUploadActivityBinding;
import com.example.myapplication.ui.BaseActivity;
import com.example.myapplication.ui.ImageColorize.ImageColorizeViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import ch.halcyon.squareprogressbar.SquareProgressBar;
import ch.halcyon.squareprogressbar.utils.PercentStyle;

public class ImageUploadActivityView extends BaseActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ImageColorizeUploadActivityBinding binding;
    private ImageUploadActivityViewModel imageUploadActivityViewModel;
    private SquareProgressBar imageDisplay;
    private Uri ColorizedImageUri;
    private ImageUploadActivityView _this = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        makeFullScreen();
        imageUploadActivityViewModel = new ViewModelProvider(this).get(ImageUploadActivityViewModel.class);
        binding = ImageColorizeUploadActivityBinding.inflate(getLayoutInflater());
        imageDisplay = binding.imageDisplay;
        Intent intent = getIntent();
        imageUploadActivityViewModel.getImageViewData().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                imageDisplay.setImageBitmap(bitmap);
            }
        });
        imageUploadActivityViewModel.progress.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                imageDisplay.setProgress(integer);
            }
        });
        imageUploadActivityViewModel.setImageViewDataByPath(intent.getStringExtra("sourceFilePath"));

        colorize_image();
        binding.CompareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUploadActivityViewModel.getImageViewData().getValue() == imageUploadActivityViewModel.getSourceImageBitmap()){
                    imageUploadActivityViewModel.setImageViewDataByBitmap(imageUploadActivityViewModel.getColorizedImageBitmap());
                }
                else{
                    imageUploadActivityViewModel.setImageViewDataByBitmap(imageUploadActivityViewModel.getSourceImageBitmap());
                }
            }});

        binding.SaveImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
                showSnackbar("Image Saved");

            }

            private void saveImage() {
                String data = PhotoLib.saveImageToGallery(getApplicationContext(), imageUploadActivityViewModel.getColorizedImageBitmap());
//                String data = MediaStore.Images.Media.insertImage(getContentResolver(), imageUploadActivityViewModel.getColorizedImageBitmap(), "Colorized Image", ":)");
                ColorizedImageUri = Uri.parse(data);

            }
        });

        binding.ShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = MediaStore.Images.Media.insertImage(getContentResolver(), imageUploadActivityViewModel.getColorizedImageBitmap(), "Colorized Image", ":)");
                ColorizedImageUri = Uri.parse(data);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, ColorizedImageUri);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");//添加分享内容标题
                intent = Intent.createChooser(intent, "Share");
                startActivity(intent);


            }
        });
        binding.imageDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(_this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                SquareProgressBar squareProgressBar = (SquareProgressBar) v;

                ImageView imgView =  new ImageView(_this);
                imgView.setImageBitmap((Bitmap) imageUploadActivityViewModel.getImageViewData().getValue());
                dialog.setContentView(imgView);
                dialog.show();
                imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _this.finish();
            }
        });


        setContentView(binding.getRoot());
    }

    private void colorize_image() {
        imageDisplay.showProgress(true);
        PercentStyle percentStyle = new PercentStyle(Paint.Align.CENTER, 190, true);
        percentStyle.setTextColor(Color.GRAY);
        imageDisplay.setPercentStyle(percentStyle);

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 90);
        valueAnimator.setDuration(1500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                imageUploadActivityViewModel.progress.setValue((int)animation.getAnimatedValue());

            }
        });
        valueAnimator.start();
        imageUploadActivityViewModel.processImage(new RequestsListener() {
            @Override
            public void success(String data) {
                valueAnimator.removeAllUpdateListeners();
                imageUploadActivityViewModel.progress.postValue(100);
                imageDisplay.showProgress(false);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.ShareButton.setEnabled(true);
                        binding.SaveImageButton.setEnabled(true);
                        binding.CompareButton.setEnabled(true);
                    }
                });

            }

            @Override
            public void failure(String info) {

            }
        });
    }



    public void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}
