package com.uyu.device.devicetraining.presentation.view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.uyu.device.devicetraining.data.entity.content.ContentManager;
import com.uyu.device.devicetraining.data.entity.type.EnumShowContentType;
import com.uyu.device.devicetraining.presentation.adapter.OnDrawFinish;

/**
 * Created by windern on 2015/12/9.
 */
public class LineView extends View {
    private Context context;
    /**
     * 原始内容
     */
    private String srcContent = "";
    /**
     * 字符mm高度
     */
    private float mmSize = 10f;
    /**
     * 一行显示的字数
     */
    private int lineShowCount;
    /**
     * 字符列表
     */
    private String[] arrayLetters = {""};
    /**
     * 第一行第一个的文字索引
     */
    private int row0Column0Index = 0;
    /**
     * 下一行第一个的文字索引
     */
    private int nextRowColumnIndex = 0;
    /**
     * 一行最多放的字的个数
     */
    private int MaxRowLetterCount = 10;
    /**
     * 内容显示类型
     */
    private EnumShowContentType showContentType = EnumShowContentType.SENTENCE;

    private int ligthGray = Color.rgb(220, 220, 220);

    private String nowShowContent = "";
    private OnDrawFinish drawFinishListener;

    public LineView(Context context) {
        super(context);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setDrawFinishListener(OnDrawFinish drawFinishListener) {
        this.drawFinishListener = drawFinishListener;
    }

    public String getSrcContent() {
        return srcContent;
    }

    public void setSrcContent(String srcContent) {
        this.srcContent = srcContent;
        arrayLetters = TextUtils.split(srcContent, "");
    }

    public EnumShowContentType getShowContentType() {
        return showContentType;
    }

    public void setShowContentType(EnumShowContentType showContentType) {
        this.showContentType = showContentType;
    }

    public float getMmSize() {
        return mmSize;
    }

    public void setMmSize(float mmSize) {
        this.mmSize = mmSize;
    }

    public int getLineShowCount() {
        return lineShowCount;
    }

    public void setLineShowCount(int lineShowCount) {
        this.lineShowCount = lineShowCount;
    }

    /**
     * 设置位置
     * @param pos
     */
    public void setPos(int pos){
        this.row0Column0Index = pos;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //测试
        final Resources res = getResources();

        TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.density = res.getDisplayMetrics().density;

        //mmSize = 7f;
        float pxSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_MM, mmSize, res.getDisplayMetrics());
        mTextPaint.setTextSize(pxSize);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        //字体高度
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;

        //每个字体的坐标点
        float baseX = 0;
        float baseY = 0;

        // 计算每一个坐标
        baseX = 0;
        baseY = -fontMetrics.top;

        //屏幕整体的宽度和高度
        float totalWidth = getWidth();
        float totalHeight = getHeight();

        //一行的显示字的个数
        int rowLetterCount = 0;

        //第一个显示字的起始坐标
        float startBaseX = 0;

        //位置变化记录，最后存储最后的位置
        int i = row0Column0Index;
        //一行实际计算显示的宽度
        float totalRowRealWidth = 0;
        //先计算显示文字总长度，然后算居中后起始的位置
        for (i = row0Column0Index; i < arrayLetters.length-1; i++) {
            String showletter = arrayLetters[i];

            //非空字段才画
            if (ContentManager.isShow(showletter)) {
                if(showContentType== EnumShowContentType.WORD && ContentManager.isWordKey(showletter)){
                    break;
                }

                float width = mTextPaint.measureText(showletter);
                if (rowLetterCount >= MaxRowLetterCount || totalRowRealWidth + width > totalWidth) {
                    break;
                }

                rowLetterCount++;

                totalRowRealWidth += width;

                if(showContentType== EnumShowContentType.SENTENCE && ContentManager.isSentenceKey(showletter)){
                    break;
                }
            }
        }


        startBaseX = (totalWidth - totalRowRealWidth) / 2;
        baseX = startBaseX;

        //计算起始的高度，高度要居中
        int thinkRowCount = (int) (totalHeight / fontTotalHeight);
        if (thinkRowCount % 2 == 0) {
            //如果可以放下偶数行，变成奇数行
            thinkRowCount--;
        }
        int halfOfThinkRowCount = thinkRowCount / 2;
        float top_offY = (totalHeight - fontTotalHeight * thinkRowCount) / 2;
        float offY = top_offY + fontTotalHeight * halfOfThinkRowCount;
        baseY = -fontMetrics.top;
        baseY += offY;

        //默认是0，如果是0，表示当前是最后一行
        nowShowContent = "";
        nextRowColumnIndex = 0;
        rowLetterCount = 0;
        //arrayLetters使用spit分割出来，最后一个为空字符，可以不需要
        for (i = row0Column0Index; i < arrayLetters.length-1; i++) {
            String showletter = arrayLetters[i];

            //非空字段才画
            if (ContentManager.isShow(showletter)) {
                if(showContentType== EnumShowContentType.WORD && ContentManager.isWordKey(showletter)){
                    //不是最后一个字符，下一给从下一个字母开始，否则nextRowColumnIndex就是0从头开始
                    if(i<arrayLetters.length-1-1) {
                        nextRowColumnIndex = i + 1;
                    }
                    break;
                }

                float width = mTextPaint.measureText(showletter);
                if (rowLetterCount >= MaxRowLetterCount || baseX + width > totalWidth) {
                    nextRowColumnIndex = i;
                    break;
                }
                canvas.drawText(showletter, baseX, baseY, mTextPaint);

                baseX += width;
                rowLetterCount++;

                nowShowContent += showletter;

                if(showContentType== EnumShowContentType.SENTENCE && ContentManager.isSentenceKey(showletter)){
                    //不是最后一个字符，下一给从下一个字母开始，否则nextRowColumnIndex就是0从头开始
                    //跟word不同的是显示关键字符
                    if(i<arrayLetters.length-1-1) {
                        nextRowColumnIndex = i + 1;
                    }
                    break;
                }
            }
        }

        if(drawFinishListener!=null){
            drawFinishListener.onDrawFinish();
        }
    }

    /**
     * 切换到下一行
     */
    public void nextRow(){
        row0Column0Index = nextRowColumnIndex;
    }

    /**
     * 是否有下一行
     * @return
     */
    public boolean isHasNextRow(){
        boolean isHasNextRow = true;
        if(nextRowColumnIndex!=0){
            isHasNextRow = true;
        }else{
            isHasNextRow = false;
        }
        return isHasNextRow;
    }

    public String getNowShowContent(){
        return nowShowContent;
    }

    /**
     * 重置位置
     */
    public void resetIndex(){
        row0Column0Index = 0;
        nextRowColumnIndex = 0;
    }
}