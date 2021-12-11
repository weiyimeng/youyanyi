package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.selfhelp.TrainPresScheme;
import com.uyu.device.devicetraining.data.entity.trainpres.ReversalTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumEyeType;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by windern on 2016/4/6.
 */
public class UserAllPresAdapater extends BaseAdapter {
    private List<TrainPres> listTrainPres;
    private Context context;
    private LayoutInflater inflater;

    public UserAllPresAdapater(Context context, List<TrainPres> listTrainPres) {
        this.context = context;
        this.listTrainPres = listTrainPres;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listTrainPres != null ? listTrainPres.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return listTrainPres.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_menu_list, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        String s = listTrainPres.get(position).getShowName();
        //String s = getString(listType.get(position));

        int num = listTrainPres.get(position).getRepeatTrainingTimes();
        String numStr = null;
        if (listTrainPres.get(position).getTrainItemType() == EnumTrainItem.REVERSAL) {
            if (((ReversalTrainPres) listTrainPres.get(position)).getEyeType() != EnumEyeType.DOUBLE) {
                numStr = "左眼右眼各训练：" + num + "遍";
            }else{
                numStr = "训练遍数:" + num + "遍";
            }
        } else {
            numStr = "训练遍数:" + num + "遍";
        }
        vh.stepTxt.setText("第" + (position + 1) + "步:    " + s);
        vh.numHintTxt.setText(numStr);
        convertView.setEnabled(true);
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }
}

class ViewHolder {
    /* @Bind(R.id.menu_item_txt)
     TextView infoTxt;*/
    @Bind(R.id.menu_step_nums)
    TextView stepTxt;

    @Bind(R.id.num_hint)
    TextView numHintTxt;

    public ViewHolder(View convertView) {
        ButterKnife.bind(this, convertView);
    }

}