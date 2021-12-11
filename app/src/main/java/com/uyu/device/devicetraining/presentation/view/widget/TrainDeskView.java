package com.uyu.device.devicetraining.presentation.view.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.ModelApiResult;
import com.uyu.device.devicetraining.data.entity.content.DeskBook;
import com.uyu.device.devicetraining.data.entity.content.NewsCategory;
import com.uyu.device.devicetraining.data.entity.content.NewsCategory_Table;
import com.uyu.device.devicetraining.data.entity.content.TrainingContent;
import com.uyu.device.devicetraining.data.entity.content.TrainingContent_Table;
import com.uyu.device.devicetraining.data.entity.content.UserUploadIds;
import com.uyu.device.devicetraining.data.net.api.ApiService;
import com.uyu.device.devicetraining.data.net.api.ServiceFactory;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;
import com.uyu.device.devicetraining.presentation.view.adapter.DeskAdapter;
import com.uyu.device.devicetraining.presentation.view.adapter.OnSelectUserContentListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.OnCloseListener;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.OnSelectArticleListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by windern on 2016/5/25.
 */
public class TrainDeskView extends LinearLayout {
    private Context context;
    private View view;
    private DeskAdapter mAdapter;

    @Bind(R.id.gridView)
    GridView gridView;

    private ArrayList<DeskBook> deskBookList;

    private int userId = 0;
    private OnCloseListener closeListener;
    private OnSelectUserContentListener selectUserContentListener;

    public TrainDeskView(Context context) {
        super(context);
        this.context = context;

        initLayout();
    }

    public TrainDeskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        initLayout();
    }

    public TrainDeskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        initLayout();
    }

    /**
     * 初始化布局
     */
    public void initLayout(){
        view = LayoutInflater.from(context).inflate(R.layout.view_train_desk, this, true);
        ButterKnife.bind(this, view);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeskBook deskBook = deskBookList.get(position);
                if(selectUserContentListener!=null){
                    String showTitle = deskBook.getTitle();
                    selectUserContentListener.onSelectUserContent(deskBook.getContentType(),deskBook.getId(),deskBook.getId(),showTitle);
                }
            }
        });
    }

    public void setCloseListener(OnCloseListener closeListener) {
        this.closeListener = closeListener;
    }

    public void setSelectUserContentListener(OnSelectUserContentListener selectUserContentListener) {
        this.selectUserContentListener = selectUserContentListener;
    }

    @OnClick(R.id.tv_close)
    public void close() {
        if (closeListener != null) {
            closeListener.onClose();
        }
    }

    public void initData(){
        ApiService apiService = ServiceFactory.create(ServiceFactory.TYPE_OBJECT);
        ContentUseCase contentUseCase = new ContentUseCase(apiService);

        String token = SharePreferenceTool.getSharePreferenceValue(context,SharePreferenceTool.PREF_DEVICE_TOKEN);
        int userId = SharePreferenceTool.getSharePreferenceValueInt(context, SharePreferenceTool.PREF_NOW_TRAIN_USER_ID);
        contentUseCase.getUploadIDs(token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(apiResult.getData(), apiResult.getData().getClass());
                        SharePreferenceTool.setSharePreferenceValue(context,SharePreferenceTool.PREF_NOW_TRAIN_USER_UPLOADIDS,resultJson);

                        Timber.d("resultJson:%s", resultJson);

                        for (UserUploadIds userUploadIds : apiResult.getData()) {
                            if (userUploadIds.type == 0) {
                                for (Integer id : userUploadIds.ids) {
                                    TrainingContent trainingContent = SQLite.select().from(TrainingContent.class).where(TrainingContent_Table.id.eq(id)).querySingle();
                                    if (trainingContent == null) {
                                        contentUseCase.getTrainingContent(token, id)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(Schedulers.io())
                                                .subscribe(trainingContentModelApiResult -> {
                                                    if (trainingContentModelApiResult.getCode() == 0) {
                                                        trainingContentModelApiResult.getData().save();
                                                    }
                                                });
                                    }
                                }
                            } else if (userUploadIds.type == 1) {
                                contentUseCase.getNewsCategories(token)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(Schedulers.io())
                                        .subscribe(newsCategoriesModelApiResult -> {
                                            if (newsCategoriesModelApiResult.getCode() == 0) {
                                                for (NewsCategory newsCategory : newsCategoriesModelApiResult.getData()) {
                                                    newsCategory.save();
                                                }
                                            }
                                        });
                            }
                        }
                        return Observable.just(apiResult.getData());
                    } else {
                        return Observable.error(new Exception(apiResult.getMessage()));
                    }
                })
                .subscribe(list -> {
                    refreshDataView(list);
                }, error -> {
                    showNetworkExceptionDialog();
                });

    }

    public void showNetworkExceptionDialog(){
        Dialog alertDialog = new AlertDialog.Builder(context).
                setTitle("提示").
                setMessage("网络响应超时，请检查网络连接").
                setIcon(R.mipmap.ic_launcher).
                setPositiveButton("重试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        retry();
                    }
                }).
                create();
        alertDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        alertDialog.show();
    }

    public void retry(){
        initData();
    }

    public void refreshDataView(List<UserUploadIds> userUploadIdsList){
        ArrayList<DeskBook> deskBooks = new ArrayList<>();

        deskBooks.add(DeskBook.getNumberDeskBook());
        deskBooks.add(DeskBook.getLetterDeskBook());

        for(UserUploadIds userUploadIds:userUploadIdsList) {
            if (userUploadIds.type == 0) {
                for (Integer id : userUploadIds.ids) {
                    TrainingContent trainingContent = SQLite.select().from(TrainingContent.class).where(TrainingContent_Table.id.eq(id)).querySingle();
                    deskBooks.add(trainingContent.toDeskBook());
                }
            } else if (userUploadIds.type == 1) {
                for (Integer id : userUploadIds.ids) {
                    NewsCategory newsCategory = SQLite.select().from(NewsCategory.class).where(NewsCategory_Table.id.eq(id)).querySingle();
                    deskBooks.add(newsCategory.toDeskBook());
                }
            }
        }
        setAdapter(deskBooks);
    }

    /**
     * 设置适配器
     * @param list
     */
    public void setAdapter(ArrayList<DeskBook> list){
        deskBookList = list;
        if(mAdapter == null){
            mAdapter = new DeskAdapter(context, deskBookList);
            gridView.setAdapter(mAdapter);
        }else{
            mAdapter.setData(deskBookList);
            mAdapter.notifyDataSetChanged();
        }
    }
}
