package com.lovelybroteam.listenexercise.player;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.lovelybroteam.listenexercise.api.IAudioMediaPlayerListener;
import com.lovelybroteam.listenexercise.constant.AppConstant;
import com.lovelybroteam.listenexercise.util.Utils;

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
        try {
            return isReady ? mediaPlayer.getDuration() : 0;
        }catch (IllegalStateException exception){
            Utils.Log(exception);
            return 0;
        }
    }

    public synchronized void onPrepared(MediaPlayer mp) {
        audioMediaPlayerListener.onLoadAudioDone(mp.getDuration());
        if (!isRelease) {
            isReady = true;
            mp.start();
        }else{
            try {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }catch (Exception ex){
                Utils.Log(ex);
            }finally {
                mediaPlayer = null;
            }
        }
    }

    public void pause(){
        if(isReady && !isRelease) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }
    }

    public void togglePlay() throws Exception {
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

    public synchronized void release() {
        try {
            if (isReady && !isRelease && mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }catch (Exception ex){
            Utils.Log(ex);
        }finally {
            isRelease = true;
            isReady = false;
        }
    }

    protected void finalize() throws Throwable {
        release();
        super.finalize();
    }

    public synchronized boolean isPlaying(){
        if(mediaPlayer != null && isReady){
            return mediaPlayer.isPlaying();
        }else {
            return false;
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        if(what>0){
            audioMediaPlayerListener.onLoadAudioError("");
            isError = true;
        }

        return false;
    }

    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        audioMediaPlayerListener.onLoadAudioBuffering(percent);
    }

    public int getCurrentPosition(){
        try {
            if(mediaPlayer == null || !isReady || isRelease){
                return 0;
            }
            return mediaPlayer.getCurrentPosition();
        }catch (IllegalStateException exception){
            Utils.Log(exception);
            return 0;
        }
    }
}
