package com.uyu.device.devicetraining.data.motor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import rx.subjects.PublishSubject;

/**
 * Created by windern on 2016/1/23.
 */
public class ControlMessageSet {
    private ArrayList<ControlMessage> list = new ArrayList<>();
    private Handler handler = null;
    private PublishSubject<ControlMessageSet> publishSubject;

    public ArrayList<ControlMessage> getList() {
        return list;
    }

    public void setList(ArrayList<ControlMessage> list) {
        this.list = list;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public PublishSubject<ControlMessageSet> getPublishSubject() {
        return publishSubject;
    }

    public void setPublishSubject(PublishSubject<ControlMessageSet> publishSubject) {
        this.publishSubject = publishSubject;
    }

    public void addMessage(ControlMessage controlMessage){
        list.add(controlMessage);
    }

    /**
     * 是否准备好
     * @return
     */
    public boolean isReady(){
        boolean isReady = true;
        for(ControlMessage controlMessage:list){
            if(!(controlMessage.getBackMessage()!=null && controlMessage.getBackMessage().getFlag()==FlagType.SUCCESS)){
                isReady = false;
                break;
            }
        }
        return isReady;
    }

    @Override
    public String toString() {
        JSONArray jsonArray = new JSONArray();
        for(ControlMessage controlMessage:list){
            try {
                jsonArray.put(controlMessage.toJson());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }

    /**
     * 没有准备好的消息
     * @return
     */
    public String toStringWithNotReady(){
        JSONArray jsonArray = new JSONArray();
        for(ControlMessage controlMessage:list){
            try {
                //没有准备好的才重发消息
                if(!(controlMessage.getBackMessage()!=null && controlMessage.getBackMessage().getFlag()==FlagType.SUCCESS)){
                    controlMessage.setBackMessage(null);
                    jsonArray.put(controlMessage.toJson());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }

    /**
     * 获取没有准备好的消息的list
     * @return
     */
    public ArrayList<ControlMessage> getNotReadyMessageList(){
        ArrayList<ControlMessage> notReadyList = new ArrayList<>();
        for(ControlMessage controlMessage:list){
            //没有准备好的才重发消息
            if(!(controlMessage.getBackMessage()!=null && controlMessage.getBackMessage().getFlag()==FlagType.SUCCESS)){
                controlMessage.setBackMessage(null);
                notReadyList.add(controlMessage);
            }
        }
        return notReadyList;
    }

    public static ControlMessageSet convert(String value){
        ControlMessageSet controlMessageSet = null;
        try {
            controlMessageSet = new ControlMessageSet();
            JSONArray jsonArray = new JSONArray(value);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ControlMessage controlMessage = ControlMessage.convert(jsonObject);
                controlMessageSet.getList().add(controlMessage);
            }
        } catch (JSONException e) {
            controlMessageSet = null;
            e.printStackTrace();
        }
        return controlMessageSet;
    }

    public void handleBackMessageSet(BackMessageSet backMessageSet){
        for(int i=0;i<backMessageSet.getList().size();i++){
            BackMessage backMessage = backMessageSet.getList().get(i);
            ControlMessage controlMessage = getControlMessage(backMessage.getSeqNum());
            if(controlMessage!=null){
                controlMessage.setBackMessage(backMessage);
            }
        }
    }

    public ControlMessage getControlMessage(int seqNum){
        for(int i=0;i<list.size();i++){
            ControlMessage controlMessage = list.get(i);
            if(controlMessage.getSeqNum()==seqNum){
                return controlMessage;
            }
        }
        return null;
    }

    public Message toMessage(){
        Message message = new Message();
        message.what = 1;
        Bundle data = new Bundle();
        data.putString("value", toString());
        message.setData(data);
        return message;
    }

    public static ControlMessageSet convert(Message msg){
        Bundle data = msg.getData();
        String value = data.getString("value");
        ControlMessageSet messageSet = convert(value);
        return messageSet;
    }

    /**
     * 根据电机编号找消息序号
     * @param motorNum
     * @return -1代表没有找到
     */
    public int getMessageSeqNum(int motorNum){
        int seqNum = -1;
        for(ControlMessage controlMessage:list){
            if(controlMessage.getMotorNum()==motorNum){
                seqNum = controlMessage.getSeqNum();
            }
        }
        return seqNum;
    }
}
