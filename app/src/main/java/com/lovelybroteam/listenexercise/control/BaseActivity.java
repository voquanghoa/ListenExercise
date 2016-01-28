package com.lovelybroteam.listenexercise.control;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.lovelybroteam.listenexercise.R;
import com.lovelybroteam.listenexercise.constant.AppConstant;
import com.lovelybroteam.listenexercise.controller.HttpDownloadController;

/**
 * Created by Vo Quang Hoa on 1/7/2016.
 */
public class BaseActivity extends Activity implements DialogInterface.OnCancelListener, HttpDownloadController.IDownload, AppConstant {

    private AdView adView;
    private static AdRequest adRequest;
    protected InterstitialAd mInterstitialAd;
    protected AppTitleControl appTitleControl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(adRequest == null){
            adRequest = createAdsRequest();
        }
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        loadBannerAds();
        appTitleControl = (AppTitleControl)findViewById(R.id.app_title_control);
    }

    protected void loadBannerAds(){
        RelativeLayout adsLayout = (RelativeLayout)findViewById(R.id.adView);
        if(adsLayout!=null){
            String AD_UNIT_ID = getString(R.string.admob_id);
            adView = new AdView(this);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(AD_UNIT_ID);
            adsLayout.addView(adView);
            adView.loadAd(adRequest);
        }
    }

    protected void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("EF966C3E6FD639F322B1250C72187DF5")
                .addTestDevice("E62072DEC66B8E891FC23264834F5CCA")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    protected void loadFullAds(){
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.admob_id_full_screen));
        requestNewInterstitial();
    }

    protected AdRequest createAdsRequest(){
        return new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("EF966C3E6FD639F322B1250C72187DF5")
                .addTestDevice("E62072DEC66B8E891FC23264834F5CCA")
                .build();
    }

    private ProgressDialog progressDialog;
    protected void showMessage(final String content){
        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(BaseActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void showMessage(final int contentId) {
        showMessage(getString(contentId));
    }

    protected synchronized void showLoadingDialog(){
        if(progressDialog==null){
            progressDialog = ProgressDialog.show(this, "",getString(R.string.download_message) , true);
            progressDialog.setOnCancelListener(this);
            progressDialog.setProgress(100);
        }
        progressDialog.show();
    }

    protected synchronized void closeLoadingDialog(){
        this.runOnUiThread(new Runnable() {
            public void run() {
                if (progressDialog != null) {
                    progressDialog.hide();
                }
            }
        });
    }

    protected synchronized void setProgressMessage(final int messageId){
        this.setProgressMessage(getString(messageId));
    }

    protected synchronized void setProgressMessage(final String message){
        this.runOnUiThread(new Runnable() {
            public void run() {
                if (progressDialog != null) {
                    progressDialog.setMessage(message);
                }
            }
        });
    }

    protected synchronized void setProgressDonePercent(final int percent){
        this.runOnUiThread(new Runnable() {
            public void run() {
                if (progressDialog != null) {
                    progressDialog.setProgress(percent);
                }
            }
        });
    }

    public void onCancel(DialogInterface dialog) {
        showMessage(R.string.download_cancel_message);
    }

    public void onDownloadDone(String url, byte[] data) {
        closeLoadingDialog();
    }

    public void onDownloadFail(HttpDownloadController.DownloadFailReason reason, int codeMessage)  {
        closeLoadingDialog();
        showMessage(R.string.download_fail_message);
    }

    public void onDownloadProgress(int done, int total) {
        setProgressMessage("Downloaded " + (done/1024)+" Kb/" + (total/1024)+" Kb.");
        setProgressDonePercent(done*100/total);
    }

    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }

        super.onDestroy();
    }

}
