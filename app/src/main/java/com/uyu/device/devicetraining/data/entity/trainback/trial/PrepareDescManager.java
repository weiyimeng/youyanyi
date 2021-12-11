package com.uyu.device.devicetraining.data.entity.trainback.trial;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.trainpres.ApproachTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.FollowTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.FracturedRulerTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.GlanceTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.RGFixedVectorTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.RGVariableVectorTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.RedGreenReadTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.ReversalTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.StereoscopeTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumFusionTrain;
import com.uyu.device.devicetraining.presentation.AppProvider;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.ContentPrepareDesc;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.PrepareDesc;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.PrepareDescType;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.ReversalPrepareDesc;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.SelectPrepareDesc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by windern on 2016/1/4.
 */
public class PrepareDescManager {
    public static List<PrepareDesc> getDeviceOverallPrepareDescList() {
        ArrayList<PrepareDesc> descList = new ArrayList<>();
        descList.add(new PrepareDesc(R.mipmap.train_step_button, AppProvider.getApplication().getString(R.string.click_button_next_step)));
        descList.add(new PrepareDesc(R.mipmap.train_step_picture9, AppProvider.getApplication().getString(R.string.adjustment_device_height)));
        descList.add(new PrepareDesc(R.mipmap.train_step_picture1, AppProvider.getApplication().getString(R.string.wear_glasses)));
        descList.add(new PrepareDesc(R.mipmap.train_step_picture7, AppProvider.getApplication().getString(R.string.adjust_heght_low_look_screen)));
        descList.add(new PrepareDesc(R.mipmap.train_step_picture8, AppProvider.getApplication().getString(R.string.hand_in_key)));
        return descList;
    }

    /**
     * 获取准备阶段的图示说明
     *
     * @param trainPres
     * @return
     */
    public static List<PrepareDesc> getPrepareDescList(TrainPres trainPres) {
        List<PrepareDesc> descList = null;
        switch (trainPres.getTrainItemType()) {
            case STEREOSCOPE:
                descList = getPrepareDescList((StereoscopeTrainPres) trainPres);
                break;
            case FRACTURED_RULER:
                descList = getPrepareDescList((FracturedRulerTrainPres) trainPres);
                break;
            case REVERSAL:
                descList = getPrepareDescList((ReversalTrainPres) trainPres);
                break;
            case RED_GREEN_READ:
                descList = getPrepareDescList((RedGreenReadTrainPres) trainPres);
                break;
            case APPROACH:
                descList = getPrepareDescList((ApproachTrainPres) trainPres);
                break;
            case R_G_VARIABLE_VECTOR:
                descList = getPrepareDescList((RGVariableVectorTrainPres) trainPres);
                break;
            case R_G_FIXED_VECTOR:
                descList = getPrepareDescList((RGFixedVectorTrainPres) trainPres);
                break;
            case GLANCE:
                descList = getPrepareDescList((GlanceTrainPres) trainPres);
                break;
            case FOLLOW:
                descList = getPrepareDescList((FollowTrainPres) trainPres);
                break;
            default:
                descList = null;
                break;
        }
        return descList;
    }

    /**
     * 获取准备阶段的图示说明
     * 比较两个处方，如果相同删除一些步骤
     * 除了有道具的立体镜、裂隙尺、推进
     * @param trainPresPrevious
     * @param trainPres
     * @return
     */
    public static List<PrepareDesc> getPrepareDescList(TrainPres trainPresPrevious, TrainPres trainPres) {
        List<PrepareDesc> descList = null;
        switch (trainPres.getTrainItemType()) {
            case STEREOSCOPE:
                descList = getPrepareDescList((StereoscopeTrainPres) trainPres);
                if(trainPresPrevious!=null && trainPresPrevious.isSameInstrument(trainPres)){
                    descList.remove(1);
                    descList.remove(0);
                }
                break;
            case FRACTURED_RULER:
                descList = getPrepareDescList((FracturedRulerTrainPres) trainPres);
                if(trainPresPrevious!=null && trainPresPrevious.isSameInstrument(trainPres)){
                    descList.remove(2);
                    descList.remove(1);
                    descList.remove(0);
                }
                break;
            case REVERSAL:
                descList = getPrepareDescList((ReversalTrainPres) trainPres);
                break;
            case RED_GREEN_READ:
                descList = getPrepareDescList((RedGreenReadTrainPres) trainPres);
                break;
            case APPROACH:
                descList = getPrepareDescList((ApproachTrainPres) trainPres);
                if(trainPresPrevious!=null && trainPresPrevious.isSameInstrument(trainPres)){
                    descList.remove(1);
                    descList.remove(0);
                }
                break;
            case R_G_VARIABLE_VECTOR:
                descList = getPrepareDescList((RGVariableVectorTrainPres) trainPres);
                break;
            case R_G_FIXED_VECTOR:
                descList = getPrepareDescList((RGFixedVectorTrainPres) trainPres);
                break;
            case GLANCE:
                descList = getPrepareDescList((GlanceTrainPres) trainPres);
                break;
            case FOLLOW:
                descList = getPrepareDescList((FollowTrainPres) trainPres);
                break;
            default:
                descList = null;
                break;
        }
        return descList;
    }

    private static List<PrepareDesc> getPrepareDescList(StereoscopeTrainPres trainPres) {
        ArrayList<PrepareDesc> descList = new ArrayList<>();
        descList.add(new PrepareDesc(R.drawable.time_3, AppProvider.getApplication().getString(R.string.coordinated_spread_estimated_time_3minutes)));
        descList.add(new PrepareDesc(R.mipmap.train_step_stereoscope_1, AppProvider.getApplication().getString(R.string.pick_up_the_z_shaped_separator)));
        descList.add(new PrepareDesc(R.mipmap.train_step_stereoscope_2, AppProvider.getApplication().getString(R.string.z_shape_separator_install)));
        descList.add(new PrepareDesc(R.mipmap.train_step_picture8, AppProvider.getApplication().getString(R.string.press_key_start_train)));
        return descList;
    }

    private static List<PrepareDesc> getPrepareDescList(FracturedRulerTrainPres trainPres) {
        ArrayList<PrepareDesc> descList = new ArrayList<>();
        if (trainPres.getTrainingType() == EnumFusionTrain.BI) {
            descList.add(new PrepareDesc(R.mipmap.train_step_fr_2, AppProvider.getApplication().getString(R.string.pick_up_double_baffle)));
            descList.add(new PrepareDesc(R.mipmap.train_step_fr_2_2, AppProvider.getApplication().getString(R.string.double_baffle_install_mode)));
            descList.add(new PrepareDesc(R.mipmap.train_step_fr_2_3, AppProvider.getApplication().getString(R.string.double_baffle_success_install)));
        } else {
            descList.add(new PrepareDesc(R.mipmap.train_step_fr_1, AppProvider.getApplication().getString(R.string.take_up_single_baffle)));
            descList.add(new PrepareDesc(R.mipmap.train_step_fr_1_2, AppProvider.getApplication().getString(R.string.single_baffle_install_mode)));
            descList.add(new PrepareDesc(R.mipmap.train_step_fr_1_3, AppProvider.getApplication().getString(R.string.single_baffle_success_install)));
        }
        descList.add(new PrepareDesc(R.mipmap.train_step_picture8, AppProvider.getApplication().getString(R.string.press_key_start_train)));
        return descList;
    }

    private static List<PrepareDesc> getPrepareDescList(ReversalTrainPres trainPres) {
        ArrayList<PrepareDesc> descList = new ArrayList<>();
//        descList.add(new ReversalPrepareDesc(R.mipmap.train_step_picture5, "请选择" + trainPres.getEyeType().getName() +
//                "眼训练级别", PrepareDescType.EYELEVEL));
//        descList.add(new ContentPrepareDesc(R.mipmap.train_step_picture5, "选择训练内容"));
//        descList.add(new PrepareDesc(R.mipmap.train_step_picture5
//                , "请在app端下载选择您想训练的内容，默认我们会使用您选择的内容进行训练\n选择文章，我们将从您选择的文章开始循环显示缓存的内容\n选择新闻，我们将从您选择的新闻开始循环显示后面的新闻"
//                , PrepareDescType.USERCONTENT));
        descList.add(new PrepareDesc(R.drawable.time_5, AppProvider.getApplication().getString(R.string.coordinated_spread_estimated_time_5minutes)));
        descList.add(new PrepareDesc(R.mipmap.train_step_zm, AppProvider.getApplication().getString(R.string.see_clearly_screen_font_word_press_key)));
        descList.add(new PrepareDesc(R.mipmap.train_step_picture8, AppProvider.getApplication().getString(R.string.press_key_start_train)));
        return descList;
    }

    private static List<PrepareDesc> getPrepareDescList(RedGreenReadTrainPres trainPres) {
        ArrayList<PrepareDesc> descList = new ArrayList<>();
        descList.add(new ContentPrepareDesc(R.mipmap.train_step_picture5, AppProvider.getApplication().getString(R.string.select_read_content)));
        descList.add(new PrepareDesc(R.mipmap.train_step_picture5, AppProvider.getApplication().getString(R.string.select_font_size), PrepareDescType.TEXTSIZE));
        descList.add(new PrepareDesc(R.mipmap.train_step_picture8, AppProvider.getApplication().getString(R.string.press_key_start_train)));

        return descList;
    }

    private static List<PrepareDesc> getPrepareDescList(ApproachTrainPres trainPres) {
        ArrayList<PrepareDesc> descList = new ArrayList<>();
        descList.add(new PrepareDesc(R.mipmap.train_step_picture2, AppProvider.getApplication().getString(R.string.guide_rod_install)));
        descList.add(new PrepareDesc(R.mipmap.train_step_picture8, AppProvider.getApplication().getString(R.string.press_key_start_train)));
        return descList;
    }

    private static List<PrepareDesc> getPrepareDescList(RGVariableVectorTrainPres trainPres) {
        ArrayList<PrepareDesc> descList = new ArrayList<>();
        descList.add(new PrepareDesc(R.drawable.time_1,AppProvider.getApplication().getString(R.string.coordinated_spread_estimated_time_1minutes)));
        descList.add(new PrepareDesc(R.mipmap.train_step_rg_vv, AppProvider.getApplication().getString(R.string.square_x_circular_keep_a_line)));
        descList.add(new PrepareDesc(R.mipmap.train_step_picture8, AppProvider.getApplication().getString(R.string.press_key_start_train)));
        return descList;
    }

    private static List<PrepareDesc> getPrepareDescList(RGFixedVectorTrainPres trainPres) {
        ArrayList<PrepareDesc> descList = new ArrayList<>();
        descList.add(new PrepareDesc(R.mipmap.train_step_fv, AppProvider.getApplication().getString(R.string.look_a_picture_keep)));
        descList.add(new PrepareDesc(R.mipmap.train_step_picture8, AppProvider.getApplication().getString(R.string.press_key_start_train)));
        return descList;
    }

    private static List<PrepareDesc> getPrepareDescList(GlanceTrainPres trainPres) {
        ArrayList<PrepareDesc> descList = new ArrayList<>();
        descList.add(new ContentPrepareDesc(R.mipmap.train_step_picture5, AppProvider.getApplication().getString(R.string.select_read_content)));
        descList.add(new PrepareDesc(R.mipmap.train_step_picture8, AppProvider.getApplication().getString(R.string.press_key_start_train)));
        return descList;
    }

    private static List<PrepareDesc> getPrepareDescList(FollowTrainPres trainPres) {
        ArrayList<PrepareDesc> descList = new ArrayList<>();
        descList.add(new SelectPrepareDesc(R.mipmap.train_step_picture5, AppProvider.getApplication().getString(R.string.select_read_content), new String[]{AppProvider.getApplication().getString(R.string.straight_line), AppProvider.getApplication().getString(R.string.dotted_line), AppProvider.getApplication().getString(R.string.curve)}));
        descList.add(new PrepareDesc(R.mipmap.train_step_picture8, AppProvider.getApplication().getString(R.string.press_key_start_train)));
        return descList;
    }


    /**
     * 获取一次完成以后的图示说明
     *
     * @param trainPres
     * @return
     */
    public static List<PrepareDesc> getOneEndPrepareDescList(TrainPres trainPres) {
        List<PrepareDesc> descList = null;
        switch (trainPres.getTrainItemType()) {
            case STEREOSCOPE:
                descList = getOneEndPrepareDescListRestNormal((StereoscopeTrainPres) trainPres);
                break;
            case FRACTURED_RULER:
                descList = getOneEndPrepareDescListRestNormal((FracturedRulerTrainPres) trainPres);
                break;
            case REVERSAL:
                descList = getOneEndPrepareDescList((ReversalTrainPres) trainPres);
                break;
            case RED_GREEN_READ:
                descList = getOneEndPrepareDescList((RedGreenReadTrainPres) trainPres);
                break;
            case APPROACH:
                descList = getOneEndPrepareDescListRestNormal((ApproachTrainPres) trainPres);
                break;
            case R_G_VARIABLE_VECTOR:
                descList = getOneEndPrepareDescListRestNormal((RGVariableVectorTrainPres) trainPres);
                break;
            case R_G_FIXED_VECTOR:
                descList = getOneEndPrepareDescListRestNormal((RGFixedVectorTrainPres) trainPres);
                break;
            case GLANCE:
                descList = getOneEndPrepareDescListRestNormal((GlanceTrainPres) trainPres);
                break;
            case FOLLOW:
                descList = getOneEndPrepareDescListRestNormal((FollowTrainPres) trainPres);
                break;
            default:
                descList = null;
                break;
        }

        return descList;
    }

    private static List<PrepareDesc> getOneEndPrepareDescListRestNormal(TrainPres trainPres) {
        ArrayList<PrepareDesc> descList = new ArrayList<>();
        descList.add(new PrepareDesc(R.mipmap.train_step_picture2, AppProvider.getApplication().getString(R.string.take_a_break)));
        return descList;
    }

    private static List<PrepareDesc> getOneEndPrepareDescList(ReversalTrainPres trainPres) {
        ArrayList<PrepareDesc> descList = new ArrayList<>();
        descList.add(new PrepareDesc(R.mipmap.train_step_picture2, AppProvider.getApplication().getString(R.string.take_a_break)));
        descList.add(new ReversalPrepareDesc(R.mipmap.train_step_picture5, AppProvider.getApplication().getString(R.string.plese_select) + trainPres.getEyeType().getName() +
                AppProvider.getApplication().getString(R.string.see_train_level), PrepareDescType.EYELEVEL));
        descList.add(new ContentPrepareDesc(R.mipmap.train_step_picture5, AppProvider.getApplication().getString(R.string.train_content_adjust)));
        return descList;
    }

    private static List<PrepareDesc> getOneEndPrepareDescList(RedGreenReadTrainPres trainPres) {
        ArrayList<PrepareDesc> descList = new ArrayList<>();
        descList.add(new PrepareDesc(R.mipmap.train_step_picture2, AppProvider.getApplication().getString(R.string.prompt_rest)));
        descList.add(new PrepareDesc(R.mipmap.train_step_picture5, AppProvider.getApplication().getString(R.string.select_font_size), PrepareDescType.TEXTSIZE));
        descList.add(new ContentPrepareDesc(R.mipmap.train_step_picture5, AppProvider.getApplication().getString(R.string.train_content_adjust)));
        return descList;
    }
}
