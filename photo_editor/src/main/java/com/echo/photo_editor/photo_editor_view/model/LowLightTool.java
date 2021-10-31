package com.echo.photo_editor.photo_editor_view.model;

import android.graphics.Bitmap;
import android.view.View;

import com.echo.photo_editor.ML.ProcessListener;
import com.echo.photo_editor.photo_editor_view.PhotoEditorView;

/**
 * @Author: WangYuyang
 * @Date: 2021/10/31-00:15
 * @Project: My Application
 * @Package: com.echo.photo_editor.photo_editor_view.model
 * @Description:
 **/
public class LowLightTool extends Tool{
    public LowLightTool(String name, Bitmap image, PhotoEditorView view) {
        super(name, image);
        super.listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.lowLightModel.process(view.model.getEditableImage().getValue().getCurrent_bitmap(), new ProcessListener() {
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

    public LowLightTool(String name, Bitmap image, View.OnClickListener listener) {
        super(name, image, listener);
    }
}
