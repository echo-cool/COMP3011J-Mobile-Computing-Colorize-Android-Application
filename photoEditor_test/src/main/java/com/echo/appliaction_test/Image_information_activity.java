package com.echo.appliaction_test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.echo.appliaction_test.databinding.ImageInformationBinding;

import org.tensorflow.lite.support.image.TensorImage;

import java.util.Arrays;

/**
 * @Author: WangYuyang
 * @Date: 2021/10/31-14:53
 * @Project: My Application
 * @Package: com.echo.appliaction_test
 * @Description:
 **/
public class Image_information_activity extends AppCompatActivity {
    private ImageInformationBinding binding;
    private Image_information_activity _this = this;
    private String sourceFilePath;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ImageInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        sourceFilePath = intent.getStringExtra("sourceFilePath");
        image = BitmapFactory.decodeFile(sourceFilePath);
        TensorImage Timage = TensorImage.fromBitmap(image);

        binding.imageView2.setImageBitmap(image);
        TextView information = new TextView(this);
        information.append("getHeight: " + image.getHeight());
        information.append("\n");
        information.append("getWidth: " + image.getWidth());
        information.append("\n");
        information.append("describeContents: " + image.describeContents());
        information.append("\n");
        information.append("getRowBytes: " + image.getRowBytes());
        information.append("\n");
        information.append("hasAlpha: " + image.hasAlpha());
        information.append("\n");
        information.append("hasMipMap: " + image.hasMipMap());
        information.append("\n");
        information.append("isPremultiplied: " + image.isPremultiplied());
        information.append("\n");
        information.append("isMutable: " + image.isMutable());
        information.append("\n");
        information.append("isRecycled: " + image.isRecycled());
        information.append("\n");
        information.append("toString: " + image.toString());
        information.append("\n");
        information.append("getAllocationByteCount: " + image.getAllocationByteCount());
        information.append("\n");
        information.append("getByteCount: " + image.getByteCount());
        information.append("\n");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            information.append("getColorSpace: " + image.getColorSpace());
        }
        information.append("\n");
        information.append("getConfig: " + image.getConfig());
        information.append("\n");
        information.append("getDensity: " + image.getDensity());
        information.append("\n");
        information.append("getGenerationId: " + image.getGenerationId());
        information.append("\n");
        information.append("-----------------Tensor---------------------");
        information.append("\n");
        information.append("getDataType: " + Timage.getDataType());
        information.append("\n");
        information.append("getDataType.byteSize: " + Timage.getDataType().byteSize());
        information.append("\n");
        information.append("toString: " + Timage.toString());
        information.append("\n");
        information.append("getFlatSize: " + Timage.getTensorBuffer().getFlatSize());
        information.append("\n");
        information.append("getShape: " + Arrays.toString(Timage.getTensorBuffer().getShape()));
        information.append("\n");
        information.append("getTypeSize: " + Timage.getTensorBuffer().getTypeSize());
        information.append("\n");
        information.append("isDynamic: " + Timage.getTensorBuffer().isDynamic());
        information.append("\n");
        if (Timage.getTensorBuffer().getFlatSize() < 10000000) {
            information.append("getIntArray length: " + Timage.getTensorBuffer().getIntArray().length);
            information.append("\n");
            int[] array;
            if (Timage.getTensorBuffer().getIntArray().length > 200)
                array = Arrays.copyOfRange(Timage.getTensorBuffer().getIntArray(), 0, 200);
            else
                array = Timage.getTensorBuffer().getIntArray();
            information.append("getIntArray(0..200): " + Arrays.toString(array));
        }


        binding.informationArea.addView(information);


    }
}
