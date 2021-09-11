package com.example.myapplication.ui.ClarityEnhancement;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ImageUtil.rcImage;
import com.example.myapplication.R;

import java.util.List;

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
