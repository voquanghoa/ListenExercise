package com.lovelybroteam.listenexercise.controller;

import android.content.Context;

import com.lovelybroteam.listenexercise.constant.AppConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Vo Quang Hoa on 2/18/2016.
 */
public class AssertDataController {
    private static AssertDataController instance;
    public synchronized static AssertDataController getInstance(){
        if(instance==null){
            instance = new AssertDataController();
        }
        return instance;
    }

    public String loadFile(Context context, String path) throws IOException {
        StringBuilder buf=new StringBuilder();
        InputStream json=context.getAssets().open(path);
        BufferedReader in= new BufferedReader(new InputStreamReader(json, AppConstant.CHARSET));
        String str;

        while ((str=in.readLine()) != null) {
            buf.append(str);
        }

        in.close();
        return buf.toString();
    }
}
