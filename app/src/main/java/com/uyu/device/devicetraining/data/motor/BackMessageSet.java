package com.uyu.device.devicetraining.data.motor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by windern on 2016/1/23.
 */
public class BackMessageSet {
    private ArrayList<BackMessage> list = new ArrayList<>();

    public ArrayList<BackMessage> getList() {
        return list;
    }

    public void setList(ArrayList<BackMessage> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        JSONArray jsonArray = new JSONArray();
        for(BackMessage backMessage:list){
            try {
                jsonArray.put(backMessage.toJson());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }

    public static BackMessageSet convert(String value){
        BackMessageSet backMessageSet = null;
        try{
            backMessageSet = new BackMessageSet();
            JSONArray jsonArray = new JSONArray(value);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                BackMessage backMessage = BackMessage.convert(jsonObject);
                backMessageSet.getList().add(backMessage);
            }
        }catch (Exception e){
            backMessageSet = null;
            e.printStackTrace();
        }
        return backMessageSet;
    }

    public BackMessage getBackMessage(int seqNum){
        for(int i=0;i<list.size();i++){
            BackMessage backMessage = list.get(i);
            if(backMessage.getSeqNum()==seqNum){
                return backMessage;
            }
        }
        return null;
    }

    public static BackMessageSet convert(ControlMessageSet messageSet){
        BackMessageSet backMessageSet = new BackMessageSet();
        for(int i=0;i<messageSet.getList().size();i++){
            ControlMessage controlMessage = messageSet.getList().get(i);
            BackMessage backMessage = BackMessage.convert(controlMessage);
            if(controlMessage.getControlType()==ControlType.STOP){
                backMessage.setValue(20);
            }
            backMessageSet.getList().add(backMessage);
        }
        return backMessageSet;
    }
}
