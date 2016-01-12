package com.lovelybroteam.listenexercise.controller;


import com.google.gson.Gson;
import com.lovelybroteam.listenexercise.model.ListenContent;

/**
 * Created by Vo Quang Hoa on 1/12/2016.
 */
public class ListenContentController {
    private static ListenContentController instance;
    public static synchronized ListenContentController getInstance(){
        if(instance == null){
            instance = new ListenContentController();
        }
        return instance;
    }

    private ListenContentController(){

    }

    private ListenContent currentListenContent;

    public ListenContent getCurrentListenContent() {
        return currentListenContent;
    }

    public void loadJson(String jsonData){
        currentListenContent = new Gson().fromJson(jsonData, ListenContent.class);
    }
}
