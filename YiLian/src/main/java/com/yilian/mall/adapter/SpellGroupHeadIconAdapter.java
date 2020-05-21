package com.yilian.mall.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.widgets.JHCircleView;
import com.yilian.mylibrary.Constants;

import java.util.List;


/**
 * Created by liuyuqi on 2017/5/21 0021.
 */
public class SpellGroupHeadIconAdapter extends BaseListAdapter<String> {


    private String imageUrl;

    public SpellGroupHeadIconAdapter(List<String> datas) {
        super(datas);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        IconHoler holer;
        if (view == null) {
            view = View.inflate(context, R.layout.item_spell_group_head_icon, null);
            holer = new IconHoler(view);
            view.setTag(holer);
        } else {
            holer = (IconHoler) view.getTag();
        }

        imageUrl = datas.get(position);
        if (!TextUtils.isEmpty(imageUrl)){
            if (imageUrl.contains("http://") || imageUrl.contains("https://")) {
                imageUrl = imageUrl;
            } else {
                imageUrl = Constants.ImageUrl + imageUrl;
            }
            imageLoader.displayImage(imageUrl,holer.ivIcon,options);
        }else{
            holer.ivIcon.setImageResource(R.mipmap.default_head_icon);
        }
        if (position==0){
            holer.tvTag.setVisibility(View.VISIBLE);
        }
        Logger.i("imageUrl  " + imageUrl);

        return view;
    }

    class IconHoler {
        JHCircleView ivIcon;
        TextView tvTag;

        public IconHoler(View view) {
            this.ivIcon = (JHCircleView) view.findViewById(R.id.headview);
            this.tvTag = (TextView) view.findViewById(R.id.tv_head_view_tag);
        }
    }
}
