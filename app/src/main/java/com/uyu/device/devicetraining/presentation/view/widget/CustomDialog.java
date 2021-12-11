package com.uyu.device.devicetraining.presentation.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.uyu.device.devicetraining.R;

/**
 * 消息提示对话框
 */
public class CustomDialog extends Dialog {

    /**
     * 标题控件
     **/
    private TextView tv_title;
    /**
     * 消息体控件
     **/
    private TextView tv_message;
    /**
     * 确定按钮
     **/
    private Button bt_confirm;
    /**
     * 隐藏按钮
     **/
    private Button bt_neutral;
    /**
     * 取消按钮
     **/
    private Button bt_cancel;
    /**
     * 显示图片
     **/
    private ImageView iv_preview;
    /**
     * 第一条分割线
     **/
    private View first_line;
    /**
     * 第二条分割线
     **/
    private View second_line;

    /**
     * 确定事件
     **/
    private OnClickListener confirm_btnClickListener;
    /**
     * 取消事件
     **/
    private OnClickListener cancel_btnClickListener;
    /**
     * 隐藏事件
     **/
    private OnClickListener neutral_btnClickListener;


    public CustomDialog(Context context) {
        super(context, R.style.mystyle); //默认主题
        initView();
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme); //自定义主题
        initView();
    }

    private void initView() {
        View contentView = View.inflate(getContext(), R.layout.uialert_customdialog, null);
        setContentView(contentView);

        first_line = findViewById(R.id.first_line);
        second_line = findViewById(R.id.second_line);
        tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        tv_message = (TextView) contentView.findViewById(R.id.tv_message);
        iv_preview = (ImageView) contentView.findViewById(R.id.iv_preview);
        bt_confirm = (Button) contentView.findViewById(R.id.bt_confirm);
        bt_neutral = (Button) contentView.findViewById(R.id.bt_neutral);
        bt_cancel = (Button) contentView.findViewById(R.id.bt_cancel);

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        p.width = (int) (dm.widthPixels * 0.8); // 宽度设置为屏幕的0.65
        p.alpha = 1;
        dialogWindow.setAttributes(p);
    }

    public CustomDialog setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
        return this;
    }

    public CustomDialog setMessage(String message) {
        if (!TextUtils.isEmpty(message)) {
            tv_message.setVisibility(View.VISIBLE);
            tv_message.setText(message);
        }
        return this;
    }

    public CustomDialog setPreview(String preview) {
        if (!TextUtils.isEmpty(preview)) {
            iv_preview.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(preview, iv_preview);
        }
        return this;
    }


    public CustomDialog setCancelButton(String cancel, @Nullable OnClickListener cancel_btnClickListener) {
        if (!TextUtils.isEmpty(cancel)) {
            this.cancel_btnClickListener = cancel_btnClickListener;
            bt_cancel.setText(cancel);
            bt_cancel.setVisibility(View.VISIBLE);
            bt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CustomDialog.this.cancel_btnClickListener != null)
                        CustomDialog.this.cancel_btnClickListener.onClick(CustomDialog.this, 1);
                    else
                        dismiss();
                }
            });
        }

        return this;
    }

    public CustomDialog setNeutralButton(String neutral,@Nullable OnClickListener neutral_btnClickListener) {
        if (!TextUtils.isEmpty(neutral)) {
            this.neutral_btnClickListener = neutral_btnClickListener;
            bt_neutral.setVisibility(View.VISIBLE);
            first_line.setVisibility(View.VISIBLE);
            bt_neutral.setText(neutral);
            bt_neutral.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CustomDialog.this.neutral_btnClickListener != null)
                        CustomDialog.this.neutral_btnClickListener.onClick(CustomDialog.this, 1);
                    else
                        dismiss();
                }
            });
        }
        return this;
    }


    public CustomDialog setConfirmButton(String confirm, OnClickListener confirm_btnClickListener) {

        if (!TextUtils.isEmpty(confirm)) {
            this.confirm_btnClickListener = confirm_btnClickListener;
            this.bt_confirm.setText(confirm);
            this.second_line.setVisibility(View.VISIBLE);
            this.bt_confirm.setVisibility(View.VISIBLE);
            this.bt_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CustomDialog.this.confirm_btnClickListener != null)
                        CustomDialog.this.confirm_btnClickListener.onClick(CustomDialog.this, 1);
                }
            });
        }
        return this;
    }
}
