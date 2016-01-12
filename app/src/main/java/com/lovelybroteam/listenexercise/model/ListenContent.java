package com.lovelybroteam.listenexercise.model;

import java.util.List;

/**
 * Created by Vo Quang Hoa on 1/12/2016.
 */
public class ListenContent {
    private List<Question> questions;
    private String script;

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
