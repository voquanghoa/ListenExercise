package com.lovelybroteam.listenexercise.helper;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.plus.PlusShare;
import com.lovelybroteam.listenexercise.constant.AppConstant;

/**
 * Created by Vo Quang Hoa on 1/17/2016.
 */
public class SocialHelper implements AppConstant{
    private final static String MARKET_URL_PATTERN = "market://details?id=%s";
    private final static String MARKET_WEB_URL_PATTERN = "https://play.google.com/store/apps/details?%s";
    private final static String FACEBOOK_URL_PATTERN = "https://www.facebook.com/sharer/sharer.php?u=%s";
    private final static String SHARE_CONTENT_TYPE = "text/plain";

    private String packageName;
    private Context context;
    private String appDescription;

    public SocialHelper(Context context){
        this.context = context;
        this.packageName = context.getPackageName();
        this.appDescription = getAppDescription();
    }

    private String getAppDescription(){
        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
            return appInfo.loadDescription(packageManager).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void rateApp(){
        Intent intent = new Intent(Intent.ACTION_VIEW);

        String marketLink = String.format(MARKET_URL_PATTERN, packageName);

        intent.setData(Uri.parse(marketLink));
        if (!SafeStartActivity(intent)) {
            String marketWebLink = String.format(MARKET_WEB_URL_PATTERN, packageName);
            intent.setData(Uri.parse(marketWebLink));
            if (!SafeStartActivity(intent)) {
                Toast.makeText(context,"Could not open Android market, please install the market app.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void shareGooglePlus(){
        String marketWebLink = String.format(MARKET_WEB_URL_PATTERN, packageName);
        Intent shareIntent = new PlusShare.Builder(context)
                .setType(SHARE_CONTENT_TYPE)
                .setText(appDescription)
                .setContentUrl(Uri.parse(marketWebLink))
                .getIntent();

        SafeStartActivity(shareIntent);
    }

    public void shareFacebook(){
        String facebookShareLink = String.format(FACEBOOK_URL_PATTERN, packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookShareLink));
        SafeStartActivity(intent);
    }

    private boolean SafeStartActivity(Intent aIntent) {
        try{
            context.startActivity(aIntent);
            return true;
        }catch (ActivityNotFoundException e){
            return false;
        }
    }
}
