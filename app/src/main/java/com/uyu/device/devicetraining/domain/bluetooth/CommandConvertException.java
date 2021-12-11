package com.uyu.device.devicetraining.domain.bluetooth;

/**
 * Created by windern on 2016/4/27.
 */
public class CommandConvertException extends Exception{
    public CommandConvertException(){
        super();
    }

    public CommandConvertException(String detailMessage){
        super(detailMessage);
    }
}
