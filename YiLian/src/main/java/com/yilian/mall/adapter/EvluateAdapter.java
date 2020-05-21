package com.yilian.mall.adapter;

import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.MTComboDetailsEntity;
import com.yilian.mall.ui.ImagePagerActivity;
import com.yilian.mall.ui.MTPackageCommentListActivity;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.widgets.JHCircleView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ScreenUtils;

import java.util.List;

/**
 * Created by liuyuqi on 2016/12/10 0010.
 */
public class EvluateAdapter extends BaseListAdapter<MTComboDetailsEntity.DataBean.EvaluateListBean> {
    private final String evaluateCount;
    private final String packageId;
    private final String merchantId;


    public EvluateAdapter(List<MTComboDetailsEntity.DataBean.EvaluateListBean> datas, String evaluateCount, String merchant_id, String package_id) {
        super(datas);
        this.evaluateCount = evaluateCount;
        this.packageId = package_id;
        this.merchantId = merchant_id;
        Logger.i("接收到的数据 packageId:" + package_id);
        Logger.i("evluateListDatas" + datas.toString());
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        EvaluateHolder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.mt_item_commodity_evaluate, null);
            holder = new EvaluateHolder();
            holder.evaluateIcon = (JHCircleView) view.findViewById(R.id.title_img);
            holder.evaluateName = (TextView) view.findViewById(R.id.tv_name);
            holder.evaluateTime = (TextView) view.findViewById(R.id.tv_time);
            holder.evaluateRating = (RatingBar) view.findViewById(R.id.ratingBar);
            holder.evaluateContent = (TextView) view.findViewById(R.id.tv_content_evluate);
            holder.rlLookMore = (RelativeLayout) view.findViewById(R.id.rl_all_comment);
            holder.lookMore = (TextView) view.findViewById(R.id.tv_look_all_comment);
            holder.llCommetnImg = (LinearLayout) view.findViewById(R.id.ll_comment_img);
            holder.llMerchantReply = (LinearLayout) view.findViewById(R.id.ll_merchant_repley);
            holder.tvMerchantReply = (TextView) view.findViewById(R.id.tv_merchant_reply);
            holder.viewLine = view.findViewById(R.id.view_line);
            view.setTag(holder);
        } else {
            holder = (EvaluateHolder) view.getTag();
        }
        MTComboDetailsEntity.DataBean.EvaluateListBean evaluateListBean = datas.get(position);
        if (!TextUtils.isEmpty(evaluateListBean.merchantComment)) {
            holder.llMerchantReply.setVisibility(View.VISIBLE);
            Spanned spanned = Html.fromHtml("<font color='#ff7701'>商家回复:</font><font color='#999999'>" + evaluateListBean.merchantComment + "</font>");
            holder.tvMerchantReply.setText(spanned);
        } else {
            holder.llMerchantReply.setVisibility(View.GONE);
        }

        String imageurl = evaluateListBean.imageUrl;
//        GlideUtil.showImageWithSuffix(context, imageurl, holder.evaluateIcon);
        if (null != imageurl) {
            if (!imageurl.contains("http://") || !imageurl.contains("https://")) {
                imageurl = Constants.ImageUrl + imageurl + Constants.ImageSuffix;
            }
        } else {
            holder.evaluateIcon.setImageResource(R.mipmap.login_module_default_jp);
        }
        imageLoader.displayImage(imageurl, holder.evaluateIcon, options);

        holder.evaluateContent.setText(evaluateListBean.consumerComment);
        holder.evaluateName.setText(evaluateListBean.name);
        holder.evaluateTime.setText(DateUtils.timeStampToStr(Long.parseLong(evaluateListBean.commitDate)));
        float graded = NumberFormat.convertToFloat(evaluateListBean.evaluate, 0f) / 10;
        if (0 == graded) {
            holder.evaluateRating.setRating(5.0f);
        } else {
            holder.evaluateRating.setRating(graded);
        }
        /**
         * ray
         */
        holder.llCommetnImg.removeAllViews();//添加之前先移除所有控件，防止图片错位
        String[] img = evaluateListBean.album.split(",");
        for (int i = 0; i < img.length; i++) {
            if (!TextUtils.isEmpty(img[i])) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(context) / 7, ScreenUtils.getScreenWidth(context) / 7);
                if (i < img.length - 1) {
                    params.setMargins(0, 0, 20, 0);
                }
                imageView.setLayoutParams(params);
                GlideUtil.showImageWithSuffix(context, img[i], imageView);
                holder.llCommetnImg.addView(imageView);
                int finalI = i;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Logger.i("当前点击的position " + finalI + " iag  " + img);
                        imageBrower(finalI, img);
                    }
                });
            }
        }

        //最后一个position
        Logger.i("dataSize" + datas.size() + "position" + position
        );
        if (datas.size() - 1 == position) {
            holder.viewLine.setVisibility(View.VISIBLE);
            if ("".equals(evaluateCount)) {
                holder.rlLookMore.setVisibility(View.VISIBLE);
                holder.lookMore.setText("暂无更多评价");
            } else if (Integer.valueOf(evaluateCount) < 3) {//只有超过两条评论时，才显示查看更多
                holder.rlLookMore.setVisibility(View.GONE);
            } else {
                holder.rlLookMore.setVisibility(View.VISIBLE);
                holder.lookMore.setText("查看全部" + evaluateCount + "条评论");
            }
        } else {
            holder.rlLookMore.setVisibility(View.GONE);
            holder.viewLine.setVisibility(View.GONE);
        }

        //TODO 评论详情
        holder.rlLookMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MTPackageCommentListActivity.class);
                intent.putExtra("packageId", packageId);
                intent.putExtra("merchantId", merchantId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                context.startActivity(intent);
            }
        });

        return view;
    }

    class EvaluateHolder {
        JHCircleView evaluateIcon;
        TextView evaluateName;
        TextView evaluateTime;
        RatingBar evaluateRating;
        TextView evaluateContent;
        public LinearLayout llCommetnImg;
        //查看全部评论
        RelativeLayout rlLookMore;
        TextView lookMore;
        LinearLayout llMerchantReply;
        TextView tvMerchantReply;
        View viewLine;
    }

    class Grid5ViewAdapter extends BaseAdapter {


        private final String[] Images;

        public Grid5ViewAdapter(String[] album) {
            this.Images = album;
        }

        @Override
        public int getCount() {
            if (Images.length <= 0) {
                return 0;
            }
            return Images.length;
        }

        @Override
        public Object getItem(int i) {
            return Images[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            //只有图片
            ImageView imageView = new ImageView(context);
            AbsListView.LayoutParams layoutParams =
                    new AbsListView.LayoutParams(ScreenUtils.getScreenWidth(context) / 7, ScreenUtils.getScreenWidth(context) / 7);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageLoader.displayImage(Images[position], imageView, options);

            imageView.setLayoutParams(layoutParams);


            return imageView;
        }
    }

    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }
}
