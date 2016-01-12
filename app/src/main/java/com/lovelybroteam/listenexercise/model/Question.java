package com.lovelybroteam.listenexercise.model;

import java.util.List;

/**
 * Created by Vo Quang Hoa on 1/12/2016.
 */
public class Question {
    private String questionTitle;
    private List<String> anwers;
    private int correctAnswer;

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public List<String> getAnwers() {
        return anwers;
    }

    public void setAnwers(List<String> anwers) {
        this.anwers = anwers;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
