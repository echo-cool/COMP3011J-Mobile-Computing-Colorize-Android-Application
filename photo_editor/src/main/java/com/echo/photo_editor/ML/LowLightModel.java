package com.echo.photo_editor.ML;

import android.content.Context;
import android.graphics.Bitmap;

import com.echo.photo_editor.ml.LiteModelZeroDce1;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.util.Arrays;

/**
 * @Author: WangYuyang
 * @Date: 2021/10/31-00:07
 * @Project: My Application
 * @Package: com.echo.photo_editor.ML
 * @Description:
 **/
public class LowLightModel {
    private Context context;

    public LowLightModel(Context context) {
        this.context = context;
    }

    public void process(Bitmap source, ProcessListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                listener.start();
                Bitmap bitmap = Bitmap.createBitmap(source);
                try {
                    ImageProcessor imageProcessor =
                            new ImageProcessor.Builder()
                                    .add(new ResizeOp(400, 600, ResizeOp.ResizeMethod.BILINEAR))
                                    .build();
                    TensorImage source_image = new TensorImage(DataType.FLOAT32);

                    source_image.load(source);
                    source_image = imageProcessor.process(source_image);

                    System.out.println(source_image.getHeight());
                    System.out.println(source_image.getWidth());
                    System.out.println(Arrays.toString(source_image.getTensorBuffer().getShape()));
                    System.out.println("source_image: " + source_image.getTensorBuffer().getFlatSize());
                    System.out.println(source_image.getDataType());
                    LiteModelZeroDce1 model = LiteModelZeroDce1.newInstance(context);
                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 400, 600, 3}, DataType.FLOAT32);
                    System.out.println("inputFeature0: " + inputFeature0.getFlatSize());

                    inputFeature0.loadBuffer(source_image.getBuffer());

                    // Runs model inference and gets result.
                    LiteModelZeroDce1.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    // Releases model resources if no longer used.
                    model.close();
                    TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                    tensorImage.load(outputFeature0);

                    listener.success(tensorImage.getBitmap());
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
