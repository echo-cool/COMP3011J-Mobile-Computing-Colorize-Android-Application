package com.echo.colorizeit.ui.f_gallery_view;

import android.graphics.BitmapFactory;
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
import com.example.myapplication.R;

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
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                holder.Image.startAnimation(animation_fade_in);
//            }
//        }, position);
        holder.Name.setText(image.getImgName());
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView Image;
        TextView Name;

        public ViewHolder (View view)
        {
            super(view);
            Image = (ImageView) view.findViewById(R.id.rc_image_view);
            Name = (TextView) view.findViewById(R.id.rc_image_name);
        }

    }
}
