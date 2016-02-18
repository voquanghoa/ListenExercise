package com.lovelybroteam.listenexercise;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.lovelybroteam.listenexercise.constant.AppConstant;
import com.lovelybroteam.listenexercise.control.BaseActivity;
import com.lovelybroteam.listenexercise.controller.AssertDataController;
import com.lovelybroteam.listenexercise.controller.DataController;
import com.lovelybroteam.listenexercise.controller.HttpDownloadController;
import com.lovelybroteam.listenexercise.helper.SocialHelper;
import com.lovelybroteam.listenexercise.util.Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MainActivity extends BaseActivity implements HttpDownloadController.IDownload {
    private String currentSelectTag;
    private AlertDialog  loveAppDialog;
    private SocialHelper socialHelper;
    private String dataFail = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        loadFullAds();
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdClosed() {
                finish();
            }
        });
        socialHelper = new SocialHelper(this);
        loadDataJson();
    }

    private void loadDataJson(){
        if(DataController.getInstance().getDataItem()==null) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        String data = AssertDataController.getInstance().loadFile(MainActivity.this, AppConstant.JSON_DATA_FILE);
                        DataController.getInstance().loadDataItem(data);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Utils.Log(e);
                        dataFail = e.getMessage();
                    } catch (IOException e) {
                        Utils.Log(e);
                        dataFail = e.getMessage();
                    }
                }
            }).start();
        }
    }

    public void onBackPressed() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Utils.Log("DID NOT LOAD FULL SCREEN ADS");
            super.onBackPressed();
        }
    }

    public void onLoveThisAppClick(View view){
        if(loveAppDialog==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(LayoutInflater.from(this).inflate(R.layout.love_app_dialog_layout, null));
            loveAppDialog = builder.create();
        }
        loveAppDialog.show();
    }

    public void onListenClick(View view){
        currentSelectTag = view.getTag().toString();
        if(dataFail != ""){
            showMessage(dataFail);
            return;
        }
        DataController.getInstance().setCurrentDataItem(currentSelectTag);
        if(DataController.getInstance().getCurrentDataItem() == null){
            showMessage(R.string.function_is_not_available);
        }else {
            startActivity(new Intent(this, ListItemActivity.class));
        }
    }

    public void onRateClick(View view){
        socialHelper.rateApp();
    }

    public void onShareGPlusClick(View view){
        socialHelper.shareGooglePlus();
    }

    public void onShareFacebookClick(View view){
        socialHelper.shareFacebook();
    }
}
