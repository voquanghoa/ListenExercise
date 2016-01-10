package com.lovelybroteam.listenexercise;

import android.widget.TextView;

import com.lovelybroteam.listenexercise.control.ListenActivityBase;

/**
 * Created by Vo Quang Hoa on 12/28/2015.
 */
public class ListenActivity extends ListenActivityBase {
    private TextView _textView;

    protected void initViewElements(){
        super.initViewElements();
        _textView = (TextView)findViewById(R.id.test_content);
    }

    protected int getChildView() {
        return R.layout.common_communication_child_layout;
    }

    protected void showTextContent(String content) {
        _textView.setText(content);
    }
}
