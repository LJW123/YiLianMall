package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.MTOrderListEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.SlantedTextView;

import java.util.ArrayList;

/**
 * 套餐评价成功界面
 */
public class MTCommentSuccessActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvLeft1;
    private RelativeLayout rlTvLeft1;
    private ImageView ivLeft1;
    private RelativeLayout rlIvLeft1;
    private ImageView ivLeft2;
    private RelativeLayout rlIvLeft2;
    private ImageView ivTitle;
    private TextView tvTitle;
    private ImageView ivRight1;
    private ImageView ivRight2;
    private RelativeLayout rlIvRight2;
    private TextView tvRight;
    private RelativeLayout rlTvRight;
    private View viewLine;
    private LinearLayout llJpTitle;
    private LinearLayout activityMtrefundSucceed;
    private ListView nslvNoComment;
    private TextView tvThanksComment;
    private int page;
    private BaseAdapter adapter;
    private ArrayList<MTOrderListEntity.DataBean> data = new ArrayList<>();
    private String orderId;
    private boolean comment_success;

    /**
     * 分享有关
     */
    private UmengDialog mShareDialog;
    JPNetRequest jpRequest;
    String share_type = "10"; //1.旗舰店分享:#
    String getedId = "0";
    String share_title,share_content,share_img,share_url,shareImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_success);
        initView();
        initData();
        //initListener();
    }

    private void initListener() {
        nslvNoComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MTCommentSuccessActivity.this, MTPackageCommentActivity.class);
                intent.putExtra(Constants.SPKEY_MT_PACKAGE_ORDER_ID, orderId);
                startActivity(intent);
                finish();
            }
        });
    }


    private void initData() {
        getData();
    }

    /**
     * 获取待评价列表
     */
    private void getData() {
        if (mtNetRequest == null) {
            mtNetRequest = new MTNetRequest(mContext);
        }
        mtNetRequest.getMTOrderList(3, page, new RequestCallBack<MTOrderListEntity>() {
            @Override
            public void onStart() {
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<MTOrderListEntity> responseInfo) {
                MTOrderListEntity result = responseInfo.result;
                switch (result.code) {
                    case 1:
                        data = result.data;
                        if (data.size() == 0 || data == null) {
                            nslvNoComment.setVisibility(View.GONE);
                        } else {
                            nslvNoComment.setVisibility(View.VISIBLE);
                            adapter = new MTRecommendCommentAdapter(data);
                            nslvNoComment.setAdapter(adapter);
                        }
                        break;
                    case -3:
                        showToast(R.string.system_busy);
                        break;
                    case -4:
                        showToast(R.string.login_failure);
                        sp.edit().putBoolean(Constants.SPKEY_STATE, false).commit();
                        startActivity(new Intent(MTCommentSuccessActivity.this, LeFenPhoneLoginActivity.class));
                        break;
                    default:
                        showToast("" + result.code);
                        break;
                }
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    private MTNetRequest mtNetRequest;

    private void initView() {
        tvThanksComment = (TextView) findViewById(R.id.tv_thanks_comment);
        tvLeft1 = (TextView) findViewById(R.id.tv_left1);
        rlTvLeft1 = (RelativeLayout) findViewById(R.id.rl_tv_left1);
        ivLeft1 = (ImageView) findViewById(R.id.iv_left1);
        ivLeft1.setImageResource(R.mipmap.v3back);
        ivLeft1.setOnClickListener(this);
        rlIvLeft1 = (RelativeLayout) findViewById(R.id.rl_iv_left1);
        ivLeft2 = (ImageView) findViewById(R.id.iv_left2);
        rlIvLeft2 = (RelativeLayout) findViewById(R.id.rl_iv_left2);
        ivTitle = (ImageView) findViewById(R.id.iv_title);
        ivTitle.setVisibility(View.GONE);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("评价成功");
        ivRight1 = (ImageView) findViewById(R.id.iv_right1);
        ivRight2 = (ImageView) findViewById(R.id.iv_right2);
        ivRight2.setVisibility(View.GONE);
        rlIvRight2 = (RelativeLayout) findViewById(R.id.rl_iv_right2);
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setTextColor(ContextCompat.getColor(mContext, R.color.color_333));
        tvRight.setText("完成");
        tvRight.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvRight.setOnClickListener(this);
        rlTvRight = (RelativeLayout) findViewById(R.id.rl_tv_right);
        viewLine = (View) findViewById(R.id.view_line);
        llJpTitle = (LinearLayout) findViewById(R.id.ll_jp_title);
        activityMtrefundSucceed = (LinearLayout) findViewById(R.id.activity_mtrefund_succeed);

        nslvNoComment = (ListView) findViewById(R.id.nslv_no_comment);

        TextView headerTextView = new TextView(mContext);
        headerTextView.setText("您还可以评价");
        headerTextView.setTextColor(getResources().getColor(R.color.color_999));
        headerTextView.setTextSize(15);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(60, 40, 60, 40);
        headerTextView.setLayoutParams(params);

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.addView(headerTextView);
        nslvNoComment.addHeaderView(linearLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
            case R.id.iv_left1:
                finish();
                break;

        }
    }

    class MTRecommendCommentAdapter extends BaseAdapter {

        private final ArrayList<MTOrderListEntity.DataBean> recommendCommentList;

        public MTRecommendCommentAdapter(ArrayList<MTOrderListEntity.DataBean> data) {
            this.recommendCommentList = data;
        }

        @Override
        public int getCount() {
            return recommendCommentList.size();
        }

        @Override
        public Object getItem(int position) {
            return recommendCommentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_comment_success_no_evaluate, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final MTOrderListEntity.DataBean dataBean = recommendCommentList.get(position);
            viewHolder.tvMtPackageName.setText(dataBean.packageName);
            viewHolder.tvMtPackageUsedTime.setText(DateUtils.formatDate(NumberFormat.convertToLong(dataBean.usableTime, 0L)) + "您消费过");
//            viewHolder.ivPackageIcon
            String iconUrl = dataBean.packageIcon;
            if(!TextUtils.isEmpty(iconUrl)&&!iconUrl.contains("http://")||!TextUtils.isEmpty(iconUrl)&&!iconUrl.contains("https://")){
                iconUrl = Constants.ImageUrl+iconUrl+Constants.ImageSuffix;
            }
            imageLoader.displayImage(iconUrl,viewHolder.ivPackageIcon,options);
            viewHolder.btnGoToEvaluate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //去评价
                    Intent intent = new Intent(MTCommentSuccessActivity.this, MTPackageCommentActivity.class);
                    intent.putExtra(Constants.SPKEY_MT_PACKAGE_ORDER_ID, dataBean.orderId);
                    startActivity(intent);
                    finish();
                }
            });
            viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //去评价
                    Intent intent = new Intent(MTCommentSuccessActivity.this, MTPackageCommentActivity.class);
                    intent.putExtra(Constants.SPKEY_MT_PACKAGE_ORDER_ID, dataBean.orderId);
                    startActivity(intent);
                    finish();
                }
            });

            if ("0".equals(dataBean.isEvaluate)) {
                viewHolder.tvTag.setVisibility(View.VISIBLE);
                if ("1".equals(dataBean.isDelivery)) {
                    viewHolder.tvTag.setText("外卖");
                } else {
                    viewHolder.tvTag.setText("套餐");
                }
            } else {
                viewHolder.tvTag.setVisibility(View.GONE);
            }
            return convertView;
        }

        public class ViewHolder {
            public View rootView;
            public ImageView ivPackageIcon;
            public TextView tvMtPackageName;
            public TextView tvMtPackageUsedTime;
            public TextView tvEvaluateGetAward;
            public Button btnGoToEvaluate;
            public SlantedTextView tvTag;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.ivPackageIcon = (ImageView) rootView.findViewById(R.id.iv_package_icon);
                this.tvMtPackageName = (TextView) rootView.findViewById(R.id.tv_mt_package_price);
                this.tvMtPackageUsedTime = (TextView) rootView.findViewById(R.id.tv_mt_package_used_time);
                this.tvEvaluateGetAward = (TextView) rootView.findViewById(R.id.tv_evaluate_get_award);
                this.btnGoToEvaluate = (Button) rootView.findViewById(R.id.btn_go_to_evaluate);
                this.tvTag = (SlantedTextView) rootView.findViewById(R.id.tv_tag);
            }

        }
    }
}
