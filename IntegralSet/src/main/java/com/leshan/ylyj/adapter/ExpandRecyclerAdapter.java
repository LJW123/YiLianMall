package com.leshan.ylyj.adapter;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.bean.ExpandBean;

import java.util.List;

/**
 * Created by hbh on 2017/4/20.
 * 适配器
 */

public class ExpandRecyclerAdapter extends RecyclerView.Adapter<ExpandBaseViewHolder> {

    private Context context;
    private List<ExpandBean> dataBeanList;
    private LayoutInflater mInflater;
    private OnScrollListener mOnScrollListener;

    public ExpandRecyclerAdapter(Context context, List<ExpandBean> dataBeanList) {
        this.context = context;
        this.dataBeanList = dataBeanList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ExpandBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case ExpandBean.PARENT_ITEM:
                view = mInflater.inflate(R.layout.expand_item_parent, parent, false);
                return new ParentViewHolder(context, view);
            case ExpandBean.CHILD_ITEM:
                view = mInflater.inflate(R.layout.expand_item_child, parent, false);
                return new ChildViewHolder(context, view);
            default:
                view = mInflater.inflate(R.layout.expand_item_parent, parent, false);
                return new ParentViewHolder(context, view);
        }
    }

    /**
     * 根据不同的类型绑定View
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ExpandBaseViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ExpandBean.PARENT_ITEM:
                ParentViewHolder parentViewHolder = (ParentViewHolder) holder;
                parentViewHolder.bindView(dataBeanList.get(position), position, itemClickListener);
                break;
            case ExpandBean.CHILD_ITEM:
                ChildViewHolder childViewHolder = (ChildViewHolder) holder;
                childViewHolder.bindView(dataBeanList.get(position), position);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataBeanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataBeanList.get(position).getType();
    }

    private ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void onExpandChildren(ExpandBean bean) {
            int position = getCurrentPosition(bean.getID());//确定当前点击的item位置
            ExpandBean children = getChildDataBean(bean);//获取要展示的子布局数据对象，注意区分onHideChildren方法中的getChildBean()。
            if (children == null) {
                return;
            }
            add(children, position + 1);//在当前的item下方插入
            if (position == dataBeanList.size() - 2 && mOnScrollListener != null) { //如果点击的item为最后一个
                mOnScrollListener.scrollTo(position + 1);//向下滚动，使子布局能够完全展示
            }
        }

        @Override
        public void onHideChildren(ExpandBean bean) {
            int position = getCurrentPosition(bean.getID());//确定当前点击的item位置
            ExpandBean children = bean.getChildBean();//获取子布局对象
            if (children == null) {
                return;
            }
            remove(position + 1);//删除
            if (mOnScrollListener != null) {
                mOnScrollListener.scrollTo(position);
            }
        }
    };

    /**
     * 在父布局下方插入一条数据
     *
     * @param bean
     * @param position
     */
    public void add(ExpandBean bean, int position) {
        dataBeanList.add(position, bean);
        notifyItemInserted(position);
    }

    /**
     * 移除子布局数据
     *
     * @param position
     */
    protected void remove(int position) {
        dataBeanList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 确定当前点击的item位置并返回
     *
     * @param uuid
     * @return
     */
    protected int getCurrentPosition(String uuid) {
        for (int i = 0; i < dataBeanList.size(); i++) {
            if (uuid.equalsIgnoreCase(dataBeanList.get(i).getID())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 封装子布局数据对象并返回
     * 注意，此处只是重新封装一个DataBean对象，为了标注Type为子布局数据，进而展开，展示数据
     * 要和onHideChildren方法里的getChildBean()区分开来
     *
     * @param bean
     * @return
     */
    private ExpandBean getChildDataBean(ExpandBean bean) {
        ExpandBean child = new ExpandBean();
        child.setType(1);
        child.setParentLeftTxt(bean.getParentLeftTxt());
        child.setParentRightTxt(bean.getParentRightTxt());
        child.setChildLeftTxt(bean.getChildLeftTxt());
        child.setChildRightTxt(bean.getChildRightTxt());
        return child;
    }

    /**
     * 滚动监听接口
     */
    public interface OnScrollListener {
        void scrollTo(int pos);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    class ParentViewHolder extends ExpandBaseViewHolder {

        private Context mContext;
        private View view;
        private RelativeLayout containerLayout;
        private TextView parentLeftView;
        private TextView parentRightView;
        private ImageView expand;


        public ParentViewHolder(Context context, View itemView) {
            super(itemView);
            this.mContext = context;
            this.view = itemView;
        }

        public void bindView(final ExpandBean dataBean, final int pos, final ItemClickListener listener) {

            containerLayout = (RelativeLayout) view.findViewById(R.id.container);
            parentLeftView = (TextView) view.findViewById(R.id.parent_left_text);
            parentRightView = (TextView) view.findViewById(R.id.parent_right_text);
            expand = (ImageView) view.findViewById(R.id.expend);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) expand
                    .getLayoutParams();
            expand.setLayoutParams(params);
            parentLeftView.setText(dataBean.getParentLeftTxt());
            parentRightView.setText(dataBean.getParentRightTxt());

//        if (dataBean.isExpand()) {
//            expand.setRotation(90);
//            parentDashedView.setVisibility(View.INVISIBLE);
//        } else {
//            expand.setRotation(0);
//            parentDashedView.setVisibility(View.VISIBLE);
//        }

            //父布局OnClick监听
            containerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        if (dataBean.isExpand()) {
                            listener.onHideChildren(dataBean);

                            dataBean.setExpand(false);
                            rotationExpandIcon(180, 0);
                        } else {
                            listener.onExpandChildren(dataBean);
                            dataBean.setExpand(true);
                            rotationExpandIcon(0, 180);
                        }
                    }
                }
            });
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        private void rotationExpandIcon(float from, float to) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);//属性动画
                valueAnimator.setDuration(500);
                valueAnimator.setInterpolator(new DecelerateInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        expand.setRotation((Float) valueAnimator.getAnimatedValue());
                    }
                });
                valueAnimator.start();
            }
        }
    }

    interface ItemClickListener {
        /**
         * 展开子Item
         *
         * @param bean
         */
        void onExpandChildren(ExpandBean bean);

        /**
         * 隐藏子Item
         *
         * @param bean
         */
        void onHideChildren(ExpandBean bean);
    }

    class ChildViewHolder extends ExpandBaseViewHolder {

        private Context mContext;
        private View view;
        private TextView childLeftText;


        public ChildViewHolder(Context context, View itemView) {
            super(itemView);
            this.mContext = context;
            this.view = itemView;
        }

        public void bindView(final ExpandBean dataBean, final int pos) {

            childLeftText = (TextView) view.findViewById(R.id.child_left_text);
            childLeftText.setText(dataBean.getChildLeftTxt());

        }
    }
}



