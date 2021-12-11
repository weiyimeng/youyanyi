package app.update.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import update.app.updatelib.R;

/**
 * 更新提示对话框
 */
public class UpdateDialog extends Dialog {

    private DialogInterface.OnClickListener upgrade_btnClickListener;
    private DialogInterface.OnClickListener cancel_btnClickListener;
    private Button bt_upgrade;
    private Button bt_cancel;
    private TextView tv_message;

    public UpdateDialog(Context context) {
        super(context, R.style.UpdateDialogStyle); //默认主题
        initView();
    }

    public UpdateDialog(Context context, int theme) {
        super(context, theme); //自定义主题
        initView();
    }

    private void initView() {
        View contentView = View.inflate(getContext(), R.layout.dialog_update, null);
        bt_cancel = (Button) contentView.findViewById(R.id.bt_cancel);
        bt_upgrade = (Button) contentView.findViewById(R.id.bt_upgrade);
        tv_message = (TextView) contentView.findViewById(R.id.tv_message);
        setContentView(contentView);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        p.width = (int) (dm.widthPixels * 0.7); // 宽度设置为屏幕的0.65
        p.alpha = 1;
        dialogWindow.setAttributes(p);
    }

    /**
     * 设置取消按钮点击事件
     * @param cancel
     * @param cancel_btnClickListener
     * @return
     */
    public UpdateDialog setCancelButton(String cancel, DialogInterface.OnClickListener cancel_btnClickListener) {
        if (!TextUtils.isEmpty(cancel)) {
            this.cancel_btnClickListener = cancel_btnClickListener;
            bt_cancel.setText(cancel);
            bt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UpdateDialog.this.cancel_btnClickListener != null)
                        UpdateDialog.this.cancel_btnClickListener.onClick(UpdateDialog.this, 1);
                    else
                        dismiss();
                }
            });
        }

        return this;
    }

    /**
     * 设置升级按钮点击事件
     * @param confirm
     * @param confirm_btnClickListener
     * @return
     */
    public UpdateDialog setUpgradeButton(String confirm, DialogInterface.OnClickListener confirm_btnClickListener) {
        if (!TextUtils.isEmpty(confirm)) {
            this.upgrade_btnClickListener = confirm_btnClickListener;
            this.bt_upgrade.setText(confirm);
            this.bt_upgrade.setVisibility(View.VISIBLE);
            this.bt_upgrade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UpdateDialog.this.upgrade_btnClickListener != null)
                        UpdateDialog.this.upgrade_btnClickListener.onClick(UpdateDialog.this, 1);
                }
            });
        }
        return this;
    }

    public UpdateDialog setForce(boolean isForce){
        if (isForce){
            this.bt_cancel.setVisibility(View.GONE);
        }
        return this;
    }

    public void setMessage(String message) {
        this.tv_message.setText(message);
    }
}
