package com.lovelybroteam.listenexercise.control;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.lovelybroteam.listenexercise.R;
import com.lovelybroteam.listenexercise.constant.AppConstant;
import com.lovelybroteam.listenexercise.controller.HttpDownloadController;

/**
 * Created by Vo Quang Hoa on 1/7/2016.
 */
public class BaseActivity extends Activity implements DialogInterface.OnCancelListener, HttpDownloadController.IDownload, AppConstant {
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

    public void onDownloadFail(String message) {
        closeLoadingDialog();
        showMessage(R.string.download_fail_message);
    }

    public void onDownloadProgress(int done, int total) {
        setProgressMessage("Download " + (done/1024)+" Kb/" + (total/1024)+" Kb.");
        setProgressDonePercent(done*100/total);
    }
}
