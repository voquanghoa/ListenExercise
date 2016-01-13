package com.lovelybroteam.listenexercise.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovelybroteam.listenexercise.R;
public class AppTitle extends RelativeLayout {
    private TextView centerTitle;
    private TextView leftTitle;

    public AppTitle(Context context) {
        super(context);
        initView();
    }

    public AppTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AppTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.app_title, this, true);


        centerTitle = (TextView) findViewById(R.id.center_title);
        leftTitle = (TextView) findViewById(R.id.left_title);
    }

    public void setCenterTitleText(String text) {
        centerTitle.setText(text);
    }

    public void setLeftTitleText(String text) {
        leftTitle.setText(text);
    }
}
