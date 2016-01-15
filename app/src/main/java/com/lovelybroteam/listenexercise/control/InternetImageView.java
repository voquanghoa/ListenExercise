package com.lovelybroteam.listenexercise.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;

import com.lovelybroteam.listenexercise.controller.HttpDownloadController;

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
        this.setVisibility(GONE);
    }
    public void setUrl(String url){
        HttpDownloadController.getInstance().startDownload(url, this);
    }

    public void onDownloadDone(String url, byte[] data) {
        this.setVisibility(VISIBLE);
    }

    public void onDownloadFail(String message) {
        this.setVisibility(GONE);
    }

    public void setVisibility(int visibility){
        super.setVisibility(visibility);
        ViewParent parent = this.getParent();
        if(parent != null){
            ((View) parent).setVisibility(GONE);
        }
    }


    public void onDownloadProgress(int done, int total) {

    }
}
