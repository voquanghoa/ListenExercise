package com.lovelybroteam.listenexercise;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.plus.PlusShare;
import com.lovelybroteam.listenexercise.constant.AppConstant;
import com.lovelybroteam.listenexercise.control.BaseActivity;
import com.lovelybroteam.listenexercise.controller.DataController;
import com.lovelybroteam.listenexercise.controller.HttpDownloadController;
import com.lovelybroteam.listenexercise.util.Utils;

import java.io.UnsupportedEncodingException;

public class MainActivity extends BaseActivity implements HttpDownloadController.IDownload {
    private String currentSelectTag;
    private AlertDialog  loveAppDialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        loadFullAds();
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdClosed() {
                finish();
            }
        });
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
        if(DataController.getInstance().getDataItem()==null){
            showLoadingDialog();
            HttpDownloadController.getInstance().startDownload(AppConstant.JSON_DATA_FILE, this);
        }else{
            showList();
        }
    }

    private void showList(){
        DataController.getInstance().setCurrentDataItem(currentSelectTag);
        if(DataController.getInstance().getCurrentDataItem() == null){
            showMessage(R.string.function_is_not_available);
        }else {
            startActivity(new Intent(this, ListItemActivity.class));
        }
    }

    public void onDownloadDone(String url, byte[] data) {
        super.onDownloadDone(url, data);
        try {
            String response = new String(data, "UTF-8");
            DataController.getInstance().loadDataItem(response);
            showList();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Utils.Log(e);
        }
    }

    public void onRateClick(View view){
        String packageName = getPackageName();
        Intent intent = new Intent(Intent.ACTION_VIEW);

        String marketLink = String.format(MARKET_URL_PATTERN, packageName);

        intent.setData(Uri.parse(marketLink));
        if (!SafeStartActivity(intent)) {
            String marketWebLink = String.format(MARKET_WEB_URL_PATTERN, packageName);
            intent.setData(Uri.parse(marketWebLink));
            if (!SafeStartActivity(intent)) {
                showMessage("Could not open Android market, please install the market app.");
            }
        }
    }

    public void onShareGPlusClick(View view){
        String marketWebLink = String.format(MARKET_WEB_URL_PATTERN, getPackageName());
        Intent shareIntent = new PlusShare.Builder(this)
                .setType("text/plain")
                .setText("Listen and exercise English for all level.")
                .setContentUrl(Uri.parse(marketWebLink))
                .getIntent();

        SafeStartActivity(shareIntent);
    }

    public void onShareFacebookClick(View view){
        String facebookShareLink = String.format(FACEBOOK_URL_PATTERN, getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookShareLink));
        SafeStartActivity(intent);
    }

    private boolean SafeStartActivity(Intent aIntent) {
        try{
            startActivity(aIntent);
            return true;
        }catch (ActivityNotFoundException e){
            return false;
        }
    }
}
