package com.uyu.device.devicetraining.presentation.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.type.EnumLineType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by windern on 2015/12/23.
 */
public class FollowInspectionDesc {
    @SerializedName("pic_name")
    @Expose
    private String picName;
    @SerializedName("line_type")
    @Expose
    private EnumLineType lineType;
    @SerializedName("line_count")
    @Expose
    private Integer lineCount;
    @SerializedName("start_points")
    @Expose
    private String startPoints;
    @SerializedName("end_points")
    @Expose
    private String endPoints;

    private Double accuracy = 0d;

    /**
     *
     * @return
     * The picName
     */
    public String getPicName() {
        return picName;
    }

    /**
     *
     * @param picName
     * The pic_name
     */
    public void setPicName(String picName) {
        this.picName = picName;
    }

    /**
     *
     * @return
     * The lineType
     */
    public EnumLineType getLineType() {
        return lineType;
    }

    /**
     *
     * @param lineType
     * The line_type
     */
    public void setLineType(EnumLineType lineType) {
        this.lineType = lineType;
    }

    /**
     *
     * @return
     * The lineCount
     */
    public Integer getLineCount() {
        return lineCount;
    }

    /**
     *
     * @param lineCount
     * The line_count
     */
    public void setLineCount(Integer lineCount) {
        this.lineCount = lineCount;
    }

    /**
     *
     * @return
     * The startPoints
     */
    public String getStartPoints() {
        return startPoints;
    }

    /**
     *
     * @param startPoints
     * The start_points
     */
    public void setStartPoints(String startPoints) {
        this.startPoints = startPoints;
    }

    /**
     *
     * @return
     * The endPoints
     */
    public String getEndPoints() {
        return endPoints;
    }

    /**
     *
     * @param endPoints
     * The end_points
     */
    public void setEndPoints(String endPoints) {
        this.endPoints = endPoints;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public String[] getArrayStartPoints(){
        String[] arrayStartPoints = TextUtils.split(startPoints,"-");
        return arrayStartPoints;
    }

    public void computeAccuracy(ArrayList<String> arrayResults){
        String[] arrayEndPoints = TextUtils.split(endPoints,"-");
        int correctCount = 0;
        int allCount = arrayEndPoints.length;
        for(int i=0;i<allCount;i++){
            if(arrayEndPoints[i].equals(arrayResults.get(i))){
                correctCount++;
            }
        }
        accuracy = correctCount/((double)allCount);
    }

//    public ArrayList<String> getRandomEnd(){
//        int arrayLength = arrayEnd.length;
//        ArrayList<String> arrayRandomEnd = new ArrayList<String>();
//        for(int i=0;i<arrayLength;i++){
//            arrayRandomEnd.add(arrayEnd[i]);
//        }
//        Collections.shuffle(arrayRandomEnd);
//        return arrayRandomEnd;
//    }
}
