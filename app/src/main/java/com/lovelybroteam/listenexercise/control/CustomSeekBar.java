package com.lovelybroteam.listenexercise.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.lovelybroteam.listenexercise.R;

/**
 * Created by Vo Quang Hoa on 12/28/2015.
 */
public class CustomSeekBar extends SeekBar {

    private Bitmap originalThumb;

    public CustomSeekBar(Context context) {
        super(context);
        initView();
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
       originalThumb = BitmapFactory.decodeResource(getResources(), R.drawable.listen_thumb);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if(originalThumb !=null) {
            setThumb(new BitmapDrawable(getResources(),getResizedBitmap(originalThumb, height, height)));
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }
}
