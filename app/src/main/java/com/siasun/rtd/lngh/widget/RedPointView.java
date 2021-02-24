package com.siasun.rtd.lngh.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class RedPointView extends View {
    private Paint paint=new Paint();
    private Context context;
    private int width,height;
    //构造方法
    public RedPointView(Context context) {
        super(context);
    }

    public RedPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RedPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RedPointView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context=context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }
    /*绘图*/
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //得到屏幕宽高
        int width = this.width;
        int radius = this.width/2;
        int height = this.height;
        // 消除锯齿
        paint.setAntiAlias(true);
        //画笔颜色
        paint.setColor(Color.RED);
        canvas.drawCircle(width,height,radius,paint);
    }

    public void setWidthAndHeight(int width,int height){
        this.width=width;
        this.height=height;
        invalidate();
    }


}
