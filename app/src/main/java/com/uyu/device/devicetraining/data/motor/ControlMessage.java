package com.uyu.device.devicetraining.data.motor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.uyu.device.devicetraining.domain.bluetooth.ByteCommand;
import com.uyu.device.devicetraining.domain.bluetooth.ComConfig;
import com.uyu.device.devicetraining.domain.bluetooth.CommandConvertException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2016/1/19.
 */
public class ControlMessage {
    private int seqNum = 1;
    private int motorNum = 1;
    private ControlType controlType = ControlType.RESET;
    private int value0 = 0;
    private int value1 = 0;

    private BackMessage backMessage = null;

    public int getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    public int getMotorNum() {
        return motorNum;
    }

    public void setMotorNum(int motorNum) {
        this.motorNum = motorNum;
    }

    public ControlType getControlType() {
        return controlType;
    }

    public void setControlType(ControlType controlType) {
        this.controlType = controlType;
    }

    public int getValue0() {
        return value0;
    }

    public void setValue0(int value0) {
        this.value0 = value0;
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

    public BackMessage getBackMessage() {
        return backMessage;
    }

    public void setBackMessage(BackMessage backMessage) {
        this.backMessage = backMessage;
    }

    public JSONObject toJson() throws JSONException{
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("seq_num", seqNum);
            jsonObject.put("motor_num", motorNum);
            jsonObject.put("ctrl_type", controlType.getValue());
            jsonObject.put("value0", value0);
            jsonObject.put("value1", value1);
            if(backMessage!=null){
                JSONObject backMessageObj = backMessage.toJson();
                jsonObject.put("back_message",backMessageObj);
            }

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }

    @Override
    public String toString() {
        String value = "";
        try {
            JSONObject jsonObject = toJson();
            value = jsonObject.toString();
        } catch (JSONException e) {
            value = "";
            e.printStackTrace();
        }
        return value;
    }

    public static ControlMessage convert(JSONObject jsonObject){
        ControlMessage controlMessage = null;
        try {
            controlMessage = new ControlMessage();
            controlMessage.setSeqNum(jsonObject.getInt("seq_num"));
            controlMessage.setMotorNum(jsonObject.getInt("motor_num"));
            int controlTypeValue = jsonObject.getInt("ctrl_type");
            controlMessage.setControlType(ControlType.values()[controlTypeValue]);
            controlMessage.setValue0(jsonObject.getInt("value0"));
            controlMessage.setValue1(jsonObject.getInt("value1"));

            try{
                JSONObject backMessageObject = jsonObject.getJSONObject("back_message");
                controlMessage.setBackMessage(BackMessage.convert(backMessageObject));
            }catch (JSONException e){
                e.printStackTrace();
            }
        } catch (JSONException e) {
            controlMessage = null;
            e.printStackTrace();
        }
        return controlMessage;
    }

    public static ControlMessage convert(String msgString){
        ControlMessage controlMessage = null;
        try {
            JSONObject jsonObject = new JSONObject(msgString);
            controlMessage = convert(jsonObject);
        } catch (JSONException e) {
            controlMessage = null;
            e.printStackTrace();
        }
        return controlMessage;
    }

    /**
     * 获取字节命令
     * @return
     */
    public ByteCommand convertByteCommand() throws CommandConvertException{
        ByteCommand byteCommand = new ByteCommand();
        switch(motorNum)
        {
            case 1: //1号电机导轨：屏的导轨
                if(0 == controlType.getValue())//0-复位
                {
                    byteCommand.SubCmd = ComConfig.SUBCMD_Motor1_LCD_Reset;
                    byteCommand.data = 0;
                }
                else if(1 == controlType.getValue())//1-指定
                {
                    byteCommand.SubCmd = ComConfig.SUBCMD_Motor1_LCD_ToPlace;
                    byteCommand.data = value0;
                }
                else if(2 == controlType.getValue()) //2-获取
                {
                    byteCommand.SubCmd = ComConfig.SUBCMD_Motor1_LCD_Place;
                    byteCommand.data = 0;
                }
                else
                {
                    //printf("JsonExe: motorNum = %d, controlType.getValue() = %d, Execute nothing!\r\n", motorNum, controlType.getValue());
                    throw new CommandConvertException("命令转换失败");
                }
                break;
            case 2: //2号电机导轨：挡板的导轨
                if(0 == controlType.getValue())//0-复位
                {
                    byteCommand.SubCmd = ComConfig.SUBCMD_Motor2_DangBan_Reset;
                    byteCommand.data = 0;
                }
                else if(1 == controlType.getValue())//1-指定
                {
                    byteCommand.SubCmd = ComConfig.SUBCMD_Motor2_DangBan_ToPlace;
                    byteCommand.data = value0;
                }
                else if(2 == controlType.getValue()) //2-获取
                {
                    byteCommand.SubCmd = ComConfig.SUBCMD_Motor2_DangBan_Place;
                    byteCommand.data = 0;
                }
                else if(3 == controlType.getValue()) //3-移动
                {
                    byteCommand.SubCmd = ComConfig.SUBCMD_Motor2_DangBan_Move;
                    byteCommand.data = value0;	//note??????
                }
                else if(4 == controlType.getValue()) //4-停止
                {
                    byteCommand.SubCmd = ComConfig.SUBCMD_Motor2_DangBan_Stop;
                    byteCommand.data = 0;
                }
                else
                {
                    //printf("JsonExe: motorNum = %d, controlType.getValue() = %d, Execute nothing!\r\n", motorNum, controlType.getValue());
                    throw new CommandConvertException("命令转换失败");
                }
                break;
            case 3: //3号电机导轨：瞳距的导轨
                if(0 == controlType.getValue())//0-复位
                {
                    byteCommand.SubCmd = ComConfig.SUBCMD_Motor3_TongJu_Reset;
                    byteCommand.data = 0;
                }
                else if(1 == controlType.getValue())//1-指定
                {
                    byteCommand.SubCmd = ComConfig.SUBCMD_Motor3_TongJu_SetDistance;
                    byteCommand.data = value0;
                }
                else if(2 == controlType.getValue()) //2-获取
                {
                    byteCommand.SubCmd = ComConfig.SUBCMD_Motor3_TongJu_Distance;
                    byteCommand.data = 0;
                }
                else
                {
                    //printf("JsonExe: motorNum = %d, controlType.getValue() = %d, Execute nothing!\r\n", motorNum, controlType.getValue());
                    throw new CommandConvertException("命令转换失败");
                }
                break;
            case 4: //4号电机转盘：左眼转盘
                if(0 == controlType.getValue())//0-复位
                {
                    byteCommand.SubCmd = ComConfig.SUBCMD_WoLunMotor4_Reset;
                    byteCommand.data = 0;
                }
                else if(1 == controlType.getValue())//1-指定
                {
                    byteCommand.SubCmd = ComConfig.SUBCMD_WoLunMotor4_ToLens;
                    byteCommand.data = value0;
                }
                else if(2 == controlType.getValue()) //2-获取
                {
                    byteCommand.SubCmd = ComConfig.SUBCMD_WoLunMotor4_Lens;
                    byteCommand.data = value0;
                }
                else
                {
                    //printf("JsonExe: motorNum = %d, controlType.getValue() = %d, Execute nothing!\r\n", motorNum, controlType.getValue());
                    throw new CommandConvertException("命令转换失败");
                }
                break;
            case 5: //5号电机转盘：右眼转盘
                if(0 == controlType.getValue())//0-复位
                {
                    byteCommand.SubCmd = ComConfig.SUBCMD_WoLunMotor5_Reset;
                    byteCommand.data = 0;
                }
                else if(1 == controlType.getValue())//1-指定
                {
                    byteCommand.SubCmd = ComConfig.SUBCMD_WoLunMotor5_ToLens;
                    byteCommand.data = value0;
                }
                else if(2 == controlType.getValue()) //2-获取
                {
                    byteCommand.SubCmd = ComConfig.SUBCMD_WoLunMotor5_Lens;
                    byteCommand.data = 0;
                }
                else
                {
                    //printf("JsonExe: motorNum = %d, controlType.getValue() = %d, Execute nothing!\r\n", motorNum, controlType.getValue());
                    throw new CommandConvertException("命令转换失败");
                }
                break;
            case 6: //6号大按钮
                byteCommand.SubCmd = ComConfig.SUBCMD_UserBigButton;
                byteCommand.data = 0;
                break;
            case 7: //充电控制
                byteCommand.SubCmd = ComConfig.SUBCMD_USBChargeCtrl;
                byteCommand.data = value0;
                break;
            default:
                throw new CommandConvertException("命令转换失败");
        }
        return byteCommand;
    }
}
