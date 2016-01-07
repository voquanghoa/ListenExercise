package com.lovelybroteam.listenexercise.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lovelybroteam.listenexercise.constant.AppConstant;
import com.lovelybroteam.listenexercise.model.DataItem;

import java.lang.reflect.Type;

/**
 * Created by voqua on 12/20/2015.
 */
public class DataController implements AppConstant {

    private static DataController instance;
    public static synchronized DataController getInstance(){
        if(instance == null){
            instance = new DataController();
        }
        return instance;
    }
    private DataController(){}

    private DataItem dataItem;
    private DataItem currentDataItem;

    private DataItem currentShowDataItem;
    private DataItem currentShowDataFolder;
    private String currentShowFolderPath;

    public void loadDataItem(String serverJsonData) {
        Type listType = new TypeToken<DataItem>(){}.getType();
        dataItem = new Gson().fromJson(serverJsonData, listType);
    }

    public DataItem getDataItem() {
        return dataItem;
    }

    public DataItem getCurrentDataItem() {
        return currentDataItem;
    }

    public void setCurrentDataItem(String currentDataItemName) {
        if (dataItem != null){
            currentDataItem = dataItem.getChild(currentDataItemName);
        }
    }


    public DataItem getCurrentShowDataItem() {
        return currentShowDataItem;
    }

    public void setCurrentShowDataItem(DataItem currentShowDataItem) {
        this.currentShowDataItem = currentShowDataItem;
    }

    public DataItem getCurrentShowDataFolder() {
        return currentShowDataFolder;
    }

    public void setCurrentShowDataFolder(DataItem currentShowDataFolder) {
        this.currentShowDataFolder = currentShowDataFolder;
    }

    public String getCurrentShowFolderPath() {
        return currentShowFolderPath;
    }

    public void setCurrentShowFolderPath(String currentShowFolderPath) {
        this.currentShowFolderPath = currentShowFolderPath;
    }
}
