package com.echo.photo_editor.photo_editor_view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.echo.photo_editor.ML.LowLightModel;
import com.echo.photo_editor.ML.StyleTransModel;
import com.echo.photo_editor.R;
import com.echo.photo_editor.databinding.ActivityPhotoEditorBinding;
import com.echo.photo_editor.photo_editor_view.adapter.ImagePreviewAdapter;
import com.echo.photo_editor.photo_editor_view.model.EditableImage;
import com.echo.photo_editor.photo_editor_view.model.LowLightTool;
import com.echo.photo_editor.photo_editor_view.model.StyleTool;
import com.echo.photo_editor.photo_editor_view.model.Tool;
import com.echo.photo_editor.photo_editor_view.model.Toolbox;
import com.echo.photo_editor.util.PhotoLib;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * @author WangYuyang
 * @date 2021-10-29 12:33:11
 */
public class PhotoEditorView extends AppCompatActivity {
    public PhotoEditorViewModel model;
    private ActivityPhotoEditorBinding binding;
    private ProgressDialog mProgressDialog;
    private ImagePreviewAdapter imagePreviewAdapter;
    public StyleTransModel styleTransModel = new StyleTransModel(this);
    public LowLightModel lowLightModel = new LowLightModel(this);
    private PhotoEditorView _this = this;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhotoEditorBinding.inflate(getLayoutInflater());
        model = new ViewModelProvider(this).get(PhotoEditorViewModel.class);
        Intent intent = getIntent();
        String sourceFilePath = intent.getStringExtra("sourceFilePath");
        model.set_sourceFilePath(sourceFilePath);
//        model.getSourceFilePath().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                Bitmap image = BitmapFactory.decodeFile(s);
//                model.getSourceImageBitmap().postValue(image);
//            }
//        });
//        model.getSourceImageBitmap().observe(this, new Observer<Bitmap>() {
//            @Override
//            public void onChanged(Bitmap bitmap) {
//                binding.mainImageContent.setImageBitmap(bitmap);
//            }
//        });
        model.getEditableImage().observe(this, new Observer<EditableImage>() {
            @Override
            public void onChanged(EditableImage editableImage) {
                binding.mainImageContent.setImageBitmap(editableImage.getCurrent_bitmap());
            }
        });
        setContentView(binding.getRoot());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.imagePreviewRcView.setLayoutManager(linearLayoutManager);
        binding.redoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.getEditableImage().getValue().redo();
            }
        });
        binding.undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.getEditableImage().getValue().undo();
            }
        });
        binding.SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoLib.saveImageToGallery(_this, model.getEditableImage().getValue().getCurrent_bitmap());
                showSnackbar("Image saved !");

            }
        });


        ArrayList<Toolbox> toolboxes = new ArrayList<>();
        ArrayList<Tool> tools = new ArrayList<>();
        StyleTool candy = new StyleTool(
                "Candy",
                BitmapFactory.decodeResource(getResources(), R.mipmap.candy),
                BitmapFactory.decodeResource(getResources(), R.mipmap.candy),
                this
        );
        StyleTool feathers = new StyleTool(
                "Feathers",
                BitmapFactory.decodeResource(getResources(), R.mipmap.feathers),
                BitmapFactory.decodeResource(getResources(), R.mipmap.feathers),
                this
        );
        StyleTool la_muse = new StyleTool(
                "LaMuse",
                BitmapFactory.decodeResource(getResources(), R.mipmap.la_muse),
                BitmapFactory.decodeResource(getResources(), R.mipmap.la_muse),
                this
        );
        StyleTool mosaic = new StyleTool(
                "mosaic",
                BitmapFactory.decodeResource(getResources(), R.mipmap.mosaic),
                BitmapFactory.decodeResource(getResources(), R.mipmap.mosaic),
                this
        );
        StyleTool the_scream = new StyleTool(
                "TheScream",
                BitmapFactory.decodeResource(getResources(), R.mipmap.the_scream),
                BitmapFactory.decodeResource(getResources(), R.mipmap.the_scream),
                this
        );

        StyleTool udnie = new StyleTool(
                "Udnie",
                BitmapFactory.decodeResource(getResources(), R.mipmap.udnie),
                BitmapFactory.decodeResource(getResources(), R.mipmap.udnie),
                this
        );

        Toolbox style_trans = new Toolbox("Style Trans", BitmapFactory.decodeResource(getResources(), R.mipmap.tool_tmp_image));
        style_trans.getTools().add(candy);
        style_trans.getTools().add(feathers);
        style_trans.getTools().add(la_muse);
        style_trans.getTools().add(mosaic);
        style_trans.getTools().add(the_scream);
        style_trans.getTools().add(udnie);

        LowLightTool lowLightTool = new LowLightTool(
                "LowLight",
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher),
                this
        );
        Toolbox lowLightToolBox = new Toolbox(
                "Lowlight",
                BitmapFactory.decodeResource(getResources(), R.mipmap.tool_tmp_image)
        );
        lowLightToolBox.getTools().add(lowLightTool);

        toolboxes.add(style_trans);
        toolboxes.add(lowLightToolBox);

        imagePreviewAdapter = new ImagePreviewAdapter(toolboxes);
        binding.imagePreviewRcView.setAdapter(imagePreviewAdapter);

        binding.exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("exitButton");
                imagePreviewAdapter.setHideTool();
            }
        });


    }

    protected void showSnackbar(@NonNull String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View view = _this.findViewById(android.R.id.content);
                if (view != null) {
                    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(_this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void showLoading(@NonNull String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = new ProgressDialog(_this);
                mProgressDialog.setMessage(message);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }
        });

    }

    public void hideLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            }
        });

    }
}
