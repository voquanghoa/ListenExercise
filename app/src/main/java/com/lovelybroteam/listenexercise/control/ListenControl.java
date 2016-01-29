package com.lovelybroteam.listenexercise.control;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovelybroteam.listenexercise.R;
import com.lovelybroteam.listenexercise.model.ListenContent;

/**
 * Created by Vo Quang Hoa on 1/26/2016.
 */
public abstract class ListenControl extends RelativeLayout implements View.OnClickListener {
    private TextView scriptTextView;
    private EffectImageView effectImageViewSubmit;
    private LinearLayout dialogTextLayout;

    public ListenControl(Context context) {
        super(context);
    }

    public ListenControl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListenControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void displayListenContent(ListenContent listenContent, String url){
        displayScriptText(listenContent.getScript());
    }

    protected void displayScriptText(String scriptText){
        scriptTextView.setText(scriptText);
    }

    protected void setContentView(int contentViewId){
        LayoutInflater.from(getContext()).inflate(contentViewId, this, true);
        scriptTextView = (TextView)findViewById(R.id.test_content);
        dialogTextLayout = (LinearLayout)findViewById(R.id.conversion_dialog);

        effectImageViewSubmit = (EffectImageView)findViewById(R.id.button_submit);
        effectImageViewSubmit.setOnClickListener(this);
        effectImageViewSubmit.setActivated(true);
    }

    protected void showConversationDialog(boolean isShow){
        if(isShow){
            dialogTextLayout.setVisibility(VISIBLE);
        }else{
            dialogTextLayout.setVisibility(GONE);
        }
    }

    public void onClick(View v) {
        ((Activity)getContext()).finish();
    }
}
