package com.lovelybroteam.listenexercise.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.lovelybroteam.listenexercise.R;

/**
 * Created by Vo Quang Hoa on 1/4/2016.
 */
public class CustomMediaControl extends RelativeLayout{
    private EffectImageView playButton;

    public CustomMediaControl(Context context) {
        super(context);
        initView();
    }

    public CustomMediaControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomMediaControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.media_control_layout, this, true);
        playButton = (EffectImageView) findViewById(R.id.button_play);
    }

    public void setPlayState(boolean isPlaying){
        if( playButton.isActivated() != isPlaying){
            playButton.setActivated(isPlaying);
        }
    }
}
