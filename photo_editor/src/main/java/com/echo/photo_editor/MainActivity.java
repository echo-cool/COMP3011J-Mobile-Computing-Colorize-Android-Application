package com.echo.photo_editor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.echo.photo_editor.databinding.ActivityMainBinding;
import com.echo.photo_editor.photo_editor_view.PhotoEditorView;
import com.echo.photo_editor.thirdparty.GlideEngine;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.util.List;

/**
 * @author WangYuyang
 * @date 2021-10-30 15:56:43
 */
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
        binding.chooseImageButton.setOnClickListener(new View.OnClickListener() {
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

        setContentView(binding.getRoot());


    }
}