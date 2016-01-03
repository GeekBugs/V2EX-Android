package com.f1reking.v2ex.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.f1reking.v2ex.util.L;
import com.f1reking.v2ex.util.ManifestConfig;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by F1ReKing on 2016/1/3.
 */
public class AppApplication extends Application {

    private List<Activity> activityList = new LinkedList<Activity>();
    private static AppApplication instance;
    private static Context context = null;

    private AppApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();

        boolean isDeveloperMode = isDeveliperMode();

        if (isDeveloperMode){
            L.openDebug();
        }else{
            L.closeDebug();
        }
    }

    /**
     * 用于读取配置，是否为开发模式，以便控制应用的日志等辅助开发功能的打开或关闭；在应用版本发布时，请在Manifest配置开发者模式为false
     */
    private boolean isDeveliperMode() {
        return ManifestConfig.getBooleanMetaValue(context,"developer_mode");
    }

    public static AppApplication getInstance() {
        if (null == instance) {
            instance = new AppApplication();
        }
        return instance;
    }

    //add Activity to container.
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    //exit
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }

}
