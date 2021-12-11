package com.uyu.device.devicetraining.domain.bluetooth;

import android.graphics.Bitmap;

import com.squareup.haha.perflib.Main;
import com.uyu.device.devicetraining.data.motor.BackMessage;
import com.uyu.device.devicetraining.data.motor.BackMessageSet;
import com.uyu.device.devicetraining.data.motor.ControlMessage;
import com.uyu.device.devicetraining.data.motor.ControlMessageSet;
import com.uyu.device.devicetraining.domain.motor.MotorBus;

import java.util.ArrayList;
import java.util.LinkedList;

import timber.log.Timber;

/**
 * Created by windern on 2016/4/27.
 */
public class ComFrame {
    public static final int HEAD_INDEX = 0;
    public static final int LENGTH_INDEX = 1;
    public static final int SOURCE_INDEX = 2;
    public static final int DESTINATION_INDEX = 3;
    public static final int SERNUMBER_INDEX = 4;
    public static final int MAINCMD_INDEX = 5;
    /**
     * 数据的第一个字节所在的位置
     */
    public static final int DATA_INDEX = 6;


    /**
     * 缓冲区
     */
    private byte[] buffer = new byte[1024];
    /**
     * 当前缓冲区长度
     */
    private int nowBufferLength = 0;

    public class ByteMsg{
        public byte[] data;
    }

    private LinkedList<ByteMsg> queue = new LinkedList<>();
    private ByteMsg nowByteMsg = null;

    private static ComFrame instatnce = null;
    public static ComFrame getInstance(){
        if(instatnce==null){
            instatnce = new ComFrame();
        }
        return instatnce;
    }

    private ComFrame(){

    }

    /**
     * 计算获取发送的byte
     *
     * @param list
     * @return
     * @throws CommandConvertException
     */
    public byte[] computeSendByte(ArrayList<ControlMessage> list) throws CommandConvertException {
        byte Head = ComConfig.COMHead;
        int Length = 4;
        byte Source = ComConfig.COMAddr_A;
        byte Destination = ComConfig.COMAddr_H;
        byte SerNumber = 0;
        byte MainCmd = ComConfig.MAINCMD_COMMON;

        int wholeByteLength = 6 + 5 * list.size() + 4;
        byte[] bytes = new byte[wholeByteLength];
        bytes[HEAD_INDEX] = Head;
        bytes[LENGTH_INDEX] = (byte) Length;
        bytes[SOURCE_INDEX] = Source;
        bytes[DESTINATION_INDEX] = Destination;
        bytes[SERNUMBER_INDEX] = SerNumber;
        bytes[MAINCMD_INDEX] = MainCmd;

        for (int i = 0; i < list.size(); i++) {
            ControlMessage controlMessage = list.get(i);
            ByteCommand byteCommand = controlMessage.convertByteCommand();
            byte[] oneByteCommandBytes = byteCommand.getBytes();
            for (int j = 0; j < oneByteCommandBytes.length; j++) {
                bytes[DATA_INDEX + i * 5 + j] = oneByteCommandBytes[j];
            }
            Length += 5;
        }
        bytes[1] = (byte) Length;

        //校验的时候加上length自己这个字节
        byte[] crc32SourceBytes = new byte[Length + 1];
        for (int i = 0; i < crc32SourceBytes.length; i++) {
            crc32SourceBytes[i] = bytes[LENGTH_INDEX + i];
        }
        int resultCrc32 = ComConfig.calcCrc32(crc32SourceBytes, crc32SourceBytes.length);
        byte[] resultByteCrc32 = ComConfig.intToByteArray(resultCrc32);
        //crc32起始的位置
        int crc32StartIndex = Length + 1 + 1;
        for (int i = 0; i < resultByteCrc32.length; i++) {
            bytes[crc32StartIndex + i] = resultByteCrc32[i];
        }

        return bytes;
    }

    /**
     * 计算获取发送的byte
     * @param controlMessage
     * @return
     * @throws CommandConvertException
     */
    public byte[] computeSendByte(ControlMessage controlMessage) throws CommandConvertException {
        byte Head = ComConfig.COMHead;
        int Length = 4;
        byte Source = ComConfig.COMAddr_A;
        byte Destination = ComConfig.COMAddr_H;
        byte SerNumber = 0;
        byte MainCmd = ComConfig.MAINCMD_COMMON;

        int wholeByteLength = 6 + 5 * 1 + 4;
        byte[] bytes = new byte[wholeByteLength];
        bytes[HEAD_INDEX] = Head;
        bytes[LENGTH_INDEX] = (byte) Length;
        bytes[SOURCE_INDEX] = Source;
        bytes[DESTINATION_INDEX] = Destination;
        bytes[SERNUMBER_INDEX] = SerNumber;
        bytes[MAINCMD_INDEX] = MainCmd;

        ByteCommand byteCommand = controlMessage.convertByteCommand();
        byte[] oneByteCommandBytes = byteCommand.getBytes();
        for (int j = 0; j < oneByteCommandBytes.length; j++) {
            bytes[DATA_INDEX + j] = oneByteCommandBytes[j];
        }
        Length += 5;

        bytes[1] = (byte) Length;

        //校验的时候加上length自己这个字节
        byte[] crc32SourceBytes = new byte[Length + 1];
        for (int i = 0; i < crc32SourceBytes.length; i++) {
            crc32SourceBytes[i] = bytes[LENGTH_INDEX + i];
        }
        int resultCrc32 = ComConfig.calcCrc32(crc32SourceBytes, crc32SourceBytes.length);
        byte[] resultByteCrc32 = ComConfig.intToByteArray(resultCrc32);
        //crc32起始的位置
        int crc32StartIndex = Length + 1 + 1;
        for (int i = 0; i < resultByteCrc32.length; i++) {
            bytes[crc32StartIndex + i] = resultByteCrc32[i];
        }

        return bytes;
    }

    public void receiveBytes(byte[] newresult){
        ByteMsg byteMsg = new ByteMsg();
        byteMsg.data = newresult;
        queue.add(byteMsg);

        handleNextMessageSet();
    }

    public void handleNextMessageSet(){
        //当前没有执行的命令
        if(nowByteMsg==null) {
            if (queue.size() > 0) {
                nowByteMsg = queue.poll();

                handleNowMessageSet();
            }
        }
    }

    private void handleNowMessageSet(){
        byte[] bytes = nowByteMsg.data;
        for(int i=0;i<bytes.length;i++){
            buffer[nowBufferLength+i] = bytes[i];
        }
        nowBufferLength = nowBufferLength + bytes.length;
        searchFrame();

        //执行完成后nowByteMsg置成null
        nowByteMsg = null;
        handleNextMessageSet();
    }

    /**
     * 搜索消息帧
     * @return
     */
    public void searchFrame()
    {
        byte[] tmphanldeBuffer = new byte[nowBufferLength];
        System.arraycopy(buffer,0,tmphanldeBuffer,0,nowBufferLength);
        Timber.d("windern:searchFrame:buffer:%s",ComConfig.bytesToString(tmphanldeBuffer));

        boolean isFindWholeFrame = false;

        //循环索引，找到消息开头的位置
        int index = 0;
        //保留剩下的索引位置
        int leftIndex = index;
        while(index<nowBufferLength){
            //找到一个，看是不是完整记录，如果是处理，否则继续搜索
            if(buffer[index] == ComConfig.COMHead){
                //找到开头帧，先把剩余的位置标记到这里
                leftIndex = index;

                //后面还有一个字节，可以查找到长度的字段
                if(nowBufferLength>index+LENGTH_INDEX){
                    int Length = ComConfig.byteToInt(buffer[index+LENGTH_INDEX]);
                    //长度字段满足要求
                    if(Length>=ComConfig.COMLengthMin && Length<=ComConfig.COMLengthMax) {
                        //后面还有许多字节，满足这次消息的长度
                        //整个所有的长度为 Length + 6
                        if (nowBufferLength >= index + Length + 6) {
                            byte Source = buffer[index + SOURCE_INDEX];
                            byte Destination = buffer[index + DESTINATION_INDEX];
                            byte SerNumber = buffer[index + SERNUMBER_INDEX];
                            byte MainCmd = buffer[index + MAINCMD_INDEX];

                            boolean caseSource = (Source == ComConfig.COMAddr_A || Source == ComConfig.COMAddr_H
                                    || Source == ComConfig.COMAddr_P || Source == ComConfig.COMAddr_T);

                            boolean caseDestination = (Destination == ComConfig.COMAddr_A || Destination == ComConfig.COMAddr_H
                                    || Destination == ComConfig.COMAddr_P || Destination == ComConfig.COMAddr_T);

                            boolean caseSourceDestination = (Source != Destination);

                            if (caseSource && caseDestination && caseSourceDestination) {
                                if ((MainCmd == ComConfig.MAINCMD_COMMON && 4 == (Length % ByteCommand.ByteLength))
                                        || MainCmd != ComConfig.MAINCMD_COMMON) {
                                    byte[] crc32SourceBytes = new byte[Length + 1];
                                    for (int i = 0; i < crc32SourceBytes.length; i++) {
                                        crc32SourceBytes[i] = buffer[index + LENGTH_INDEX + i];
                                    }
                                    //计算得到的crc32
                                    int resultCrc32 = ComConfig.calcCrc32(crc32SourceBytes, crc32SourceBytes.length);
                                    byte[] resultByteCrc32 = new byte[4];
                                    for (int i = 0; i < resultByteCrc32.length; i++) {
                                        resultByteCrc32[i] = buffer[index + Length + 1 + 1 + i];
                                    }
                                    //返回的crc32
                                    int backResultCrc32 = ComConfig.byteArrayToInt(resultByteCrc32);
                                    if (resultCrc32 == backResultCrc32) {
                                        byte[] wholeFrameBytes = new byte[Length + 6];
                                        for(int i=0;i<wholeFrameBytes.length;i++){
                                            wholeFrameBytes[i]  = buffer[index+i];
                                        }

                                        Timber.d("windern:index:%s:wholeFrame:%s",index,ComConfig.bytesToString(wholeFrameBytes));
                                        isFindWholeFrame = handleWholeFrame(wholeFrameBytes);

                                        //如果校验失败，直接跳到下一个字节去
                                        //校验成功删除这条消息,直接找到下一条的位置
                                        index = index + Length + 6;
                                        //如果是完整帧，剩余位置标记到下一个字节
                                        leftIndex = index;
                                        //完成后直接继续
                                        continue;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //中途出现错误，没有找到完整帧，直接下一个字节
            index++;
        }

        //剩余的有变化
        if(leftIndex!=0) {
            byte[] tmpBuffer = new byte[1024];
            nowBufferLength = nowBufferLength - leftIndex;
            //删除之前没用的数据，不是开头或者已经找到完整帧的
            for (int i = 0; i < nowBufferLength; i++) {
                tmpBuffer[i] = buffer[leftIndex + i];
            }
            for(int i=0;i<nowBufferLength;i++){
                buffer[i] = tmpBuffer[i];
            }
        }

        //现在一个字节一个字节的收数据，如果收到就发送，压力很大，程序会一直跑
//        //如果没有找到，发送一个代表接收到了消息，但是格式不正确，需要发送新的消息过去
//        if(!isFindWholeFrame){
//            MotorBus.getInstance().receiveBackByteCommand(null);
//        }
    }

    /**
     * 处理完整一帧的数据
     * @param bytes
     * @return
     */
    public boolean handleWholeFrame(byte[] bytes){
        boolean isSuccess = false;

        byte Head = bytes[HEAD_INDEX];
        int Length = ComConfig.byteToInt(bytes[LENGTH_INDEX]);
        byte Source = bytes[SOURCE_INDEX];
        byte Destination = bytes[DESTINATION_INDEX];
        byte SerNumber = bytes[SERNUMBER_INDEX];
        byte MainCmd = bytes[MAINCMD_INDEX];

        if(Destination!= ComConfig.COMAddr_A)
        {
            //continue;
            return isSuccess;
        }

        switch(MainCmd)
        {
            case ComConfig.MAINCMD_COMMON:
                int backItemCount = (Length-4)/ByteCommand.ByteLength;

                ByteCommand[] arrayByteCommand = new ByteCommand[backItemCount];

                for (int i = 0; i < backItemCount; i++) {
                    byte[] oneByteCommandBytes = new byte[ByteCommand.ByteLength];
                    for (int j = 0; j < oneByteCommandBytes.length; j++) {
                        oneByteCommandBytes[j] = bytes[DATA_INDEX + i * 5 + j];
                    }
                    arrayByteCommand[i] = ByteCommand.convert(oneByteCommandBytes);
                }
                MotorBus.getInstance().receiveBackByteCommand(arrayByteCommand);
                isSuccess = true;
                break;
            default:
                break;
        }
        return isSuccess;
    }
}
