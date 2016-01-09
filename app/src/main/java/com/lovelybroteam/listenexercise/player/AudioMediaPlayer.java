package com.lovelybroteam.listenexercise.player;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.lovelybroteam.listenexercise.api.IAudioMediaPlayerListener;
import com.lovelybroteam.listenexercise.constant.AppConstant;

import java.io.IOException;

/**
 * Created by Vo Quang Hoa on 1/9/2016.
 */
public class AudioMediaPlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnBufferingUpdateListener{

    private MediaPlayer mediaPlayer;
    private boolean isRelease = false;
    private boolean isReady = false;
    private boolean isError = false;
    private IAudioMediaPlayerListener audioMediaPlayerListener;

    public AudioMediaPlayer(IAudioMediaPlayerListener audioMediaPlayerListener){
        this.audioMediaPlayerListener = audioMediaPlayerListener;
    }

    public void load(String url) throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(AppConstant.SERVER_BASE_PATH + url);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.prepareAsync();
    }

    public int getDuration(){
        return mediaPlayer.getDuration();
    }

    public void onPrepared(MediaPlayer mp) {
        audioMediaPlayerListener.onLoadDone(mp.getDuration());
        if (!isRelease) {
            isReady = true;
            mp.start();
        }
    }

    public void toggePlay() throws Exception {
        if(isError){
            throw new Exception("Can not play the file");
        }
        if(isReady && !isRelease){
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }else {
                if(mediaPlayer.getDuration()<=mediaPlayer.getCurrentPosition()){
                    mediaPlayer.seekTo(0);
                }
                mediaPlayer.start();
            }
        }
    }

    public void seekTo(int percent){
        if(isReady){
            int newPosition = percent*mediaPlayer.getDuration()/100;
            mediaPlayer.seekTo(newPosition);
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }
        }
    }

    public void release() throws IOException {
        if(!isRelease){
            isRelease = true;
            mediaPlayer.prepare();
            mediaPlayer = null;
        }
    }

    protected void finalize() throws Throwable {
        release();
        super.finalize();
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        if(what>0){
            audioMediaPlayerListener.onLoadError("");
            isError = true;
        }

        return false;
    }

    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        audioMediaPlayerListener.onBuffer(percent);
    }

    public int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }
}
