package com.echo.colorizeit;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.leancloud.LCObject;
import cn.leancloud.LCUser;
import io.reactivex.disposables.Disposable;

/**
 * @author Wang Yuyang
 * @date 2021-09-22 13:52:43
 */
public class Util {
    public static List<String> getFilesAllName(File path) {
        File file = path;
        File[] files = file.listFiles();
        if (files == null) {
            Log.e("error", "空目录");
            return new ArrayList<>();
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i].toString());
            if (files[i].toString().contains("jpg"))
                s.add(files[i].getAbsolutePath());
        }
        Collections.reverse(s);
        return s;
    }

    public static void updateUser() {
        LCUser currentUser = LCUser.getCurrentUser();
        currentUser.fetchInBackground().subscribe(new io.reactivex.Observer<LCObject>() {
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(LCObject user) {

            }

            public void onError(Throwable throwable) {
            }

            public void onComplete() {
            }
        });
    }

    public static Integer getRemaining() {
        LCUser currentUser = LCUser.getCurrentUser();
        return currentUser.getInt("RemainingCount");
    }

    public static void UpdateCountOnProcess() {
        LCUser currentUser = LCUser.getCurrentUser();
        Log.d("UpdateCountOnProcess", currentUser.getUsername());
        Log.d("UpdateCountOnProcess", String.valueOf(currentUser.getInt("RemainingCount")));
        currentUser.fetchInBackground().subscribe(new io.reactivex.Observer<LCObject>() {
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(LCObject user) {
                minusRemainingBy1((LCUser) user);
                addCountBy1((LCUser) user);
                user.saveInBackground().subscribe(new io.reactivex.Observer<LCObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull LCObject lcObject) {
                        Log.d("UpdateCountOnProcess", String.valueOf(lcObject.getInt("RemainingCount")));

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


            }

            public void onError(Throwable throwable) {
            }

            public void onComplete() {
            }
        });

    }

    public static void minusRemainingBy1(LCUser currentUser) {

        currentUser.put("RemainingCount", currentUser.getInt("RemainingCount") - 1);

    }

    public static void addCountBy1(LCUser currentUser) {
        currentUser.put("ProcessedCount", currentUser.getInt("ProcessedCount") + 1);

    }

    public static String getUUID(Context context) {
        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("ID:", android_id);

        return android_id;
    }


}
