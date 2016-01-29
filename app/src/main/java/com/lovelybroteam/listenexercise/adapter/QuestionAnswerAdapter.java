package com.lovelybroteam.listenexercise.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lovelybroteam.listenexercise.R;
import com.lovelybroteam.listenexercise.control.AnswerRadioButton;
import com.lovelybroteam.listenexercise.model.ListenContent;
import com.lovelybroteam.listenexercise.model.Question;

import java.util.List;

/**
 * Created by Vo Quang Hoa on 12/22/2015.
 */
public class QuestionAnswerAdapter extends BaseAdapter implements AnswerRadioButton.OnCheckedChangeListener {
    private Context context;
    private ListenContent listenContent;
    private boolean showAnswer = false;
    private int[] userSelection;
    private Drawable correctDrawable;
    private Drawable incorrectDrawable;
    private Drawable unselectedDrawable;
    private int[] radioButtonId = new int[]{
            R.id.answer_a,
            R.id.answer_b,
            R.id.answer_c,
            R.id.answer_d
    };

    public QuestionAnswerAdapter(Context context) {
        this.context = context;
    }

    public void setListenContent(ListenContent listenContent){
        this.listenContent = listenContent;
        initUserSelect();
    }

    private void initUserSelect(){
        this.userSelection = new int[listenContent.getQuestions().size()];
        this.clearUserSelect();
    }

    public void clearUserSelect(){
        for(int i=0;i<this.userSelection.length;i++){
            this.userSelection[i] = -1;
        }
    }

    public int getTotal(){
        return this.userSelection.length;
    }

    public int getCorrects(){
        int correct =0;
        for(int i=0;i<getTotal();i++){
            if(userSelection[i] == listenContent.getQuestions().get(i).getCorrectAnswer()){
                correct ++;
            }
        }
        return correct;
    }

    public String getResultAsString() {
        return String.format(this.context.getString(R.string.dialog_result_content), getCorrects(), getTotal());
    }

    public boolean isShowAnswer() {
        return showAnswer;
    }

    public void setShowAnswer(boolean showAnswer){
        this.showAnswer = showAnswer;
        this.notifyDataSetChanged();
    }

    public int getCount() {
        return listenContent.getQuestions().size();
    }

    public Object getItem(int position) {
        return listenContent.getQuestions().get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Question question = listenContent.getQuestions().get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.question_answer_layout, parent, false);
        }

        TextView tvQuestion = (TextView)convertView.findViewById(R.id.question);
        RadioGroup radioGroup = (RadioGroup)convertView.findViewById(R.id.answer_group);
        AnswerRadioButton[] radioButtons = getRadioButtons(convertView);

        setRadioOnChanged(radioButtons, false);
        setRadioValue(userSelection[position], radioButtons);
        setRadioTag(radioGroup, radioButtons, position);

        radioGroup.setEnabled(!showAnswer);
        setQuestionText(question, tvQuestion, position);
        setAnswerText(radioButtons, question);
        setRadioOnChanged(radioButtons, true);
        return convertView;
    }

    private AnswerRadioButton[] getRadioButtons(View convertView) {
        return new AnswerRadioButton[]{
                    (AnswerRadioButton)convertView.findViewById(R.id.answer_a),
                    (AnswerRadioButton)convertView.findViewById(R.id.answer_b),
                    (AnswerRadioButton)convertView.findViewById(R.id.answer_c),
                    (AnswerRadioButton)convertView.findViewById(R.id.answer_d)
            };
    }

    private void setRadioValue(int selectedIndex, AnswerRadioButton[] answerRadioButtons) {
        for(int i=0;i<answerRadioButtons.length;i++){
            answerRadioButtons[i].setChecked(i==selectedIndex);
        }
    }

    private void setRadioTag(RadioGroup radioGroups, AnswerRadioButton[] radioButtons, int questionId){
        radioGroups.setTag(questionId);
        for (AnswerRadioButton radioButton : radioButtons) {
            radioButton.setTag(questionId);
        }
    }

    private void setRadioOnChanged(AnswerRadioButton[] radioButtons, boolean isSet){
        for (AnswerRadioButton radioButton : radioButtons) {
            radioButton.setOnCheckedChangeListener(isSet ? this : null);
        }
    }

    private void setAnswerText(AnswerRadioButton[] radioButtons,Question question){
        List<String> answers = question.getAnwers();
        for(int i=0;i<radioButtons.length;i++){
            AnswerRadioButton radioButton = radioButtons[i];
            if(answers.size() > i){
                radioButton.setText(answers.get(i));
                radioButton.setIsCorrect(question.getCorrectAnswer() == i);
                radioButton.setEnabled(!showAnswer);
                radioButton.updateIcon();
                radioButton.updateTextStyle();
                radioButton.setVisibility(View.VISIBLE);
            }else{
                radioButton.setVisibility(View.GONE);
            }
        }
    }

    private void setQuestionText(Question question, TextView tvQuestion, int position) {
        String questionTitle = question.getQuestionTitle();

        if(questionTitle.length() == 0 || !Character.isDigit(questionTitle.charAt(0))){
            questionTitle = (1+position) +". " + questionTitle;
        }

        tvQuestion.setText(questionTitle);
    }

    public void onCheckedChanged(AnswerRadioButton buttonView, boolean isChecked) {
        if(buttonView.isChecked()){
            int questionIndex = (int)buttonView.getTag();
            int viewId = buttonView.getId();

            for(int id=0;id<radioButtonId.length;id++) {
                if (viewId == radioButtonId[id]) {
                    userSelection[questionIndex] = id;
                }
            }
        }
    }
}
