package com.example.myapplication;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.UUID;

import cn.leancloud.LCLogger;
import cn.leancloud.LCObject;
import cn.leancloud.LCUser;
import cn.leancloud.LeanCloud;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 提供 this、App ID、App Key、Server Host 作为参数
        // 注意这里千万不要调用 cn.leancloud.core.LeanCloud 的 initialize 方法，否则会出现 NetworkOnMainThread 等错误。
        LeanCloud.initialize(this,
                "IIuObRE1fWDsL8sOMXuqLgux-gzGzoHsz",
                "IwTKYR5dPLT901k7hlbGxtuX",
                "https://iiuobre1.lc-cn-n1-shared.com");
        LeanCloud.setLogLevel(LCLogger.Level.DEBUG);


    }

}
