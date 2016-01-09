package com.lovelybroteam.listenexercise.api;

/**
 * Created by Vo Quang Hoa on 1/9/2016.
 */
public interface IAudioMediaPlayerListener {
    void onLoadDone(int duration);
    void onLoadError(String message);
    void onBuffer(int percent);
}
