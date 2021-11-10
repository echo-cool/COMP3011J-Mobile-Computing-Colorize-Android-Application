package com.echo.colorizeit.BaiduAPI;

import android.util.Log;

import com.baidu.aip.imageprocess.AipImageProcess;
import com.echo.colorizeit.Interfaces.RequestsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;



/**
 * @author Wang Yuyang
 * @date 2021-09-22 13:52:43
 */
public class BaiduImageAPI {
    //设置APPID/AK/SK
    public static final String APP_ID = "25146767";
    public static final String API_KEY = "bF9HZSY82nDUzxnnD4lQZh40";
    public static final String SECRET_KEY = "s4AaZEUjgyv3yFuYd3Zo4VGPYZpvmNtP";

    public BaiduImageAPI() {
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

    private AipImageProcess NewClient(){
        AipImageProcess client;
        client = new AipImageProcess(APP_ID, API_KEY, SECRET_KEY);
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        return client;
    }

}
