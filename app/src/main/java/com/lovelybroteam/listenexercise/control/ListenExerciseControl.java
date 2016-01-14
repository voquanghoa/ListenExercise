package com.lovelybroteam.listenexercise.control;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovelybroteam.listenexercise.R;
import com.lovelybroteam.listenexercise.adapter.QuestionAnswerAdapter;
import com.lovelybroteam.listenexercise.api.IListenControl;
import com.lovelybroteam.listenexercise.controller.UserResultController;
import com.lovelybroteam.listenexercise.model.ListenContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vo Quang Hoa on 1/12/2016.
 */
public class ListenExerciseControl extends RelativeLayout implements IListenControl, View.OnClickListener {
    private TextView scriptTextView;
    private LinearLayout questionListView;
    private QuestionAnswerAdapter questionAnswerAdapter;
    private List<View> questionViews;
    private ListenContent listenContent;
    private EffectImageView effectImageViewSubmit;
    private String currentFileName;

    public ListenExerciseControl(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.listen_exercise_child_layout, this, true);
        scriptTextView = (TextView) findViewById(R.id.test_content);
        questionListView = (LinearLayout)findViewById(R.id.question_list_view);
        questionViews= new ArrayList<View>();
        effectImageViewSubmit = (EffectImageView)findViewById(R.id.button_submit);
        effectImageViewSubmit.setOnClickListener(this);
        questionAnswerAdapter = new QuestionAnswerAdapter(getContext());
        scriptTextView.setVisibility(GONE);
    }

    public void displayListenContent(ListenContent listenContent, String filePath) {
        this.listenContent = listenContent;
        scriptTextView.setVisibility(GONE);
        currentFileName = filePath;
        questionAnswerAdapter.setShowAnswer(false);
        questionAnswerAdapter.setListenContent(listenContent);
        refreshView();
    }

    public void refreshView() {
        scriptTextView.setText(listenContent.getScript());
        questionListView.removeAllViews();

        for(int i=0, j=questionAnswerAdapter.getCount();i<j;i++){
            View view = null;
            if(questionViews.size()>i){
                view = questionAnswerAdapter.getView(i, questionViews.get(i), null);
            }else{
                view = questionAnswerAdapter.getView(i, null, null);
                questionViews.add(view);
            }
            questionListView.addView(view);
        }
    }

    public void onClick(View v) {
        if(questionAnswerAdapter.isShowAnswer()){
            ((Activity)getContext()).finish();
        }else{
            questionAnswerAdapter.setShowAnswer(true);
            scriptTextView.setVisibility(VISIBLE);
            showResult();
            UserResultController.getInstance().setResult(currentFileName,
                    questionAnswerAdapter.getCorrects(), questionAnswerAdapter.getTotal());
        }
    }

    public void showResult(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder
                .setTitle(R.string.dialog_result_title)
                .setMessage(questionAnswerAdapter.getResultAsString())
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                questionAnswerAdapter.setShowAnswer(true);
                                refreshView();
                                //effectImageViewSubmit.setText(R.string.finish);
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
