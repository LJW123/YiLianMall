package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yilian.mall.R;
import com.yilian.mall.entity.AnswerListEntity;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mylibrary.GlideUtil;

import java.util.List;

/**
 * Created by Ray_L_Pain on 2016/10/24 0024.
 */

public class JPCommQuestionAdapter extends android.widget.BaseAdapter {
    private Context context;
    private List<AnswerListEntity.ListBean> list;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private AnswerListEntity.ListBean answerEntity;
    private String iconUrl;
    BitmapUtils bitmapUtils;

    public JPCommQuestionAdapter(Context context, List<AnswerListEntity.ListBean> list) {
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
        QuestionViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_commodity_question, null);
            holder = new QuestionViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.title_img);
            holder.tvQ = (TextView) convertView.findViewById(R.id.tv_question);
            holder.tvA = (TextView) convertView.findViewById(R.id.tv_answer);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (QuestionViewHolder) convertView.getTag();
        }

        answerEntity = list.get(position);

        GlideUtil.showCirImage(context, answerEntity.photo, holder.iv);

        holder.tvQ.setText(answerEntity.question_info);

        if (TextUtils.isEmpty(answerEntity.reply)) {
            holder.tvA.setText(R.string.merchant_no_reply);
        } else {
            holder.tvA.setText(Html.fromHtml("<font color='#333333'>卖家: </font><font color='#666666'>" + answerEntity.reply + "</font>"));
        }

        holder.tvTime.setText(DateUtils.timeStampToStr(Long.parseLong(answerEntity.create_time)));

        return convertView;
    }

    class QuestionViewHolder {
        ImageView iv;
        TextView tvQ;
        TextView tvA;
        TextView tvTime;
    }

}
