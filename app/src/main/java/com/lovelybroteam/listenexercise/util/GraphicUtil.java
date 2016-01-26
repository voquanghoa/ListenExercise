package com.lovelybroteam.listenexercise.util;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;

/**
 * Created by Vo Quang Hoa on 1/26/2016.
 */
public class GraphicUtil {
    public static Drawable getDrawable(Resources resources, int drawableId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return resources.getDrawable(drawableId, null);
        } else {
            return resources.getDrawable(drawableId);
        }
    }
}
