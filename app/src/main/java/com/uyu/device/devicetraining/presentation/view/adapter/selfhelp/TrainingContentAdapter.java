package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.net.api.ServiceConfig;

import java.util.List;

/**
 * Created by windern on 2016/1/8.
 */
public class TrainingContentAdapter extends BaseAdapter {
    /**
     * 上下文
     */
    private Context context;
    /**
     * 用来导入布局
     */
    private LayoutInflater inflater = null;

    private DisplayImageOptions options;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public List<TrainingContentWeb> objects;

    private OnSelectArticleListener selectArticleListener;

    public TrainingContentAdapter(Context context,
                                  List<TrainingContentWeb> objects, OnSelectArticleListener selectArticleListener) {
        this.context = context;

        // 记录下来稍后使用
        inflater = LayoutInflater.from(context);

        this.objects = objects;

        this.selectArticleListener = selectArticleListener;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.img_book_default)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                        //.displayer(new RoundedBitmapDisplayer(20))
                .build();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return objects.size();
    }


    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return objects.get(position);
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    public void setData(List<TrainingContentWeb> objects) {
        this.objects = objects;
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout imageListView;

        //获取数据
        TrainingContentWeb model = objects.get(position);

        // 系统显示列表时，首先实例化一个适配器（这里将实例化自定义的适配器）。
        // 当手动完成适配时，必须手动映射数据，这需要重写getView（）方法。
        // 系统在绘制列表的每一行的时候将调用此方法。
        // getView()有三个参数，
        // position表示将显示的是第几行，
        // covertView是从布局文件中inflate来的布局。
        // 我们用LayoutInflater的方法将定义好的image_item.xml文件提取成View实例用来显示。
        // 然后将xml文件中的各个组件实例化（简单的findViewById()方法）。
        // 这样便可以将数据对应到各个组件上了。
        //
        ViewHolder holder = null;
        //不能获取原来的，否则点击以后查看内容不正确
        //if (convertView == null) {
        // 获得ViewHolder对象
        holder = new ViewHolder();
        // 导入布局并赋值给convertview
        convertView = inflater.inflate(R.layout.item_training_content_web, parent,false);
        holder.txt_cover_book_name = (TextView) convertView.findViewById(R.id.txt_cover_book_name);
        holder.imgView_cover = (ImageView) convertView.findViewById(R.id.imgView_cover);
        holder.txt_title = (TextView) convertView.findViewById(R.id.txt_title);
        holder.txt_user_name = (TextView) convertView.findViewById(R.id.txt_user_name);
        holder.txt_breif = (TextView) convertView.findViewById(R.id.txt_breif);
        holder.btn_download = (Button) convertView.findViewById(R.id.btn_download);

        // 为view设置标签
        convertView.setTag(holder);
        //} else {
        // 取出holder
        //holder = (ViewHolder) convertView.getTag();
        //}

        // 获取控件,填充数据
        //holder.cimg_portrait
        //holder.txt_cover_book_name.setText(model.Title);
        holder.txt_title.setText(model.getTitle());
        holder.txt_user_name.setText(model.getUser_nick_name());
        holder.txt_breif.setText(model.getBrief());
        //holder.imgView_selected.

        if (model.getPic_url() != null && !model.getPic_url().equals("")) {
            ImageLoader.getInstance().displayImage(ServiceConfig.DOMAIN + "/" + model.getPic_url(), holder.imgView_cover, options, animateFirstListener);
        } else {
            holder.txt_cover_book_name.setText(model.getTitle());
        }


        holder.btn_download.setTag(position);
        holder.btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectPosition = (int) v.getTag();
                TrainingContentWeb model = objects.get(selectPosition);
                selectArticleListener.selectArticle(model.getId(),model.getTitle());
//                DownloadTrainingContentTask task = new DownloadTrainingContentTask(context, ID, (Button) v, finishListener);
//                task.execute();
            }
        });


        return convertView;
    }

    public class ViewHolder {
        public TextView txt_cover_book_name;
        public ImageView imgView_cover;
        public TextView txt_title;
        public TextView txt_user_name;
        public TextView txt_breif;
        public Button btn_download;
    }
}