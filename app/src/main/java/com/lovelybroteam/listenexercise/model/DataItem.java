package com.lovelybroteam.listenexercise.model;

import java.util.ArrayList;
import java.util.List;


public class DataItem {
    private String fileName;
    private String display;
    private List<DataItem> children;

    public DataItem(){
        children = new ArrayList<>();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public List<DataItem> getChildren() {
        return children;
    }

    public void setChildren(List<DataItem> children) {
        this.children = children;
    }

    public boolean isFileTest(){
        return (children==null || children.size()==0);
    }

    public DataItem getChild(String childName){
        if( this.children != null){
            for (DataItem dataItem : this.getChildren()) {
                if (dataItem.getFileName().equals(childName)){
                    return dataItem;
                }
            }
        }
        return null;
    }
}
