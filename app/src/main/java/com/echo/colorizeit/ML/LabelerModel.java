package com.echo.colorizeit.ML;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.myapplication.ml.LiteModelObjectDetectionMobileObjectLabelerV11;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;

import java.io.IOException;
import java.util.List;

/**
 * @Author: WangYuyang
 * @Date: 2021/10/31-15:58
 * @Project: My Application
 * @Package: com.echo.photo_editor.ML
 * @Description:
 **/
public class LabelerModel {
    private Context context;

    public LabelerModel(Context context) {
        this.context = context;
    }

    public void process(Bitmap source, CategoryProcessListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                listener.start();
                try {
                    LiteModelObjectDetectionMobileObjectLabelerV11 model = LiteModelObjectDetectionMobileObjectLabelerV11.newInstance(context);

                    // Creates inputs for reference.
                    TensorImage image = TensorImage.fromBitmap(source);

                    // Runs model inference and gets result.
                    LiteModelObjectDetectionMobileObjectLabelerV11.Outputs outputs = model.process(image);
                    List<Category> probability = outputs.getProbabilityAsCategoryList();

                    // Releases model resources if no longer used.
                    model.close();
                    listener.success(probability);

                } catch (IOException e) {
                    // TODO Handle the exception
                    listener.failed(e.getMessage());
                }
                finally {
                    listener.complete();
                }
            }
        }).start();
    }
}
