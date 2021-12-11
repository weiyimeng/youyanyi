package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.config.FinishDescManager;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/1/8 0008.
 */
public class TrainAllFinishView extends LinearLayout {
    public interface TrainAllFinishViewListener{
        void onFinishViewClick();
    }

    private Context context;
    private TrainAllFinishViewListener finishViewListener;
    private EnumTrainItem enumTrainItem;


    public EnumTrainItem getEnumTrainItem() {
        return enumTrainItem;
    }

    public void setEnumTrainItem(EnumTrainItem enumTrainItem) {
        this.enumTrainItem = enumTrainItem;
    }

    public TrainAllFinishViewListener getFinishViewListener() {
        return finishViewListener;
    }

    public void setFinishViewListener(TrainAllFinishViewListener finishViewListener) {
        this.finishViewListener = finishViewListener;
    }

    @Bind(R.id.finish_tips_text)
    TextView finishTxt;
    @Bind(R.id.next_train_item_btn)
    Button nextBtn;

    public TrainAllFinishView(Context context) {
        super(context);
        this.context = context;
        initViews();
    }

    public TrainAllFinishView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initViews();
    }

    public TrainAllFinishView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initViews();
    }

    private void initViews() {
        View mainView = LayoutInflater.from(context).inflate(R.layout.train_finish_view, this, true);
        ButterKnife.bind(this, mainView);
    }

    public void setFinishTxt() {
        switch (enumTrainItem) {
            case STEREOSCOPE:
                finishTxt.setText(FinishDescManager.STEREOSCOPE_FINISH[0] + "\n\n" + FinishDescManager.STEREOSCOPE_FINISH[1] + "\n\n");
                break;
            case FRACTURED_RULER:
                finishTxt.setText(FinishDescManager.FRACTURED_RULER_FINISH[0] + "\n\n" + FinishDescManager.FRACTURED_RULER_FINISH[1] + "\n\n");
                break;
            case REVERSAL:
                finishTxt.setText(FinishDescManager.NORMAL_FINISH[0] + "\n\n");
                break;
            case R_G_VARIABLE_VECTOR:
                finishTxt.setText(FinishDescManager.NORMAL_FINISH[0] + "\n\n");
                break;
            case RED_GREEN_READ:
                finishTxt.setText(FinishDescManager.NORMAL_FINISH[0] + "\n\n");
                break;
            case APPROACH:
                finishTxt.setText(FinishDescManager.APPROACH_FINISH[0] + "\n\n" + FinishDescManager.APPROACH_FINISH[1]);
                break;
            case R_G_FIXED_VECTOR:
                finishTxt.setText(FinishDescManager.NORMAL_FINISH[0] + "\n\n");
                break;
            case FOLLOW:
                finishTxt.setText(FinishDescManager.NORMAL_FINISH[0] + "\n\n");
                break;
            case GLANCE:
                finishTxt.setText(FinishDescManager.NORMAL_FINISH[0] + "\n\n");
                break;
        }
    }

    @OnClick(R.id.next_train_item_btn)
    public void next() {
        finishViewListener.onFinishViewClick();
    }

    public void setFinishTxt(String finishContent){
        finishTxt.setText(finishContent);
    }

}
