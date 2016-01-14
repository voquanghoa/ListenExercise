package com.lovelybroteam.listenexercise.application;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.lovelybroteam.listenexercise.controller.FileStoreController;
import com.lovelybroteam.listenexercise.controller.UserResultController;
import com.lovelybroteam.listenexercise.util.Utils;

/**
 * Created by Vo Quang Hoa on 1/14/2016.
 */
public class ListenExerciseApplication  extends Application {
    private static ListenExerciseApplication singleton;

    public ListenExerciseApplication getInstance(){
        return singleton;
    }

    public void onCreate() {
        super.onCreate();
        singleton = this;

        FileStoreController.getInstance().setBaseDir(getDataDir());
        UserResultController.getInstance().load();
    }

    private String getDataDir(){
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.applicationInfo.dataDir;
        } catch (PackageManager.NameNotFoundException e) {
            Utils.Log("Error Package name not found ");
            Utils.Log(e);
        }
        return "";
    }
}
