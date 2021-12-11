package com.uyu.device.devicetraining.presentation.view.fragment;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.selfhelp.PushTrainContentContent;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.presentation.internal.di.components.UserComponent;
import com.uyu.device.devicetraining.presentation.presenter.quick.ReversalTrainPresenter;
import com.uyu.device.devicetraining.presentation.view.widget.LineView;
import com.uyu.device.devicetraining.presentation.view.widget.SelectDropView;

import butterknife.Bind;

/**
 * Created by windern on 2015/12/4.
 */
public class ReversalTrainFragment extends TrainFragment<ReversalTrainPresenter> implements ReversalTrainPresenter.ReversalListener{
    @Bind(R.id.tv_train_content)
    LineView tv_train_content;
    @Bind(R.id.view_loop_type)
    SelectDropView view_loop_type;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_reversal_train;
    }

    @Override
    public String getTrainItemName() {
        return EnumTrainItem.REVERSAL.getShowName()+getActivity().getResources().getString(R.string.train);
    }

    @Override
    protected void initialize() {
        this.getComponent(UserComponent.class).inject(this);
        trainPresenter.setTflistener(this);
        trainPresenter.setItemListener(this);

        trainPresenter.setLineView(tv_train_content);
        trainPresenter.setViewLoopType(view_loop_type);
    }

    /**
     * 接收推送的训练内容
     * @param pushTrainContentContent
     */
    public void receivePushTrainContentMsg(PushTrainContentContent pushTrainContentContent){
        trainPresenter.receivePushTrainContentMsg(pushTrainContentContent);
    }
}
