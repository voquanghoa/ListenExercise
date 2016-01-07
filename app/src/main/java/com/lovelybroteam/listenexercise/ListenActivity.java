package com.lovelybroteam.listenexercise;

import android.os.Bundle;
import android.widget.TextView;

import com.lovelybroteam.listenexercise.constant.AppConstant;
import com.lovelybroteam.listenexercise.control.BaseActivity;
import com.lovelybroteam.listenexercise.controller.DataController;
import com.lovelybroteam.listenexercise.controller.HttpDownloadController;
import com.lovelybroteam.listenexercise.model.DataItem;
import com.lovelybroteam.listenexercise.util.Utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by Vo Quang Hoa on 12/28/2015.
 */
public class ListenActivity extends BaseActivity {
    private TextView _textView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listen_activity_layout);
        _textView = (TextView)findViewById(R.id.test_content);
        loadData(DataController.getInstance().getCurrentShowFolderPath(), DataController.getInstance().getCurrentShowDataItem());
    }

    private void loadData(String folder, DataItem dataItem){
        HttpDownloadController.getInstance().startDownload(folder + dataItem.getFileName()+TEXT_FILE_EXTENSION, this);
    }

    public void onDownloadDone(String url, byte[] data) {
        super.onDownloadDone(url, data);
        try {
            final String content = new String(data, AppConstant.CHARSET);
            this.runOnUiThread(new Runnable() {
                public void run() {
                    _textView.setText(content);
                }
            });
        } catch (UnsupportedEncodingException e) {
            Utils.Log(e);
        }
    }
}
