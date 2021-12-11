package com.uyu.device.devicetraining.data.entity.trainback.trial;

import com.uyu.device.devicetraining.data.entity.config.trial.ReversalConfig;
import com.uyu.device.devicetraining.data.entity.trainback.ReversalTrainBack;
import com.uyu.device.devicetraining.data.entity.util.TrialScoreManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2016/6/2.
 */
public class TrialReversalTrainBack extends ReversalTrainBack {
    public JSONObject toJson() throws JSONException {
        try {
            JSONObject jsonObject = super.toJson();
            jsonObject.remove("reversal_train_back_details");

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }

    /**
     * 计算得分
     * 为实最终级数的上一级的最高得分加上该级按比例的得分。
     */
    public void computeScore(){
        int asthenopiaScore = TrialScoreManager.computeReversalTrain(eyeType,reversal_train_back_details);
        int xscore = 100-asthenopiaScore;
        int glassLevel = lNegativeDegreeLevel;
        int startScore = 0;
        if(glassLevel>0){
            startScore = ReversalConfig.arrayLevelSectionScore[glassLevel-1];
        }
        int endScore = ReversalConfig.arrayLevelSectionScore[glassLevel];

        // 四舍五入计算方式
        score = startScore + Math.round(((endScore-startScore)*xscore/100f));

        //最低得分1分
        if(score<=1){
            score = 1;
        }
    }
}
