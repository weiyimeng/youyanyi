package com.uyu.device.devicetraining.data.entity.content;

import com.uyu.device.devicetraining.data.entity.type.EnumContentType;
import com.uyu.device.devicetraining.data.entity.type.EnumShowContentType;
import com.uyu.device.devicetraining.presentation.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by windern on 16/7/7.
 */
public class ContentManager {
    /**
     * 句子的关键字：中文逗号和句号
     */
    private static String[] SentenceKeys = {"，","。"};
    /**
     * 词语的关键字：中文的逗号、英文的逗号、间隔符
     */
    private static String[] WordKeys = {"，","。",",",".","-"};

    /**
     * 非空字段才画
     * @param showletter
     * @return
     */
    public static boolean isShow(String showletter){
        //非空、非换行符
        return !(showletter.equals("") ||
                showletter.equals(" ") ||//手动打的空格
                showletter.equals(" ") ||//输出显示空格' ' 32
                showletter.equals("　") ||//输出显示tab'　' 12288
                showletter.equals("\t") ||
                showletter.equals("\r") ||
                showletter.equals("\n")
        );
    }

    /**
     * 去除空字符
     * @param src
     * @return
     */
    public static String removeEmptyChar(String src){
        String dst = src;
        //ios那边存文件，没管字符去除，这边也不管
//        dst = dst.replace(" ","");//手动打的空格
//        dst = dst.replace("\t","");
//        dst = dst.replace("\r","");
//        dst = dst.replace("\n","");
//        dst = dst.replace(" ","");//输出显示空格' ' 32
//        dst = dst.replace("　","");//输出显示tab'　' 12288
        return dst;
    }

    /**
     * 是否是句子的关键字
     * @param showletter
     * @return
     */
    public static boolean isSentenceKey(String showletter){
        boolean isKey = false;
        for(String key : SentenceKeys){
            if(key.equals(showletter)){
                isKey = true;
            }
        }
        return isKey;
    }

    /**
     * 是否是词语的关键字
     * @param showletter
     * @return
     */
    public static boolean isWordKey(String showletter){
        boolean isKey = false;
        for(String key : WordKeys){
            if(key.equals(showletter)){
                isKey = true;
            }
        }
        return isKey;
    }

    /**
     * 生成显示的内容
     * 预先生成好，就跟文章的逻辑一样了
     * @param contentType
     * @param maxCount
     * @return
     */
    public static List<ShowContent> createShowContentList(EnumContentType contentType, int maxCount){
        List<ShowContent> listShowContent = new ArrayList<>();
        String previousRandomLetters = "";
        int count = 0;
        int letterCount = 3;
        while(count<maxCount){
            String randomLetters = Util.getOneRandomText(letterCount);
            if(contentType== EnumContentType.NUMBER){
                randomLetters = Util.getOneRandomNumber(letterCount);
            }
            if(!previousRandomLetters.equals(randomLetters)){
                ShowContent showContent = new ShowContent();
                showContent.setTitle(contentType.getName());
                showContent.setContent(randomLetters);
                showContent.setShowContentType(EnumShowContentType.NORMAL);
                showContent.setTrainingContentType(contentType);
                listShowContent.add(showContent);
                count++;

                previousRandomLetters = randomLetters;
            }
        }

        return listShowContent;
    }
}
