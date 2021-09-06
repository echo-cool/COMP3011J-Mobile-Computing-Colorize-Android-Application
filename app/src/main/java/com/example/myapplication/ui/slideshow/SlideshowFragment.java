package com.example.myapplication.ui.slideshow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.liveedgedetection.ScanConstants;
import com.example.myapplication.liveedgedetection.activity.ScanActivity;
import com.example.myapplication.ui.image_upload.ImageUploadActivityView;
import com.example.myapplication.ImageUtil.GlideEngine;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSlideshowBinding;
import com.example.myapplication.ui.others.ResizableImageView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;

import java.util.List;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    private ImageView pointer;
    private ResizableImageView imageLayer1_Left, imageLayer2_Right;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private RotatingTextWrapper rotatingTextWrapper;
    private int screenWidth;
    private int screenHeight;
    private String sourceFilePath;
    private SlideshowFragment _this = this;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth= displayMetrics.widthPixels;
        screenHeight=displayMetrics.heightPixels;
        pointer = binding.pointer;
        imageLayer1_Left = binding.imageLayer1;
        imageLayer2_Right = binding.imageLayer2;



        slideshowViewModel.ImageLayer1_Right_Bitmap.observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                imageLayer1_Left.setImageBitmap(bitmap);
            }
        });

        slideshowViewModel.ImageLayer2_Right_Bitmap.observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                imageLayer2_Right.setImageBitmap(bitmap);
            }
        });
        slideshowViewModel.sliderX.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                if (integer > 0 && integer < screenWidth - 80) {
                    pointer.setX(integer - pointer.getWidth()/2 + 53);
                }
                try {

                    slideshowViewModel.process_touch(screenWidth, imageLayer2_Right.getMeasuredWidth(), imageLayer2_Right.getMeasuredHeight());
                }catch (Exception e){
                    slideshowViewModel.process_touch(screenWidth, screenWidth, screenHeight);
                }
            }
        });
        pointer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                slideshowViewModel.sliderX.setValue((int) event.getRawX());

                return true;
            }
        });
        slideshowViewModel.ImageLayer1_Right_Bitmap.setValue(BitmapFactory.decodeResource(getResources(), R.mipmap.index_page_black));
        slideshowViewModel.ImageLayer2_Right_Bitmap.setValue(BitmapFactory.decodeResource(getResources(), R.mipmap.index_page_colorized));
        slideshowViewModel.Right_Bitmap_Source = BitmapFactory.decodeResource(getResources(), R.mipmap.index_page_colorized);

        root.post(new Runnable() {
            @Override
            public void run() {
//                slideshowViewModel.sliderX.setValue(screenWidth/2 - 50);
                ValueAnimator animator = ValueAnimator.ofInt(screenWidth/4,(screenWidth/4)*3);
                animator.setDuration(1000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int curValue = (int)animation.getAnimatedValue();
                        slideshowViewModel.sliderX.setValue((int) curValue);
                    }
                });
                animator.start();
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        ValueAnimator animator2 = ValueAnimator.ofInt((screenWidth/4)*3,(screenWidth/2) -50);
                        animator2.setDuration(1000);
                        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int curValue = (int)animation.getAnimatedValue();
                                slideshowViewModel.sliderX.setValue((int) curValue);
                            }
                        });
                        animator2.start();
                    }
                });
            }
        });

        binding.imageUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NavHostFragment.findNavController(SlideshowFragment.this)
//                        .navigate(R.id.nav_home);
                PictureSelector.create(_this)
                        .openGallery(PictureMimeType.ofAll())
                        .imageEngine(GlideEngine.createGlideEngine())
                        .selectionMode(PictureConfig.SINGLE)
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                sourceFilePath = result.get(0).getRealPath();
                                Intent intent = new Intent(getActivity(), ImageUploadActivityView.class);
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

        binding.liveCamear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScanActivity.class);
                startActivityForResult(intent, getActivity().RESULT_OK);
            }
        });




        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}