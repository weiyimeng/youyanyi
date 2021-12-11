package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/23 0023.
 */
public class CheckLinearLayout extends LinearLayout {

    //画笔
    private Paint myPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //圆心
    private float myCenterX, myCenterY;
    //top left
    private int[] myLocaltion = new int[2];
    //延迟时间
    private int INVALITIONDATE_DURATION = 40;
    //目标控件的宽高
    private int myTargetHeight, myTargetWidth;
    //关于半径的属性
    private int myRevealRadious = 0, myRevealRadiousGap, myMaxRadius;
    //控件的最大最小值
    private int myMinBetweenWidthAndHeight, myMaxBetweenWidthAndHeight;
    //是否按下
    private boolean myIspressed;
    //是否重新绘制波纹
    private boolean myShouldDoAnimation;
    //目标控件
    private View myTargetView;


    public CheckLinearLayout(Context context) {
        super(context);
        init();
    }


    public CheckLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CheckLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化配置
     */
    private void init() {
        //是程序调用自己重写的OnDraw方法
        setWillNotDraw(false);
        myPaint.setColor(Color.parseColor("#1b000000"));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //获
        this.getLocationOnScreen(myLocaltion);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (myTargetView == null || !myShouldDoAnimation || myTargetHeight <= 0 || myTargetWidth <= 0)
            return;
        if (myRevealRadious > myMinBetweenWidthAndHeight / 2)
            myRevealRadious += myRevealRadiousGap * 4;
        else
            myRevealRadious += myRevealRadiousGap;

        int[] localtion = new int[2];
        this.getLocationOnScreen(myLocaltion);
        myTargetView.getLocationOnScreen(localtion);

        int top = localtion[1] - myLocaltion[1];
        int left = localtion[0] - myLocaltion[0];
        int right = left + myTargetView.getMeasuredWidth();
        int bottom = top + myTargetView.getMeasuredHeight();

        canvas.save();
        canvas.clipRect(left, top, right, bottom);
        canvas.drawCircle(myCenterX, myCenterY, myRevealRadious, myPaint);
        canvas.restore();

        if (myRevealRadious <= myMaxRadius)
            postInvalidateDelayed(INVALITIONDATE_DURATION, left, top, right, bottom);
        else if (!myIspressed) {
            myShouldDoAnimation = false;
            postInvalidateDelayed(INVALITIONDATE_DURATION, left, top, right, bottom);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();//getX()getX是获取以widget左上角为坐标原点计算的Ｘ轴坐标值．
        int y = (int) ev.getRawY();//getRawX 获取的是以屏幕左上角为坐标原点计算的Ｘ轴坐标值．
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                View targetView = getTargetView(this, x, y);
                if (targetView != null && targetView.isEnabled()) {
                    myTargetView = targetView;
                    //初始化定义的数据
                    initParameterasForChild(ev, targetView);
                    postInvalidateDelayed(INVALITIONDATE_DURATION);
                }
                break;
            case MotionEvent.ACTION_UP:
                myIspressed = false;
                postInvalidateDelayed(INVALITIONDATE_DURATION);
                postInvalidateDelayed(INVALITIONDATE_DURATION);
                break;
            case MotionEvent.ACTION_CANCEL:
                myIspressed = false;
                postInvalidateDelayed(INVALITIONDATE_DURATION);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    //获取点击的view控件
    private View getTargetView(CheckLinearLayout myLayout, int x, int y) {
        View target = null;
        ArrayList<View> views = myLayout.getTouchables();
        for (View child : views) {
            if (isTouchPointInView(child, x, y)) {
                target = child;
                break;
            }
        }
        return target;
    }

    private void initParameterasForChild(MotionEvent ev, View targetView) {
        myCenterX = ev.getX();
        myCenterY = ev.getY();

        myTargetWidth = targetView.getMeasuredWidth();
        myTargetHeight = targetView.getMeasuredHeight();

        myMinBetweenWidthAndHeight = Math.min(myTargetWidth, myTargetHeight);
        myMaxBetweenWidthAndHeight = Math.max(myTargetWidth, myTargetHeight);

        myRevealRadious = 0;
        myRevealRadiousGap = myMinBetweenWidthAndHeight / 10;

        myIspressed = true;
        myShouldDoAnimation = true;

        int[] location = new int[2];
        myTargetView.getLocationOnScreen(location);

        //计算出能画圆的最大半径
        int left = location[0] - myLocaltion[0];
        int mTransformedCenterX = (int) myCenterX - left;
        myMaxRadius = Math.max(mTransformedCenterX, myTargetWidth - mTransformedCenterX);

    }

    //判断子控件是否在父控件中
    private boolean isTouchPointInView(View child, int x, int y) {
        int[] location = new int[2];
        child.getLocationOnScreen(location);
        int top = location[1];
        int left = location[0];

        int right = left + child.getMeasuredWidth();
        int bottom = top + child.getMeasuredHeight();
        if (child.isClickable() && y >= top && y <= bottom && x > left && x <= right)
            return true;
        else
            return false;
    }
}
