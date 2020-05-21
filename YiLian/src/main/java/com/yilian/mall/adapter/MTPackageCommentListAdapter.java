package com.yilian.mall.adapter;/**
 * Created by  on 2016/12/7 0007.
 */

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.MTPackageCommentListEntity;
import com.yilian.mall.ui.ImagePagerActivity;
import com.yilian.mall.utils.BitmapUtil;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.widgets.JHCircleView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ScreenUtils;

import java.util.ArrayList;

/**
 * Created by  on 2016/12/7 0007.
 */
public class MTPackageCommentListAdapter extends android.widget.BaseAdapter {
    private final Context context;
    private final ArrayList<MTPackageCommentListEntity.DataBean> list;
    private final ImageLoader imageLoader;
    private final DisplayImageOptions options;

    public MTPackageCommentListAdapter(Context context, ArrayList<MTPackageCommentListEntity.DataBean> list, ImageLoader imageLoader, DisplayImageOptions options) {
        this.context = context;
        this.list = list;
        this.imageLoader = imageLoader;
        this.options = options;
    }

    @Override
    public int getCount() {
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_mt_package_comment_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MTPackageCommentListEntity.DataBean dataBean = list.get(position);
        if(!TextUtils.isEmpty(dataBean.merchantComment)){
            viewHolder.llMerchantReply.setVisibility(View.VISIBLE);
            viewHolder.tvMerchantReply.setText("                   " + dataBean.merchantComment);
        }else{
            viewHolder.llMerchantReply.setVisibility(View.GONE);
        }
        String imageUrl = dataBean.imageUrl;
        if (!imageUrl.contains("http://")||!imageUrl.contains("https://")) {
            imageUrl = Constants.ImageUrl + imageUrl;
        }
        imageLoader.displayImage(imageUrl, viewHolder.cvHeadPhoto, options);//设置头像
        viewHolder.tvNick.setText(dataBean.name);//用户名
        viewHolder.tvAnnouncedTime.setText(DateUtils.timeStampToStr(NumberFormat.convertToLong(dataBean.commitDate, 0L)));//评论时间
        viewHolder.rbPackageTotalScore.setRating(NumberFormat.convertToFloat(dataBean.evaluate, 0f) / 10);//评星 服务器返回数据除以10之后进行赋值
        viewHolder.tvComment.setText(dataBean.consumerComment);//评论内容
        String[] commentImgUrls = dataBean.album.split(",");//评论图片
        Logger.i("评论图片集合：" + commentImgUrls.toString());
        String commentImgUrl;
        viewHolder.llSharePic.removeAllViews();//添加之前先移除所有子控件，防止重复添加
        for (int i = 0, count = commentImgUrls.length; i < count; i++) {
            commentImgUrl = commentImgUrls[i];
            Logger.i("评论图片："+commentImgUrl);
            if (!TextUtils.isEmpty(commentImgUrl)) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(context) / 7, ScreenUtils.getScreenWidth(context) / 7);
                if (i < commentImgUrls.length - 1) {//如果不是最后一个，都加上marginRight
                    params.rightMargin = 20;
                }
                imageView.setLayoutParams(params);

                if (!commentImgUrl.contains("http://")||!commentImgUrl.contains("https://")) {
                    commentImgUrl = Constants.ImageUrl + commentImgUrl + BitmapUtil.getBitmapSuffix(context, 5);
                }
                imageLoader.displayImage(commentImgUrl, imageView, options);


                viewHolder.llSharePic.addView(imageView);
                int finalI = i;
                imageView.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
//                    TODO 点击查看每个评论图片的大图
                                                     imageBrower(finalI,commentImgUrls);
                                                 }
                                             }
                );
            }

        }
        return convertView;
    }

    public static class ViewHolder {
        public View rootView;
        public JHCircleView cvHeadPhoto;
        public TextView tvNick;
        public TextView tvAnnouncedTime;
        public RatingBar rbPackageTotalScore;
        public TextView tvComment;
        public LinearLayout llSharePic;
        LinearLayout llMerchantReply;
        TextView tvMerchantReply;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.cvHeadPhoto = (JHCircleView) rootView.findViewById(R.id.cv_head_photo);
            this.tvNick = (TextView) rootView.findViewById(R.id.tv_nick);
            this.tvAnnouncedTime = (TextView) rootView.findViewById(R.id.tv_announced_time);
            this.rbPackageTotalScore = (RatingBar) rootView.findViewById(R.id.rb_package_total_score);
            this.tvComment = (TextView) rootView.findViewById(R.id.tv_comment);
            this.llSharePic = (LinearLayout) rootView.findViewById(R.id.ll_share_pic);
            this.llMerchantReply = (LinearLayout)rootView.findViewById(R.id.ll_merchant_repley);
            this.tvMerchantReply = (TextView)rootView.findViewById(R.id.tv_merchant_reply);
        }

    }
    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }
}
