package com.lovelybroteam.listenexercise.control;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovelybroteam.listenexercise.R;
import com.lovelybroteam.listenexercise.api.IListenControl;
import com.lovelybroteam.listenexercise.model.ListenContent;

/**
 * Created by Vo Quang Hoa on 1/12/2016.
 */
public class PureListenControl extends RelativeLayout implements IListenControl, View.OnClickListener {
    private TextView scriptTextView;
    private EffectImageView effectImageViewSubmit;

    public PureListenControl(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.listen_pure_layout, this, true);
        scriptTextView = (TextView)findViewById(R.id.test_content);
        effectImageViewSubmit = (EffectImageView)findViewById(R.id.button_submit);
        effectImageViewSubmit.setOnClickListener(this);
    }

    public void displayListenContent(ListenContent listenContent){
        scriptTextView.setText(listenContent.getScript());
    }

    public void onClick(View v) {
        ((Activity)getContext()).finish();
    }
}
