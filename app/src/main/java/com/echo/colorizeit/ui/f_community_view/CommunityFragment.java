package com.echo.colorizeit.ui.f_community_view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.echo.colorizeit.ImageUtil.thirdparty.GlideEngine;
import com.echo.colorizeit.ui.BaseFragment;
import com.example.myapplication.databinding.FragmentCommunityBinding;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cn.leancloud.LCFile;
import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.LCUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Author: WangYuyang
 * @Date: 2021/11/10-18:16
 * @Project: My Application
 * @Package: com.echo.colorizeit.ui.f_community_view
 * @Description:
 **/
public class CommunityFragment extends BaseFragment {
    public CommunityViewModel model;
    private FragmentCommunityBinding binding;
    private PostAdapter adapter = new PostAdapter(this);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentCommunityBinding.inflate(getLayoutInflater());
        model = new ViewModelProvider(this).get(CommunityViewModel.class);
        binding.rcGallery.setAdapter(adapter);
        do_update_posts();

        binding.addNewPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(getActivity())
                        .openGallery(PictureMimeType.ofAll())
                        .imageEngine(GlideEngine.createGlideEngine())
                        .selectionMode(PictureConfig.SINGLE)
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                String sourceFilePath = result.get(0).getRealPath();
//                                Bitmap img = BitmapFactory.decodeFile(sourceFilePath);
                                try {
                                    String[] tmp = sourceFilePath.split("\\.");
                                    LCFile file = LCFile.withAbsoluteLocalPath(System.currentTimeMillis() + tmp[tmp.length - 1], sourceFilePath);
                                    file.saveInBackground().subscribe(new Observer<LCFile>() {
                                        public void onSubscribe(Disposable disposable) {
                                        }

                                        public void onNext(LCFile file) {
                                            System.out.println("文件保存完成。URL：" + file.getUrl());
                                            LCObject post = new LCObject("Posts");
                                            post.put("Image", file);
                                            post.put("Username", LCUser.currentUser().getUsername());
                                            post.put("User", LCUser.currentUser());
                                            post.saveInBackground().subscribe(new Observer<LCObject>() {
                                                public void onSubscribe(Disposable disposable) {}
                                                public void onNext(LCObject todo) {
                                                    // 成功保存之后，执行其他逻辑
                                                    System.out.println("保存成功。objectId：" + todo.getObjectId());
                                                    do_update_posts();
                                                }
                                                public void onError(Throwable throwable) {
                                                    // 异常处理
                                                }
                                                public void onComplete() {}
                                            });

                                        }

                                        public void onError(Throwable throwable) {
                                            // 保存失败，可能是文件无法被读取，或者上传过程中出现问题
                                        }

                                        public void onComplete() {
                                        }
                                    });
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onCancel() {

                            }
                        });


            }
        });


        return binding.getRoot();
    }

    private void do_update_posts() {
        LCQuery<LCObject> query = new LCQuery<>("Posts");
        query = query.include("User");
        query.findInBackground().subscribe(new Observer<List<LCObject>>() {
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(List<LCObject> posts) {
                ArrayList tmp = new ArrayList(posts);
                adapter.update_posts(tmp);
            }

            public void onError(Throwable throwable) {
            }

            public void onComplete() {
            }
        });
    }
}
