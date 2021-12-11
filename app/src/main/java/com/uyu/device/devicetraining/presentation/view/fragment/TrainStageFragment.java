package com.uyu.device.devicetraining.presentation.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.selfhelp.TrainPresScheme;
import com.uyu.device.devicetraining.data.entity.trainpres.ApproachTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.FollowTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.FracturedRulerTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.GlanceTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.RGFixedVectorTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.RGVariableVectorTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.RedGreenReadTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.StereoscopeTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.presentation.internal.di.components.UserComponent;
import com.uyu.device.devicetraining.presentation.presenter.TrainStagePresenter;
import com.uyu.device.devicetraining.presentation.view.activity.WelcomeActivity;
import com.uyu.device.devicetraining.presentation.view.adapter.OnSelectUserContentListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.OnSelectContentListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.OnStartTrainListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.OnTextSizeChangeListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.PrepareDesc;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.PrepareDescManager;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.ReversalPresChangeListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.TrainAllFinishView;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.TrainItemTimeType;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.TrainPrepareView;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.TrainPrepareViewA;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.UserAllPresView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by windern on 2016/4/5.
 */
public class TrainStageFragment extends BaseFragment implements TrainStagePresenter.TrainStageViewListener
        , OnStartTrainListener
        , TrainAllFinishView.TrainAllFinishViewListener
        , UserAllPresView.UserAllPresViewListener {
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

    @Bind(R.id.userAllPresView)
    UserAllPresView userAllPresView;
    @Bind(R.id.device_overall_prepare_view)
    TrainPrepareViewA device_overall_prepare_view;
    @Bind(R.id.train_prepare_view)
    TrainPrepareViewA train_prepare_view;
    @Bind(R.id.one_end_prepare_view)
    TrainPrepareView one_end_prepare_view;
    @Bind(R.id.trainallfinishview)
    TrainAllFinishView all_finish_view;

    //训练状态
    TrainItemTimeType trainItemTimeType = TrainItemTimeType.PREPARE;

    @Inject
    TrainStagePresenter trainStagePresenter;

    private TrainPresScheme trainPresScheme;

    public void setTrainPresScheme(TrainPresScheme trainPresScheme) {
        this.trainPresScheme = trainPresScheme;
        userAllPresView.setTrainPresScheme(this.trainPresScheme);
        train_prepare_view.train_type = "youyan";
        device_overall_prepare_view.setPrepareItems(getPrepareLeftGuideData());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_train_stage, container, true);
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
        trainStagePresenter.setViewListener(this);
    }

    public void setTrainItemTimeType(TrainItemTimeType trainItemTimeType) {
        this.trainItemTimeType = trainItemTimeType;
        rl_welcome.setVisibility(trainItemTimeType == TrainItemTimeType.WELCOME ? View.VISIBLE : View.GONE);
        rl_prepare.setVisibility(trainItemTimeType == TrainItemTimeType.PREPARE ? View.VISIBLE : View.GONE);
        rl_one_end.setVisibility(trainItemTimeType == TrainItemTimeType.ONE_END ? View.VISIBLE : View.GONE);
        rl_all_end.setVisibility(trainItemTimeType == TrainItemTimeType.ALL_END ? View.VISIBLE : View.GONE);
        rl_user_all_pres.setVisibility(trainItemTimeType == TrainItemTimeType.ALL_PRES ? View.VISIBLE : View.GONE);
        rl_device_overall_prepare.setVisibility(trainItemTimeType == TrainItemTimeType.DEVICE_OVERALL_PREPARE ? View.VISIBLE : View.GONE);

        //如果是设备准备页面，解决一个人训练完成后，另外一个人训练，显示处方后会跳回到设备的第一项
        if(trainItemTimeType == TrainItemTimeType.DEVICE_OVERALL_PREPARE){
            device_overall_prepare_view.reset();
        }
    }

    private Map<String, String> getPrepareLeftGuideData(){
        List<TrainPres> trainPres = null;
        Map<String,String> map = new LinkedHashMap<>();
        map.put(device_overall_prepare_view.PREPARE,getActivity().getResources().getString(R.string.prepare));
        if(trainPresScheme != null && trainPresScheme.getTrainingPresList() != null) {
            trainPres = trainPresScheme.getTrainingPresList();
        }
//        for (int i = 0 ;i < trainPres.size();i++){
//            map.put(trainPres.get(i).getTrainItemType().getName(),trainPres.get(i).getTrainItemType().getShowName());
//        }
        for (int i = 0 ;i < trainPres.size();i++){
            String strName = "";
            String strShowName = "";
            EnumTrainItem trainItem = trainPres.get(i).getTrainItemType();
            TrainPres pres = trainPres.get(i);
            if (trainItem == EnumTrainItem.STEREOSCOPE) {
                strName = ((StereoscopeTrainPres)pres).getTrainingType().getName();
                strShowName = ((StereoscopeTrainPres)pres).getTrainingType().getShowName();
            } else if (trainItem == EnumTrainItem.R_G_VARIABLE_VECTOR) {
                strName = ((RGVariableVectorTrainPres)pres).getTrainingType().getName();
                strShowName = ((RGVariableVectorTrainPres)pres).getTrainingType().getShowName();
            } else if (trainItem == EnumTrainItem.FRACTURED_RULER) {
                strName = ((FracturedRulerTrainPres)pres).getTrainingType().getName();
            }else if (trainItem == EnumTrainItem.R_G_FIXED_VECTOR) {
                strName = ((RGFixedVectorTrainPres)pres).getTrainingType().getName();
                strShowName = ((RGFixedVectorTrainPres)pres).getTrainingType().getShowName();
            }else if (trainItem == EnumTrainItem.RED_GREEN_READ) {
                strName = "";
            }else if (trainItem == EnumTrainItem.APPROACH) {
                strName = "";
            } else if (trainItem == EnumTrainItem.GLANCE) {
                strName = "";
            } else if (trainItem == EnumTrainItem.FOLLOW) {
                strName = "";
            }
            map.put(trainPres.get(i).getTrainItemType().getName()+strName,
                    trainPres.get(i).getTrainItemType().getShowName()+strShowName);
        }
        return map;
    }

    public void initDeviceOverallPrepareData() {
        List<PrepareDesc> descList = PrepareDescManager.getDeviceOverallPrepareDescList();
        //画准备的界面
        device_overall_prepare_view.setDescList(descList, this, null);
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
    public void initPrepareData(TrainPres trainPresPrevious,TrainPres trainPres
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
            ((WelcomeActivity) getActivity()).stepDeviceOverallPrepareFinish();
        } else if (trainItemTimeType == TrainItemTimeType.PREPARE) {
            ((WelcomeActivity) getActivity()).prepareFinish();
        } else if (trainItemTimeType == TrainItemTimeType.ONE_END) {
            ((WelcomeActivity) getActivity()).oneEndFinish();
        }
    }

    @Override
    public void onFinishViewClick() {
        ((WelcomeActivity) getActivity()).onFinishViewClick();
    }

    @Override
    public void startTrain() {
        ((WelcomeActivity) getActivity()).stepUserAllPresShowFinish();
    }

    public void pressEnter(){
        go2NextStage();
    }

    public void go2NextStage(){
        if (trainItemTimeType == TrainItemTimeType.ALL_PRES) {
            ((WelcomeActivity) getActivity()).stepUserAllPresShowFinish();
        }else if (trainItemTimeType == TrainItemTimeType.DEVICE_OVERALL_PREPARE) {
            device_overall_prepare_view.go2NextStage();
        } else if (trainItemTimeType == TrainItemTimeType.PREPARE) {
            train_prepare_view.go2NextStage();
        } else if (trainItemTimeType == TrainItemTimeType.ONE_END) {
            one_end_prepare_view.go2NextStage();
        } else if(trainItemTimeType == TrainItemTimeType.ALL_END){
            ((WelcomeActivity) getActivity()).onFinishViewClick();
        }
    }
}
