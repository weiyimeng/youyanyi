package com.uyu.device.devicetraining.presentation.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.uyu.device.devicetraining.presentation.adapter.OnFinishResultListener;
import com.uyu.device.devicetraining.presentation.model.FollowInspectionDesc;
import com.uyu.device.devicetraining.presentation.type.EnumResultType;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;
import com.uyu.device.devicetraining.presentation.voice.TtsEngine;
import com.uyu.device.devicetraining.presentation.voice.IatEngine;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by windern on 2015/12/11.
 */
public class FollowView extends ImageView{
    protected IatEngine iatEngine;
    protected TtsEngine ttsEngine;

    protected double accuracy = 0;
    private int nowIndex = 0;
    private int nowResultIndex = 0;
    private FollowInspectionDesc followInspectionDesc;
    private ArrayList<String> arrayResults = new ArrayList<>();

    protected OnFinishResultListener resultListener;
    protected ArrayList<FollowInspectionDesc> list;

    public FollowView(Context context) {
        super(context);
    }

    public FollowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FollowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setResultListener(OnFinishResultListener resultListener) {
        this.resultListener = resultListener;

        if(iatEngine==null){
            iatEngine = new IatEngine(getContext());
            iatEngine.setup(getContext());
        }

        if(ttsEngine==null){
            ttsEngine = new TtsEngine(getContext());
            ttsEngine.setup(getContext());
        }
    }

    public ArrayList<FollowInspectionDesc> getList() {
        return list;
    }

    public void setList(ArrayList<FollowInspectionDesc> list) {
        this.list = list;
    }

    public void pressOne(){
        arrayResults.add("A");
        finishOneLine();
    }

    public void pressTwo(){
        arrayResults.add("B");
        finishOneLine();
    }

    public void pressThree(){
        arrayResults.add("C");
        finishOneLine();
    }

    public void start(){
        accuracy = 0;
        nowIndex = 0;

        startVoiceRecognition();
        restart();
    }

    private void restart(){
        resetInitStatus();
        showPic();
    }

    /**
     * 恢复到初始状态
     */
    public void resetInitStatus(){
        followInspectionDesc = list.get(nowIndex);
        nowResultIndex = 0;
        arrayResults = new ArrayList<>();
    }

    public void showPic(){
        FollowInspectionDesc followInspectionDesc = list.get(nowIndex);
        Context context = getContext();
        int resId = context.getResources().getIdentifier(followInspectionDesc.getPicName(), "drawable", context.getPackageName());
        setImageResource(resId);

        speakStartTip();
    }

    private void speakStartTip(){
        String[] arrayStartPoints = followInspectionDesc.getArrayStartPoints();
        String startPoint = arrayStartPoints[nowResultIndex];
        String speakTip = String.format("请找出字母%s对应的数字", startPoint);
        Timber.d("speakStartTip:speakTip:%s",speakTip);
        ttsEngine.startSpeaking(speakTip);
    }

    /**
     * 完成一条线
     */
    public void finishOneLine(){
        String[] startPoints = followInspectionDesc.getStartPoints().split("-");
        String[] endPoints = followInspectionDesc.getEndPoints().split("-");
        ToastUtil.showShortToast(getContext(),"正确答案:"+startPoints[nowResultIndex]+"="+endPoints[nowResultIndex]);
        Timber.d("finishOneLine:arrayResults:%s", arrayResults);
        nowResultIndex++;
        if(nowResultIndex>=followInspectionDesc.getArrayStartPoints().length){
            finishOnePic();
        }else{
            speakStartTip();
        }
    }

    /**
     * 完成一张图
     */
    public void finishOnePic(){
        followInspectionDesc.computeAccuracy(arrayResults);

        Timber.d("finishOnePic:arrayResults:%s",arrayResults);
        nowIndex++;
        if(nowIndex>=list.size()){
            finishTrain();
        }else{
            restart();
        }
    }

    public void finishTrain(){
        iatEngine.stop();
        resultListener.onFinishResult(EnumResultType.SUCCESS);
    }

    public double getAccuracy(){
        computeAccuracy();
        return accuracy;
    }

    private void computeAccuracy(){
        double sumAccuracy = 0;
        int count = list.size();
        for(int i=0;i<count;i++){
            FollowInspectionDesc desc = list.get(i);
            sumAccuracy += desc.getAccuracy();
        }
        accuracy = sumAccuracy/count;
    }

    /**
     * 开启语音识别
     */
    private void startVoiceRecognition() {
        iatEngine.setListener(new IatEngine.ResultListener() {
            @Override
            public void onResult(boolean isSuccess, String retvalue) {

                Timber.d("Voice Result: " + Thread.currentThread().getName() + ",isSuccess: " + isSuccess + ",retValue: " + retvalue);

                if (isSuccess) {
                    if (retvalue.toUpperCase().contains("一") || retvalue.toUpperCase().contains("1")){
                        pressOne();
                    }else if(retvalue.toUpperCase().contains("二") || retvalue.toUpperCase().contains("2")){
                        pressTwo();
                    }else if(retvalue.toUpperCase().contains("三") || retvalue.toUpperCase().contains("3") || retvalue.toUpperCase().contains("山")){
                        pressThree();
                    }
                    iatEngine.cleanResult();
                }
            }
        });

        iatEngine.startRecognize();
    }

    public void initViewAfterFinish(){
        ttsEngine.stopSpeaking();
        iatEngine.stop();
    }

    public void pauseTrain() {
        initViewAfterFinish();
    }

    public void resumeTrain() {
        restart();
    }
}
