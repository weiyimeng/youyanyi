package com.uyu.device.devicetraining.presentation;

import android.app.Application;

import com.uyu.device.devicetraining.R;

/**
 * raw资源提供类
 * Created by jwc on 2016/12/15.
 */
public class RawProvider {

    /**
     * 中文标识
     */
    public static final String CHINESE = "CN";
    /**
     * 英语标识
     */
    public static final String ENGLISH = "US";

    /**
     * 资源id
     */
    public static int adjust_device_finish;
    public static int approach_end_move_tip;
    public static int approach_finish_tip;
    public static int approach_one_keep_tip;
    public static int approach_start_tip;
    public static int approach_two_move_tip;
    public static int device_prepare;
    public static int device_prepare_finish;
    public static int finish_tip;
    public static int fractured_ruler_finish_tip;
    public static int fractured_ruler_fusing_tip;
    public static int fractured_ruler_keeping_tip;
    public static int fractured_ruler_pass_one_tip;
    public static int glance_prompt;
    public static int intro_content;
    public static int one_item_finish_tip;
    public static int r_g_variable_vector_end_move_tip;
    public static int r_g_variable_vector_finish_tip;
    public static int r_g_variable_vector_fusing_tip;
    public static int r_g_variable_vector_keeping_tip;
    public static int r_g_variable_vector_one_keep_tip;
    public static int r_g_variable_vector_start_tip;
    public static int r_g_variable_vector_two_move_tip;
    public static int raw_key_file;
    public static int reversal_tip;
    public static int show_result;
    public static int stereoscope_fusing_tip;
    public static int stereoscope_keeping_tip;
    public static int wait_adjust_device;


    /**
     * 初始化方法
     */
    public static void init(Application application){
        // 获取当前系统语言
        String tag = application.getResources().getConfiguration().locale.getCountry().toString();
        setResourceId(tag);
    }

    /**
     * 设置资源id
     * @param tag
     */
    public static void setResourceId(String tag){
        if(tag.equals(CHINESE)){
            adjust_device_finish = R.raw.adjust_device_finish;
            approach_end_move_tip = R.raw.approach_end_move_tip;
            approach_finish_tip = R.raw.approach_finish_tip;
            approach_one_keep_tip = R.raw.approach_one_keep_tip;
            approach_start_tip = R.raw.approach_start_tip;
            approach_two_move_tip = R.raw.approach_two_move_tip;
            device_prepare = R.raw.device_prepare;
            device_prepare_finish = R.raw.device_prepare_finish;
            finish_tip = R.raw.finish_tip;
            fractured_ruler_finish_tip = R.raw.fractured_ruler_finish_tip;
            fractured_ruler_fusing_tip = R.raw.fractured_ruler_fusing_tip;
            fractured_ruler_keeping_tip = R.raw.fractured_ruler_keeping_tip;
            fractured_ruler_pass_one_tip = R.raw.fractured_ruler_pass_one_tip;
            glance_prompt = R.raw.glance_prompt;
            intro_content = R.raw.intro_content;
            one_item_finish_tip = R.raw.one_item_finish_tip;
            r_g_variable_vector_end_move_tip = R.raw.r_g_variable_vector_end_move_tip;
            r_g_variable_vector_finish_tip = R.raw.r_g_variable_vector_finish_tip;
            r_g_variable_vector_fusing_tip = R.raw.r_g_variable_vector_fusing_tip;
            r_g_variable_vector_keeping_tip = R.raw.r_g_variable_vector_keeping_tip;
            r_g_variable_vector_one_keep_tip = R.raw.r_g_variable_vector_one_keep_tip;
            r_g_variable_vector_start_tip = R.raw.r_g_variable_vector_start_tip;
            r_g_variable_vector_two_move_tip = R.raw.r_g_variable_vector_two_move_tip;
            raw_key_file = R.raw.raw_key_file;
            reversal_tip = R.raw.reversal_tip;
            show_result = R.raw.show_result;
            stereoscope_fusing_tip = R.raw.stereoscope_fusing_tip;
            stereoscope_keeping_tip = R.raw.stereoscope_keeping_tip;
            wait_adjust_device = R.raw.wait_adjust_device;
        }else if(tag.equals(ENGLISH)){
            adjust_device_finish = R.raw.adjust_device_finish_english;
            approach_end_move_tip = R.raw.approach_end_move_tip_english;
            approach_finish_tip = R.raw.approach_finish_tip_english;
            approach_one_keep_tip = R.raw.approach_one_keep_tip_english;
            approach_start_tip = R.raw.approach_start_tip_english;
            approach_two_move_tip = R.raw.approach_two_move_tip_english;
            device_prepare = R.raw.device_prepare_english;
            device_prepare_finish = R.raw.device_prepare_finish_english;
            finish_tip = R.raw.finish_tip_english;
            fractured_ruler_finish_tip = R.raw.fractured_ruler_finish_tip_english;
            fractured_ruler_fusing_tip = R.raw.fractured_ruler_fusing_tip_english;
            fractured_ruler_keeping_tip = R.raw.fractured_ruler_keeping_tip_english;
            fractured_ruler_pass_one_tip = R.raw.fractured_ruler_pass_one_tip_english;
            glance_prompt = R.raw.glance_prompt_english;
            intro_content = R.raw.intro_content_english;
            one_item_finish_tip = R.raw.one_item_finish_tip_english;
            intro_content = R.raw.intro_content_english;
            r_g_variable_vector_end_move_tip = R.raw.r_g_variable_vector_end_move_tip_english;
            r_g_variable_vector_finish_tip = R.raw.r_g_variable_vector_finish_tip_english;
            r_g_variable_vector_fusing_tip = R.raw.r_g_variable_vector_fusing_tip_english;
            r_g_variable_vector_keeping_tip = R.raw.r_g_variable_vector_keeping_tip_english;
            r_g_variable_vector_one_keep_tip = R.raw.r_g_variable_vector_one_keep_tip_english;
            r_g_variable_vector_start_tip = R.raw.r_g_variable_vector_start_tip_english;
            r_g_variable_vector_two_move_tip = R.raw.r_g_variable_vector_two_move_tip_english;
            reversal_tip = R.raw.reversal_tip_english;
            show_result = R.raw.show_result_english;
            stereoscope_fusing_tip = R.raw.stereoscope_fusing_tip_english;
            stereoscope_keeping_tip = R.raw.stereoscope_keeping_tip_english;
            wait_adjust_device = R.raw.wait_adjust_device_english;
        }else{
            adjust_device_finish = R.raw.adjust_device_finish;
            approach_end_move_tip = R.raw.approach_end_move_tip;
            approach_finish_tip = R.raw.approach_finish_tip;
            approach_one_keep_tip = R.raw.approach_one_keep_tip;
            approach_start_tip = R.raw.approach_start_tip;
            approach_two_move_tip = R.raw.approach_two_move_tip;
            device_prepare = R.raw.device_prepare;
            device_prepare_finish = R.raw.device_prepare_finish;
            finish_tip = R.raw.finish_tip;
            fractured_ruler_finish_tip = R.raw.fractured_ruler_finish_tip;
            fractured_ruler_fusing_tip = R.raw.fractured_ruler_fusing_tip;
            fractured_ruler_keeping_tip = R.raw.fractured_ruler_keeping_tip;
            fractured_ruler_pass_one_tip = R.raw.fractured_ruler_pass_one_tip;
            glance_prompt = R.raw.glance_prompt;
            intro_content = R.raw.intro_content;
            one_item_finish_tip = R.raw.one_item_finish_tip;
            r_g_variable_vector_end_move_tip = R.raw.r_g_variable_vector_end_move_tip;
            r_g_variable_vector_finish_tip = R.raw.r_g_variable_vector_finish_tip;
            r_g_variable_vector_fusing_tip = R.raw.r_g_variable_vector_fusing_tip;
            r_g_variable_vector_keeping_tip = R.raw.r_g_variable_vector_keeping_tip;
            r_g_variable_vector_one_keep_tip = R.raw.r_g_variable_vector_one_keep_tip;
            r_g_variable_vector_start_tip = R.raw.r_g_variable_vector_start_tip;
            r_g_variable_vector_two_move_tip = R.raw.r_g_variable_vector_two_move_tip;
            raw_key_file = R.raw.raw_key_file;
            reversal_tip = R.raw.reversal_tip;
            show_result = R.raw.show_result;
            stereoscope_fusing_tip = R.raw.stereoscope_fusing_tip;
            stereoscope_keeping_tip = R.raw.stereoscope_keeping_tip;
            wait_adjust_device = R.raw.wait_adjust_device;
        }
    }

}
