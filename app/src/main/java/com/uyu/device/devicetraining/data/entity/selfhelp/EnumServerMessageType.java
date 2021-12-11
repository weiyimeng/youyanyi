package com.uyu.device.devicetraining.data.entity.selfhelp;

/**
 * Created by windern on 2015/12/5.
 */
public enum EnumServerMessageType {

    SELFHELP_CREATE_TRAIN(0,"SELFHELP_CREATE_TRAIN","自助创建训练"),
    SELFHELP_FINISH_TRAIN(1,"SELFHELP_FINISH_TRAIN","自助结束训练"),
    SELFHELP_LOCK_TRAIN(2,"SELFHELP_LOCK_TRAIN","自助锁定训练"),
    SELFHELP_UNLOCK_TRAIN(3,"SELFHELP_UNLOCK_TRAIN","自助解锁训练"),
    CONTROL_EXIT_TRAIN(4,"CONTROL_EXIT_TRAIN","控制退出训练"),
    SELFHELP_CREATE_TRAIN_TRIAL(5,"SELFHELP_CREATE_TRAIN_TRIAL","创建自助体验训练"),
    PUSH_TRAIN_CONTENT(6,"PUSH_TRAIN_CONTENT","推送训练内容");

    /**
     * 值
     */
    private int value;

    /**
     * 显示名称
     */
    private String key;

    /**
     * 显示名称
     */
    private String showName;

    /*
     * 构造方法
     */
    private EnumServerMessageType(int value,String key,String showName) {
        this.value = value;
        this.key = key;
        this.showName = showName;
    }

    public int getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    /**
     * 获取显示名称
     * @return
     */
    public String getShowName() {
        return showName;
    }


    @Override
    public String toString() {
        return super.toString();
    }

    public static EnumServerMessageType getValueOf(String key){
        for(EnumServerMessageType type: EnumServerMessageType.values()){
            if(type.getKey().equals(key)){
                return type;
            }
        }
        return null;
    }
}
