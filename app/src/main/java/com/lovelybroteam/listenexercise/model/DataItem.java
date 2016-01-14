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

    public int getSize(){
        return children!=null? children.size():0;
    }

    public int getDataItemIndex(DataItem dataItem){
        if(children!=null){
            for(int i=0;i<children.size();i++){
                if(children.get(i)==dataItem){
                    return i;
                }
            }
        }
        return -1;
    }

    public DataItem getNextItem(DataItem dataItem){
        int index = getDataItemIndex(dataItem);

        if(children!=null && index< children.size()){
            return children.get(index + 1);
        }

        return null;
    }

    public DataItem getPreviousItem(DataItem dataItem){
        int index = getDataItemIndex(dataItem);

        if(index>0){
            return children.get(index-1);
        }

        return null;
    }
}
