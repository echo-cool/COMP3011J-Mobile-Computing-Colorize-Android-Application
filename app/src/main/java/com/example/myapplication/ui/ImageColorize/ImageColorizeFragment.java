package com.example.myapplication.ui.ImageColorize;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.BaiduAPI.BaiduImageAPI;
import com.example.myapplication.ImageUtil.GlideEngine;
import com.example.myapplication.ImageUtil.PhotoLib;
import com.example.myapplication.Interfaces.RequestsListener;

import com.example.myapplication.ML.ProcessListener;
import com.example.myapplication.ML.StyleTransModel;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentImageColorizeBinding;
import com.example.myapplication.ui.BaseFragment;
import com.himanshurawat.imageworker.Extension;
import com.himanshurawat.imageworker.ImageWorker;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;


import java.util.List;

import cn.hutool.Hutool;
import cn.hutool.core.util.SerializeUtil;

public class ImageColorizeFragment extends BaseFragment {

    private ImageColorizeViewModel imageColorizeViewModel;
    private FragmentImageColorizeBinding binding;
    private ImageColorizeFragment _this = this;
    private String sourceFilePath;

    private StyleTransModel styleTransModel;

//    private ActivityResultLauncher requestPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
//        @Override
//        public void onActivityResult(Boolean result) {
//        }
//    });
//    private ActivityResultLauncher chooseImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
//        @Override
//        public void onActivityResult(Uri result) {
//            sourceFilePath = result;
//            homeViewModel.setSourceImageBitmap(BitmapFactory.decodeFile(sourceFilePath.getPath()));
//        }
//    });

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        styleTransModel = new StyleTransModel(getContext());
//        superResolutionModel = new SuperResolutionModel(getContext());
        imageColorizeViewModel =
                new ViewModelProvider(this).get(ImageColorizeViewModel.class);

        binding = FragmentImageColorizeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Fade slideTracition = new Fade();
        slideTracition.setDuration(getResources().getInteger(R.integer.config_navAnimTime));
        this.setEnterTransition(slideTracition);
        this.setExitTransition(slideTracition);

        final TextView textView = binding.textHome;
        imageColorizeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        final Button ImageImportButton = binding.ImageImportButton;
        ImageImportButton.setOnClickListener(new View.OnClickListener() {
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
                                imageColorizeViewModel.setSourceImageBitmap(BitmapFactory.decodeFile(sourceFilePath));
                            }
                            @Override
                            public void onCancel() {
                                // 取消
                            }
                        });
            }
        });

        final ImageView sourceImageView = binding.SourceImage;
        imageColorizeViewModel.getSourceImageBitmap().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                sourceImageView.setImageBitmap(bitmap);
            }
        });

        final ImageView colorizedImageView = binding.ColorizedImage;
        imageColorizeViewModel.getColorizedImageBitmap().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                colorizedImageView.setImageBitmap(bitmap);
            }
        });

        final Button colorizeImageButton = binding.ImageColorizeButton;
        colorizeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sourceFilePath != null){
                    colorizeImage();
                }
            }
            private void colorizeImage() {
                showLoading("processing....");
//                styleTransModel.process(sourceFilePath, new ProcessListener() {
//                    @Override
//                    public void success(String data) {
//                    }
//
//                    @Override
//                    public void failure(String info) {
//                    }
//                    @Override
//                    public void success(Bitmap styledImageBitmap) {
//                        imageColorizeViewModel.setColorizedImageBitmap(styledImageBitmap);
//                    }
//                });
                styleTransModel.process(sourceFilePath, new ProcessListener() {
                    @Override
                    public void success(String data) {
                        hideLoading();
                    }
                    @Override
                    public void failure(String info) {
                        hideLoading();
                    }
                    @Override
                    public void success(Bitmap styledImageBitmap) {
                        imageColorizeViewModel.setColorizedImageBitmap(styledImageBitmap);
                        hideLoading();
                    }
                });

            }
        });
        final Button saveColorizedImageButton = binding.saveColorizedImageButton;
        saveColorizedImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
                showSnackbar("Image Saved !");
            }

            private void saveImage() {
                MediaStore.Images.Media.insertImage(getContext().getContentResolver(), imageColorizeViewModel.getColorizedImageBitmap().getValue(), "title", "description");
            }

        });
        imageColorizeViewModel.getSaveButtonVisbility().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                saveColorizedImageButton.setEnabled(bool);
            }
        });
        imageColorizeViewModel.setSourceImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.black_demo_image));
        imageColorizeViewModel.setColorizedImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.colorized_demo_image));


        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}