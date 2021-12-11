package com.uyu.device.devicetraining.data.entity.selfhelp;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by windern on 2016/4/5.
 */
public class PushTrainContentContent extends ServerEmqttMessageContent {
    @SerializedName("mode")
    @Expose
    private int mode;
    @SerializedName("content_type")
    @Expose
    private int contentType;
    @SerializedName("cate_id")
    @Expose
    private int cateId;
    @SerializedName("content_id")
    @Expose
    private int contentId;
    @SerializedName("content_pos")
    @Expose
    private int contentPos;

    /**
     *
     * @return
     * The mode
     */
    public int getMode() {
        return mode;
    }

    /**
     *
     * @param mode
     * The mode
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     *
     * @return
     * The contentType
     */
    public int getContentType() {
        return contentType;
    }

    /**
     *
     * @param contentType
     * The content_type
     */
    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    /**
     *
     * @return
     * The cateId
     */
    public int getCateId() {
        return cateId;
    }

    /**
     *
     * @param cateId
     * The cate_id
     */
    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    /**
     *
     * @return
     * The contentId
     */
    public int getContentId() {
        return contentId;
    }

    /**
     *
     * @param contentId
     * The content_id
     */
    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    /**
     *
     * @return
     * The contentPos
     */
    public int getContentPos() {
        return contentPos;
    }

    /**
     *
     * @param contentPos
     * The content_pos
     */
    public void setContentPos(int contentPos) {
        this.contentPos = contentPos;
    }

    public static PushTrainContentContent convert(JSONObject jsonObject) throws Exception {
        try {
            Gson gson = new Gson();
            PushTrainContentContent pushTrainContentContent = gson.fromJson(jsonObject.toString(),PushTrainContentContent.class);

            return pushTrainContentContent;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public JSONObject toJson() throws Exception {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(this);

            JSONObject jsonObject = new JSONObject(json);

            return jsonObject;
        }catch (JSONException e){
            throw e;
        }
    }
}
