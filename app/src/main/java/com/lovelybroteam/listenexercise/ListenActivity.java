package com.lovelybroteam.listenexercise;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.lovelybroteam.listenexercise.api.IAudioMediaPlayerListener;
import com.lovelybroteam.listenexercise.control.BaseActivity;
import com.lovelybroteam.listenexercise.control.CustomMediaControl;
import com.lovelybroteam.listenexercise.control.CustomSeekBar;
import com.lovelybroteam.listenexercise.control.ListenExerciseControl;
import com.lovelybroteam.listenexercise.control.PureListenControl;
import com.lovelybroteam.listenexercise.controller.AssertDataController;
import com.lovelybroteam.listenexercise.controller.DataController;
import com.lovelybroteam.listenexercise.controller.ListenContentController;
import com.lovelybroteam.listenexercise.model.DataItem;
import com.lovelybroteam.listenexercise.model.ListenContent;
import com.lovelybroteam.listenexercise.player.AudioMediaPlayer;
import com.lovelybroteam.listenexercise.util.Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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

    private TextView questionHeaderTextView;
    private ArrayList<AudioMediaPlayer> audioMediaPlayerArrayList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isNeedShowAds = Utils.checkAds();
        audioMediaPlayerArrayList = new ArrayList<>();
        setContentView(R.layout.listen_activity_layout);
        initViewElements();
        loadData();
    }

    protected void initViewElements(){
        questionHeaderTextView = (TextView)findViewById(R.id.text_question_title);
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
        customMediaControl.setPlayState(false);
        appTitleControl.setTitle(dataItem.getDisplay());
        customSeekBar.setPercent(0);
        customSeekBar.setBufferPercent(0);
        reInitAudioMediaPlayer();
        try {
            audioMediaPlayer.load(folder + dataItem.getFileName() + AUDIO_FILE_EXTENSION);
        } catch (IOException e) {
            Utils.Log(e);
        }
        loadTextContext(folder + dataItem.getFileName());
    }

    private void reInitAudioMediaPlayer() {
        try {
            if (audioMediaPlayer != null) {
                audioMediaPlayer.release();
            }
        }catch (Exception ex){
            Utils.Log(ex);
        }finally {
            if(audioMediaPlayer!=null){
                audioMediaPlayerArrayList.add(audioMediaPlayer);
            }
            audioMediaPlayer = new AudioMediaPlayer(this);
        }
    }

    public void loadTextContext(final String currentFileName) {

        try {
            final String content = AssertDataController.getInstance().loadFile(this, currentFileName+TEXT_FILE_EXTENSION);
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
            showMessage(e.getMessage());
        } catch (IOException e) {
            Utils.Log(e);
            showMessage(e.getMessage());
        }
    }

    private void updateTitle(){
        ListenContent listenContent = ListenContentController.getInstance().getCurrentListenContent();
        int currentListSize = DataController.getInstance().getCurrentListSize();
        int currentFileIndex = DataController.getInstance().getCurrentFileIndex();
        if(listenContent.isHasQuestion()){
            questionHeaderTextView.setText(String.format(getString(R.string.listen_and_exercise_title_format), currentFileIndex + 1, currentListSize));
        }else{
            questionHeaderTextView.setText(String.format(getString(R.string.listen_title_format),currentFileIndex+1, currentListSize));
        }
    }

    public void onLoadAudioDone(int duration) {
        showCurrentPosition();
    }

    public void onLoadAudioError() {
        showMessage(R.string.audio_loaded_error);
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
        } catch (AudioMediaPlayer.CouldNotLoadAudioException e) {
            showMessage(R.string.audio_loaded_error);
            Utils.Log(e);
        } catch (AudioMediaPlayer.BufferingNotFinishedException e) {
            showMessage(R.string.audio_loading_warning);
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
