package com.uyu.device.devicetraining.domain.bluetooth;

import com.uyu.device.devicetraining.data.motor.BackMessage;
import com.uyu.device.devicetraining.data.motor.ControlMessageSet;
import com.uyu.device.devicetraining.data.motor.FlagType;

/**
 * 二进制命令-占5个字节
 * Created by windern on 2016/4/27.
 */
public class ByteCommand {
    public byte SubCmd = 0x00;
    public int data = 0;

    public static final int ByteLength = 5;
    public static final int IntByteLength = 4;

    /**
     * 获取字节
     * @return
     */
    public byte[] getBytes(){
        byte[] bytes = new byte[ByteLength];
        byte[] byteData = ComConfig.intToByteArray(data);
        bytes[0] = SubCmd;
        for(int i=0;i<IntByteLength;i++){
            bytes[i+1] = byteData[i];
        }
        return bytes;
    }

    /**
     * byte转换
     * @param bytes
     * @return
     */
    public static ByteCommand convert(byte[] bytes){
        ByteCommand byteCommand = null;
        if(bytes!=null && bytes.length==5){
            byteCommand = new ByteCommand();

            byteCommand.SubCmd = bytes[0];
            byte[] dataBytes = new byte[4];
            for(int i=0;i<dataBytes.length;i++){
                dataBytes[i] = bytes[i+1];
            }
            byteCommand.data = ComConfig.byteArrayToInt(dataBytes);
        }
        return byteCommand;
    }

    /**
     * 根据当前执行的命令集，返回backmessage
     * @param controlMessageSet
     * @return
     */
    public BackMessage createBackMessage(ControlMessageSet controlMessageSet){
        int motorNum = 0;
        switch(SubCmd)
        {
            //大按钮
            case ComConfig.SUBCMD_UserBigButton:
                motorNum = 6;
                break;
            //充电
            case ComConfig.SUBCMD_USBChargeCtrl:
                motorNum = 7;
                break;
            //Motor1
            case ComConfig.SUBCMD_Motor1_LCD_Reset:
            case ComConfig.SUBCMD_Motor1_LCD_ToPlace:
            case ComConfig.SUBCMD_Motor1_LCD_Place:
                motorNum = 1;
                break;
            //Motor2
            case ComConfig.SUBCMD_Motor2_DangBan_Reset:
            case ComConfig.SUBCMD_Motor2_DangBan_ToPlace:
            case ComConfig.SUBCMD_Motor2_DangBan_Place:
            case ComConfig.SUBCMD_Motor2_DangBan_Move:
            case ComConfig.SUBCMD_Motor2_DangBan_Stop:
                motorNum = 2;
                break;
            //Motor3
            case ComConfig.SUBCMD_Motor3_TongJu_Reset:
            case ComConfig.SUBCMD_Motor3_TongJu_SetDistance:
            case ComConfig.SUBCMD_Motor3_TongJu_Distance:
                motorNum = 3;
                break;
            //Motor4
            case ComConfig.SUBCMD_WoLunMotor4_Reset:
            case ComConfig.SUBCMD_WoLunMotor4_ToLens:
            case ComConfig.SUBCMD_WoLunMotor4_Lens:
                motorNum = 4;
                break;
            //Motor5
            case ComConfig.SUBCMD_WoLunMotor5_Reset:
            case ComConfig.SUBCMD_WoLunMotor5_ToLens:
            case ComConfig.SUBCMD_WoLunMotor5_Lens:
                motorNum = 5;
                break;

        }

        BackMessage backMessage = null;
        if(motorNum==6){
            backMessage = new BackMessage();
            backMessage.setSeqNum(0);
            backMessage.setValue(data);
            backMessage.setFlag(FlagType.SUCCESS);
        }else if(motorNum==7){
            backMessage = new BackMessage();
            backMessage.setSeqNum(-1);
            backMessage.setValue(data);
            backMessage.setFlag(FlagType.SUCCESS);
        }else {
            //充电也是同样的判断
            if(controlMessageSet!=null){
                int seqNum = controlMessageSet.getMessageSeqNum(motorNum);
                if (seqNum != -1) {
                    backMessage = new BackMessage();
                    backMessage.setSeqNum(seqNum);
                    backMessage.setValue(data);
                    backMessage.setFlag(FlagType.SUCCESS);
                }
            }
        }
        return backMessage;
    }
}
