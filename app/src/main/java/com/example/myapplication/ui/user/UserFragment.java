package com.example.myapplication.ui.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.ImageUtil.GlideEngine;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSlideshowBinding;
import com.example.myapplication.databinding.FragmentUserBinding;
import com.example.myapplication.liveedgedetection.activity.ScanActivity;
import com.example.myapplication.ui.image_upload.ImageUploadActivityView;
import com.example.myapplication.ui.slideshow.SlideshowViewModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.util.List;

public class UserFragment extends Fragment {
    private UserFragmentViewModel userFragmentViewModel;
    private FragmentUserBinding binding;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userFragmentViewModel = new ViewModelProvider(this).get(UserFragmentViewModel.class);
        binding = FragmentUserBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        Fade slideTracition = new Fade();
        slideTracition.setDuration(getResources().getInteger(R.integer.config_navAnimTime));
        this.setEnterTransition(slideTracition);
        this.setExitTransition(slideTracition);


        return root;
    }
}
