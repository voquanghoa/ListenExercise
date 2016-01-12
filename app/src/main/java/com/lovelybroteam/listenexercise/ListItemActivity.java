package com.lovelybroteam.listenexercise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.lovelybroteam.listenexercise.adapter.FileSelectAdapter;
import com.lovelybroteam.listenexercise.control.BaseActivity;
import com.lovelybroteam.listenexercise.controller.DataController;
import com.lovelybroteam.listenexercise.controller.UserResultController;
import com.lovelybroteam.listenexercise.model.DataItem;

/**
 * Created by Vo Quang Hoa on 1/7/2016.
 */
public class ListItemActivity extends BaseActivity implements FileSelectAdapter.FileSelectFeedback {
    private FileSelectAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_select_layout);
        adapter = new FileSelectAdapter(this, DataController.getInstance().getDataItem(),
                DataController.getInstance().getCurrentDataItem(), this);
        ((ListView)findViewById(R.id.list_item)).setAdapter(adapter);
    }

    public void onBackPressed() {
        if(!adapter.showParent()){
            super.onBackPressed();
        }
    }

    public void onResume() {
        super.onResume();
        if(UserResultController.getInstance().isRequestUpdate()){
            adapter.notifyDataSetChanged();
        }
    }

    public void openFile(String folderPath, DataItem folder, DataItem file) {
        DataController.getInstance().setCurrentShowDataFolder(folder);
        DataController.getInstance().setCurrentShowDataItem(file);
        DataController.getInstance().setCurrentShowFolderPath(folderPath);
        startActivity(new Intent(this, ListenActivity.class));
    }
}
