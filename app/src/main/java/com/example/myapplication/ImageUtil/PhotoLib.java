package com.example.myapplication.ImageUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.nio.ByteBuffer;

public class PhotoLib {
    public static byte[] BitmapToBytes(Bitmap img){
        int bytes = img.getByteCount();
        ByteBuffer buf = ByteBuffer.allocate(bytes);
        img.copyPixelsToBuffer(buf);
        byte[] byteArray = buf.array();
        return byteArray;
    }
    public static Bitmap BytesToBitmap(byte[] bytes){
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }
    public static Bitmap Base64ToBitmap(String data){
        byte[] decodedString = Base64.decode(data, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }


}
