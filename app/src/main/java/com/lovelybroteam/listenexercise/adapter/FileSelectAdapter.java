package com.lovelybroteam.listenexercise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lovelybroteam.listenexercise.R;
import com.lovelybroteam.listenexercise.controller.UserResultController;
import com.lovelybroteam.listenexercise.model.DataItem;
import com.lovelybroteam.listenexercise.model.UserResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
/**
 * Created by Vo Quang Hoa on 12/20/2015.
 */
public class FileSelectAdapter extends BaseAdapter {
    private Context context;
    private DataItem dataItem;
    private List<DataItem> children;
    private Stack<DataItem> pathStack;
    private FileSelectFeedback selectFeedback;
    public FileSelectAdapter(Context context, DataItem rootItem, DataItem brandItem,  FileSelectFeedback selectFeedback){
        this.context = context;
        this.pathStack = new Stack<>();
        this.pathStack.push(rootItem);
        this.selectFeedback = selectFeedback;
        setDisplayDataItem(brandItem);
    }

    public boolean showParent(){
        if(pathStack.size()>1){
            setDisplayDataItem(pathStack.pop());
            return true;
        }
        return false;
    }

    public void setDisplayDataItem(DataItem dataItem){
        this.dataItem = dataItem;
        this.children = dataItem.getChildren();
        if(this.children == null){
            this.children = new ArrayList<>();
        }
        this.notifyDataSetChanged();
    }

    public int getCount() {
        return children.size();
    }

    public Object getItem(int position) {
        return children.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final DataItem rowDataItem = (DataItem) getItem(position);

        convertView = createView(rowDataItem);

        TextView button =  (TextView)convertView.findViewById(R.id.button_main_menu);
        View.OnClickListener onClickListener = new View.OnClickListener(){
            public void onClick(View v) {
                List<DataItem> localChildren = rowDataItem.getChildren();

                if(localChildren!=null && localChildren.size()>0){
                    FileSelectAdapter.this.pathStack.push(dataItem);
                    FileSelectAdapter.this.setDisplayDataItem(rowDataItem);
                }else if(selectFeedback!=null){
                    if(rowDataItem.isFileTest()){
                        selectFeedback.openFile(getCurrentPath(), dataItem, rowDataItem);
                    }
                }
            }
        };

        if(rowDataItem.isFileTest()){
            setUserResult(rowDataItem, convertView);
        }

        button.setOnClickListener(onClickListener);
        convertView.setOnClickListener(onClickListener);

        button.setText(rowDataItem.getDisplay());
        return convertView;
    }

    private void setUserResult(DataItem dataItem, View displayView){
        String filePath = getCurrentPath() + dataItem.getFileName();
        UserResult userResult = UserResultController.getInstance().getResult(filePath);
        TextView tvResult = (TextView)displayView.findViewById(R.id.text_badge);

        if(userResult == null){
            tvResult.setVisibility(View.INVISIBLE);
        }else{
            tvResult.setVisibility(View.VISIBLE);
            if(userResult.getCorrect()*2 >= userResult.getTotal()){
                tvResult.setBackgroundResource(R.drawable.green_badge);
            }
            else {
                tvResult.setBackgroundResource(R.drawable.gray_badge);
            }

            tvResult.setText(userResult.getDisplay());
        }
    }

    private String getCurrentPath(){
        StringBuilder sb = new StringBuilder();
        for(int i=1; i<pathStack.size(); i++){
            sb.append(pathStack.get(i).getFileName()).append("/");
        }
        sb.append(dataItem.getFileName()).append("/");
        return sb.toString();
    }

    private View createView(DataItem dataItem){
        //NOTED : Implement pool object if any issue relative to memory leak or performance here
        int layout =  R.layout.data_item_folder_layout;

        if(dataItem.isFileTest()){
            layout = R.layout.data_item_file_test;
        }

        return LayoutInflater.from(context).inflate(layout, null);
    }

    public interface FileSelectFeedback {
        void openFile(String folderPath, DataItem folder, DataItem file);
    }
}
