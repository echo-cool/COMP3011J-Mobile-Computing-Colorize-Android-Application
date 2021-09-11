package com.example.myapplication;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static List<String> getFilesAllName(String path) {
        File file=new File(path);
        File[] files=file.listFiles();
        if (files == null){
            Log.e("error","空目录");
            return new ArrayList<>();
        }
        List<String> s = new ArrayList<>();
        for(int i =0;i<files.length;i++){
            s.add(files[i].getAbsolutePath());
        }
        return s;
    }
}
