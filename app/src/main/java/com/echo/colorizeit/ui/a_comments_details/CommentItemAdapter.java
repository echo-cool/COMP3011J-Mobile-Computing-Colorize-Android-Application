package com.echo.colorizeit.ui.a_comments_details;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.CommunityDetailItemBinding;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import cn.leancloud.LCFile;
import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.gson.GsonObject;
import cn.leancloud.json.JSONObject;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Author: WangYuyang
 * @Date: 2021/11/13-14:34
 * @Project: My Application
 * @Package: com.echo.colorizeit.ui.a_comments_details
 * @Description:
 **/
public class CommentItemAdapter extends RecyclerView.Adapter {
    private CommentsActivity context;
    private JSONObject comments = new GsonObject();

    public CommentItemAdapter(CommentsActivity context) {
        this.context = context;
    }

    public void update_comments(JSONObject posts) {
        this.comments = new GsonObject(posts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommunityDetailItemBinding binding;
        binding = CommunityDetailItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        CommentsViewHolder holder = new CommentsViewHolder(binding.getRoot());
        holder.binding = binding;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CommentsViewHolder holder1 = (CommentsViewHolder) holder;
        JSONObject object = comments.getJSONObject(String.valueOf(position));
        holder1.binding.commentCommentText.setText(object.getString("text"));
        holder1.binding.commentPostDate.setText(object.getString("time"));
        holder1.binding.commentUsername.setText(object.getString("username") != null ? object.getString("username").toString() : "");
        holder1.binding.commentsUserIcon.setClipToOutline(true);
        JSONObject user = object.getJSONObject("User");
        if (user != null) {
            System.out.println(user);
            JSONObject user_icon = user.getJSONObject("serverData");
            user_icon = user_icon.getJSONObject("user_icon");
            JSONObject user_avatar = user_icon.getJSONObject("serverData");
            LCQuery<LCObject> query2 = new LCQuery<>("_File");
            query2.getInBackground(user_avatar.getString("objectId")).subscribe(new Observer<LCObject>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull LCObject lcObject) {
                    LCFile tmp = (LCFile) lcObject;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(400 * position);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            URL url = null;
                            try {
                                url = new URL(tmp.getUrl());
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            InputStream is = null;
                            try {
                                is = url.openStream();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (is != null) {
                                Bitmap bitmap = BitmapFactory.decodeStream(is);
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        holder1.binding.commentsUserIcon.setImageBitmap(bitmap);
                                    }
                                });

                            }


                        }
                    }).start();
                }

                @Override
                public void onError(@NonNull Throwable e) {
                }

                @Override
                public void onComplete() {

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    private static class CommentsViewHolder extends RecyclerView.ViewHolder {
        public CommunityDetailItemBinding binding;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
