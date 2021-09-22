package com.example.myapplication.ui.image_upload;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.myapplication.ImageUtil.PhotoLib;
import com.example.myapplication.Interfaces.RequestsListener;
import com.example.myapplication.R;
import com.example.myapplication.Util;
import com.example.myapplication.databinding.ImageColorizeUploadActivityBinding;
import com.example.myapplication.ui.BaseActivity;

import ch.halcyon.squareprogressbar.SquareProgressBar;
import ch.halcyon.squareprogressbar.utils.PercentStyle;

public class ImageUploadActivityView extends BaseActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ImageColorizeUploadActivityBinding binding;
    private ImageUploadActivityViewModel model;
    private ImageView imageDisplay;
    private Uri ColorizedImageUri;
    private ImageUploadActivityView _this = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        makeFullScreen();
        model = new ViewModelProvider(this).get(ImageUploadActivityViewModel.class);
        binding = ImageColorizeUploadActivityBinding.inflate(getLayoutInflater());
        imageDisplay = binding.imageDisplay;
        Intent intent = getIntent();
        model.getImageViewData().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                imageDisplay.setImageBitmap(bitmap);
            }
        });
//        imageUploadActivityViewModel.progress.observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                imageDisplay.setProgress(integer);
//            }
//        });
        model.setImageViewDataByPath(intent.getStringExtra("sourceFilePath"));

        colorize_image();
        binding.CompareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(model.getImageViewData().getValue() == model.getSourceImageBitmap()){
                    model.setImageViewDataByBitmap(model.getColorizedImageBitmap());
                    binding.uploadFinishAnimationView.playAnimation();
                }
                else{
                    model.setImageViewDataByBitmap(model.getSourceImageBitmap());
                }
            }});

        binding.SaveImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation_fade_out = AnimationUtils.loadAnimation(_this, R.anim.fade_out);
                Animation animation_fade_in = AnimationUtils.loadAnimation(_this, R.anim.fade_in);
                binding.SaveImageButton.startAnimation(animation_fade_out);
                animation_fade_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        binding.SaveImageButton.setVisibility(View.VISIBLE);
                        binding.imageSaveFinished.cancelAnimation();
                        saveImage();
                        showSnackbar("Image Saved");

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                animation_fade_out.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        binding.SaveImageButton.setVisibility(View.INVISIBLE);
                        binding.imageSaveFinished.setVisibility(View.VISIBLE);
                        binding.imageSaveFinished.playAnimation();

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                binding.imageSaveFinished.addAnimatorListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        binding.SaveImageButton.startAnimation(animation_fade_in);
                    }
                });


            }

            private void saveImage() {
                String data = PhotoLib.saveImageToGallery(getApplicationContext(), model.getColorizedImageBitmap());
//                String data = MediaStore.Images.Media.insertImage(getContentResolver(), imageUploadActivityViewModel.getColorizedImageBitmap(), "Colorized Image", ":)");
                ColorizedImageUri = Uri.parse(data);

            }
        });

        binding.ShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = MediaStore.Images.Media.insertImage(getContentResolver(), model.getColorizedImageBitmap(), "Colorized Image", ":)");
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


                ImageView imgView =  new ImageView(_this);
                imgView.setImageBitmap((Bitmap) model.getImageViewData().getValue());
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
        PercentStyle percentStyle = new PercentStyle(Paint.Align.CENTER, 190, true);
        percentStyle.setTextColor(Color.GRAY);


        model.processImage(new RequestsListener() {
            @Override
            public void success(String data) {
                model.progress.postValue(100);
                Util.UpdateCountOnProcess();
//                imageDisplay.showProgress(false);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.ShareButton.setEnabled(true);
                        binding.SaveImageButton.setEnabled(true);
                        binding.CompareButton.setEnabled(true);
                        binding.uploadFinishAnimationView.playAnimation();
                        Animation animation_fade_out = AnimationUtils.loadAnimation(_this, R.anim.fade_out);
                        Animation animation_fade_out1 = AnimationUtils.loadAnimation(_this, R.anim.fade_out);
                        Animation animation_fade_in = AnimationUtils.loadAnimation(_this, R.anim.fade_in);
                        Animation animation_fade_in1 = AnimationUtils.loadAnimation(_this, R.anim.fade_in);
                        animation_fade_in.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                binding.imageDisplay.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        animation_fade_in1.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                binding.textView3.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        animation_fade_out.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                binding.imageProcessingAnimation.setVisibility(View.INVISIBLE);
                                binding.imageDisplay.startAnimation(animation_fade_in);


                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        animation_fade_out1.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                binding.meachineViewAnim.setVisibility(View.INVISIBLE);
                                binding.textView3.startAnimation(animation_fade_in1);


                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        binding.imageProcessingAnimation.startAnimation(animation_fade_out);
                        binding.meachineViewAnim.startAnimation(animation_fade_out1);
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
