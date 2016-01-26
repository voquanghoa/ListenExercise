package com.lovelybroteam.listenexercise.control;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.lovelybroteam.listenexercise.R;
import com.lovelybroteam.listenexercise.adapter.QuestionAnswerAdapter;
import com.lovelybroteam.listenexercise.constant.AppConstant;
import com.lovelybroteam.listenexercise.controller.UserResultController;
import com.lovelybroteam.listenexercise.model.ListenContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vo Quang Hoa on 1/12/2016.
 */
public class ListenExerciseControl extends ListenControl{
    private LinearLayout questionListView;
    private QuestionAnswerAdapter questionAnswerAdapter;
    private List<View> questionViews;
    private ListenContent listenContent;
    private EffectImageView effectImageViewSubmit;
    private String currentFileName;
    private InternetImageView internetImageView;

    public ListenExerciseControl(Context context) {
        super(context);
        setContentView(R.layout.listen_exercise_child_layout);
        questionListView = (LinearLayout)findViewById(R.id.question_list_view);
        internetImageView = (InternetImageView)findViewById(R.id.picture_view);

        questionViews= new ArrayList<View>();
        effectImageViewSubmit = (EffectImageView)findViewById(R.id.button_submit);
        effectImageViewSubmit.setOnClickListener(this);
        effectImageViewSubmit.setActivated(false);
        questionAnswerAdapter = new QuestionAnswerAdapter(getContext());
        showConversationDialog(false);
        internetImageView.setVisibility(GONE);
    }

    public void displayListenContent(ListenContent listenContent, String filePath) {
        this.listenContent = listenContent;
        showConversationDialog(false);
        currentFileName = filePath;
        questionAnswerAdapter.setShowAnswer(false);
        questionAnswerAdapter.setListenContent(listenContent);
        effectImageViewSubmit.setActivated(false);
        refreshView();
    }

    public void refreshView() {
        displayScriptText(listenContent.getScript());
        ((ScrollView)this.getParent()).scrollTo(0, 0);
        questionListView.removeAllViews();
        internetImageView.setUrl(currentFileName+ AppConstant.PICTURE_FILE_EXTENSION);

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
            super.onClick(v);
        }else{
            effectImageViewSubmit.setActivated(true);
            questionAnswerAdapter.setShowAnswer(true);
            showConversationDialog(true);
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
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
