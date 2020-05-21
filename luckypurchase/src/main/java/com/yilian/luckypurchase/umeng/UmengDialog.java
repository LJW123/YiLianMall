package com.yilian.luckypurchase.umeng;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.luckypurchase.R;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class UmengDialog extends Dialog implements OnItemClickListener, View.OnClickListener {

    private Context mContext;
    private View view;
    private GridView grid;
    private TextView del;
    private List<IconModel> icon = new ArrayList<IconModel>();
    private OnListItemClickListener itemLister;
    private Animation animBottom;

    public UmengDialog(Context context, ArrayList<IconModel> array) {
        super(context);
        mContext = context;
        view = LayoutInflater.from(mContext).inflate(R.layout.lucky_umeng_share_dialog,
                null);
        icon = array;
    }

    public UmengDialog(Context context, Animation animBottom, int theme, List<IconModel> list) {
        super(context, theme);
        mContext = context;
        animBottom = animBottom;
        icon = list;
        view = LayoutInflater.from(mContext).inflate(R.layout.lucky_umeng_share_dialog,
                null);
        setContentView(view);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (int) (d.getWidth() * 1); // 宽度设置为屏幕的0.65
        getWindow().setAttributes(p);
        getWindow().setGravity(Gravity.BOTTOM);
        grid = (GridView) view.findViewById(R.id.umeng_dialog__grid);
        grid.setAdapter(new IconAdapter());
        grid.setOnItemClickListener(this);
        del = (TextView) view.findViewById(R.id.quxiao);
        del.setOnClickListener(this);
        view.setAnimation(animBottom);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public class IconAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return icon.size();
        }

        @Override
        public Object getItem(int arg0) {
            return icon.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            Hodler hoder = null;
            IconModel model = icon.get(arg0);
            if (arg1 == null) {
                arg1 = LayoutInflater.from(mContext).inflate(
                        R.layout.lucky_item_umeng_grid, null);
                hoder = new Hodler();
                hoder.img = (ImageView) arg1.findViewById(R.id.item_umeng_img);
                hoder.tx = (TextView) arg1.findViewById(R.id.item_umeng_tx);
                arg1.setTag(hoder);
            } else {
                hoder = (Hodler) arg1.getTag();
            }

            hoder.tx.setText(model.getTitle());
            switch (model.getType()) {
                case 0:
                    hoder.img.setImageResource(R.mipmap.lucky_wx);
                    break;
                case 1:
                    hoder.img.setImageResource(R.mipmap.lucky_weixinpengyouquan);
                    break;
            /*case 2:
                hoder.img.setImageResource(R.drawable.xinlangweibo);
				break;*/
                case 3:
                    hoder.img.setImageResource(R.mipmap.lucky_qkongjian);
                    break;
                case 4:
                    hoder.img.setImageResource(R.mipmap.lucky_qqhaoyou);
                    break;
            /*case 5:
				hoder.img.setImageResource(R.drawable.tengxunweibo);
				break;*/
                default:
                    break;
            }
            return arg1;
        }

    }

    public class Hodler {
        public ImageView img;
        public TextView tx;
    }

    public void setItemLister(OnListItemClickListener itemLister) {
        this.itemLister = itemLister;
    }

    /*
     * 列表项点击事件监听器
     */
    public interface OnListItemClickListener extends EventListener {
        public void OnItemClick(AdapterView<?> arg0, View arg1,
                                int arg2, long arg3, Object arg4);
    }

    public void setcancle() {
        dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        if (itemLister != null) {
            itemLister.OnItemClick(arg0, arg1, arg2, arg3, icon.get(arg2));
        }
        dismiss();
    }

    @Override
    public void onClick(View v) {
        if (animBottom != null) {
            if (animBottom.hasStarted()) {
                animBottom.cancel();
            }
            animBottom = null;
        }

        dismiss();
    }

}