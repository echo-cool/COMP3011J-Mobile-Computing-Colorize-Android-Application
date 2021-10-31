package com.echo.photo_editor.ML;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.echo.photo_editor.ml.MagentaArbitraryImageStylizationV1256Fp16Prediction1;
import com.echo.photo_editor.ml.MagentaArbitraryImageStylizationV1256Fp16Transfer1;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;

/**
 * @author WangYuyang
 * @date 2021-09-22 13:54:24
 */
public class StyleTransModel implements StyleMLModel {
    private Context context;

    public StyleTransModel(Context context) {
        this.context = context;
    }

    @Override
    public void process(String path, ImageProcessListener listener) {
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
                    listener.failed(e.getMessage());
                } finally {
                    listener.complete();
                }
            }
        }).start();

    }

    @Override
    public void process(Bitmap img_style, Bitmap img_to_be_process, ImageProcessListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                listener.start();
                //Style Prediction
                try {
                    MagentaArbitraryImageStylizationV1256Fp16Prediction1 model1 = MagentaArbitraryImageStylizationV1256Fp16Prediction1.newInstance(context);
                    // Creates inputs for reference.
                    TensorImage styleImage = TensorImage.fromBitmap(img_style);
                    // Runs model inference and gets result.
                    MagentaArbitraryImageStylizationV1256Fp16Prediction1.Outputs outputs1 = model1.process(styleImage);
                    TensorBuffer styleBottleneck = outputs1.getStyleBottleneckAsTensorBuffer();
                    // Releases model resources if no longer used.
                    model1.close();

                    MagentaArbitraryImageStylizationV1256Fp16Transfer1 model2 = MagentaArbitraryImageStylizationV1256Fp16Transfer1.newInstance(context);

                    // Creates inputs for reference.
                    TensorImage contentImage = TensorImage.fromBitmap(img_to_be_process);
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
                    listener.failed(e.getMessage());
                } finally {
                    listener.complete();
                }
            }
        }).start();
    }
}
