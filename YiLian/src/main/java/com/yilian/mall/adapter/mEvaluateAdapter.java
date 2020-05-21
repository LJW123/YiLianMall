package com.yilian.mall.adapter;

import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.MTMerchantDetailEntity;
import com.yilian.mall.ui.ImagePagerActivity;
import com.yilian.mall.ui.MTPackageCommentListActivity;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.widgets.JHCircleView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ScreenUtils;

import java.util.List;


/**
 * Created by liuyuqi on 2016/12/8 0008.
 * 总体评价的adapter
 */
public class mEvaluateAdapter extends BaseListAdapter<MTMerchantDetailEntity.InfoBean.CommentsBean> {

    private String commentsCount;
    private String merchantId;

    public mEvaluateAdapter(List<MTMerchantDetailEntity.InfoBean.CommentsBean> datas, String commentsCount, String merchantId) {
        super(datas);
        this.commentsCount = commentsCount;
        this.merchantId = merchantId;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        EvaluateHolder holder = null;
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
            holder.tvMerchantReply = (TextView) view.findViewById(R.id.tv_merchant_reply);
            holder.llMerchantReply = (LinearLayout) view.findViewById(R.id.ll_merchant_repley);
            holder.llCommetnImg = (LinearLayout) view.findViewById(R.id.ll_comment_img);
            holder.viewLine = view.findViewById(R.id.view_line);
            view.setTag(holder);
        } else {
            holder = (EvaluateHolder) view.getTag();
        }

        MTMerchantDetailEntity.InfoBean.CommentsBean commentsBean = datas.get(position);
        if(!TextUtils.isEmpty(commentsBean.merchantComment)){
            holder.llMerchantReply.setVisibility(View.VISIBLE);
            Spanned spanned = Html.fromHtml("<font color='#ff7701'>商家回复:</font><font color='#999999'>" + commentsBean.merchantComment + "</font>");
            holder.tvMerchantReply.setText(spanned);
        }else{
            holder.llMerchantReply.setVisibility(View.GONE);
        }
        String imageurl = commentsBean.imageurl;
        if (null != imageurl) {
            if (!imageurl.contains("http://")||!imageurl.contains("https://")) {
                imageurl = Constants.ImageUrl + imageurl + Constants.ImageSuffix;
            }
        } else {
            holder.evaluateIcon.setImageResource(R.mipmap.login_module_default_jp);
        }
        imageLoader.displayImage(imageurl, holder.evaluateIcon, options);
        Logger.i("taocan商家详情  commentsBean.imageurl " + commentsBean.imageurl + "  处理过后的imageUrl  " + imageurl);

        holder.evaluateName.setText(commentsBean.name);
        //返回的是间戳
        holder.evaluateTime.setText(DateUtils.timeStampToStr(Long.parseLong(commentsBean.commitDate)));
        holder.evaluateContent.setText(commentsBean.consumerComment);
        int graded = Integer.parseInt(commentsBean.evaluate) / 10;
        Logger.i("graded" + graded + " commentsBean.taste" + commentsBean.evaluate);
        if (0 == graded) {
            holder.evaluateRating.setRating(5.0f);
        } else {
            holder.evaluateRating.setRating(graded);
        }

        if (datas.size() - 1 == position && Integer.parseInt(commentsCount) > 2) {
            holder.rlLookMore.setVisibility(View.VISIBLE);
            holder.viewLine.setVisibility(View.VISIBLE);
            if (!"".equals(commentsCount)) {
                holder.lookMore.setText("查看全部" + commentsCount + "条评论");
            } else {
                holder.lookMore.setText("暂无更多评论");
            }
        } else {
            holder.rlLookMore.setVisibility(View.GONE);
            holder.viewLine.setVisibility(View.GONE);
        }

        holder.rlLookMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MTPackageCommentListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("packageId", "0");
                intent.putExtra("merchantId", merchantId);
                context.startActivity(intent);
            }
        });
        holder.llCommetnImg.removeAllViews();//添加之前先移除所有控件，防止图片错位
        String[] img = commentsBean.album.split(",");
        for (int i = 0; i < img.length; i++) {
            if (!TextUtils.isEmpty(img[i])) {
                img[i] = Constants.ImageUrl + img[i];
                ImageView imageView = new ImageView(context);

                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(context) / 7, ScreenUtils.getScreenWidth(context) / 7);
                if (i < img.length - 1) {
                    params.setMargins(0, 0, 20, 0);
                }
                imageView.setLayoutParams(params);
                imageLoader.displayImage(img[i], imageView);
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


        return view;
    }


    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }

    class EvaluateHolder {
        JHCircleView evaluateIcon;
        TextView evaluateName;
        TextView evaluateTime;
        RatingBar evaluateRating;
        TextView evaluateContent;
        LinearLayout llMerchantReply;
        //查看全部评论
        RelativeLayout rlLookMore;
        TextView lookMore;
        public LinearLayout llCommetnImg;
        TextView tvMerchantReply;
        View viewLine;
    }
}
