package com.lovelybroteam.listenexercise;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lovelybroteam.listenexercise.api.IAudioMediaPlayerListener;
import com.lovelybroteam.listenexercise.constant.AppConstant;
import com.lovelybroteam.listenexercise.control.BaseActivity;
import com.lovelybroteam.listenexercise.control.CustomSeekBar;
import com.lovelybroteam.listenexercise.controller.DataController;
import com.lovelybroteam.listenexercise.controller.HttpDownloadController;
import com.lovelybroteam.listenexercise.model.DataItem;
import com.lovelybroteam.listenexercise.player.AudioMediaPlayer;
import com.lovelybroteam.listenexercise.util.Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Vo Quang Hoa on 12/28/2015.
 */
public class ListenActivity extends BaseActivity implements IAudioMediaPlayerListener {
    private TextView _textView;
    private AudioMediaPlayer audioMediaPlayer;
    private int currentAudioDuration;
    private CustomSeekBar customSeekBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listen_activity_layout);
        customSeekBar =(CustomSeekBar) findViewById(R.id.media_seekbar);
        customSeekBar.setOnUserChanged(new Runnable() {
            public void run() {
                audioMediaPlayer.seekTo(customSeekBar.getPercent());
            }
        });

        _textView = (TextView)findViewById(R.id.test_content);
        audioMediaPlayer = new AudioMediaPlayer(this);
        loadData(DataController.getInstance().getCurrentShowFolderPath(), DataController.getInstance().getCurrentShowDataItem());
        showCurrentPosition();
    }

    private void loadData(String folder, DataItem dataItem){
        try {
            audioMediaPlayer.load(folder + dataItem.getFileName()+AUDIO_FILE_EXTENSION);
        } catch (IOException e) {
            Utils.Log(e);
        }
        HttpDownloadController.getInstance().startDownload(folder + dataItem.getFileName() + TEXT_FILE_EXTENSION, this);
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

    public void onLoadDone(int duration) {
        currentAudioDuration = duration;
    }

    public void onLoadError(String message) {

    }

    public void onBuffer(int percent) {
        customSeekBar.setBufferPercent(percent);
    }

    public void onPrevious(View view){

    }

    public void onPlay(View view){
        try {
            audioMediaPlayer.toggePlay();
        } catch (Exception e) {
            Utils.Log(e);
        }
    }

    public void onNext(View view){

    }

    @Override
    public void finish() {
        audioMediaPlayer = null;
        super.finish();
    }

    private void showCurrentPosition(){
        new Thread(new Runnable() {
            public void run() {
                while (audioMediaPlayer!=null){
                    synchronized (audioMediaPlayer){
                        final int currentPosition = audioMediaPlayer.getCurrentPosition();
                        final int duration = audioMediaPlayer.getDuration();

                        if(duration >0){
                            ListenActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    customSeekBar.setPercent(100 * currentPosition / duration);
                                }
                            });
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
