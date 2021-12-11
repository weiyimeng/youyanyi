package com.uyu.device.devicetraining.presentation.view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.uyu.device.devicetraining.data.entity.content.ContentManager;
import com.uyu.device.devicetraining.data.entity.type.EnumContentType;
import com.uyu.device.devicetraining.presentation.adapter.OnDrawFinish;
import com.uyu.device.devicetraining.presentation.adapter.OnFinishResultListener;
import com.uyu.device.devicetraining.presentation.type.EnumResultType;
import com.uyu.device.devicetraining.presentation.util.Util;

import timber.log.Timber;

/**
 * Created by windern on 2015/12/18.
 */
public class RedGreenReadView extends View {
    private Context context;

    private EnumContentType contentType= EnumContentType.LETTER;
    /**
     * 原始内容
     */
    private String srcContent = "";
    /**
     * 字符mm高度
     */
    private float mmSize = 5f;

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
    private int nowIndex = 0;
    /**
     * 下一行第一个的文字索引
     */
    private int nextIndex = 0;
    /**
     * 一行最多放的字的个数
     */
    private int MaxRowLetterCount = 10;

    /**
     * 当前显示的字母数
     */
    private int nowShowLetterCount = 0;
    /**
     * 需要训练的个数
     */
    private int needTrainLetterCount = 100;
    /**
     * 已经看过的数
     */
    private int hasTrainLetterCount = 0;
    private OnFinishResultListener finishResultListener;

    private int ligthGray = Color.rgb(220, 220, 220);

    private int[] colorArray = {0xff6e0909,0xFFFFFF,0xff135d2b,0xFFF};

    private String nowShowContent = "";
    private OnDrawFinish drawFinishListener;

    public void setDrawFinishListener(OnDrawFinish drawFinishListener) {
        this.drawFinishListener = drawFinishListener;
    }

    public RedGreenReadView(Context context) {
        super(context);
        this.context = context;
    }

    public RedGreenReadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public RedGreenReadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public float getMmSize() {
        return mmSize;
    }

    public void setMmSize(float mmSize) {
        this.mmSize = mmSize;
    }

    public String getSrcContent() {
        return srcContent;
    }

    public void setSrcContent(String srcContent) {
        this.srcContent = srcContent;
        arrayLetters = TextUtils.split(srcContent, "");
        //invalidate();
    }

    public EnumContentType getContentType() {
        return contentType;
    }

    public void setContentType(EnumContentType contentType) {
        this.contentType = contentType;
    }

    public int getNeedTrainLetterCount() {
        return needTrainLetterCount;
    }

    public void setNeedTrainLetterCount(int needTrainLetterCount) {
        this.needTrainLetterCount = needTrainLetterCount;
    }

    public void setFinishResultListener(OnFinishResultListener finishResultListener) {
        this.finishResultListener = finishResultListener;
    }

    public int getHasTrainLetterCount() {
        return hasTrainLetterCount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final Resources res = getResources();

        TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.density = res.getDisplayMetrics().density;

        //mmSize = 7f;
        float pxSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_MM, mmSize, res.getDisplayMetrics());
        mTextPaint.setTextSize(pxSize);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        //文字的高度
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;

        float oneWidth = mTextPaint.measureText("一");
        //文字的宽度，长高一致，一般高度比宽度高
        float realLetterWidth = oneWidth;
        realLetterWidth = fontTotalHeight;

        //起始坐标
        float startBaseX = 0;
        float startBaseY = 0;
        //变化的坐标
        float baseX = 0;
        float baseY = 0;

        // 计算每一个坐标
        startBaseX = 0;
        startBaseY = -fontMetrics.top;

        float totalWidth = getWidth();
        float totalHeight = getHeight();

        //实际的行数
        int realRowCount = (int)Math.floor(totalHeight/fontTotalHeight);
        if(realRowCount>MaxRowLetterCount){
            realRowCount = MaxRowLetterCount;
        }
        //实际的每行的字母数
        int realRowLetterCount = realRowCount;

        //计算初始的位置，保持居中
        if (realLetterWidth * realRowLetterCount < totalWidth) {
            //先通过“一”字的宽度，计算大概整行的宽度，剩下的就是多余的宽度，需要除以2，两边都留一点
            startBaseX = (totalWidth - realLetterWidth * realRowLetterCount) / 2;
        }
        if(fontTotalHeight*realRowCount<totalHeight){
            startBaseY += (totalHeight-fontTotalHeight*realRowCount)/2;
        }
        baseX = startBaseX;
        baseY = startBaseY;

        //画每列的竖线背景
        float bgStartBaseY = startBaseY+fontMetrics.top;
        for(int i=0;i<realRowLetterCount;i++){
            float nowBaseX = startBaseX+realLetterWidth*i;
            RectF rectF = new RectF(nowBaseX,bgStartBaseY,nowBaseX+realLetterWidth,bgStartBaseY+fontTotalHeight*realRowCount);
            Paint redPaint=  new  Paint();
            redPaint.setColor(colorArray[i%colorArray.length]);
            canvas.drawRect(rectF,redPaint);
        }

        //行数
        int rowCount = 0;
        //每行画过的字母数
        int rowLetterCount = 0;

        if(contentType!= EnumContentType.ARTICLE){
            refreshContent(realRowCount*realRowLetterCount);
        }

        nowShowContent = "";
        //画文字
        int i=nowIndex;
        int nowHasDrawCount = 0;
        while(rowCount*realRowLetterCount+rowLetterCount<realRowCount*realRowLetterCount && hasTrainLetterCount+nowHasDrawCount<needTrainLetterCount){
            //文章结束了，从头开始继续画
            if(i==arrayLetters.length){
                i=0;
            }
            String showletter = arrayLetters[i];

            //非空字段才画
            if (ContentManager.isShow(showletter)) {
                float width = mTextPaint.measureText(showletter);
                if (rowLetterCount == realRowLetterCount) {
                    //一行画完了换行
                    //换行画第一个字
                    rowLetterCount = 0;
                    rowCount++;

                    //超过进行换行
                    baseX = startBaseX;
                    if (baseY + fontTotalHeight > totalHeight) {
                        //如果下一个超过整个高，则停止，否则继续
                        break;
                    } else {
                        baseY += fontTotalHeight;
                    }
                }
                //画文字
                canvas.drawText(showletter, baseX + (realLetterWidth - width) / 2, baseY, mTextPaint);
                baseX += realLetterWidth;
                rowLetterCount++;

                nowHasDrawCount++;

                nowShowContent += showletter;
            }
            i++;
        }

        nowShowLetterCount = nowHasDrawCount;
        //记录下一个开始
        nextIndex = i;

        if(drawFinishListener!=null){
            Timber.d("ondraw:nowShowContent:%s", nowShowContent);
            drawFinishListener.onDrawFinish();
        }
    }

    public void nextPage(){
        hasTrainLetterCount += nowShowLetterCount;
        if(hasTrainLetterCount>=needTrainLetterCount){
            finishResultListener.onFinishResult(EnumResultType.SUCCESS);
        }else {
            nowIndex = nextIndex;
            invalidate();
        }
    }

    public void refreshContent(int totalLetterCount){
        if(contentType== EnumContentType.LETTER){
            String content = Util.getOneRandomText(totalLetterCount);
            arrayLetters = TextUtils.split(content, "");
        }else if(contentType== EnumContentType.NUMBER){
            String content = Util.getOneRandomNumber(totalLetterCount);
            arrayLetters = TextUtils.split(content, "");
        }
    }

    /**
     * 恢复到开始状态
     * @param isTotalRestart 是否完全重新开始，是内容从0开始
     */
    public void resetStartStatus(boolean isTotalRestart){
        nowShowLetterCount = 0;
        hasTrainLetterCount = 0;
        if(isTotalRestart){
            nowIndex = 0;
            nextIndex = 0;
        }
    }

    public String getNowShowContent(){
        return nowShowContent;
    }
}
