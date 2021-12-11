package com.uyu.device.devicetraining.presentation.model;

import com.uyu.device.devicetraining.data.entity.type.EnumLineType;

import java.util.ArrayList;

/**
 * Created by windern on 2015/12/23.
 */
public class FollowInspectionDescManager {
    private static ArrayList<FollowInspectionDesc> list = null;
    private static void createList(){
        list = new ArrayList<>();

        EnumLineType lineType = EnumLineType.STRAIGHT;
        FollowInspectionDesc followInspectionDesc01 = new FollowInspectionDesc();
        followInspectionDesc01.setPicName("follow_0_1");
        followInspectionDesc01.setLineType(lineType);
        followInspectionDesc01.setLineCount(2);
        followInspectionDesc01.setStartPoints("A-B");
        followInspectionDesc01.setEndPoints("2-1");
        list.add(followInspectionDesc01);

        FollowInspectionDesc followInspectionDesc02 = new FollowInspectionDesc();
        followInspectionDesc02.setPicName("follow_0_2");
        followInspectionDesc02.setLineType(lineType);
        followInspectionDesc02.setLineCount(2);
        followInspectionDesc02.setStartPoints("A-B");
        followInspectionDesc02.setEndPoints("2-1");
        list.add(followInspectionDesc02);

        FollowInspectionDesc followInspectionDesc03 = new FollowInspectionDesc();
        followInspectionDesc03.setPicName("follow_0_3");
        followInspectionDesc03.setLineType(lineType);
        followInspectionDesc03.setLineCount(3);
        followInspectionDesc03.setStartPoints("A-B-C");
        followInspectionDesc03.setEndPoints("3-2-1");
        list.add(followInspectionDesc03);

        FollowInspectionDesc followInspectionDesc04 = new FollowInspectionDesc();
        followInspectionDesc04.setPicName("follow_0_4");
        followInspectionDesc04.setLineType(lineType);
        followInspectionDesc04.setLineCount(3);
        followInspectionDesc04.setStartPoints("A-B-C");
        followInspectionDesc04.setEndPoints("2-1-3");
        list.add(followInspectionDesc04);

        lineType = EnumLineType.CURVE;
        FollowInspectionDesc followInspectionDesc11 = new FollowInspectionDesc();
        followInspectionDesc11.setPicName("follow_1_1");
        followInspectionDesc11.setLineType(lineType);
        followInspectionDesc11.setLineCount(2);
        followInspectionDesc11.setStartPoints("A-B");
        followInspectionDesc11.setEndPoints("2-1");
        list.add(followInspectionDesc11);

        FollowInspectionDesc followInspectionDesc12 = new FollowInspectionDesc();
        followInspectionDesc12.setPicName("follow_1_2");
        followInspectionDesc12.setLineType(lineType);
        followInspectionDesc12.setLineCount(2);
        followInspectionDesc12.setStartPoints("A-B");
        followInspectionDesc12.setEndPoints("2-1");
        list.add(followInspectionDesc12);

        FollowInspectionDesc followInspectionDesc13 = new FollowInspectionDesc();
        followInspectionDesc13.setPicName("follow_1_3");
        followInspectionDesc13.setLineType(lineType);
        followInspectionDesc13.setLineCount(3);
        followInspectionDesc13.setStartPoints("A-B-C");
        followInspectionDesc13.setEndPoints("3-1-2");
        list.add(followInspectionDesc13);

        FollowInspectionDesc followInspectionDesc14 = new FollowInspectionDesc();
        followInspectionDesc14.setPicName("follow_1_4");
        followInspectionDesc14.setLineType(lineType);
        followInspectionDesc14.setLineCount(3);
        followInspectionDesc14.setStartPoints("A-B-C");
        followInspectionDesc14.setEndPoints("2-3-1");
        list.add(followInspectionDesc14);

        lineType = EnumLineType.DASHED;
        FollowInspectionDesc followInspectionDesc21 = new FollowInspectionDesc();
        followInspectionDesc21.setPicName("follow_2_1");
        followInspectionDesc21.setLineType(lineType);
        followInspectionDesc21.setLineCount(2);
        followInspectionDesc21.setStartPoints("A-B");
        followInspectionDesc21.setEndPoints("2-1");
        list.add(followInspectionDesc21);

        FollowInspectionDesc followInspectionDesc22 = new FollowInspectionDesc();
        followInspectionDesc22.setPicName("follow_2_2");
        followInspectionDesc22.setLineType(lineType);
        followInspectionDesc22.setLineCount(2);
        followInspectionDesc22.setStartPoints("A-B");
        followInspectionDesc22.setEndPoints("2-1");
        list.add(followInspectionDesc22);

        FollowInspectionDesc followInspectionDesc23 = new FollowInspectionDesc();
        followInspectionDesc23.setPicName("follow_2_3");
        followInspectionDesc23.setLineType(lineType);
        followInspectionDesc23.setLineCount(3);
        followInspectionDesc23.setStartPoints("A-B-C");
        followInspectionDesc23.setEndPoints("1-3-2");
        list.add(followInspectionDesc23);

        FollowInspectionDesc followInspectionDesc24 = new FollowInspectionDesc();
        followInspectionDesc24.setPicName("follow_2_4");
        followInspectionDesc24.setLineType(lineType);
        followInspectionDesc24.setLineCount(3);
        followInspectionDesc24.setStartPoints("A-B-C");
        followInspectionDesc24.setEndPoints("2-1-3");
        list.add(followInspectionDesc24);
    }

    public static ArrayList<FollowInspectionDesc> getList(EnumLineType lineType,int lineCount,int picCount){
        if(list==null){
            createList();
        }

        ArrayList<FollowInspectionDesc> listOne = new ArrayList<>();

        int i=0;
        while(i<picCount){
            for(int j=0;j<list.size() && i<picCount;j++){
                if(list.get(j).getLineType()==lineType){
                    listOne.add(list.get(j));
                    i++;
                }
            }
        }

        return listOne;
    }
}
