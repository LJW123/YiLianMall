package com.yilian.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.R;

/**
 * Created by Administrator on 2016/5/24.
 */
    public class NRecordAdapter extends android.widget.BaseAdapter {

    private Context context;

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_nrecord,null);
            }
            return convertView;
        }

    class ViewHolder{
        TextView tv_issue,tv_time,tv_describe,tv_number,tv_count;
        ImageView img_photo;
    }
    }
