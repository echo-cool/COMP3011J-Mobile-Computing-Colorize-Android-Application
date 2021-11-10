package com.echo.colorizeit;

import android.app.Application;

import cn.leancloud.LCLogger;
import cn.leancloud.LeanCloud;

/**
 * @author Wang Yuyang
 * @date 2021-09-22 13:52:43
 */
public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 提供 this、App ID、App Key、Server Host 作为参数
        // 注意这里千万不要调用 cn.leancloud.core.LeanCloud 的 initialize 方法，否则会出现 NetworkOnMainThread 等错误。
        LeanCloud.initialize(this,
                "OdD4tNlK5eChmderJ9ryf5m2-gzGzoHsz",
                "17l22uySxDwuba465xU85VXD",
                "https://odd4tnlk.lc-cn-n1-shared.com");
        LeanCloud.setLogLevel(LCLogger.Level.DEBUG);


    }

}
