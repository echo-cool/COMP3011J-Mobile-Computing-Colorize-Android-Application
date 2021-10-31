package com.echo.colorizeit.ui.a_image_upload_activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.echo.colorizeit.ImageUtil.PhotoLib;
import com.echo.colorizeit.Interfaces.RequestsListener;
import com.echo.colorizeit.ML.CategoryProcessListener;
import com.echo.colorizeit.ML.LabelerModel;
import com.echo.colorizeit.Util;
import com.echo.colorizeit.ui.BaseActivity;
import com.echo.photo_editor.photo_editor_view.PhotoEditorView;
import com.echo.stinger_game.myganme.GameActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ImageColorizeUploadActivityBinding;

import org.tensorflow.lite.support.label.Category;

import java.util.Comparator;
import java.util.List;

import ch.halcyon.squareprogressbar.utils.PercentStyle;

/**
 * @author Wang Yuyang
 * @date 2021-09-22 13:52:43
 */
public class ImageUploadViewActivity extends BaseActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ImageColorizeUploadActivityBinding binding;
    private ImageUploadActivityViewModel model;
    private ImageView imageDisplay;
    private Uri ColorizedImageUri;
    private ImageUploadViewActivity _this = this;
    private LabelerModel labelerModel = new LabelerModel(this);
    private LabelAdapter labelAdapter = new LabelAdapter();


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
        binding.lableView.setAdapter(labelAdapter);
        model.setImageViewDataByPath(intent.getStringExtra("sourceFilePath"));
        binding.imageProcessingAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_this, GameActivity.class);
                startActivity(intent);
            }
        });
        colorize_image();
        binding.CompareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getImageViewData().getValue() == model.getSourceImageBitmap()) {
                    model.setImageViewDataByBitmap(model.getColorizedImageBitmap());
                    binding.uploadFinishAnimationView.playAnimation();
                } else {
                    model.setImageViewDataByBitmap(model.getSourceImageBitmap());
                }
            }
        });

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
                String data = MediaStore.Images.Media.insertImage(getContentResolver(), model.getColorizedImageBitmap(), String.valueOf(System.currentTimeMillis()), ":)");
                while (data == null) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
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


                ImageView imgView = new ImageView(_this);
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

        binding.ContinueEditingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ContinueEditingButton");
                String data = PhotoLib.saveImageToGallery(getApplicationContext(), model.getColorizedImageBitmap());
                Intent intent = new Intent(_this, PhotoEditorView.class);
                intent.putExtra("sourceFilePath", data);
                startActivity(intent);

            }
        });


        setContentView(binding.getRoot());
    }

    private void colorize_image() {
        PercentStyle percentStyle = new PercentStyle(Paint.Align.CENTER, 190, true);
        percentStyle.setTextColor(Color.GRAY);

        labelerModel.process(model.getSourceImageBitmap(), new CategoryProcessListener() {
            @Override
            public void start() {

            }

            @Override
            public void success(List<Category> result) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    result.sort(new Comparator<Category>() {
                        @Override
                        public int compare(Category o1, Category o2) {
                            return (int) (o2.getScore() * 100 - o1.getScore() * 100);
                        }
                    });
                }
                for (Category c : result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(c.getScore() >= 0.01)
                                labelAdapter.addLabels(c.getLabel() + ":" + c.getScore());
                        }
                    });

                }
            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void complete() {

            }
        });

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
                        binding.ContinueEditingButton.setEnabled(true);
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
