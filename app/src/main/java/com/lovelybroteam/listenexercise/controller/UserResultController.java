package com.lovelybroteam.listenexercise.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lovelybroteam.listenexercise.constant.AppConstant;
import com.lovelybroteam.listenexercise.model.UserResult;
import com.lovelybroteam.listenexercise.util.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Vo Quang Hoa on 12/26/2015.
 */
public class UserResultController implements AppConstant {
    private static UserResultController instance;
    public static synchronized UserResultController getInstance(){
        if(instance==null){
            instance = new UserResultController();
        }
        return instance;
    }
    private ArrayList<UserResult> userResults;
    private UserResultController(){
        userResults = new ArrayList<>();
    }

    private boolean isUpdated = false;
    public boolean isRequestUpdate(){
        boolean needUpdate = isUpdated;
        isUpdated = false;
        return needUpdate;
    }

    public void setResult(String fileName, int correct, int total){
        UserResult userResult = getResult(fileName);

        if(userResult==null){
            userResult = new UserResult();
            userResult.setFileName(fileName);
            userResults.add(userResult);
        }

        userResult.setCorrect(correct);
        userResult.setTotal(total);
        save();
        isUpdated = true;
    }

    public UserResult getResult(String fileName){
        for (UserResult userResult : userResults){
            if(userResult.getFileName().equals(fileName)){
                return userResult;
            }
        }
        return null;
    }

    public synchronized void load(){
        try {
            String fileData = FileStoreController.getInstance().readFile(USER_RESULT_FILE);
            Type listType = new TypeToken<ArrayList<UserResult>>(){}.getType();
            userResults = new Gson().fromJson(fileData, listType);
        } catch (Exception e) {
            Utils.Log(e);
        }finally {
            if(userResults==null){
                userResults = new ArrayList<>();
            }
        }
    }

    public synchronized void save(){
        try {
            String fileData = new Gson().toJson(userResults);
            FileStoreController.getInstance().writeFile(USER_RESULT_FILE, fileData);
        } catch (Exception e) {
            Utils.Log(e);
        }
    }
}
