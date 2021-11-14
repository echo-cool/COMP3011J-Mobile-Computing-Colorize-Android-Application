package com.echo.colorizeit.ui.a_comments_details;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.echo.colorizeit.ImageUtil.PhotoLib;
import com.echo.colorizeit.ui.BaseActivity;
import com.example.myapplication.databinding.CommunityDetailBinding;

import java.time.LocalTime;
import java.util.ArrayList;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.LCUser;
import cn.leancloud.gson.GsonObject;
import cn.leancloud.json.JSONObject;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Author: WangYuyang
 * @Date: 2021/11/13-14:32
 * @Project: My Application
 * @Package: com.echo.colorizeit.ui.a_comments_details
 * @Description:
 **/
public class CommentsActivity extends BaseActivity {
    private CommunityDetailBinding binding;
    private CommentsViewModel model;
    private CommentItemAdapter adapter = new CommentItemAdapter(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CommunityDetailBinding.inflate(getLayoutInflater());
        model = new ViewModelProvider(this).get(CommentsViewModel.class);
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String image_path = intent.getStringExtra("image_path");
        String image_object_id = intent.getStringExtra("object_id");
        LCQuery<LCObject> query = new LCQuery<>("Posts");
        query.getInBackground(image_object_id).subscribe(new Observer<LCObject>() {
            public void onSubscribe(Disposable disposable) {}
            public void onNext(LCObject post) {
                JSONObject comments = post.getJSONObject("comments");
                adapter.update_comments(comments);
                binding.sendCommentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("sendCommentButton");
                        String text = binding.commentTextField.getText().toString();
                        JSONObject comment_object = new GsonObject();
                        comment_object.put("userid", LCUser.currentUser().getObjectId());
                        comment_object.put("username", LCUser.currentUser().getUsername());
                        comment_object.put("text", text);
                        comment_object.put("User", LCUser.currentUser());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            comment_object.put("time", LocalTime.now().toString());
                        }
                        comments.put(String.valueOf(comments.size()),comment_object);
                        post.put("comments",comments);
                        post.saveInBackground().subscribe(new Observer<LCObject>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull LCObject o) {
                                adapter.update_comments(comments);
                                System.out.println("Save finished.");
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                    }
                });
            }
            public void onError(Throwable throwable) {}
            public void onComplete() {}
        });
        Bitmap image = BitmapFactory.decodeFile(image_path);
        binding.imageView3.setImageBitmap(image);
        binding.recyclerView.setAdapter(adapter);
        binding.imageView3.setClipToOutline(true);






    }
}
