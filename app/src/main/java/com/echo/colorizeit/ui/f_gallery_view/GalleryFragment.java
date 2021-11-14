package com.echo.colorizeit.ui.f_gallery_view;

import static com.echo.colorizeit.Util.check_is_grayscale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.echo.colorizeit.ImageUtil.PhotoLib;
import com.echo.colorizeit.ImageUtil.rcImage;
import com.echo.colorizeit.ImageUtil.thirdparty.GlideEngine;
import com.echo.colorizeit.Util;
import com.echo.colorizeit.ui.BaseFragment;
import com.echo.colorizeit.ui.a_image_upload_activity.ImageUploadViewActivity;
import com.echo.colorizeit.ui.a_login_activity.LoginViewActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentClarityEnhancementBinding;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import org.tensorflow.lite.support.image.TensorImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.leancloud.LCUser;


/**
 * @author Wang Yuyang
 * @date 2021-09-22 13:52:43
 */
public class GalleryFragment extends BaseFragment {

    private GalleryViewModel model;
    private FragmentClarityEnhancementBinding binding;
    private RecyclerView.Adapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        model =
                new ViewModelProvider(this).get(GalleryViewModel.class);


        binding = FragmentClarityEnhancementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Fade slideTracition = new Fade();
        slideTracition.setDuration(getResources().getInteger(R.integer.config_navAnimTime));
        this.setEnterTransition(slideTracition);
        this.setExitTransition(slideTracition);

        final TextView textView = binding.textGallery;
        model.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        RecyclerView image_gallery = binding.rcGallery;
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        adapter = new ImageAdapter(getRcImageList());
//        image_gallery.setLayoutManager(layoutManager);
        image_gallery.setAdapter(adapter);
        binding.textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("textGallery");
                PictureSelector.create(getActivity())
                        .openGallery(PictureMimeType.ofAll())
                        .imageEngine(GlideEngine.createGlideEngine())
                        .selectionMode(PictureConfig.SINGLE)
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                String sourceFilePath = result.get(0).getRealPath();
                                Bitmap img = BitmapFactory.decodeFile(sourceFilePath);
                                PhotoLib.saveImageToGallery(getContext(),img);

                            }


                            @Override
                            public void onCancel() {
                                // 取消
                            }
                        });
            }
        });
//        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        //防止item 交换位置
//        image_gallery.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                layoutManager.invalidateSpanAssignments(); //防止第一行到顶部有空白区域
//            }
//        });


        return root;
    }
    private List<String> getImagePathList(){
        List<String> data = new ArrayList<>();
        data = Util.getFilesAllName(this.getContext().getFilesDir());
        System.out.println(this.getContext().getFilesDir());
        return data;
    }
    private List<rcImage> getRcImageList(){
        List<String> filepathList = getImagePathList();
        List<rcImage> data = new ArrayList<>();
        for (String tmp:filepathList) {
            data.add(new rcImage(tmp.split("/")[tmp.split("/").length-1], tmp));
        }
        return data;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onStart() {
        super.onStart();
        if (LCUser.currentUser() == null) {
            Intent intent = new Intent(getActivity(), LoginViewActivity.class);
            startActivity(intent);
        }
    }
}