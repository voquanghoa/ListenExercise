package com.lovelybroteam.listenexercise.api;

/**
 * Created by Vo Quang Hoa on 1/9/2016.
 */
public interface IAudioMediaPlayerListener {
    void onLoadAudioDone(int duration);
    void onLoadAudioError();
    void onLoadAudioBuffering(int percent);
}
