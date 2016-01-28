package com.lovelybroteam.listenexercise.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lovelybroteam.listenexercise.R;
import com.lovelybroteam.listenexercise.controller.QuestionHelper;
import com.lovelybroteam.listenexercise.model.ListenContent;
import com.lovelybroteam.listenexercise.model.Question;
import com.lovelybroteam.listenexercise.util.GraphicUtil;

import java.util.List;

/**
 * Created by Vo Quang Hoa on 12/22/2015.
 */
public class QuestionAnswerAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
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
        Resources resources = context.getResources();
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                resources.getDimension(R.dimen.radio_button_result_size),
                resources.getDisplayMetrics());
        correctDrawable = resize(GraphicUtil.getDrawable(resources, R.drawable.radio_button_correct), px);
        incorrectDrawable = resize(GraphicUtil.getDrawable(resources, R.drawable.radio_button_incorrect), px);
        unselectedDrawable = resize(GraphicUtil.getDrawable(resources, R.drawable.radio_button_not_select), px);
    }

    public void setListenContent(ListenContent listenContent){
        this.listenContent = listenContent;
        initUserSelect();
    }

    private void initUserSelect(){
        this.userSelection = new int[listenContent.getQuestions().size()];
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
        RadioButton[] radioButtons = getRadioButtons(convertView);

        setRadioOnChanged(radioButtons, false);
        setRadioValue(userSelection[position], radioGroup);
        setRadioTag(radioGroup, radioButtons, position);

        radioGroup.setEnabled(!showAnswer);
        setRadioButtonEnable(radioButtons);
        setQuestionText(question, tvQuestion, position);
        setAnswerText(radioButtons, question);
        setRadioOnChanged(radioButtons, true);
        return convertView;
    }

    private RadioButton[] getRadioButtons(View convertView) {
        return new RadioButton[]{
                    (RadioButton)convertView.findViewById(R.id.answer_a),
                    (RadioButton)convertView.findViewById(R.id.answer_b),
                    (RadioButton)convertView.findViewById(R.id.answer_c),
                    (RadioButton)convertView.findViewById(R.id.answer_d)
            };
    }

    private void setRadioValue(int i, RadioGroup radioGroup) {
        if(i ==-1){
            radioGroup.check(-1);
        }else{
            radioGroup.check(radioButtonId[i]);
        }
    }

    private void setRadioTag(RadioGroup radioGroups, RadioButton[] radioButtons, int questionId){
        radioGroups.setTag(questionId);
        for (RadioButton radioButton : radioButtons) {
            radioButton.setTag(questionId);
        }
    }

    private void setRadioOnChanged(RadioButton[] radioButtons, boolean isSet){
        for (RadioButton radioButton : radioButtons) {
            radioButton.setOnCheckedChangeListener(isSet ? this : null);
        }
    }

    private void setAnswerText(RadioButton[] radioButtons,Question question){
        List<String> answers = question.getAnwers();
        for(int i=0;i<radioButtons.length;i++){
            RadioButton radioButton = radioButtons[i];
            radioButton.setPaintFlags(radioButton.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            if(answers.size() > i){
                String answerText = answers.get(i).replaceAll("^\\([ABCD]\\)","");
                if(showAnswer){
                    String textDisplay = "&nbsp;" +answerText;
                    String colorCode = "white";
                    if(i==question.getCorrectAnswer()){
                        colorCode = "yellow";
                        radioButton.setButtonDrawable(correctDrawable);
                    }else{
                        if(radioButton.isChecked()){
                            colorCode = "red";
                            radioButton.setPaintFlags(radioButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            radioButton.setButtonDrawable(incorrectDrawable);
                        }else{
                            radioButton.setButtonDrawable(unselectedDrawable);
                        }
                    }
                    textDisplay = QuestionHelper.convertToColor(textDisplay, colorCode);
                    radioButton.setText(Html.fromHtml(textDisplay));
                }else{
                    radioButton.setButtonDrawable(R.drawable.radio_question_normal);
                    radioButton.setText(" "+answerText);
                }

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

    private void setRadioButtonEnable(RadioButton[] radioButtons){
        for(RadioButton radioButton: radioButtons){
            radioButton.setEnabled(!showAnswer);
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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

    private Drawable resize(Drawable image, int dpSize) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, dpSize, dpSize, false);
        return new BitmapDrawable(context.getResources(), bitmapResized);
    }
}
