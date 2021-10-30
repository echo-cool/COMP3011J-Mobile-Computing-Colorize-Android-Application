package com.echo.photo_editor.photo_editor_view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.echo.photo_editor.databinding.EditorPerviewItemBinding;
import com.echo.photo_editor.photo_editor_view.model.Tool;
import com.echo.photo_editor.photo_editor_view.model.ToolBarItem;
import com.echo.photo_editor.photo_editor_view.model.Toolbox;

import java.util.ArrayList;

public class ImagePreviewAdapter extends RecyclerView.Adapter {

    private Boolean isShowing_tool = false;
    private Toolbox current_toolbox = null;

    public ArrayList<Toolbox> toolboxes = new ArrayList<>();
    public ArrayList<ToolBarItem> tool_bar_items = new ArrayList<>();

    public ImagePreviewAdapter(ArrayList<Toolbox> toolboxes) {
        this.toolboxes = new ArrayList<>(toolboxes);
        tool_bar_items.addAll(toolboxes);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EditorPerviewItemBinding binding;
        binding = EditorPerviewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ImageViewHolder holder = new ImageViewHolder(binding.getRoot());
        holder.binding = binding;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EditorPerviewItemBinding binding = ((ImageViewHolder) holder).binding;
        ToolBarItem toolBarItem = tool_bar_items.get(position);
        ImageView imageView = binding.imageView;
        TextView textView = binding.textView;
        ConstraintLayout item = binding.toolBarItem;
        imageView.setImageBitmap(toolBarItem.getImage());
        textView.setText(toolBarItem.getName());
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowing_tool)
                    setShowTool((Toolbox) toolBarItem);
                else {
                    Tool tool = (Tool) toolBarItem;
                    tool.listener.onClick(v);
                }
            }
        });


    }

    public void setShowTool(Toolbox toolbox) {
        isShowing_tool = true;
        tool_bar_items.clear();
        tool_bar_items.addAll(toolbox.tools);
        notifyDataSetChanged();
//        for(int i = 0; i < tool_bar_items.size(); i ++){
//            tool_bar_items.remove(i);
//            notifyItemRemoved(i + 1);
//        }
//        for(int i = 0; i < toolbox.tools.size(); i ++){
//            tool_bar_items.add(toolbox.tools.get(i));
//            notifyItemInserted(i);
//        }
    }

    public void setHideTool() {
        isShowing_tool = false;
        tool_bar_items.clear();
        tool_bar_items.addAll(toolboxes);
        notifyDataSetChanged();
//        for(int i = 0; i < tool_bar_items.size(); i ++){
//            tool_bar_items.remove(i);
//            notifyItemRemoved(i);
//        }
//        for(int i = 0; i < toolboxes.size(); i ++){
//            tool_bar_items.add(toolboxes.get(i));
//            notifyItemInserted(i);
//        }
    }


    @Override
    public int getItemCount() {
        return tool_bar_items.size();

    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder {
        public EditorPerviewItemBinding binding;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
