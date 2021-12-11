package com.uyu.device.devicetraining.presentation.view.fragment.trial;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.trainback.trial.PrepareDescManager;
import com.uyu.device.devicetraining.data.entity.trainpres.StereoscopeTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.presentation.internal.di.components.UserComponent;
import com.uyu.device.devicetraining.presentation.presenter.trial.TrainTrialStagePresenter;
import com.uyu.device.devicetraining.presentation.view.activity.WelcomeActivity;
import com.uyu.device.devicetraining.presentation.view.adapter.OnSelectUserContentListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.OnSelectContentListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.OnStartTrainListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.OnTextSizeChangeListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.PrepareDesc;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.ReversalPresChangeListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.ShowResultView;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.TrainAllFinishView;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.TrainItemTimeType;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.TrainPrepareView;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.TrainPrepareViewA;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.UserAllPresView;
import com.uyu.device.devicetraining.presentation.view.fragment.BaseFragment;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by windern on 2016/4/5.
 */
public class TrialTrainStageFragment extends BaseFragment implements TrainTrialStagePresenter.TrainTrialStagePresenterListener
        , OnStartTrainListener
        , TrainAllFinishView.TrainAllFinishViewListener
        , UserAllPresView.UserAllPresViewListener {
    public interface TrialTrainStageFragmentListener{
        void stepDeviceOverallPrepareFinish();
        void stepUserAllPresShowFinish();
        void prepareFinish();
        void onFinishViewClick();
        void oneEndFinish();
    }

    private TrialTrainStageFragmentListener listener;

    public void setListener(TrialTrainStageFragmentListener listener) {
        this.listener = listener;
    }

    //用户处方界面
    @Bind(R.id.rl_welcome)
    RelativeLayout rl_welcome;
    //用户处方界面
    @Bind(R.id.rl_user_all_pres)
    RelativeLayout rl_user_all_pres;
    //device整体准备的界面
    @Bind(R.id.rl_device_overall_prepare)
    RelativeLayout rl_device_overall_prepare;
    //准备界面
    @Bind(R.id.rl_prepare)
    RelativeLayout rl_prepare;
    //结束一遍训练界面
    @Bind(R.id.rl_one_end)
    RelativeLayout rl_one_end;
    //结束所有训练界面
    @Bind(R.id.rl_all_end)
    RelativeLayout rl_all_end;
    // 显示结果页面
    @Bind(R.id.rl_show_result)
    RelativeLayout rl_show_result;

    @Bind(R.id.userAllPresView)
    UserAllPresView userAllPresView;
    @Bind(R.id.device_overall_prepare_view)
    TrainPrepareViewA device_overall_prepare_view;
    @Bind(R.id.train_prepare_view)
    TrainPrepareViewA train_prepare_view;
    @Bind(R.id.one_end_prepare_view)
    TrainPrepareViewA one_end_prepare_view;
    @Bind(R.id.trainallfinishview)
    TrainAllFinishView all_finish_view;
    @Bind(R.id.show_result_view)
    ShowResultView show_result_view;

    //训练状态
    TrainItemTimeType trainItemTimeType = TrainItemTimeType.PREPARE;

    @Inject
    TrainTrialStagePresenter trainTrialStagePresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_train_trial_stage, container, true);
        ButterKnife.bind(this, fragmentView);

        userAllPresView.setListener(this);
        initDeviceOverallPrepareData();

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
    }

    protected void initialize() {
        this.getComponent(UserComponent.class).inject(this);
        trainTrialStagePresenter.setListener(this);
    }

    public void setTrainItemTimeType(TrainItemTimeType trainItemTimeType) {
        this.trainItemTimeType = trainItemTimeType;
        rl_welcome.setVisibility(trainItemTimeType == TrainItemTimeType.WELCOME ? View.VISIBLE : View.GONE);
        rl_prepare.setVisibility(trainItemTimeType == TrainItemTimeType.PREPARE ? View.VISIBLE : View.GONE);
        rl_one_end.setVisibility(trainItemTimeType == TrainItemTimeType.ONE_END ? View.VISIBLE : View.GONE);
        rl_all_end.setVisibility(trainItemTimeType == TrainItemTimeType.ALL_END ? View.VISIBLE : View.GONE);
        rl_user_all_pres.setVisibility(trainItemTimeType == TrainItemTimeType.ALL_PRES ? View.VISIBLE : View.GONE);
        rl_device_overall_prepare.setVisibility(trainItemTimeType == TrainItemTimeType.DEVICE_OVERALL_PREPARE ? View.VISIBLE : View.GONE);
        rl_show_result.setVisibility(trainItemTimeType == TrainItemTimeType.SHOWRESULT ? View.VISIBLE : View.GONE);

        //如果是设备准备页面，解决一个人训练完成后，另外一个人训练，显示处方后会跳回到设备的第一项
        if(trainItemTimeType == TrainItemTimeType.DEVICE_OVERALL_PREPARE){
            device_overall_prepare_view.reset();
            ((WelcomeActivity)getActivity()).banner_bar_view.startTime();
            ((WelcomeActivity)getActivity()).banner_bar_view.showTime();
        }
        if(trainItemTimeType == TrainItemTimeType.SHOWRESULT){
            ((WelcomeActivity)getActivity()).banner_bar_view.stopTime();
        }
    }

    public void initDeviceOverallPrepareData() {
        train_prepare_view.train_type = "weixin";
        device_overall_prepare_view.setPrepareItems(getPrepareLeftGuideData());
        List<PrepareDesc> descList = PrepareDescManager.getDeviceOverallPrepareDescList();
        //画准备的界面
        device_overall_prepare_view.setDescList(descList, this, null);
    }

    private Map<String, String> getPrepareLeftGuideData(){
        Map<String,String> map = new LinkedHashMap<>();
        map.put(device_overall_prepare_view.PREPARE,getActivity().getResources().getString(R.string.prepare));
        map.put(EnumTrainItem.REVERSAL.getName(),getActivity().getResources().getString(R.string.reversal));
        map.put(EnumTrainItem.STEREOSCOPE.getName(),getActivity().getResources().getString(R.string.stereoscope));
        map.put(EnumTrainItem.R_G_VARIABLE_VECTOR.getName(),getActivity().getResources().getString(R.string.red_green));
        return map;
    }

    /**
     * 准备的数据
     */
    public void initPrepareData(TrainPres trainPres
            , ReversalPresChangeListener reversalPresChangeListener
            , OnSelectContentListener onSelectContentListener
            , OnTextSizeChangeListener onTextSizeChangeListener
            , OnSelectUserContentListener selectUserContentListener) {
        if (trainPres == null)
            return;
        all_finish_view.setEnumTrainItem(trainPres.getTrainItemType());
        all_finish_view.setFinishViewListener(this);
        all_finish_view.setFinishTxt();

        //准备数据
        List<PrepareDesc> descList = PrepareDescManager.getPrepareDescList(trainPres);
        //画准备的界面
        train_prepare_view.setReversalPresChangeListener(reversalPresChangeListener);
        train_prepare_view.setOnTextSizeChangeListener(onTextSizeChangeListener);
        train_prepare_view.setSelectContentListener(onSelectContentListener);
        train_prepare_view.setSelectUserContentListener(selectUserContentListener);
        train_prepare_view.setDescList(descList, this, trainPres);

        //一次结束后准备的数据
        List<PrepareDesc> descListOneEnd = PrepareDescManager.getOneEndPrepareDescList(trainPres);
        //一次结束后准备的界面
        one_end_prepare_view.setOnTextSizeChangeListener(onTextSizeChangeListener);
        one_end_prepare_view.setReversalPresChangeListener(reversalPresChangeListener);
        one_end_prepare_view.setSelectContentListener(onSelectContentListener);
        one_end_prepare_view.setSelectUserContentListener(selectUserContentListener);
        one_end_prepare_view.setDescList(descListOneEnd, this, trainPres);
    }

    /**
     * 准备的数据
     * @param trainPresPrevious
     * @param trainPres
     * @param reversalPresChangeListener
     * @param onSelectContentListener
     * @param onTextSizeChangeListener
     * @param selectUserContentListener
     */
    public void initPrepareData(TrainPres trainPresPrevious, TrainPres trainPres
            , ReversalPresChangeListener reversalPresChangeListener
            , OnSelectContentListener onSelectContentListener
            , OnTextSizeChangeListener onTextSizeChangeListener
            , OnSelectUserContentListener selectUserContentListener) {
        if (trainPres == null)
            return;
        all_finish_view.setEnumTrainItem(trainPres.getTrainItemType());
        all_finish_view.setFinishViewListener(this);
        all_finish_view.setFinishTxt();


        train_prepare_view.setPrepareItems(getPrepareLeftGuideData());

        //准备数据
        List<PrepareDesc> descList = PrepareDescManager.getPrepareDescList(trainPresPrevious,trainPres);
        //画准备的界面
        train_prepare_view.setReversalPresChangeListener(reversalPresChangeListener);
        train_prepare_view.setOnTextSizeChangeListener(onTextSizeChangeListener);
        train_prepare_view.setSelectContentListener(onSelectContentListener);
        train_prepare_view.setSelectUserContentListener(selectUserContentListener);
        train_prepare_view.setDescList(descList, this, trainPres);

        //一次结束后准备的数据
        List<PrepareDesc> descListOneEnd = PrepareDescManager.getOneEndPrepareDescList(trainPres);
        //一次结束后准备的界面
        one_end_prepare_view.setOnTextSizeChangeListener(onTextSizeChangeListener);
        one_end_prepare_view.setReversalPresChangeListener(reversalPresChangeListener);
        one_end_prepare_view.setSelectContentListener(onSelectContentListener);
        one_end_prepare_view.setSelectUserContentListener(selectUserContentListener);
        one_end_prepare_view.setDescList(descListOneEnd, this, trainPres);
    }

    public void refreshAllFinishView(String finishContent){
        all_finish_view.setFinishTxt(finishContent);
    }

    public void refreshPrepareView(List<PrepareDesc> descList){
        train_prepare_view.setDescList(descList);
    }

    public void refreshOneEndPrepareView(List<PrepareDesc> descList){
        train_prepare_view.setDescList(descList);
    }

    @Override
    public void onStartTrain() {
        if (trainItemTimeType == TrainItemTimeType.DEVICE_OVERALL_PREPARE) {
            listener.stepDeviceOverallPrepareFinish();
        } else if (trainItemTimeType == TrainItemTimeType.PREPARE) {
            listener.prepareFinish();
        } else if (trainItemTimeType == TrainItemTimeType.ONE_END) {
            listener.oneEndFinish();
        }
    }

    @Override
    public void onFinishViewClick() {
        listener.onFinishViewClick();
    }

    @Override
    public void startTrain() {
        listener.stepUserAllPresShowFinish();
    }

    public void pressEnter(){
        go2NextStage();
    }

    public void go2NextStage(){
        if (trainItemTimeType == TrainItemTimeType.ALL_PRES) {
            listener.stepUserAllPresShowFinish();
        }else if (trainItemTimeType == TrainItemTimeType.DEVICE_OVERALL_PREPARE) {
            device_overall_prepare_view.go2NextStage();
        } else if (trainItemTimeType == TrainItemTimeType.PREPARE) {
            train_prepare_view.go2NextStage();
        } else if (trainItemTimeType == TrainItemTimeType.ONE_END) {
            one_end_prepare_view.go2NextStage();
        } else if(trainItemTimeType == TrainItemTimeType.ALL_END){
            listener.onFinishViewClick();
        } else if(trainItemTimeType == TrainItemTimeType.SHOWRESULT){
            show_result_view.exit();
        }
    }
}
