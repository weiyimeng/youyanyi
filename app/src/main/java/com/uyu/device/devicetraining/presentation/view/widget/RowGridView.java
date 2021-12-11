package com.uyu.device.devicetraining.presentation.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.GridView;

import com.uyu.device.devicetraining.R;

/**
 * Created by Administrator on 2015/4/28 0028.
 */
public class RowGridView extends GridView {

    private Bitmap background;

    public RowGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.bg_one_bookshelf);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int count = getChildCount();
        int top = count>0 ? getChildAt(0).getTop() : 0;
        int backgroundWidth = background.getWidth();
        int backgroundHeight = background.getHeight();
        int width = getWidth();
        int height = getHeight();

        int itemHeight = count>0 ? getChildAt(0).getHeight() : 0;
        for(int i=0;i<count;i++){
            int itemTop = count>0 ? getChildAt(i).getTop() : 0;
            canvas.drawBitmap(background, 0, itemTop+itemHeight, null);
        }


//        for (int y = top+itemHeight; y<height; y += itemHeight){
//            canvas.drawBitmap(background, 0, y, null);
//        }

        super.dispatchDraw(canvas);
    }
}
