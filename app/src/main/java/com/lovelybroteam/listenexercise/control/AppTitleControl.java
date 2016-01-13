package com.lovelybroteam.listenexercise.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.lovelybroteam.listenexercise.R;

/**
 * Created by Vo Quang Hoa on 1/4/2016.
 */
public class AppTitleControl extends RelativeLayout {
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
    }
}
