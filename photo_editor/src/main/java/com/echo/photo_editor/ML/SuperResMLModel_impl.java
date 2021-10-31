package com.echo.photo_editor.ML;

import android.content.Context;
import android.graphics.Bitmap;

import com.echo.photo_editor.ml.EsrganTf21;
import com.echo.photo_editor.ml.MagentaArbitraryImageStylizationV1256Fp16Prediction1;
import com.echo.photo_editor.ml.MagentaArbitraryImageStylizationV1256Fp16Transfer1;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;

/**
 * @Author: WangYuyang
 * @Date: 2021/10/31-00:00
 * @Project: My Application
 * @Package: com.echo.photo_editor.ML
 * @Description:
 **/
public class SuperResMLModel_impl implements SuperResMLModel {
    private Context context;

    public SuperResMLModel_impl(Context context) {
        this.context = context;
    }

    @Override
    public void process(Bitmap source, ProcessListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Bitmap bitmap = Bitmap.createBitmap(source);
//                //Style Prediction
//                try {
////                    listener.success(styledImageBitmap);
////            listener.success(SerializeUtil.serialize(styledImageBitmap));
//                } catch (IOException e) {
//                    // TODO Handle the exception
//                    listener.failed(e.getMessage());
//                } finally {
//                    listener.complete();
//                }
            }
        }).start();
    }
}
