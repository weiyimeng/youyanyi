package com.uyu.device.devicetraining.presentation.view.thirdly.picker.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.presentation.view.thirdly.picker.utils.ScreenInfo;
import com.uyu.device.devicetraining.presentation.view.thirdly.picker.view.WheelChoose;

import java.util.Date;

/**
 * 时间选择器
 *
 * @author Sai
 */
public class ChoosePopupWindow extends PopupWindow implements OnClickListener {
    public enum Type {
        ONE, TWO, THREE, FOUR
    }// 四种选择模式，显示一个，两个，三个，四个

    private View rootView; // 总的布局
    WheelChoose wheelChoose;
    private View btnSubmit, btnCancel;
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    private OnTimeSelectListener timeSelectListener;


    public ChoosePopupWindow(Context context, Type type, String title1, String title2) {
        this(context, type, title1, title2, null, null);
    }

    public ChoosePopupWindow(Context context, Type type, String title1) {
        this(context, type, title1, null, null, null);
    }


    public ChoosePopupWindow(Context context, Type type, String title1, String title2, String title3) {
        this(context, type, title1, title2, title3, null);
    }


    public ChoosePopupWindow(Context context, Type type, String title1, String title2, String title3, String title4) {
        super(context);
        this.setWidth(LayoutParams.FILL_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new BitmapDrawable());// 这样设置才能点击屏幕外dismiss窗口
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.picker_timepopwindow_anim_style);
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        rootView = mLayoutInflater.inflate(R.layout.uikit_pw_choose, null);
        // -----确定和取消按钮
        btnSubmit = rootView.findViewById(R.id.btnSubmit);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        btnCancel.setTag(TAG_CANCEL);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        // ----时间转轮
        final View choosepickerview = rootView.findViewById(R.id.choosepicker);
        ScreenInfo screenInfo = new ScreenInfo((Activity) context);
        wheelChoose = new WheelChoose(choosepickerview, type);
        wheelChoose.screenheight = screenInfo.getHeight();
        wheelChoose.setPicker(title1, title2, title3, title4);
        setContentView(rootView);
    }


    /**
     * 指定选中的时间，显示选择器
     *
     * @param parent
     * @param gravity
     * @param x
     * @param y
     * @param date
     */
    public void showAtLocation(View parent, int gravity, int x, int y, Date date) {
        update();
        super.showAtLocation(parent, gravity, x, y);
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wheelChoose.setCyclic(cyclic);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_CANCEL)) {
            dismiss();
            return;
        } else {
            if (timeSelectListener != null) {
                timeSelectListener.onTimeSelect(wheelChoose.getString());
            }
            dismiss();
            return;
        }
    }

    public interface OnTimeSelectListener {
        void onTimeSelect(String str);
    }

    public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
        this.timeSelectListener = timeSelectListener;
    }
}
