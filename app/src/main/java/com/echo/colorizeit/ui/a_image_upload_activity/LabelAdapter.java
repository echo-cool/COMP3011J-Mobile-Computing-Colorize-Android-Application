package com.echo.colorizeit.ui.a_image_upload_activity;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.LableItemBinding;

import java.util.ArrayList;
import java.util.Random;

/**
 * @Author: WangYuyang
 * @Date: 2021/10/31-16:24
 * @Project: My Application
 * @Package: com.echo.colorizeit.ui.a_image_upload_activity
 * @Description:
 **/
public class LabelAdapter extends RecyclerView.Adapter {
    private ArrayList<String> labels = new ArrayList<>();
    private int[] colors = new int[]{Color.parseColor("#FF1461"), Color.parseColor("#18FF92"), Color.parseColor("#5A87FF"), Color.parseColor("#FBF38C"), Color.parseColor("#FBF38C")};
    private int[] resource = new int[]{R.drawable.lablestyle, R.drawable.lablestyle1, R.drawable.lablestyle2, R.drawable.lablestyle3, R.drawable.lablestyle4};
    private Random random = new Random();

    public void addLabels(String label){
        labels.add(label);
        notifyDataSetChanged();

    }
    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LableItemBinding binding;
        binding = LableItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        LabelViewHolder holder = new LabelViewHolder(binding.getRoot());
        holder.binding = binding;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LableItemBinding binding = ((LabelViewHolder) holder).binding;
        binding.lableName.setText(labels.get(position));
//        binding.lableName.setBackgroundColor(colors[random.nextInt(colors.length)]);
        binding.lableName.setBackgroundResource(resource[random.nextInt(resource.length)]);

    }

    @Override
    public int getItemCount() {
        return labels.size();
    }

    private static class LabelViewHolder extends RecyclerView.ViewHolder {
        private LableItemBinding binding;

        public LabelViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
