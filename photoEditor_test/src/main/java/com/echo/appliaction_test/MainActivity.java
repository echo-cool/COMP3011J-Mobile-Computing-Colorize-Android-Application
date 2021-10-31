package com.echo.appliaction_test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.echo.appliaction_test.databinding.ActivityMainBinding;
import com.echo.photo_editor.photo_editor_view.PhotoEditorView;
import com.echo.photo_editor.thirdparty.GlideEngine;
import com.echo.stinger_game.myganme.GameActivity;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import org.tensorflow.lite.support.image.TensorImage;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainActivity _this = this;
    private String sourceFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //-----------------------------TEST Image-------------------------------
//        sourceFilePath = "/storage/emulated/0/Pictures/Colorized Image (26).jpg";
//        Intent intent = new Intent(_this, PhotoEditorView.class);
//        intent.putExtra("sourceFilePath", sourceFilePath);
//        startActivity(intent);
        setContentView(binding.getRoot());
        TextView textView = binding.textView2;
        TextView textView1 = binding.textView3;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");// HH:mm:ss
                                Date date = new Date(System.currentTimeMillis());
                                textView1.setText(simpleDateFormat.format(date));
                                textView.setText(String.valueOf(System.currentTimeMillis()));
                            } catch (Exception e) {

                            }
                        }
                    });
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        }).start();
        binding.ChooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PictureSelector.create(_this)
                        .openGallery(PictureMimeType.ofAll())
                        .imageEngine(GlideEngine.createGlideEngine())
                        .selectionMode(PictureConfig.SINGLE)
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> result) {

                                sourceFilePath = result.get(0).getRealPath();
//                                Bitmap img = BitmapFactory.decodeFile(sourceFilePath);
                                Intent intent = new Intent(_this, PhotoEditorView.class);
                                intent.putExtra("sourceFilePath", sourceFilePath);
                                startActivity(intent);

                            }

                            @Override
                            public void onCancel() {
                                // 取消
                            }
                        });
            }
        });
        binding.ImageInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(_this)
                        .openGallery(PictureMimeType.ofAll())
                        .imageEngine(GlideEngine.createGlideEngine())
                        .selectionMode(PictureConfig.SINGLE)
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> result) {

                                sourceFilePath = result.get(0).getRealPath();
//                                Bitmap img = BitmapFactory.decodeFile(sourceFilePath);
                                Intent intent = new Intent(_this, Image_information_activity.class);
                                intent.putExtra("sourceFilePath", sourceFilePath);
                                startActivity(intent);

                            }

                            @Override
                            public void onCancel() {
                                // 取消
                            }
                        });
            }
        });


        binding.gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_this, GameActivity.class);
                startActivity(intent);
            }
        });


    }

    public static Boolean check_is_grayscale(Bitmap img) {
        int pixel_count = 0;
        TensorImage image = TensorImage.fromBitmap(img);
        int width = image.getWidth();
        int height = image.getHeight();
        int[] imageIntArray = image.getTensorBuffer().getIntArray();
        Random random = new Random(imageIntArray.length);
        for (int i = 0; i < 100; i++) {
            int pixel_index = random.nextInt(width * height);
            int R = imageIntArray[3 * pixel_index];
            int G = imageIntArray[3 * pixel_index + 1];
            int B = imageIntArray[3 * pixel_index + 2];
            System.out.println(Arrays.toString(new int[]{R, G, B}));
            if (R == G && R == B && B == G) {
                pixel_count++;
            }
        }
        System.out.println(pixel_count);
        if (pixel_count > 50) {
            return true;
        } else {
            return false;
        }
    }

}