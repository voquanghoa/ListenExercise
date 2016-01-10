package com.lovelybroteam.listenexercise.control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;

import com.lovelybroteam.listenexercise.R;
import com.lovelybroteam.listenexercise.api.IAudioMediaPlayerListener;
import com.lovelybroteam.listenexercise.constant.AppConstant;
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
public abstract class ListenActivityBase extends BaseActivity implements IAudioMediaPlayerListener {
    private AudioMediaPlayer audioMediaPlayer;
    private int currentAudioDuration;
    private CustomSeekBar customSeekBar;
    private CustomMediaControl customMediaControl;
    private ScrollView textContentScrollView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listen_activity_layout);
        initViewElements();
        loadData(DataController.getInstance().getCurrentShowFolderPath(), DataController.getInstance().getCurrentShowDataItem());
    }

    protected void initViewElements(){
        textContentScrollView = (ScrollView)findViewById(R.id.text_scroll_view);
        customMediaControl = (CustomMediaControl)findViewById(R.id.custom_media_control);
        customSeekBar =(CustomSeekBar) findViewById(R.id.media_seekbar);
        customSeekBar.setOnUserChanged(new Runnable() {
            public void run() {
                audioMediaPlayer.seekTo(customSeekBar.getPercent());
            }
        });

        audioMediaPlayer = new AudioMediaPlayer(this);
        LayoutInflater.from(this).inflate(getChildView(), textContentScrollView);
    }

    protected abstract int getChildView();

    private void loadData(String folder, DataItem dataItem){
        try {
            audioMediaPlayer.load(folder + dataItem.getFileName() + AUDIO_FILE_EXTENSION);
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
                    showTextContent(content);
                }
            });
        } catch (UnsupportedEncodingException e) {
            Utils.Log(e);
        }
    }

    public void onLoadDone(int duration) {
        currentAudioDuration = duration;
        showCurrentPosition();
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
            audioMediaPlayer.togglePlay();
        } catch (Exception e) {
            Utils.Log(e);
        }
    }

    public void onNext(View view){

    }

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
                            updateAudioProgress(currentPosition, duration);
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

    private void updateAudioProgress(final int currentPosition, final int duration) {
        ListenActivityBase.this.runOnUiThread(new Runnable() {
            public void run() {
                customSeekBar.setPercent(100 * currentPosition / duration);
                customMediaControl.setPlayState(audioMediaPlayer.isPlaying());
            }
        });
    }

    protected abstract void showTextContent(String content);
}
