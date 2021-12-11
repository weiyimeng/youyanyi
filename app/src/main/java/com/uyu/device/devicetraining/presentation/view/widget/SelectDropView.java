package com.uyu.device.devicetraining.presentation.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uyu.device.devicetraining.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by windern on 16/7/8.
 */
public class SelectDropView extends RelativeLayout{
    public interface SelectDropViewListener{
        void select(int pos);
    }

    private SelectDropViewListener viewListener;

    @Bind(R.id.tv_select_tip)
    TextView tv_select_tip;
    @Bind(R.id.tv_select_content)
    TextView tv_select_content;

    private Context context;
    private View view;
    private ListPopupWindow listPopupWindow;
    private ArrayList<String> dataList =  new ArrayList<String>();

    public SelectDropView(Context context) {
        super(context);
        this.context = context;

        initLayout();
    }

    public SelectDropView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        initLayout();
    }

    public SelectDropView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        initLayout();
    }

        /**
         * 初始化布局
         */
    public void initLayout(){
        view = LayoutInflater.from(context).inflate(R.layout.layout_select_drop_view, this,
                true);
        ButterKnife.bind(this, view);
    }

    public SelectDropViewListener getViewListener() {
        return viewListener;
    }

    public void setViewListener(SelectDropViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @OnClick(R.id.tv_select_content)
    public void selectContent(TextView textView) {
        listPopupWindow.setAnchorView(textView);
        listPopupWindow.show();
    }

    public ArrayList<String> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<String> dataList) {
        this.dataList = dataList;
        tv_select_content.setText(dataList.get(0));
        initPop(context);
    }

    public void changeShowPos(int pos){
        tv_select_content.setText(dataList.get(pos));
    }

    public void setSelectTip(String selectTip){
        tv_select_tip.setText(selectTip);
    }

    public void setSelectContent(String selectContent){
        tv_select_content.setText(selectContent);
    }

    private void initPop(Context context) {
        listPopupWindow = new ListPopupWindow(context);
        listPopupWindow.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_single_choice, dataList));
        listPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                tv_select_content.setText(dataList.get(pos));
                viewListener.select(pos);
                listPopupWindow.dismiss();
            }
        });
    }
}
