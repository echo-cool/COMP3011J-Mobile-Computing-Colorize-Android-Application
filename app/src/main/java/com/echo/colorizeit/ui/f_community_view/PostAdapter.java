package com.echo.colorizeit.ui.f_community_view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.echo.colorizeit.ImageUtil.PhotoLib;
import com.echo.colorizeit.Util;
import com.echo.colorizeit.ui.a_comments_details.CommentsActivity;
import com.example.myapplication.databinding.CommunityItemBinding;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.leancloud.LCFile;
import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Author: WangYuyang
 * @Date: 2021/11/10-18:16
 * @Project: My Application
 * @Package: com.echo.colorizeit.ui.f_community_view
 * @Description:
 **/
public class PostAdapter extends RecyclerView.Adapter {
    private CommunityFragment context;
    private ArrayList<LCObject> posts = new ArrayList<>();

    public PostAdapter(CommunityFragment context) {
        this.context = context;


    }

    public void update_posts(List<LCObject> posts) {
        this.posts = new ArrayList<>(posts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommunityItemBinding binding;
        binding = CommunityItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        PostViewHolder holder = new PostViewHolder(binding.getRoot());
        holder.binding = binding;
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PostViewHolder holder1 = (PostViewHolder) holder;
        LCObject object = posts.get(position);
        holder1.binding.postedBy.setText(object.get("Username") != null ? object.get("Username").toString() : "");
        holder1.binding.thumbUpCount.setText(object.get("thumb_up_count").toString());
        holder1.binding.shareCount.setText(object.get("share_count").toString());
        holder1.binding.downloadCount.setText(object.get("download_count").toString());
        holder1.binding.postedTime.setText(object.getCreatedAt().toLocaleString());
        holder1.binding.userIcon.setClipToOutline(true);

        LCObject file_id = (LCObject) object.get("Image");
        LCQuery<LCObject> query = new LCQuery<>("_File");
        query.getInBackground(file_id.getObjectId()).subscribe(new Observer<LCObject>() {
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(LCObject file) {
                LCFile tmp = (LCFile) file;
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
                            context.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    holder1.binding.rcImageView2.setImageBitmap(bitmap);
                                    holder1.binding.rcImageView2.setVisibility(View.VISIBLE);
                                    holder1.binding.imageLoadingViewAnim.setVisibility(View.GONE);
                                    holder1.binding.rcImageView2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(holder.itemView.getContext(), CommentsActivity.class);
                                            String image_path = PhotoLib.saveImageTmp(holder.itemView.getContext(),bitmap);
                                            intent.putExtra("image_path", image_path);
                                            intent.putExtra("object_id", object.getObjectId());
                                            holder.itemView.getContext().startActivity(intent);
                                        }
                                    });
                                    holder1.binding.thumbUpButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            object.put("thumb_up_count", ((int) object.get("thumb_up_count")) + 1);
                                            holder1.binding.thumbUpCount.setText(String.valueOf((int) object.get("thumb_up_count")));
                                            object.saveInBackground().subscribe(new Observer<LCObject>() {
                                                public void onSubscribe(Disposable disposable) {
                                                }

                                                public void onNext(LCObject savedTodo) {
                                                    System.out.println("保存成功");
                                                }

                                                public void onError(Throwable throwable) {
                                                    System.out.println("保存失败！");
                                                }

                                                public void onComplete() {
                                                }
                                            });
                                            ;
                                        }
                                    });
                                    holder1.binding.gallerySaveButton2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            object.put("download_count", ((int) object.get("download_count")) + 1);
                                            holder1.binding.downloadCount.setText(String.valueOf(((int) object.get("download_count"))));
                                            object.saveInBackground().subscribe(new Observer<LCObject>() {
                                                public void onSubscribe(Disposable disposable) {
                                                }

                                                public void onNext(LCObject savedTodo) {
                                                    System.out.println("保存成功");
                                                }

                                                public void onError(Throwable throwable) {
                                                    System.out.println("保存失败！");
                                                }

                                                public void onComplete() {
                                                }
                                            });
                                            ;
                                            Bitmap tmp = bitmap;
                                            String data = MediaStore.Images.Media.insertImage(holder.itemView.getContext().getContentResolver(), tmp, String.valueOf(System.currentTimeMillis()), ":)");
                                            final AlertDialog.Builder alterDialog = new AlertDialog.Builder(holder.itemView.getContext());
                                            alterDialog.setTitle("Image saved successfully.");//文字
                                            alterDialog.setMessage("You can view it in your gallery.");//提示消息
                                            alterDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent();
                                                    intent.setAction(android.content.Intent.ACTION_VIEW);
                                                    intent.setType("image/*");
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    holder.itemView.getContext().startActivity(intent);
                                                }
                                            });

                                            alterDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                            alterDialog.show();

                                        }
                                    });
                                    holder1.binding.galleryShareButton2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            object.put("share_count", ((int) object.get("share_count")) + 1);
                                            holder1.binding.shareCount.setText(String.valueOf(((int) object.get("share_count"))));
                                            object.saveInBackground().subscribe(new Observer<LCObject>() {
                                                public void onSubscribe(Disposable disposable) {
                                                }

                                                public void onNext(LCObject savedTodo) {
                                                    System.out.println("保存成功");
                                                }

                                                public void onError(Throwable throwable) {
                                                    System.out.println("保存失败！");
                                                }

                                                public void onComplete() {
                                                }
                                            });
                                            ;
                                            Bitmap tmp = bitmap;
                                            String data = MediaStore.Images.Media.insertImage(holder.itemView.getContext().getContentResolver(), tmp, String.valueOf(System.currentTimeMillis()), ":)");
                                            while (data == null) {
                                                try {
                                                    Thread.sleep(1);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            Uri ColorizedImageUri = Uri.parse(data);
                                            Intent intent = new Intent();
                                            intent.setAction(Intent.ACTION_SEND);
                                            intent.setType("image/*");
                                            intent.putExtra(Intent.EXTRA_STREAM, ColorizedImageUri);
                                            intent.putExtra(Intent.EXTRA_SUBJECT, "Share");//添加分享内容标题
                                            intent = Intent.createChooser(intent, "Share");
                                            holder.itemView.getContext().startActivity(intent);
                                        }
                                    });
                                }
                            });

                        }
                    }
                }).start();


            }

            public void onError(Throwable throwable) {
            }

            public void onComplete() {
            }
        });

        LCObject user = (LCObject) object.get("User");
        System.out.println(user);
        LCObject user_avatar = (LCObject) user.get("user_icon");
        LCQuery<LCObject> query2 = new LCQuery<>("_File");
        query2.getInBackground(user_avatar.getObjectId()).subscribe(new Observer<LCObject>() {
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
                            Thread.sleep(300 * position);
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
                            context.getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    holder1.binding.userIcon.setImageBitmap(bitmap);
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

    @Override
    public int getItemCount() {
        return posts.size();
    }

    private static class PostViewHolder extends RecyclerView.ViewHolder {
        public CommunityItemBinding binding;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
