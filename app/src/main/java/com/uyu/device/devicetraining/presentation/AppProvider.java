package com.uyu.device.devicetraining.presentation;

import android.app.Application;

/**
 * Created by jwc on 2016/11/28.
 */
public abstract class AppProvider {

    private static Application sApplication;

    public static void init(Application application){
        sApplication = application;
    }

    public static Application getApplication() {
        return sApplication;
    }

    private static EnumLanguageType sLanguageType;

    public static EnumLanguageType getLanguageType(){
        if(sLanguageType == null) {
            String tag = getApplication().getResources().getConfiguration().locale.getCountry().toString();
            if (tag.equals(EnumLanguageType.CHINESE.getValue())) {
                sLanguageType = EnumLanguageType.CHINESE;
            } else if (tag.equals(EnumLanguageType.ENGELISH.getValue())) {
                sLanguageType = EnumLanguageType.ENGELISH;
            }
        }
        return sLanguageType;
    }


    enum EnumLanguageType{
        CHINESE("CN","中文"),
        ENGELISH("US","英语");

        private String value;

        private String name;

        EnumLanguageType(String value,String name){
            this.value = value;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}
