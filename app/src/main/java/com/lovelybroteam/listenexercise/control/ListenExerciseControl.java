package com.lovelybroteam.listenexercise.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovelybroteam.listenexercise.R;
import com.lovelybroteam.listenexercise.adapter.QuestionAnswerAdapter;
import com.lovelybroteam.listenexercise.api.IListenControl;
import com.lovelybroteam.listenexercise.model.ListenContent;

/**
 * Created by Vo Quang Hoa on 1/12/2016.
 */
public class ListenExerciseControl extends RelativeLayout implements IListenControl {
    private TextView scriptTextView;
    private LinearLayout questionListView;
    private QuestionAnswerAdapter questionAnswerAdapter;

    public ListenExerciseControl(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.listen_exercise_child_layout, this, true);
        scriptTextView = (TextView) findViewById(R.id.test_content);
        questionListView = (LinearLayout)findViewById(R.id.question_list_view);
    }

    public void displayListenContent(ListenContent listenContent) {
        scriptTextView.setText(listenContent.getScript());
        questionAnswerAdapter = new QuestionAnswerAdapter(getContext(), listenContent);

        for(int i=0, j=questionAnswerAdapter.getCount();i<j;i++){
            questionListView.addView(questionAnswerAdapter.getView(i, null, null));
        }
    }
}
