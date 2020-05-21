package com.yilian.mall.adapter.imadapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yilian.mall.R;
import com.yilian.mall.entity.MemberLevel1Entity;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;

/**
 * author XiuRenLi on 2016/8/18  PRAY NO BUG
 */

public class BuddyListAdapter extends BaseAdapter {
    private final ArrayList<MemberLevel1Entity.Member> list;
    private final Context context;
    private final ImageLoader imageLoader;

    public BuddyListAdapter(Context context, ArrayList list, ImageLoader imageLoader) {
        this.context = context;
        this.list = list;
        this.imageLoader = imageLoader;
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = getInflate();
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        setViewHolderData(position, holder);
        return convertView;
    }

    private View getInflate() {
        return View.inflate(context, R.layout.im_item_buddy_list, null);
    }

    private void setViewHolderData(final int position, ViewHolder holder) {

        MemberLevel1Entity.Member member = list.get(position);
        holder.tv_initial.setText(TextUtils.isEmpty(member.nameInitial)?"#":member.nameInitial.toUpperCase());
        if(list.size()>1){//只有两个或两个以上好友时，才进行首字母比较
            if(position>0){
                //从第二个开始比较,防止获取不到信息是报空指针 由于该listview添加了头部，所以头部占据了一个position
                //item的position是从1开始的
                if(member.nameInitial.equals(list.get(position-1).nameInitial)){
                    holder.tv_initial.setVisibility(View.GONE);
                }
            }
        }

        holder.name.setText(TextUtils.isEmpty(member.memberName)?"暂无昵称":member.memberName);
        String iconUrl = member.memberIcon;
        if(!TextUtils.isEmpty(iconUrl)){
            GlideUtil.showImageWithSuffix(context,iconUrl,holder.iv_photo);
        }
    }

    public static class ViewHolder {
        private  LinearLayout llBuddy;
        public View rootView;
        public TextView tv_initial;
        public ImageView iv_photo;
        public TextView name;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tv_initial = (TextView) rootView.findViewById(R.id.tv_initial);
            this.iv_photo = (ImageView) rootView.findViewById(R.id.iv_photo);
            this.name = (TextView) rootView.findViewById(R.id.name);
           this.llBuddy = (LinearLayout) rootView.findViewById(R.id.ll_buddy);
        }

    }


//    public int getPositionForSelection(int selection) {
//        for (int i = 0; i < getCount(); i++) {
//            char c = list.get(i).agencyId.charAt(0);
//            if (c == selection) {
//                return i;
//            }
//        }
//        return -1;
//    }
}
