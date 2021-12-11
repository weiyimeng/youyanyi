package com.uyu.device.devicetraining.presentation.view.thirdly.picker.view;

import android.view.View;
import android.widget.TextView;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.presentation.view.thirdly.picker.adapter.ArrayWheelAdapter;
import com.uyu.device.devicetraining.presentation.view.thirdly.picker.data.DataMoudle;
import com.uyu.device.devicetraining.presentation.view.thirdly.picker.popwindow.ChoosePopupWindow;

import java.util.ArrayList;

public class WheelChoose {
    private View view;
    private WheelView wv_wheel1;
    private WheelView wv_wheel2;
    private WheelView wv_wheel3;
    private WheelView wv_wheel4;
    private TextView label1, label2, label3, label4;
    public int screenheight;
    ArrayList<String> nums1 = new ArrayList<>();
    ArrayList<String> nums2 = new ArrayList<>();
    ArrayList<String> nums3 = new ArrayList<>();
    ArrayList<String> nums4 = new ArrayList<>();


    private ChoosePopupWindow.Type type;

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public WheelChoose(View view) {
        super();
        this.view = view;
        setView(view);
    }

    public WheelChoose(View view, ChoosePopupWindow.Type type) {
        super();
        this.view = view;
        this.type = type;
        setView(view);
    }


    /**
     * @Description: TODO 弹出日期时间选择器
     */
    public void setPicker(String title1, String title2, String title3, String title4) {
        initLabel(view);
        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = 0;
        switch (type) {
            case FOUR:
                textSize = (screenheight / 100) * 3;
                label1.setText(title1);
                label2.setText(title2);
                label3.setText(title3);
                label4.setText(title4);
                if (title1.equals("水平眼位") && title2.equals("检查值") && title3.equals("垂直眼位") && title4.equals("检查值")) {
                    nums1.addAll(DataMoudle.Horizontal_Eye_Point);
                    nums2.addAll(DataMoudle.Horizontal_Eye_Nums);
                    nums3.addAll(DataMoudle.Vertical_Eye_Point);
                    nums4.addAll(DataMoudle.Vertical_Eye_Nums);
                }
                wv_wheel1.setAdapter(new ArrayWheelAdapter<String>(nums1));
                wv_wheel2.setAdapter(new ArrayWheelAdapter<String>(nums2));
                wv_wheel3.setAdapter(new ArrayWheelAdapter<String>(nums3));
                wv_wheel4.setAdapter(new ArrayWheelAdapter<String>(nums4));
                wv_wheel1.setCurrentItem(nums1.size() / 2);
                wv_wheel2.setCurrentItem(nums2.size() / 2);
                wv_wheel3.setCurrentItem(nums3.size() / 2);
                wv_wheel4.setCurrentItem(nums4.size() / 2);

                break;
            case THREE:
                textSize = (screenheight / 100) * 4;
                wv_wheel4.setVisibility(View.GONE);
                label4.setVisibility(View.GONE);
                label1.setText(title1);
                label2.setText(title2);
                label3.setText(title3);
                if (title1.equals("球镜度数") && title2.equals("柱镜度数") && title3.equals("散光轴位")) {
                    nums1.addAll(DataMoudle.Qiu_Jing);
                    nums2.addAll(DataMoudle.Zhu_Jing);
                    nums3.addAll(DataMoudle.San_Guan_Zhou);
                }
                if (title1.equals("模糊值") && title2.equals("破裂值") && title3.equals("恢复值")) {
                    nums1.addAll(DataMoudle.P_M_H);
                    nums2.addAll(DataMoudle.P_M_H);
                    nums3.addAll(DataMoudle.P_M_H);
                }
                if (title1.equals("NRA") && title2.equals("左眼") && title3.equals("双眼")) {
                    label1.setText("右眼");
                    nums1.addAll(DataMoudle.NRA);
                    nums2.addAll(DataMoudle.NRA);
                    nums3.addAll(DataMoudle.NRA);
                }
                if (title1.equals("PRA") && title2.equals("左眼") && title3.equals("双眼")) {
                    label1.setText("右眼");
                    nums1.addAll(DataMoudle.PRA);
                    nums2.addAll(DataMoudle.PRA);
                    nums3.addAll(DataMoudle.PRA);
                }
                if (title1.equals("AMP") && title2.equals("左眼") && title3.equals("双眼")) {
                    label1.setText("右眼");
                    nums1.addAll(DataMoudle.AMP);
                    nums2.addAll(DataMoudle.AMP);
                    nums3.addAll(DataMoudle.AMP);
                }
                if (title1.equals("灵敏度") && title2.equals("左眼") && title3.equals("双眼")) {
                    label1.setText("右眼");
                    nums1.addAll(DataMoudle.Ling_Ming_Du);
                    nums2.addAll(DataMoudle.Ling_Ming_Du);
                    nums3.addAll(DataMoudle.Ling_Ming_Du);
                }
                wv_wheel1.setAdapter(new ArrayWheelAdapter<String>(nums1));
                wv_wheel2.setAdapter(new ArrayWheelAdapter<String>(nums2));
                wv_wheel3.setAdapter(new ArrayWheelAdapter<String>(nums3));

                wv_wheel1.setCurrentItem(nums1.size() / 2);
                wv_wheel2.setCurrentItem(nums2.size() / 2);
                wv_wheel3.setCurrentItem(nums3.size() / 2);
                break;
            case TWO:
                textSize = (screenheight / 100) * 5;
                wv_wheel3.setVisibility(View.GONE);
                wv_wheel4.setVisibility(View.GONE);
                label4.setVisibility(View.GONE);
                label3.setVisibility(View.GONE);
                label1.setText(title1);
                label2.setText(title2);
                if (title1.equals("眼位") && title2.equals("眼位值")) {
                    nums1.addAll(DataMoudle.Eye_point);
                    nums2.addAll(DataMoudle.Eye_point_Nums);
                }
                wv_wheel1.setAdapter(new ArrayWheelAdapter<String>(nums1));
                wv_wheel2.setAdapter(new ArrayWheelAdapter<String>(nums2));
                wv_wheel1.setCurrentItem(nums1.size() / 2);
                wv_wheel2.setCurrentItem(nums2.size() / 2);
                break;
            case ONE:
                textSize = (screenheight / 100) * 5;
                wv_wheel2.setVisibility(View.GONE);
                wv_wheel3.setVisibility(View.GONE);
                wv_wheel4.setVisibility(View.GONE);
                label2.setVisibility(View.GONE);
                label3.setVisibility(View.GONE);
                label4.setVisibility(View.GONE);
                label1.setText(title1);

                if (title1.equals("瞳距")) {
                    nums1.addAll(DataMoudle.Tong_Ju);
                }
                if (title1.equals("视力")) {
                    nums1.addAll(DataMoudle.Eye_Sight);
                }
                if (title1.equals("ADD")) {
                    nums1.addAll(DataMoudle.ADD);
                }
                if (title1.equals("BCC")) {
                    nums1.addAll(DataMoudle.BCC);
                }
                wv_wheel1.setAdapter(new ArrayWheelAdapter<String>(nums1));
                wv_wheel1.setCurrentItem(nums1.size() / 2);
        }
        setLabelTextSize(textSize);
    }

    private void setLabelTextSize(int textSize) {
        label1.setTextSize(textSize / 4);
        label2.setTextSize(textSize / 4);
        label3.setTextSize(textSize / 4);
        label4.setTextSize(textSize / 4);
        wv_wheel1.TEXT_SIZE = textSize;
        wv_wheel2.TEXT_SIZE = textSize;
        wv_wheel3.TEXT_SIZE = textSize;
        wv_wheel4.TEXT_SIZE = textSize;
    }

    private void initLabel(View view) {
        label1 = (TextView) view.findViewById(R.id.pop_label1);
        label2 = (TextView) view.findViewById(R.id.pop_label2);
        label3 = (TextView) view.findViewById(R.id.pop_label3);
        label4 = (TextView) view.findViewById(R.id.pop_label4);
        wv_wheel1 = (WheelView) view.findViewById(R.id.wheel1);
        wv_wheel2 = (WheelView) view.findViewById(R.id.wheel2);
        wv_wheel3 = (WheelView) view.findViewById(R.id.wheel3);
        wv_wheel4 = (WheelView) view.findViewById(R.id.wheel4);
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wv_wheel1.setCyclic(cyclic);
        wv_wheel2.setCyclic(cyclic);
        wv_wheel3.setCyclic(cyclic);
        wv_wheel4.setCyclic(cyclic);
    }

    public String getString() {
        StringBuffer sb = new StringBuffer();
        switch (type) {
            case ONE:
                sb.append(label1.getText())
                        .append(":")
                        .append(nums1.get(wv_wheel1.getCurrentItem()));
                break;
            case TWO:
                sb.append(label1.getText())
                        .append(":")
                        .append(nums1.get(wv_wheel1.getCurrentItem()))
                        .append(",")
                        .append(label2.getText())
                        .append(":")
                        .append(nums2.get(wv_wheel2.getCurrentItem()));
                break;
            case THREE:
                sb.append(label1.getText())
                        .append(":")
                        .append(nums1.get(wv_wheel1.getCurrentItem()))
                        .append(",")
                        .append(label2.getText())
                        .append(":")
                        .append(nums2.get(wv_wheel2.getCurrentItem()))
                        .append(",")
                        .append(label3.getText())
                        .append(":")
                        .append(nums3.get(wv_wheel3.getCurrentItem()));
                break;
            case FOUR:
                sb.append(label1.getText())
                        .append(":")
                        .append(nums1.get(wv_wheel1.getCurrentItem()))
                        .append(",")
                        .append(label2.getText())
                        .append(":")
                        .append(nums2.get(wv_wheel2.getCurrentItem()))
                        .append(",")
                        .append(label3.getText())
                        .append(":")
                        .append(nums3.get(wv_wheel3.getCurrentItem()))
                        .append(",")
                        .append(label4.getText())
                        .append(":")
                        .append(nums4.get(wv_wheel4.getCurrentItem()));
                break;
        }
        return sb.toString();
    }
}
