package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
public class CategoryAdapter extends BaseAdapter {
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

    public List<Category> objects;

    public CategoryAdapter(Context context,
                           List<Category> objects) {
        this.context = context;
        // 记录下来稍后使用
        inflater = LayoutInflater.from(context);
        this.objects = objects;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.img_category_default)
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


    public void setData(List<Category> objects){
        this.objects = objects;
    }



    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout imageListView;

        //获取数据
        Category model = objects.get(position);


        ViewHolder holder = null;

        holder = new ViewHolder();
        // 导入布局并赋值给convertview
        convertView = inflater.inflate(R.layout.item_category,parent,false);
        holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
        holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);

        // 为view设置标签
        convertView.setTag(holder);

        holder.txt_name.setText(model.getName());
        if(model.getPic_url()!=null && !model.getPic_url().equals("")) {
            ImageLoader.getInstance().displayImage(ServiceConfig.DOMAIN + model.getPic_url(), holder.iv_pic, options, animateFirstListener);
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView txt_name;
        public ImageView iv_pic;
    }
}
