package com.yilian.mall.jd.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择
 * by Zg 2018.6.20
 */
public class JDPickImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public static final String ADD = "add";
    private int maxNum = 9;

    public JDPickImageAdapter(int maxNum) {
        super(R.layout.jd_item_pick_image);
        this.maxNum = maxNum;
        //添加 add
        List<String> list = new ArrayList<>();
        list.add(JDPickImageAdapter.ADD);
        setNewData(list);
    }

    /**
     * 处理图片添加逻辑
     *
     * @param index
     * @param str
     */
    public void addItem(int index, String str) {
        if (getData() != null) {
            if (getData().size() >= maxNum) {
                setData(getData().size() - 1, str);
            } else {
                addData(getData().size() - 1, str);
            }
        } else {
            addData(str);
            addData(JDPickImageAdapter.ADD);
        }
    }

    /**
     * 处理图片移除
     *
     * @param position
     */
    @Override
    public void remove(int position) {
        //移除
        super.remove(position);

        //判断是否存在add图标
        List<String> list = getData();
        boolean existAdd = false;
        for (String str : list) {
            if (str.equals(JDPickImageAdapter.ADD)) {
                existAdd = true;
            }
        }
        if (!existAdd && getData().size() < maxNum) {
            addData(JDPickImageAdapter.ADD);
        }

//        Iterator<String> it = getData().iterator();
//        boolean existAdd = false;
//        while (it.hasNext()) {
//            String str = it.next();
//            if (str.equals(JDPickImageAdapter.ADD)) {
//                existAdd = true;
//            }
//        }
//        if (!existAdd) {
//            addData(JDPickImageAdapter.ADD);
//        }
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if (item.equals(ADD)) {
            helper.getView(R.id.iv_del).setVisibility(View.GONE);
            helper.setImageResource(R.id.iv, R.mipmap.jd_pick_img_add);
        } else {
            helper.getView(R.id.iv_del).setVisibility(View.VISIBLE);
            GlideUtil.showImageWithSuffix(mContext, item, helper.getView(R.id.iv));
        }
        helper.addOnClickListener(R.id.iv_del);
        helper.addOnClickListener(R.id.iv);
    }

    public List<String> getImgList() {
        List<String> list = getData();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(ADD)) {
                list.remove(i);
            }
        }
        return list;
    }

}
