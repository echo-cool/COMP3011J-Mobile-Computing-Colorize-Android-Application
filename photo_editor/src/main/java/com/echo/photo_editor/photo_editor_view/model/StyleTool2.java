package com.echo.photo_editor.photo_editor_view.model;


import android.graphics.Bitmap;
import android.view.View;

import com.echo.photo_editor.ML.RequestsListener;
import com.echo.photo_editor.ML.styleTransTypes;
import com.echo.photo_editor.photo_editor_view.PhotoEditorView;
import com.echo.photo_editor.util.PhotoLib;

/**
 * @author WangYuyang
 * @date 2021-10-29 22:51:42
 */
public class StyleTool2 extends Tool {
    public styleTransTypes style_type;

    public StyleTool2(String name, Bitmap image, styleTransTypes style_type, PhotoEditorView view) {
        super(name, image);
        this.style_type = style_type;
        super.listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.showLoading("Processing....");
                view.baiduImageAPI.styleTrans(new RequestsListener() {
                    @Override
                    public void success(String data) {
                        Bitmap res_image = PhotoLib.Base64ToBitmap(data);
                        view.model.getEditableImage().getValue().update_current_bitmap(res_image);
                        view.hideLoading();
                    }

                    @Override
                    public void failure(String info) {
                        System.out.println(info);
                        view.hideLoading();

                    }
                }, view.model.getEditableImage().getValue().getCurrent_bitmap(), style_type);
            }
        };
    }

}
