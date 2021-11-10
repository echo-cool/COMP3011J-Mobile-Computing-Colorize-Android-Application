package com.echo.colorizeit.ui.f_gallery_view;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.echo.colorizeit.ImageUtil.rcImage;
import com.echo.colorizeit.ui.a_image_upload_activity.ImageUploadViewActivity;
import com.echo.photo_editor.photo_editor_view.PhotoEditorView;
import com.example.myapplication.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * @author Wang Yuyang
 * @date 2021-09-22 13:52:43
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<rcImage> imageList;
    public ImageAdapter(List<rcImage> list) {
        this.imageList = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcimage_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        rcImage image = imageList.get(position);
        holder.Image.setImageBitmap(BitmapFactory.decodeFile(image.getImgPath()));
        holder.Image.setVisibility(View.INVISIBLE);
        Animation animation_fade_in = AnimationUtils.loadAnimation(holder.Image.getContext(), R.anim.fade_in);
        animation_fade_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                holder.Image.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        holder.Image.startAnimation(animation_fade_in);
        holder.Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.Image.getContext(), PhotoEditorView.class);
                intent.putExtra("sourceFilePath", image.getImgPath());
                holder.Image.getContext().startActivity(intent);
            }
        });
        holder.Edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.Image.getContext(), PhotoEditorView.class);
                intent.putExtra("sourceFilePath", image.getImgPath());
                holder.Image.getContext().startActivity(intent);
            }
        });
        holder.Share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap tmp = BitmapFactory.decodeFile(image.getImgPath());
                String data = MediaStore.Images.Media.insertImage(holder.Image.getContext().getContentResolver(), tmp, String.valueOf(System.currentTimeMillis()), ":)");
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
                holder.Image.getContext().startActivity(intent);
            }
        });
        holder.Save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap tmp = BitmapFactory.decodeFile(image.getImgPath());
                String data = MediaStore.Images.Media.insertImage(holder.Image.getContext().getContentResolver(), tmp, String.valueOf(System.currentTimeMillis()), ":)");
                final AlertDialog.Builder alterDialog = new AlertDialog.Builder(holder.Image.getContext());

                alterDialog.setTitle("Image saved successfully.");//文字
                alterDialog.setMessage("You can view it in your gallery.");//提示消息
                alterDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(android.content.Intent.ACTION_VIEW);
                        intent.setType("image/*");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        holder.Image.getContext().startActivity(intent);
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

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                holder.Image.startAnimation(animation_fade_in);
//            }
//        }, position);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView Image;
        ImageView Share_button;
        ImageView Edit_button;
        ImageView Save_button;

        public ViewHolder (View view)
        {
            super(view);
            Image = (ImageView) view.findViewById(R.id.rc_image_view);
            Share_button = view.findViewById(R.id.gallery_share_button);
            Edit_button = view.findViewById(R.id.gallery_edit_button);
            Save_button = view.findViewById(R.id.gallery_save_button);
        }

    }
}
