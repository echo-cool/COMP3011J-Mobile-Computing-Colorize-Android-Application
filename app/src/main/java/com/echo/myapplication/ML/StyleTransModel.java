package com.echo.myapplication.ML;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.myapplication.ml.MagentaArbitraryImageStylizationV1256Fp16Prediction1;
import com.example.myapplication.ml.MagentaArbitraryImageStylizationV1256Fp16Transfer1;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;

public class StyleTransModel implements MLModel{
    private Context context;

    public StyleTransModel(Context context) {
        this.context = context;
    }

    @Override
    public void process(String path, ProcessListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                //Style Prediction
                try {
                    MagentaArbitraryImageStylizationV1256Fp16Prediction1 model1 = MagentaArbitraryImageStylizationV1256Fp16Prediction1.newInstance(context);
                    // Creates inputs for reference.
                    TensorImage styleImage = TensorImage.fromBitmap(bitmap);
                    // Runs model inference and gets result.
                    MagentaArbitraryImageStylizationV1256Fp16Prediction1.Outputs outputs1 = model1.process(styleImage);
                    TensorBuffer styleBottleneck = outputs1.getStyleBottleneckAsTensorBuffer();
                    // Releases model resources if no longer used.
                    model1.close();

                    MagentaArbitraryImageStylizationV1256Fp16Transfer1 model2 = MagentaArbitraryImageStylizationV1256Fp16Transfer1.newInstance(context);

                    // Creates inputs for reference.
                    TensorImage contentImage = TensorImage.fromBitmap(bitmap);
                    // Runs model inference and gets result.
                    MagentaArbitraryImageStylizationV1256Fp16Transfer1.Outputs outputs2 = model2.process(contentImage, styleBottleneck);
                    TensorImage styledImage = outputs2.getStyledImageAsTensorImage();
                    Bitmap styledImageBitmap = styledImage.getBitmap();
                    // Releases model resources if no longer used.
                    model2.close();
                    listener.success(styledImageBitmap);
//            listener.success(SerializeUtil.serialize(styledImageBitmap));
                } catch (IOException e) {
                    // TODO Handle the exception
                }
            }
        }).start();

    }
}
