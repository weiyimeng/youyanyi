package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.content.NewsCategory;
import com.uyu.device.devicetraining.data.entity.content.NewsCategory_Table;
import com.uyu.device.devicetraining.data.entity.content.TrainingContent;
import com.uyu.device.devicetraining.data.entity.content.TrainingContent_Table;
import com.uyu.device.devicetraining.data.entity.trainpres.ReversalTrainPres;
import com.uyu.device.devicetraining.data.entity.trainpres.TrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumContentType;
import com.uyu.device.devicetraining.data.entity.type.EnumEyeType;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.presentation.view.adapter.OnSelectUserContentListener;
import com.uyu.device.devicetraining.presentation.view.thirdly.picker.data.DataMoudle;
import com.uyu.device.devicetraining.presentation.view.thirdly.picker.popwindow.OptionsPopupWindow;
import com.uyu.device.devicetraining.presentation.view.widget.TrainDeskView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by windern on 2016/1/8.
 */
public class PrepareDescView extends RelativeLayout implements OnCloseListener, OnSelectArticleListener, OnSelectUserContentListener {
    @Bind(R.id.step_img)
    ImageView tipImg;
    @Bind(R.id.tips_txt)
    TextView tipTxt;

    @Bind(R.id.prepare_rg)
    RadioGroup selectRg;

    @Bind(R.id.prepare_rb1)
    RadioButton radioButton1;
    @Bind(R.id.prepare_rb2)
    RadioButton radioButton2;
    @Bind(R.id.prepare_rb3)
    RadioButton radioButton3;

    @Bind(R.id.select_book_btn)
    Button btnSelectBook;
    @Bind(R.id.train_content)
    TextView tvTrainContent;

    @Bind(R.id.start_train_btn)
    Button startBtn;

    @Bind(R.id.level_linearlayout)
    LinearLayout linearLayout;

    @Bind(R.id.left_level)
    TextView leftEyeTxt;
    @Bind(R.id.right_level)
    TextView rightEyeTxt;

    @Bind(R.id.select_txt_size)
    TextView textView;
    private View view;
    private Context context;
    private PrepareDesc prepareDesc;
    private OnStartTrainListener listener;
    private OnSelectContentListener selectContentListener;
    private OnSelectUserContentListener selectUserContentListener;
    private ReversalPresChangeListener reversalPresChangeListener;
    private OnTextSizeChangeListener onTextSizeChangeListener;
    private TrainPres trainPres;

    private int trainingContentType = 0;
    private int articleId = 0;
    private String articleName = "";
    private int eyeType = -1;
    private ListPopupWindow listPopupWindow;

    private PopupWindow popupWindow = null;
    private OptionsPopupWindow optionsPopupWindow = null;

    @Bind(R.id.btn_select_user_content)
    Button btn_select_user_content;

    @Bind(R.id.rl_main_container)
    RelativeLayout rl_main_container;

    @Bind(R.id.rl_popup_container)
    RelativeLayout rl_popup_container;

    @Bind(R.id.selectContentView)
    SelectContentView selectContentView;

    @Bind(R.id.trainDeskView)
    TrainDeskView trainDeskView;

    @Bind(R.id.tv_num)
    TextView tv_num;

    public PrepareDescView(Context context) {
        super(context);
        this.context = context;

        initLayout();
    }

    private void initLayout() {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.new_view_prepare_desc, this, true);
        }
        ButterKnife.bind(this, view);
    }

    public void setParam(int i ,final PrepareDesc prepareDesc, OnStartTrainListener listener
            , final OnSelectContentListener selectContentListener
            , final ReversalPresChangeListener reversalPresChangeListener
            , final TrainPres trainPres
            , final OnTextSizeChangeListener onTextSizeChangeListener
            , final OnSelectUserContentListener selectUserContentListener) {
        this.trainPres = trainPres;
        this.prepareDesc = prepareDesc;
        this.listener = listener;
        this.selectContentListener = selectContentListener;
        this.reversalPresChangeListener = reversalPresChangeListener;
        this.onTextSizeChangeListener = onTextSizeChangeListener;
        this.selectUserContentListener = selectUserContentListener;
        tipImg.setImageResource(prepareDesc.getImage());
        tv_num.setText(i+"");
        tipTxt.setText("\t"+prepareDesc.getDesc());
        startBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PrepareDescView.this.listener.onStartTrain();
            }
        });
        if (PrepareDescType.TEXTSIZE == prepareDesc.getType()) {
            textView.setVisibility(VISIBLE);
            tipTxt.setText("\t\t\t" + prepareDesc.getDesc() + "(默认字体大小:6)");
            optionsPopupWindow = new OptionsPopupWindow(context);
            optionsPopupWindow.setPicker(DataMoudle.TEXTSIZE);
            optionsPopupWindow.setOnoptionsSelectListener(new OptionsPopupWindow.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3) {
                    if (onTextSizeChangeListener != null) {
                        int i = Integer.parseInt(DataMoudle.TEXTSIZE.get(options1));
                        onTextSizeChangeListener.onTxtSizeChange(i);
                        tipTxt.setText("字体大小:" + i);
                    }
                    optionsPopupWindow.dismiss();
                }
            });
        }
        if (prepareDesc.getType() == PrepareDescType.SELECT || prepareDesc.getType() == PrepareDescType.CONTENT) {
            String[] arrayOptions = ((SelectPrepareDesc) prepareDesc).getArrayOptions();
            radioButton1.setText(arrayOptions[0]);
            radioButton2.setText(arrayOptions[1]);
            if (arrayOptions.length >= 3) {
                radioButton3.setText(arrayOptions[2]);
            } else {
                radioButton3.setVisibility(GONE);
            }
            selectRg.setVisibility(VISIBLE);
            ((RadioButton) selectRg.getChildAt(1)).setChecked(true);
            selectRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.prepare_rb1:
                            trainingContentType = EnumContentType.NUMBER.getValue();
                            break;
                        case R.id.prepare_rb2:
                            trainingContentType = EnumContentType.LETTER.getValue();
                            break;
                        case R.id.prepare_rb3:
                            trainingContentType = EnumContentType.ARTICLE.getValue();
                            break;
                    }
                    if (prepareDesc.getType() == PrepareDescType.CONTENT && trainingContentType == EnumContentType.ARTICLE.getValue()) {
                        setSelectArticleVisible(View.VISIBLE);
                    } else {
                        setSelectArticleVisible(View.GONE);
                    }
                    if (selectContentListener != null) {
                        onSelectContent(trainingContentType, articleId, articleName);
                    }
                }
            });
            if (prepareDesc.getType() == PrepareDescType.CONTENT) {
                btnSelectBook.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSelectContent();
                    }
                });

                //显示之前手动去获取数据
                selectContentView.getData();
                selectContentView.setVisibility(View.VISIBLE);
            }
        }
        if (prepareDesc.getType() == PrepareDescType.EYELEVEL) {
            linearLayout.setVisibility(VISIBLE);
            if (trainPres.getTrainItemType() == EnumTrainItem.REVERSAL) {
                if (((ReversalTrainPres) trainPres).getEyeType() == EnumEyeType.LEFT) {
                    rightEyeTxt.setVisibility(GONE);
                } else if (((ReversalTrainPres) trainPres).getEyeType() == EnumEyeType.RIGHT) {
                    leftEyeTxt.setVisibility(GONE);
                }
            }
            if (reversalPresChangeListener != null) {
                initPop(context);
            }
        }

        if(prepareDesc.getType()== PrepareDescType.USERCONTENT){
            btn_select_user_content.setVisibility(View.VISIBLE);
            btn_select_user_content.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSelectUserContent();
                }
            });

            tvTrainContent.setVisibility(View.VISIBLE);
            if (trainPres.getTrainItemType() == EnumTrainItem.REVERSAL) {
                String bookName = "";
                if(((ReversalTrainPres)trainPres).getTrainingContentType()== EnumContentType.ARTICLE){
                    TrainingContent trainingContent = SQLite.select().from(TrainingContent.class)
                            .where(TrainingContent_Table.id.eq(((ReversalTrainPres)trainPres).getTrainingContentArticleId())).querySingle();
                    if(trainingContent!=null){
                        bookName = trainingContent.getTitle();
                    }else{
                        bookName = getContext().getResources().getString(R.string.zimu);
                        ((ReversalTrainPres)trainPres).setTrainingContentType(EnumContentType.LETTER);
                    }
                }else if(((ReversalTrainPres)trainPres).getTrainingContentType()== EnumContentType.NEWS){
                    NewsCategory newsCategory = SQLite.select().from(NewsCategory.class)
                            .where(NewsCategory_Table.id.eq(((ReversalTrainPres)trainPres).getTrainingContentCategoryId())).querySingle();
                    if(newsCategory!=null){
                        bookName = newsCategory.categoryName;
                    }else{
                        bookName = getContext().getResources().getString(R.string.zimu);
                        ((ReversalTrainPres)trainPres).setTrainingContentType(EnumContentType.LETTER);
                    }
                }else{
                    bookName = ((ReversalTrainPres)trainPres).getTrainingContentType().getName();
                }
                String str = getContext().getString(R.string.select_book_is);
                String trainContentSelectTip = String.format(str+"%s", bookName);
                tvTrainContent.setText(trainContentSelectTip);
            }

            trainDeskView.setVisibility(View.VISIBLE);
            trainDeskView.setCloseListener(this);
            trainDeskView.setSelectUserContentListener(this);
        }
    }

    @OnClick(R.id.left_level)
    public void left_choose(TextView textView) {
        eyeType = EnumEyeType.LEFT.getValue();

        listPopupWindow.setAnchorView(textView);
        listPopupWindow.show();
    }


    @OnClick(R.id.right_level)
    public void right_choose(TextView textView) {
        eyeType = EnumEyeType.RIGHT.getValue();
        listPopupWindow.setAnchorView(textView);
        listPopupWindow.show();
    }


    @OnClick(R.id.select_txt_size)
    public void select() {
        optionsPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private void initPop(Context context) {
        final ArrayList<String> datas = new ArrayList<>();
        datas.clear();
        datas.add("(一级)+1.50/-1.50");
        datas.add("(二级)+2.50/-2.50");
        datas.add("(三级)+2.50/-3.50");
        datas.add("(四级)+2.50/-4.50");
        datas.add("(五级)+2.50/-6.00");
        datas.add("(六级)+2.50/-8.00");
        listPopupWindow = new ListPopupWindow(context);
        listPopupWindow.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_single_choice, datas));
        listPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (eyeType == EnumEyeType.LEFT.getValue()) {
                    leftEyeTxt.setText(getContext().getResources().getString(R.string.left_eye) + datas.get(position));
                    reversalPresChangeListener.changeLeft(position);
                } else if (eyeType == EnumEyeType.RIGHT.getValue()) {
                    rightEyeTxt.setText(getContext().getResources().getString(R.string.right_eye) + datas.get(position));
                    reversalPresChangeListener.changeRight(position);
                }
                listPopupWindow.dismiss();
            }
        });

        leftEyeTxt.setText(getContext().getResources().getString(R.string.left_eye) + datas.get(0) + "默认");
        rightEyeTxt.setText(getContext().getResources().getString(R.string.right_eye) + datas.get(0) + "默认");
    }

    public void setStartBtnVisible() {
        startBtn.setVisibility(View.VISIBLE);
    }

    private void showSelectContent() {
        rl_main_container.setVisibility(View.GONE);
        rl_popup_container.setVisibility(View.VISIBLE);
    }

    private void showSelectUserContent() {
        rl_main_container.setVisibility(View.GONE);
        rl_popup_container.setVisibility(View.VISIBLE);
        trainDeskView.initData();
    }

    @Override
    public void onClose() {
        rl_main_container.setVisibility(View.VISIBLE);
        rl_popup_container.setVisibility(View.GONE);
    }

    @Override
    public void selectArticle(int articleId, String articleName) {
        this.articleId = articleId;
        String str = getContext().getString(R.string.select_book_is);
        String trainContentSelectTip = String.format(str+"：%s", articleName);
        tvTrainContent.setText(trainContentSelectTip);
        tvTrainContent.setVisibility(View.VISIBLE);
        popupWindow.dismiss();
        onSelectContent(this.trainingContentType, this.articleId, articleName);
    }

    public void setSelectArticleVisible(int selectArticleVisible) {
        tvTrainContent.setVisibility(selectArticleVisible);
        btnSelectBook.setVisibility(selectArticleVisible);
    }

    public void onSelectContent(int paramOneValue, int paramTwoValue, String title) {
        onClose();
        selectContentListener.onSelectContent(paramOneValue, paramTwoValue, title);
    }

    /**
     * 改变内容
     *
     * @param paramOneValue
     * @param paramTwoValue
     * @param title
     */
    public void changeContent(int paramOneValue, int paramTwoValue, String title) {
        if (prepareDesc.getType() == PrepareDescType.SELECT || prepareDesc.getType() == PrepareDescType.CONTENT) {
            trainingContentType = paramOneValue;
            articleId = paramTwoValue;
            articleName = title;
            if (prepareDesc.getType() == PrepareDescType.CONTENT && trainingContentType == EnumContentType.ARTICLE.getValue()) {
                setSelectArticleVisible(View.VISIBLE);
                String str = getContext().getString(R.string.select_book_is);
                String trainContentSelectTip = String.format(str+"：%s", articleName);
                tvTrainContent.setText(trainContentSelectTip);
                tvTrainContent.setVisibility(View.VISIBLE);
            } else {
                setSelectArticleVisible(View.GONE);
            }
        }
    }

    @Override
    public void onSelectUserContent(EnumContentType trainingContentType, int trainingContentCategoryId, int trainingContentArticleId, String showTitle) {
        onClose();
        String str = getContext().getString(R.string.select_book_is);
        String trainContentSelectTip = String.format(str+"%s", showTitle);
        tvTrainContent.setText(trainContentSelectTip);
        selectUserContentListener.onSelectUserContent(trainingContentType,trainingContentCategoryId,trainingContentArticleId,showTitle);
    }
}
