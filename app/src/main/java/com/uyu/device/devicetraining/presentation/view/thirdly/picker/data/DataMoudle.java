package com.uyu.device.devicetraining.presentation.view.thirdly.picker.data;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/5 0005.
 */
public class DataMoudle {
    public static final ArrayList<String> Tong_Ju = new ArrayList<>();
    public static final ArrayList<String> Qiu_Jing = new ArrayList<>();
    public static final ArrayList<String> Zhu_Jing = new ArrayList<>();
    public static final ArrayList<String> San_Guan_Zhou = new ArrayList<>();
    public static final ArrayList<String> Eye_Sight = new ArrayList<>();
    public static final ArrayList<String> ADD = new ArrayList<>();
    public static final ArrayList<String> Horizontal_Eye_Point = new ArrayList<>();
    public static final ArrayList<String> Vertical_Eye_Point = new ArrayList<>();
    public static final ArrayList<String> Horizontal_Eye_Nums = new ArrayList<>();
    public static final ArrayList<String> Vertical_Eye_Nums = new ArrayList<>();
    public static final ArrayList<String> Eye_point = new ArrayList<>();
    public static final ArrayList<String> Eye_point_Nums = new ArrayList<>();
    public static final ArrayList<String> BCC = new ArrayList<>();
    //破裂值，模糊值，恢复值
    public static final ArrayList<String> P_M_H = new ArrayList<>();

    public static final ArrayList<String> NRA = new ArrayList<>();
    public static final ArrayList<String> PRA = new ArrayList<>();
    public static final ArrayList<String> AMP = new ArrayList<>();
    public static final ArrayList<String> TEXTSIZE = new ArrayList<>();


    // 灵敏度
    public static final ArrayList<String> Ling_Ming_Du = new ArrayList<>();


    static {
        initAdd();
        initBcc();
        initLinMingDu();
        initTong_ju();
        initAMP();
        initEyesPoint();
        initEye_Sight();
        initNRA();
        initQiu_Jing();
        initSan_Guan_Zhou();
        initZhu_Jing();
        initEye_point();
        initPMH();
        initPRA();
        initTXTSIZE();
    }

    private static void initTXTSIZE() {
        for (int i = 1; i <= 15; i++) {
            TEXTSIZE.add("" + i);
        }
    }

    //0.25-2.50
    public static void initNRA() {
        NRA.add("X");
        for (float i = (float) 0.25; i <= 2.50; i += 0.25) {
            if (i > 0) {
                NRA.add("+" + String.format("%.2f", i));
            }
            if (i == 0) {
                NRA.add("0.00");
            }
            if (i < 0) {
                NRA.add(String.format("%.2f", i));
            }

        }

    }

    //0.25-2.50
    public static void initPRA() {
        PRA.add("X");
        PRA.add("0.00");
        for (float i = (float) -0.25; i >= -15.00; i -= 0.25) {
            PRA.add(String.format("%.2f", i));
        }
    }

    //0.25-2.50
    public static void initAMP() {
        AMP.add("X");
        for (float i = (float) 0.00; i <= 18.00; i += 0.25) {
            AMP.add(String.format("%.2f", i));
        }
    }


    /**
     * 初始化灵敏度
     */
    public static void initLinMingDu() {
        Ling_Ming_Du.add("X");
        for (float i = (float) 0.0; i <= 50; i += 0.5) {
            Ling_Ming_Du.add(String.format("%.1f", i));
        }
    }

    /**
     * 破裂值，模糊值，恢复值
     */
    public static void initPMH() {
        P_M_H.add("X");
        for (int i = -40; i <= 40; i++) {
            if (i == -1) {
                P_M_H.add(i + "");
                P_M_H.add("X");
                continue;
            }
            P_M_H.add(i + "");
        }
    }

    /**
     * BCC
     */
    public static void initBcc() {
        BCC.add("X");
        for (float i = (float) 4.00; i >= -3.00; i -= 0.25) {
            if (i > 0) {
                BCC.add("+" + String.format("%.2f", i));
            }
            if (i == 0) {
                BCC.add("0.00");
            }
            if (i < 0) {
                BCC.add(String.format("%.2f", i));
            }
        }
        BCC.add("垂直偏好");
    }


    /**
     * AC/A
     */
    public static void initEyesPoint() {
        Eye_point.add("BI");
        Eye_point.add("BO");
        Eye_point_Nums.add("X");
        for (float i = (float) 0.0; i <= 20; i += 0.5) {
            Eye_point_Nums.add(String.format("%.1f", i));
        }
    }


    /**
     * 眼位
     */
    public static void initEye_point() {
        Horizontal_Eye_Point.add("BI");
        Horizontal_Eye_Point.add("BO");
        Vertical_Eye_Point.add("BU");
        Vertical_Eye_Point.add("BD");
        Horizontal_Eye_Nums.add("X");
        for (float i = (float) 0.0; i <= 30.01; i += 0.5) {
            Horizontal_Eye_Nums.add(String.format("%.1f", i));
        }
        Vertical_Eye_Nums.add("X");
        Vertical_Eye_Nums.add("0.0");
        for (float i = (float) 0.5; i <= 8.0; i += 0.5) {
            Vertical_Eye_Nums.add(String.format("%.1f", i));
        }
    }

    /**
     * 瞳距
     */
    public static void initTong_ju() {
        Tong_Ju.add("X");
        for (int i = 50; i <= 75; i++) {
            Tong_Ju.add(i + "");
        }
    }

    /**
     * 柱镜
     */
    public static void initZhu_Jing() {
        Zhu_Jing.add("X");
        for (double i = 6.00; i >= -6.00; i -= 0.25) {
            if (i > 0) {
                Zhu_Jing.add("+" + String.format("%.2f", i));
            }
            if (i < 0) {
                Zhu_Jing.add("" + String.format("%.2f", i));
            }
            if (i == 0) {
                Zhu_Jing.add("0.00");
            }
        }
    }

    /**
     * 球镜
     */
    public static void initQiu_Jing() {
        Qiu_Jing.add("X");
        for (double i = 10.00; i >= -15.00; i -= 0.25) {
            if (i > 0) {
                Qiu_Jing.add("+" + String.format("%.2f", i));
            }
            if (i < 0) {
                Qiu_Jing.add("" + String.format("%.2f", i));
            }
            if (i == 0) {
                Qiu_Jing.add("0.00");
            }
        }
    }

    /**
     * 瞳距
     */
    public static void initSan_Guan_Zhou() {
        San_Guan_Zhou.add("X");
        for (int i = 0; i <= 180; i++) {
            San_Guan_Zhou.add(i + "");
        }
    }

    /**
     * 视力
     */
    public static void initEye_Sight() {
        Eye_Sight.add("X");
        Eye_Sight.add(0.1 + "");
        Eye_Sight.add(0.2 + "");
        Eye_Sight.add(0.3 + "");
        Eye_Sight.add(0.4 + "");
        Eye_Sight.add(0.5 + "");
        Eye_Sight.add(0.6 + "");
        Eye_Sight.add(0.7 + "");
        Eye_Sight.add(0.8 + "");
        Eye_Sight.add(0.9 + "");
        Eye_Sight.add(1.0 + "");
        Eye_Sight.add(1.2 + "");
        Eye_Sight.add(1.5 + "");
        Eye_Sight.add(2.0 + "");
    }

    public static void initAdd() {
        ADD.add("X");
        for (float i = (float) 0.50; i <= 5.00; i += 0.25) {
            ADD.add("+" + String.format("%.2f", i));
        }
    }
}
