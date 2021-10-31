package com.echo.photo_editor.photo_editor_view.model;


import android.graphics.Bitmap;
import android.view.View;

import com.echo.photo_editor.ML.ProcessListener;
import com.echo.photo_editor.photo_editor_view.PhotoEditorView;

/**
 * @author WangYuyang
 * @date 2021-10-29 22:51:42
 */
public class StyleTool extends Tool {
    public Bitmap style_image;

    public StyleTool(String name, Bitmap image, Bitmap style_image, PhotoEditorView view) {
        super(name, image);
        this.style_image = style_image;
        super.listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                view.styleTransModel.process(style_image, view.model.getEditableImage().getValue().getCurrent_bitmap(), new ProcessListener() {
                    @Override
                    public void start() {
                        view.showLoading("(On-Device ML) Processing....");

                    }

                    @Override
                    public void success(Bitmap FinishedBitmap) {
                        view.model.getEditableImage().getValue().update_current_bitmap(FinishedBitmap);
                    }

                    @Override
                    public void failed(String message) {
                        System.out.println(message);
                    }

                    @Override
                    public void complete() {
                        view.hideLoading();
                    }
                });
            }
        };
    }

    public StyleTool(String name, Bitmap image, View.OnClickListener listener, Bitmap style_image) {
        super(name, image, listener);
        this.style_image = style_image;
    }

    public Bitmap getStyle_image() {
        return style_image;
    }

    public void setStyle_image(Bitmap style_image) {
        this.style_image = style_image;
    }

}
