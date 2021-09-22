package com.example.myapplication.ui.f_main_index_page_view;

import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Fade;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Util;
import com.example.myapplication.ui.BaseFragment;
import com.example.myapplication.ui.a_image_upload_activity.ImageUploadViewActivity;
import com.example.myapplication.ImageUtil.GlideEngine;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSlideshowBinding;
import com.example.myapplication.ui.v_others.ResizableImageView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;

import java.util.List;

public class MainIndexPageFragment extends BaseFragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private MainIndexPageViewModel model;
    private FragmentSlideshowBinding binding;
    private ImageView pointer;
    private ResizableImageView imageLayer1_Left, imageLayer2_Right;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private RotatingTextWrapper rotatingTextWrapper;
    private int screenWidth;
    private int screenHeight;
    private String sourceFilePath;
    private MainIndexPageFragment _this = this;
    private View root;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        model =
                new ViewModelProvider(this).get(MainIndexPageViewModel.class);
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);

        root = binding.getRoot();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        pointer = binding.pointer;
        imageLayer1_Left = binding.imageLayer1;
        imageLayer2_Right = binding.imageLayer2;

        Fade slideTracition = new Fade();
        slideTracition.setDuration(getResources().getInteger(R.integer.config_navAnimTime));
        this.setEnterTransition(slideTracition);
        this.setExitTransition(slideTracition);


        model.ImageLayer1_Right_Bitmap.observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                imageLayer1_Left.setImageBitmap(bitmap);
            }
        });

        model.ImageLayer2_Right_Bitmap.observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                imageLayer2_Right.setImageBitmap(bitmap);
            }
        });
        model.sliderX.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                if (integer > 0 && integer < screenWidth - 80) {
                    pointer.setX(integer - pointer.getWidth() / 2 + 53);
                }
                try {

                    model.process_touch(screenWidth, imageLayer2_Right.getMeasuredWidth(), imageLayer2_Right.getMeasuredHeight());
                } catch (Exception e) {
                    model.process_touch(screenWidth, screenWidth, screenHeight);
                }
            }
        });
        pointer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                model.sliderX.setValue((int) event.getRawX());

                return true;
            }
        });
        model.ImageLayer1_Right_Bitmap.setValue(BitmapFactory.decodeResource(getResources(), R.mipmap.index_page_black));
        model.ImageLayer2_Right_Bitmap.setValue(BitmapFactory.decodeResource(getResources(), R.mipmap.index_page_colorized));
        model.Right_Bitmap_Source = BitmapFactory.decodeResource(getResources(), R.mipmap.index_page_colorized);


        binding.imageUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NavHostFragment.findNavController(SlideshowFragment.this)
//                        .navigate(R.id.nav_home);
                if (Util.getRemaining() > 0) {

                    PictureSelector.create(_this)
                            .openGallery(PictureMimeType.ofAll())
                            .imageEngine(GlideEngine.createGlideEngine())
                            .selectionMode(PictureConfig.SINGLE)
                            .forResult(new OnResultCallbackListener<LocalMedia>() {
                                @Override
                                public void onResult(List<LocalMedia> result) {
                                    sourceFilePath = result.get(0).getRealPath();
                                    Intent intent = new Intent(getActivity(), ImageUploadViewActivity.class);
                                    intent.putExtra("sourceFilePath", sourceFilePath);
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancel() {
                                    // 取消
                                }
                            });
                } else {
                    Toast.makeText(getContext(), "No Enough Remaining, Please top up !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.liveCamear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.getRemaining() > 0) {
                    if(checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                100);
                    }
                    else {
//                        Intent intent = new Intent(getActivity(), CameraKitActivity.class);
//                        startActivity(intent);
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                    Intent intent = new Intent(getActivity(), ScanActivity.class);
//                    startActivityForResult(intent, getActivity().RESULT_OK);
                    }
                } else {
                    Toast.makeText(getContext(), "No Enough Remaining, Please top up !", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return root;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        root.post(new Runnable() {
            @Override
            public void run() {
//                slideshowViewModel.sliderX.setValue(screenWidth/2 - 50);
                ValueAnimator animator = ValueAnimator.ofInt((int) screenWidth / 2 - 50, (screenWidth / 4) * 3);
                animator.setDuration(1000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int curValue = (int) animation.getAnimatedValue();
                        model.sliderX.setValue((int) curValue);
                    }
                });
                animator.start();
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        ValueAnimator animator2 = ValueAnimator.ofInt((screenWidth / 4) * 3, (screenWidth / 2) - 50);
                        animator2.setDuration(1000);
                        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int curValue = (int) animation.getAnimatedValue();
                                model.sliderX.setValue((int) curValue);
                            }
                        });
                        animator2.start();
                    }
                });
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}