package com.uyu.device.devicetraining.presentation.model;

/**
 * Created by windern on 2015/12/8.
 */
public class VisionCardLevel {
    /**
     * 名字
     */
    private String Name;
    /**
     * 视标大小mm
     */
    private float TextSize;
    /**
     * 分值
     */
    private int Score;

    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public float getTextSize() {
        return TextSize;
    }
    public void setTextSize(float textSize) {
        TextSize = textSize;
    }
    public int getScore() {
        return Score;
    }
    public void setScore(int score) {
        Score = score;
    }

    public VisionCardLevel(){
    }

    public VisionCardLevel(String name, float textSize) {
        Name = name;
        TextSize = textSize;
    }

    public VisionCardLevel(String Name, float TextSize, int Score){
        this.Name = Name;
        this.TextSize = TextSize;
        this.Score = Score;
    }
}
