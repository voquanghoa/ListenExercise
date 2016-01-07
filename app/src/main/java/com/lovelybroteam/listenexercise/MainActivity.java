package com.lovelybroteam.listenexercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lovelybroteam.listenexercise.constant.AppConstant;
import com.lovelybroteam.listenexercise.control.BaseActivity;
import com.lovelybroteam.listenexercise.controller.DataController;
import com.lovelybroteam.listenexercise.controller.HttpDownloadController;
import com.lovelybroteam.listenexercise.util.Utils;

import java.io.UnsupportedEncodingException;

public class MainActivity extends BaseActivity implements HttpDownloadController.IDownload {
    private String currentSelectTag;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
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
        startActivity(new Intent(this, ListItemActivity.class));
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
}
