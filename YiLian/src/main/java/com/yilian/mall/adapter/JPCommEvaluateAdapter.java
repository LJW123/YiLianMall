package com.yilian.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.EvaluateList;
import com.yilian.mall.ui.ImagePagerActivity;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mall.widgets.NoScrollGridView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;

import java.util.List;

import static com.yilian.mall.R.id.ratingBar;

/**
 * Created by Ray_L_Pain on 2016/10/24 0024.
 */

public class JPCommEvaluateAdapter extends android.widget.BaseAdapter {
    private Context context;
    private List<EvaluateList.Evaluate> list;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private EvaluateList.Evaluate evaluate;
    private String iconUrl;
    BitmapUtils bitmapUtils;

    public JPCommEvaluateAdapter(Context context, List<EvaluateList.Evaluate> list) {
        this.context = context;
        this.list = list;
        bitmapUtils = new BitmapUtils(context);
        options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.ic_evaluate_user)
                .showImageForEmptyUri(R.mipmap.ic_evaluate_user).showImageOnFail(R.mipmap.ic_evaluate_user)
                // 这里的三张状态用一张替代
                .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public int getCount() {
        if (list.size() == 0 || list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EvaluateClassHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_commodity_evaluate, null);
            holder = new EvaluateClassHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.title_img);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            holder.gridView = (NoScrollGridView) convertView.findViewById(R.id.girdView);
            convertView.setTag(holder);
        } else {
            holder = (EvaluateClassHolder) convertView.getTag();
        }

        evaluate = list.get(position);

        Logger.i("rng-" + evaluate.icon);
        GlideUtil.showCirImage(context, evaluate.icon, holder.iv);

        holder.tvName.setText(evaluate.commentConsumer);
        holder.tvTime.setText(StringFormat.formatDate(evaluate.commentTime, "yyyy-MM-dd"));

        float grade = (float) (Math.round((evaluate.commentScore / 10.0f) * 10)) / 10;
        holder.ratingBar.setRating(grade);
        holder.tvContent.setText(evaluate.commentContent);

        final String[] img = evaluate.commentImages.split(",");

        for (int i = 0; i < img.length; i++) {
            if (!TextUtils.isEmpty(img[i])) {
                img[i] = Constants.ImageUrl + img[i] + Constants.ImageSuffix;
                holder.gridView.setVisibility(View.VISIBLE);
            } else {
                holder.gridView.setVisibility(View.GONE);
            }
        }
        JPGridViewAdapter adapter = new JPGridViewAdapter(context, img);
        holder.gridView.setAdapter(adapter);
        holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageBrower(position, img);
            }
        });

        return convertView;
    }

    class EvaluateClassHolder {
        ImageView iv;
        TextView tvName;
        TextView tvTime;
        RatingBar ratingBar;
        TextView tvContent;
        NoScrollGridView gridView;
    }

    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }
}
