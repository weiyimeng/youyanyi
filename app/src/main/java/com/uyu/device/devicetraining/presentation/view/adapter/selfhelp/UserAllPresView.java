package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.selfhelp.TrainPresScheme;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by windern on 2016/4/6.
 */
public class UserAllPresView extends LinearLayout{
    public interface UserAllPresViewListener{
        void startTrain();
    }

    private UserAllPresViewListener listener;

    public void setListener(UserAllPresViewListener listener) {
        this.listener = listener;
    }

    private Context context;

    @Bind(R.id.menu_listview)
    ListView menuListView;

    private TrainPresScheme trainPresScheme;

    public UserAllPresView(Context context) {
        super(context);
        this.context = context;
        initLayout();
    }

    public UserAllPresView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initLayout();
    }

    public UserAllPresView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initLayout();
    }

    private void initLayout() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_user_all_pres, this, true);
        ButterKnife.bind(this, view);
    }

    public TrainPresScheme getTrainPresScheme() {
        return trainPresScheme;
    }

    public void setTrainPresScheme(TrainPresScheme trainPresScheme) {
        this.trainPresScheme = trainPresScheme;
        refreshView();
    }

    public void refreshView(){
        UserAllPresAdapater adapter = new UserAllPresAdapater(context, trainPresScheme.getTrainingPresList());
        menuListView.setAdapter(adapter);
    }

    @OnClick(R.id.btn_start_train)
    public void startTrain(){
        if(listener!=null){
            listener.startTrain();
        }
    }
}
