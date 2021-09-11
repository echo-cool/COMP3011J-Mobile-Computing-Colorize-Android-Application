package com.example.myapplication.ML;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.myapplication.R;
import com.example.myapplication.ml.ESRGAN;
import com.example.myapplication.ml.LiteModelMirnetFixedFp161;
import com.example.myapplication.ml.MagentaArbitraryImageStylizationV1256Fp16Prediction1;
import com.example.myapplication.ml.MagentaArbitraryImageStylizationV1256Fp16Transfer1;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.GpuDelegate;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class SuperResolutionModel implements MLModel{
    private Context context;


    public SuperResolutionModel(Context context) {
        this.context = context;
    }


    @Override
    public void process(String path, ProcessListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                //Style Prediction
                LiteModelMirnetFixedFp161 model = null;
                try {
                    model = LiteModelMirnetFixedFp161.newInstance(context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Creates inputs for reference.
                TensorImage lowLightImage = TensorImage.fromBitmap(bitmap);
                // NEW: Prepare GPU delegate.

                // Runs model inference and gets result.
                LiteModelMirnetFixedFp161.Outputs outputs = model.process(lowLightImage);
                TensorImage enhancedImage = outputs.getEnhancedImageAsTensorImage();
                Bitmap enhancedImageBitmap = enhancedImage.getBitmap();
                listener.success(enhancedImageBitmap);
                // Releases model resources if no longer used.
//                    model.close();
            }
        }).start();

    }
}
