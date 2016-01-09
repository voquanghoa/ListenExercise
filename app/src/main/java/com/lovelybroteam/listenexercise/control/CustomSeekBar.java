package com.lovelybroteam.listenexercise.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lovelybroteam.listenexercise.R;
import com.lovelybroteam.listenexercise.util.Utils;

/**
 * Created by Vo Quang Hoa on 12/28/2015.
 */
public class CustomSeekBar extends View {

    private Bitmap thumb;
    private Bitmap drawThumb;
    private int percent, drawPercent;
    private final double progressBarHeightRatio = 0.2;

    private int width, height, lineSize;
    private Paint backgroundPaint;
    private Paint bufferPaint;
    private int bufferPercent;
    private boolean isOnHold = false;
    private Runnable onUserChanged;

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
        percent = 0;
        thumb = BitmapFactory.decodeResource(getResources(), R.drawable.listen_thumb);
        backgroundPaint = new Paint();
        backgroundPaint.setColor(getContext().getResources().getColor(R.color.media_seekbar_background_color));

        bufferPaint = new Paint();
        bufferPaint.setColor(getContext().getResources().getColor(R.color.media_seekbar_buffer_color));
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        lineSize = width - height;
        if(thumb !=null) {
            drawThumb = getResizedBitmap(thumb, height, height);
        }
    }

    public void draw(Canvas canvas) {
        int height1 = (int)(height*(1-progressBarHeightRatio)/2);
        int height2 = (int)(height*(1+progressBarHeightRatio)/2);
        int x = height/2 + lineSize * drawPercent / 100;

        if(x<height/2){
            x = height/2;
        }

        if(x > width - height/2){
            x = width - height/2;
        }
        canvas.drawRect(0, height1, width, height2, backgroundPaint);
        canvas.drawRect(0, height1, width*bufferPercent/100, height2, bufferPaint);
        canvas.drawBitmap(drawThumb, x - height / 2, 0, null);
        super.draw(canvas);
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

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_MOVE:
                drawPercent =(int)(100 * (event.getX() - height/2)/lineSize);
                Utils.Log("ON TOUCH " + event.getX());
                isOnHold = true;
                this.invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                drawPercent = percent;
                isOnHold = false;
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                drawPercent =(int)(100 * (event.getX() - height/2)/lineSize);
                percent = drawPercent;
                isOnHold = false;
                onUserChanged.run();
                this.invalidate();
        }

        drawPercent = Utils.limit(drawPercent, 0, 100);
        percent = Utils.limit(percent, 0, 100);

        return true;
    }

    public void setBufferPercent(int bufferPercent) {
        this.bufferPercent = bufferPercent;
        this.invalidate();
    }

    public void setPercent(int percent){
        if(!isOnHold){
            this.percent = this.drawPercent = percent;
            this.invalidate();
        }
    }

    public void setOnUserChanged(Runnable onUserChanged) {
        this.onUserChanged = onUserChanged;
    }

    public int getPercent(){
        return percent;
    }
}
