package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.ModelApiResult;
import com.uyu.device.devicetraining.data.net.api.ApiService;
import com.uyu.device.devicetraining.data.net.api.ServiceConfig;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.util.ServiceFactory;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by windern on 2016/1/8.
 */
public class SelectContentView extends LinearLayout {
    private Context context;

    private int selectArticleId = 0;
    private OnCloseListener closeListener;
    private OnSelectArticleListener selectArticleListener;

    @Bind(R.id.tv_view_bar_title)
    TextView tvViewBarTitle;

    public SelectContentView(Context context) {
        super(context);
        this.context = context;
        initLayout();
    }

    public SelectContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initLayout();
    }

    public SelectContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initLayout();
    }

    private void initLayout() {
        View view = LayoutInflater.from(context).inflate(R.layout.new_view_select_content, this, true);
        ButterKnife.bind(this, view);

        initView();
    }

    public void setCloseListener(OnCloseListener closeListener) {
        this.closeListener = closeListener;
    }

    public void setSelectArticleListener(OnSelectArticleListener selectArticleListener) {
        this.selectArticleListener = selectArticleListener;
    }

    public final static int StatusCategory = 0;
    public final static int StatusListContent = 1;
    public final static int StatusOneContent = 2;

    /**
     * 类别的栈，存储类别的路径
     */
    //public Stack<Integer> categoryStack = new Stack<Integer>();

    /**
     * 当前所在状态
     */
    public int status = StatusCategory;

    /**
     * 当前显示的标题
     */
    public String nowTitle = "云书架";

    @Bind(R.id.swip_category)
    public SwipeRefreshLayout swip_category;

    @Bind(R.id.swip_list_trainingContent)
    public SwipeRefreshLayout swip_list_trainingContent;


    @Bind(R.id.gridview_category)
    public GridView gridview_category;

    //private int categoryID = 1;
    private Category nowCategory;

    private List<Category> mListItems;
    private CategoryAdapter mAdapter;

    //trainingcontentgrid相关的空间
    @Bind(R.id.gridview_trainingContent)
    public GridView gridview_trainingContent;

    private final static int pageSize = 10;
    /**
     * 类别下内容当前页数
     */
    private int page = 0;

    private List<TrainingContentWeb> mListItemContent;
    private GridView mGridView;
    private TrainingContentAdapter mAdapterGridView;

    private int trainingContentID;
    private TrainingContentWeb nowTrainingContentWeb;
    //one view training content
    @Bind(R.id.ll_one_view)
    public LinearLayout ll_one_view;

    @Bind(R.id.imgView_cover)
    public ImageView imgView_cover;

    @Bind(R.id.txt_title)
    public TextView txt_title;

    @Bind(R.id.txt_user_name)
    public TextView txt_user_name;

    @Bind(R.id.txt_download_amount)
    public TextView txt_download_amount;

    @Bind(R.id.txt_breif)
    public TextView txt_breif;

    //    private RequestQueue mQueue;
    private ApiService service;

    /**
     * 类别的堆，用于存储浏览记录
     */
    public Stack<Category> categoryStack = new Stack<>();

    private DisplayImageOptions options;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private void initView() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();

//        mQueue = Volley.newRequestQueue(context);

        service = ServiceFactory.createApiServie();

        nowCategory = new Category();
        nowCategory.id = 1;
        nowCategory.name = "云书架";

        mListItems = new ArrayList<>();

        mAdapter = new CategoryAdapter(context, mListItems);


        gridview_category.setAdapter(mAdapter);

        gridview_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                Category categoryClone = nowCategory.clone();
                //先把当前父类加进去
                categoryStack.push(categoryClone);

                Category category = (Category) parent.getAdapter().getItem(position);

                nowCategory = category;
                showNowStatus();

                getData();

            }
        });

        mListItemContent = new ArrayList<>();

        TextView tv = new TextView(context);
        tv.setGravity(Gravity.CENTER);
        tv.setText("空列表，下拉刷新");

        mAdapterGridView = new TrainingContentAdapter(context, mListItemContent, downloadTrainingContentListener);
        gridview_trainingContent.setAdapter(mAdapterGridView);


        swip_category.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swip_category.setRefreshing(true);

                getData();

            }
        });
        swip_category.setMode(SwipeRefreshLayout.Mode.PULL_FROM_START);

        swip_list_trainingContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swip_list_trainingContent.setRefreshing(true);
                page = 0;
                mListItemContent.clear();
                getListTrainingContent();
            }
        });

        swip_list_trainingContent.setOnLoadListener(new SwipeRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                swip_list_trainingContent.setLoading(true);
                getListTrainingContent();
            }
        });

        //init的时候不获取数据，最后显示的时候获取数据
        //getData();
    }


    @OnClick(R.id.btn_download)
    public void download(View view) {
        selectArticleId = trainingContentID;
    }

    public OnSelectArticleListener downloadTrainingContentListener = new OnSelectArticleListener() {
        @Override
        public void selectArticle(int articleId, String articleName) {
            SelectContentView.this.selectArticleId = articleId;
            selectArticleListener.selectArticle(articleId, articleName);
        }
    };

    public void getData() {
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        Observable<ModelApiResult<List<Category>>> categroyNew = service.getCategroyNew(nowCategory.id, token);
        categroyNew.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelApiResult<List<Category>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (context != null) {
                            ToastUtil.showShortToast(context, e.getMessage());
                        }
                        swip_list_trainingContent.setRefreshing(false);
                        swip_list_trainingContent.setLoading(false);
                    }

                    @Override
                    public void onNext(ModelApiResult<List<Category>> listModelApiResult) {

                        ModelApiResult<List<Category>> apiResult = listModelApiResult;
                        if (apiResult.getCode() == 0) {
                            List<Category> result = apiResult.getData();
                            if (result.size() == 0) {
                                go2TrainingContentList();
                            } else {
                                mListItems.clear();
                                for (int i = 0; i < result.size(); i++) {
                                    mListItems.add(result.get(i));
                                }

                                mAdapter.notifyDataSetChanged();
                                swip_category.setRefreshing(false);
                            }
                        } else {
                            ToastUtil.showShortToast(context, "获取类别列表失败");
                        }


                        swip_list_trainingContent.setRefreshing(false);
                        swip_list_trainingContent.setLoading(false);
                    }
                });
    }

    /**
     * 去跳转到列表下的文章列表
     */
    public void go2TrainingContentList() {
        status = StatusListContent;
        showNowStatus();

        page = 0;
        mListItemContent.clear();
        getListTrainingContent();
    }

    private void getListTrainingContent() {
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        Observable<ModelApiResult<List<TrainingContentWeb>>> itemBooksNew = service.getItemBooksNew(nowCategory.id, page, pageSize, token);
        itemBooksNew.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelApiResult<List<TrainingContentWeb>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Log.e("TAG", error.getMessage(), error);
                        if (context != null) {
                            ToastUtil.showShortToast(context, e.getMessage());
                        }

                        swip_category.setRefreshing(false);
                    }

                    @Override
                    public void onNext(ModelApiResult<List<TrainingContentWeb>> listModelApiResult) {
                        ModelApiResult<List<TrainingContentWeb>> apiResult = listModelApiResult;
                        if (apiResult.getCode() == 0) {
                            List<TrainingContentWeb> result = apiResult.getData();

                            if (result.size() == 0) {
                                ToastUtil.showShortToast(context, "已经全部加载完了");
                            } else {
                                for (int i = 0; i < result.size(); i++) {
                                    mListItemContent.add(result.get(i));
                                }
                                page++;
                            }
                            mAdapterGridView.notifyDataSetChanged();
                            swip_list_trainingContent.setRefreshing(false);
                            swip_list_trainingContent.setLoading(false);
                        } else {
                            ToastUtil.showShortToast(context, "获取图书列表失败");
                        }

                        Type type = new TypeToken<List<Category>>() {
                        }.getType();
                    }
                });
    }

    private void getOneTrainingContent() {
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        Observable<ModelApiResult<TrainingContentWeb>> articleNew = service.getArticleNew(trainingContentID, token);

        articleNew.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelApiResult<TrainingContentWeb>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (context != null) {
                            ToastUtil.showShortToast(context, e.getMessage());
                        }

                        swip_category.setRefreshing(false);
                    }

                    @Override
                    public void onNext(ModelApiResult<TrainingContentWeb> trainingContentWebModelApiResult) {
                        ModelApiResult<TrainingContentWeb> apiResult = trainingContentWebModelApiResult;
                        if (apiResult.getCode() == 0) {
                            TrainingContentWeb result = apiResult.getData();
                            showOne(result);
                        } else {
                            ToastUtil.showShortToast(context, "获取图书失败");
                        }

                        Type type = new TypeToken<List<Category>>() {
                        }.getType();

                    }
                });

    }

    /**
     * 显示一篇文章
     *
     * @param result
     */
    private void showOne(TrainingContentWeb result) {
        txt_title.setText(result.title);
        txt_user_name.setText(result.user_nick_name);
        txt_download_amount.setText(String.valueOf(result.download_amount));
        txt_breif.setText(result.brief);

        if (result.pic_url != null && !result.pic_url.equals("")) {
            ImageLoader.getInstance().displayImage(ServiceConfig.DOMAIN + result.pic_url, imgView_cover, options, animateFirstListener);
        } else {

        }
    }

    /**
     * 显示当前status
     */
    public void showNowStatus() {
        switch (status) {
            case StatusCategory:
                swip_category.setVisibility(View.VISIBLE);
                swip_list_trainingContent.setVisibility(View.GONE);
                ll_one_view.setVisibility(View.GONE);
                nowTitle = nowCategory.name;
                setViewBarTitle(nowTitle);
                break;
            case StatusListContent:
                swip_category.setVisibility(View.GONE);
                swip_list_trainingContent.setVisibility(View.VISIBLE);
                ll_one_view.setVisibility(View.GONE);
                nowTitle = nowCategory.name;
                setViewBarTitle(nowTitle);
                break;
            case StatusOneContent:
                swip_category.setVisibility(View.GONE);
                swip_list_trainingContent.setVisibility(View.GONE);
                ll_one_view.setVisibility(View.VISIBLE);
                nowTitle = nowTrainingContentWeb.title;
                setViewBarTitle(nowTitle);
                break;
            default:
                break;
        }
    }

    private void setViewBarTitle(String viewBarTitle) {
        tvViewBarTitle.setText(viewBarTitle);
    }

    /**
     * 是否全部退完了
     *
     * @return true 全部退完了，可以关闭了，false不用关闭
     */
    @OnClick(R.id.login_title_back)
    public void back() {
        boolean isClose = false;
        if (status == StatusCategory) {
            if (categoryStack.size() > 0) {
                Category previousCategory = categoryStack.pop();
                nowCategory = previousCategory;
                showNowStatus();

                getData();
            } else {
                isClose = true;
            }
        } else if (status == StatusListContent) {
            status = StatusCategory;

            //当前的已经进去了,需要先取两回，然后不需要重新取数据
            Category previousCategory = categoryStack.pop();
            nowCategory = previousCategory;

            showNowStatus();
        } else {
            status = StatusListContent;
            showNowStatus();
        }
    }

    @OnClick(R.id.tv_close)
    public void close() {
        if (closeListener != null) {
            closeListener.onClose();
        }
    }
}
