package com.lovelybroteam.listenexercise.control;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovelybroteam.listenexercise.R;

/**
 * Created by Vo Quang Hoa on 1/4/2016.
 */
public class AppTitleControl extends RelativeLayout {
    private TextView titleTextView;
    private EffectImageView appTitleBackButton;
    public AppTitleControl(Context context) {
        super(context);
        initView();
    }

    public AppTitleControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AppTitleControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.app_title_layout_new, this, true);
        titleTextView = (TextView) findViewById(R.id.txt_title);
        appTitleBackButton = (EffectImageView)findViewById(R.id.app_title_back);
        appTitleBackButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                ((Activity)getContext()).finish();
            }
        });
    }

    public void setTitle(String title){
        titleTextView.setText(title);
    }
}
