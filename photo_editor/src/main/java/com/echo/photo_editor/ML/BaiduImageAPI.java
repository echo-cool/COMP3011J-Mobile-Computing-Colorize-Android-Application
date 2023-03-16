package com.echo.photo_editor.ML;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.baidu.aip.imageprocess.AipImageProcess;
import com.echo.photo_editor.util.PhotoLib;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class BaiduImageAPI {
    //设置APPID/AK/SK
    public static final String APP_ID = "xxxx";
    public static final String API_KEY = "xxx";
    public static final String SECRET_KEY = "xxx";
    public Context context;

    public BaiduImageAPI(Context context) {
     this.context = context;
    }


    public void styleTrans(RequestsListener listener, String imagePath, styleTransTypes style) {
        new Thread(() -> {
            AipImageProcess client = NewClient();
            // 传入可选参数调用接口
            HashMap<String, String> options = new HashMap<String, String>();
            options.put("option", style.toString());
            JSONObject res = client.styleTrans(imagePath, options);
            try {
                Log.d("INFO", String.valueOf(res));
                String ImgData = res.getString("image");
                listener.success(ImgData);
            } catch (JSONException e) {
                listener.failure(e.toString());
                e.printStackTrace();
            }
        }).start();
    }
    public void styleTrans(RequestsListener listener, Bitmap image, styleTransTypes style) {
        new Thread(() -> {
            AipImageProcess client = NewClient();
            // 传入可选参数调用接口
            HashMap<String, String> options = new HashMap<String, String>();
            options.put("option", style.toString());

            JSONObject res = client.styleTrans(PhotoLib.saveImageTmp(context, image), options);
            try {
                Log.d("INFO", String.valueOf(res));
                String ImgData = res.getString("image");
                listener.success(ImgData);
            } catch (JSONException e) {
                listener.failure(e.toString());
                e.printStackTrace();
            }
        }).start();
    }

    public void dehaze(RequestsListener listener, String imagePath) {
        new Thread(() -> {
            AipImageProcess client = NewClient();
            HashMap<String, String> options = new HashMap<String, String>();

            JSONObject res = client.dehaze(imagePath, options);
            try {
                Log.d("INFO", String.valueOf(res));
                String ImgData = res.getString("image");
                listener.success(ImgData);
            } catch (JSONException e) {
                listener.failure(e.toString());
                e.printStackTrace();
            }
        }).start();
    }
    public void dehaze(RequestsListener listener, Bitmap image) {
        new Thread(() -> {
            AipImageProcess client = NewClient();
            HashMap<String, String> options = new HashMap<String, String>();

            JSONObject res = client.dehaze(PhotoLib.saveImageTmp(context, image), options);
            try {
                Log.d("INFO", String.valueOf(res));
                String ImgData = res.getString("image");
                listener.success(ImgData);
            } catch (JSONException e) {
                listener.failure(e.toString());
                e.printStackTrace();
            }
        }).start();
    }

    public void contrastEnhance(RequestsListener listener, String imagePath) {
        new Thread(() -> {
            AipImageProcess client = NewClient();
            HashMap<String, String> options = new HashMap<String, String>();

            JSONObject res = client.contrastEnhance(imagePath, options);
            try {
                Log.d("INFO", String.valueOf(res));
                String ImgData = res.getString("image");
                listener.success(ImgData);
            } catch (JSONException e) {
                listener.failure(e.toString());
                e.printStackTrace();
            }
        }).start();
    }
    public void contrastEnhance(RequestsListener listener, Bitmap image) {
        new Thread(() -> {
            AipImageProcess client = NewClient();
            HashMap<String, String> options = new HashMap<String, String>();

            JSONObject res = client.contrastEnhance(PhotoLib.saveImageTmp(context, image), options);
            try {
                Log.d("INFO", String.valueOf(res));
                String ImgData = res.getString("image");
                listener.success(ImgData);
            } catch (JSONException e) {
                listener.failure(e.toString());
                e.printStackTrace();
            }
        }).start();
    }

    public void colourize(RequestsListener listener, String imagePath) {
        new Thread(() -> {
            AipImageProcess client = NewClient();
            HashMap<String, String> options = new HashMap<String, String>();
            JSONObject res = client.colourize(imagePath, options);
            try {
                Log.d("INFO", String.valueOf(res));
                String ImgData = res.getString("image");
                listener.success(ImgData);
            } catch (JSONException e) {
                listener.failure(e.toString());
                e.printStackTrace();
            }
        }).start();
    }

    public void selfieAnime(RequestsListener listener, String imagePath) {
        new Thread(() -> {
            AipImageProcess client = NewClient();
            HashMap<String, String> options = new HashMap<String, String>();

            JSONObject res = client.selfieAnime(imagePath, options);
            try {
                Log.d("INFO", String.valueOf(res));
                String ImgData = res.getString("image");
                listener.success(ImgData);
            } catch (JSONException e) {
                listener.failure(e.toString());
                e.printStackTrace();
            }
        }).start();
    }
    public void selfieAnime(RequestsListener listener, Bitmap image) {
        new Thread(() -> {
            AipImageProcess client = NewClient();
            HashMap<String, String> options = new HashMap<String, String>();

            JSONObject res = client.selfieAnime(PhotoLib.saveImageTmp(context, image), options);
            try {
                Log.d("INFO", String.valueOf(res));
                String ImgData = res.getString("image");
                listener.success(ImgData);
            } catch (JSONException e) {
                listener.failure(e.toString());
                e.printStackTrace();
            }
        }).start();
    }

    public void imageDefinitionEnhance(RequestsListener listener, String imagePath) {
        new Thread(() -> {
            AipImageProcess client = NewClient();
            HashMap<String, String> options = new HashMap<String, String>();

            JSONObject res = client.imageDefinitionEnhance(imagePath, options);
            try {
                Log.d("INFO", String.valueOf(res));
                String ImgData = res.getString("image");
                listener.success(ImgData);
            } catch (JSONException e) {
                listener.failure(e.toString());
                e.printStackTrace();
            }
        }).start();
    }
    public void imageDefinitionEnhance(RequestsListener listener, byte[] image) {
        new Thread(() -> {
            AipImageProcess client = NewClient();
            HashMap<String, String> options = new HashMap<String, String>();

            JSONObject res = client.imageDefinitionEnhance(image, options);
            try {
                Log.d("INFO", String.valueOf(res));
                String ImgData = res.getString("image");
                listener.success(ImgData);
            } catch (JSONException e) {
                listener.failure(e.toString());
                e.printStackTrace();
            }
        }).start();
    }

    private AipImageProcess NewClient(){
        AipImageProcess client;
        client = new AipImageProcess(APP_ID, API_KEY, SECRET_KEY);
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        return client;
    }

}
