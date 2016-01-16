package com.lovelybroteam.listenexercise.control;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.lovelybroteam.listenexercise.controller.HttpDownloadController;
import com.lovelybroteam.listenexercise.util.Utils;

/**
 * Created by Vo Quang Hoa on 1/15/2016.
 */
public class InternetImageView extends ImageView implements HttpDownloadController.IDownload {
    public InternetImageView(Context context) {
        super(context);
        init();
    }

    public InternetImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InternetImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

    }
    public void setUrl(String url){
        this.setVisibility(GONE);
        HttpDownloadController.getInstance().startDownload(url, this);
    }

    public void onDownloadDone(String url, final byte[] data) {
        ((Activity)getContext()).runOnUiThread(new Runnable() {
            public void run() {
                InternetImageView.this.setVisibility(VISIBLE);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                InternetImageView.this.setImageBitmap(bitmap);
            }
        });
    }

    public void onDownloadFail(HttpDownloadController.DownloadFailReason reason, String message) {
        ((Activity)getContext()).runOnUiThread(new Runnable() {
            public void run() {
                InternetImageView.this.setVisibility(GONE);
            }
        });
        Utils.Log(message);
    }

    public void onDownloadProgress(int done, int total) {

    }
}
