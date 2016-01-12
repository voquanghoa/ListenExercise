package com.lovelybroteam.listenexercise.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovelybroteam.listenexercise.R;
import com.lovelybroteam.listenexercise.api.IListenControl;
import com.lovelybroteam.listenexercise.model.ListenContent;

/**
 * Created by Vo Quang Hoa on 1/12/2016.
 */
public class PureListenControl extends RelativeLayout implements IListenControl {

    private TextView scriptTextView;

    public PureListenControl(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.listen_pure_layout, this, true);
        scriptTextView = (TextView)findViewById(R.id.test_content);
    }

    public void displayListenContent(ListenContent listenContent){
        scriptTextView.setText(listenContent.getScript());
    }
}
