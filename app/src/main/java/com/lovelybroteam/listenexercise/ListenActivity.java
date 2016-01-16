package com.lovelybroteam.listenexercise;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.lovelybroteam.listenexercise.api.IAudioMediaPlayerListener;
import com.lovelybroteam.listenexercise.constant.AppConstant;
import com.lovelybroteam.listenexercise.control.AppTitleControl;
import com.lovelybroteam.listenexercise.control.BaseActivity;
import com.lovelybroteam.listenexercise.control.CustomMediaControl;
import com.lovelybroteam.listenexercise.control.CustomSeekBar;
import com.lovelybroteam.listenexercise.control.ListenExerciseControl;
import com.lovelybroteam.listenexercise.control.PureListenControl;
import com.lovelybroteam.listenexercise.controller.DataController;
import com.lovelybroteam.listenexercise.controller.HttpDownloadController;
import com.lovelybroteam.listenexercise.controller.ListenContentController;
import com.lovelybroteam.listenexercise.model.DataItem;
import com.lovelybroteam.listenexercise.model.ListenContent;
import com.lovelybroteam.listenexercise.player.AudioMediaPlayer;
import com.lovelybroteam.listenexercise.util.Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Vo Quang Hoa on 12/28/2015.
 */
public class ListenActivity extends BaseActivity implements IAudioMediaPlayerListener {
    private AudioMediaPlayer audioMediaPlayer;
    private CustomSeekBar customSeekBar;
    private CustomMediaControl customMediaControl;
    private ScrollView textContentScrollView;
    private PureListenControl pureListenControl;
    private ListenExerciseControl listenExerciseControl;
    private boolean isNeedShowAds;
    private AppTitleControl appTitleControl;
    private TextView questionHeaderTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isNeedShowAds = Utils.checkAds();
        setContentView(R.layout.listen_activity_layout);
        initViewElements();
        loadData();
    }

    protected void initViewElements(){
        questionHeaderTextView = (TextView)findViewById(R.id.text_question_title);
        appTitleControl = (AppTitleControl)findViewById(R.id.app_title_control);
        textContentScrollView = (ScrollView)findViewById(R.id.text_scroll_view);
        customMediaControl = (CustomMediaControl)findViewById(R.id.custom_media_control);
        customSeekBar =(CustomSeekBar) findViewById(R.id.media_seekbar);
        customSeekBar.setOnUserChanged(new Runnable() {
            public void run() {
                audioMediaPlayer.seekTo(customSeekBar.getPercent());
            }
        });

        audioMediaPlayer = new AudioMediaPlayer(this);
        pureListenControl = new PureListenControl(this);
        listenExerciseControl = new ListenExerciseControl(this);
    }

    private void loadData(){
        loadData(DataController.getInstance().getCurrentShowFolderPath(), DataController.getInstance().getCurrentShowDataItem());
    }

    private void loadData(String folder, DataItem dataItem){
        appTitleControl.setTitle(dataItem.getDisplay());
        customSeekBar.setPercent(0);
        customSeekBar.setBufferPercent(0);
        reInitAudioMediaPlayer();
        try {
            audioMediaPlayer.load(folder + dataItem.getFileName() + AUDIO_FILE_EXTENSION);
        } catch (IOException e) {
            Utils.Log(e);
        }
        showLoadingDialog();
        HttpDownloadController.getInstance().startDownload(folder + dataItem.getFileName() + TEXT_FILE_EXTENSION, this);
    }

    private void reInitAudioMediaPlayer() {
        try {
            if (audioMediaPlayer != null) {
                audioMediaPlayer.release();
            }
        }catch (Exception ex){
            Utils.Log(ex);
        }finally {
            audioMediaPlayer = null;
            audioMediaPlayer = new AudioMediaPlayer(this);
        }
    }

    public void onDownloadDone(final String url, byte[] data) {
        super.onDownloadDone(url, data);
        final String currentFileName = url.replace(AppConstant.SERVER_BASE_PATH, "").
                replace(AppConstant.TEXT_FILE_EXTENSION, "");
        try {
            final String content = new String(data, AppConstant.CHARSET);
            ListenContentController.getInstance().loadJson(content);


            this.runOnUiThread(new Runnable() {
                public void run() {
                    textContentScrollView.scrollTo(0,0);
                    updateTitle();
                    textContentScrollView.removeAllViews();

                    ListenContent listenContent = ListenContentController.getInstance().getCurrentListenContent();
                    if(listenContent.getQuestions() == null || listenContent.getQuestions().size()==0){
                        textContentScrollView.addView(pureListenControl);
                        pureListenControl.displayListenContent(listenContent, currentFileName);
                    }else{
                        textContentScrollView.addView(listenExerciseControl);
                        listenExerciseControl.displayListenContent(listenContent, currentFileName);
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            Utils.Log(e);
        }
    }

    private void updateTitle(){
        ListenContent listenContent = ListenContentController.getInstance().getCurrentListenContent();
        String textDisplay = "";
        int currentListSize = DataController.getInstance().getCurrentListSize();
        int currentFileIndex = DataController.getInstance().getCurrentFileIndex();
        if(listenContent.isHasQuestion()){
            textDisplay = String.format(getString(R.string.listen_and_exercise_title_format),currentFileIndex+1, currentListSize);
        }else{
            textDisplay = String.format(getString(R.string.listen_title_format),currentFileIndex+1, currentListSize);
        }
        questionHeaderTextView.setText(textDisplay);
    }

    public void onLoadAudioDone(int duration) {
        showCurrentPosition();
    }

    public void onDownloadFail(HttpDownloadController.DownloadFailReason reason, String message) {
        closeLoadingDialog();
        this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListenActivity.this);
                alertDialogBuilder
                        .setTitle(R.string.download_fail)
                        .setMessage(R.string.download_fail_exit)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ListenActivity.this.finish();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    public void onLoadAudioError(String message) {
    }

    public void onLoadAudioBuffering(int percent) {
        customSeekBar.setBufferPercent(percent);
    }

    public void onPrevious(View view){
        if(DataController.getInstance().goPreviousItem()){
            loadData();
        }else{
            showMessage(R.string.first_question_warning);
        }
    }

    public void onPlay(View view){
        try {
            audioMediaPlayer.togglePlay();
        } catch (Exception e) {
            Utils.Log(e);
        }
    }

    public void onNext(View view){
        if(DataController.getInstance().goNextItem()){
            loadData();
        }else{
            showMessage(R.string.last_question_warning);
        }
    }

    public void finish() {
        if (isNeedShowAds){
            isNeedShowAds = false;
            loadFullAds();
            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdFailedToLoad(int errorCode) {
                    ListenActivity.this.finish();
                }
                public void onAdLoaded() {
                    mInterstitialAd.show();
                }
                public void onAdClosed(){
                    ListenActivity.this.finish();
                }
            });
        }else {
            super.finish();
        }
    }

    public void onDestroy() {
        if (audioMediaPlayer != null) {
            final AudioMediaPlayer audioMediaPlayer = this.audioMediaPlayer;
            new Thread(new Runnable() {
                public void run() {
                    audioMediaPlayer.release();
                }
            }).start();
        }
        super.onDestroy();
    }

    public void onPause() {
        if(audioMediaPlayer!=null){
            audioMediaPlayer.requestPause();
            audioMediaPlayer.pause();
        }
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        if(audioMediaPlayer!=null){
            audioMediaPlayer.requestResume();
        }
    }

    private void showCurrentPosition(){
        new Thread(new Runnable() {
            public void run() {
                while (audioMediaPlayer!=null){
                    updateAudioProgress();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private synchronized void updateAudioProgress() {
        if( audioMediaPlayer != null ){
            final int currentPosition = audioMediaPlayer.getCurrentPosition();
            final int duration = audioMediaPlayer.getDuration();

            if(duration >0){
                ListenActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        customSeekBar.setPercent(100 * currentPosition / duration);
                        customMediaControl.setPlayState(audioMediaPlayer.isPlaying());
                    }
                });
            }
        }
    }
}
