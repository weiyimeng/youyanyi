package com.uyu.device.devicetraining.presentation.view.adapter;

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
import com.uyu.device.devicetraining.data.entity.content.DeskBook;
import com.uyu.device.devicetraining.data.net.api.ServiceConfig;
import com.uyu.device.devicetraining.presentation.view.adapter.selfhelp.AnimateFirstDisplayListener;

import java.util.List;

public class DeskAdapter extends BaseAdapter {
    /**
     * 上下文
     */
    private Context context;
    /**
     * 用来导入布局
     */
    private LayoutInflater inflater = null;

    public List<DeskBook> objects;

    private boolean isEditStatus = false;

    private View.OnClickListener mDelListener;

    private DisplayImageOptions options;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private long selectedDeskBookID = 0;

    public DeskAdapter(Context context, List<DeskBook> objects) {
        this.context = context;

        // 记录下来稍后使用
        inflater = LayoutInflater.from(context);

        this.objects = objects;

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
        return objects.size();
    }


    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public void setData(List<DeskBook> objects) {
        this.objects = objects;
    }

    public boolean isEditStatus() {
        return isEditStatus;
    }

    public void setEditStatus(boolean isEditStatus) {
        this.isEditStatus = isEditStatus;
    }

    public View.OnClickListener getmDelListener() {
        return mDelListener;
    }

    public void setmDelListener(View.OnClickListener mDelListener) {
        this.mDelListener = mDelListener;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout imageListView;
        DeskBook model = objects.get(position);
        ViewHolder holder = null;
        holder = new ViewHolder();
        convertView = inflater.inflate(R.layout.adapter_desk, null);
        holder.view_del = (ImageView) convertView.findViewById(R.id.view_del);
        holder.imgView_cover = (ImageView) convertView.findViewById(R.id.imgView_cover);
        holder.imgView_selected = (ImageView) convertView.findViewById(R.id.imgView_selected);
        holder.txt_title = (TextView) convertView.findViewById(R.id.txt_title);

        convertView.setTag(holder);

        holder.view_del.setTag(model);
        if (isEditStatus) {
            holder.view_del.setVisibility(View.VISIBLE);
        } else {
            holder.view_del.setVisibility(View.GONE);
        }

        // 获取控件,填充数据
        holder.txt_title.setText(model.getTitle());

        if (model.getPicUrl() != null && !model.getPicUrl().equals("")) {
            ImageLoader.getInstance().displayImage(ServiceConfig.DOMAIN + model.getPicUrl(), holder.imgView_cover, options, animateFirstListener);
            holder.txt_title.setText(model.getTitle());
        } else {
            holder.imgView_cover.setImageResource(R.drawable.cover_txt);
            holder.txt_title.setText(model.getTitle());
        }

        holder.view_del.setOnClickListener(mDelListener);
        return convertView;
    }

    public class ViewHolder {
        public ImageView view_del;
        public ImageView imgView_cover;
        public ImageView imgView_selected;
        public TextView txt_title;
    }

}
