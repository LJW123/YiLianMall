package com.yilian.mall.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.ActivityDetailEntity;
import com.yilian.mall.entity.GoodsSkuPrice;
import com.yilian.mall.entity.GoodsSkuProperty;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.ui.ActTouchListActivity;
import com.yilian.mall.ui.ActivityDetailActivity;
import com.yilian.mall.ui.ImagePagerActivity;
import com.yilian.mall.ui.JPFlagshipActivity;
import com.yilian.mall.ui.MallMainActivity;
import com.yilian.mall.ui.WebViewActivity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.utils.Toast;
import com.yilian.mall.widgets.CustScrollView;
import com.yilian.mall.widgets.MyLoading;
import com.yilian.mall.widgets.RandomTextView;
import com.yilian.mall.widgets.ScreenNumView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.RandomUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.networkingmodule.entity.ActRuleEntity;
import com.yilian.networkingmodule.entity.GALLuckyPrizeEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yilian.mall.ui.fragment.BaseFragment.mContext;


/**
 * Created by Ray_L_Pain on 2016/11/1 0001.
 */

public class ActivityDetailTopFragment extends Fragment {

    private static final int ROTATE = 0;//viewpager轮播消息
    public CustScrollView customScrollView;
    public TextView tv_container_tag;
    public TextView tv_container_price;
    public TextView tv_container_marker;
    public TextView tv_container_juan;
    public LinearLayout ll_container_choose;
    public TextView tv_container_choose;
    public String goodsId, activityId, activityType, supplierId, tagsName;
    public ActivityDetailEntity.DataBean.GoodInfoBean goodInfo;
    public String urlOne;
    public DisplayImageOptions options;
    public boolean isCollectTop;
    public Map<String, GoodsSkuProperty> mSelectedProperty;
    public Map<String, GoodsSkuPrice> mSelectedPropertyPrice;
    public String collectType;
    protected int screenWidth;
    protected MyLoading myloading;
    AlbumAdapter albumAdapter;
    private View rootView;
    private ActivityDetailActivity activity;
    private Context context;
    private TextView tv_container_title;
    private RatingBar ratingBar;
    private View view_container_choose;
    private TextView tv_container_grade;
    private TextView tv_container_sale;
    private LinearLayout ll_container_marker;
    private TextView tv_container_question;
    private TextView tv_container_assure1;
    private TextView tv_container_assure2;
    private TextView tv_container_assure3;
    private TextView tv_container_assure4;
    private TextView tv_product_name;
    private TextView tv_product_from;
    private TextView tv_product_desc;
    private LinearLayout ll_product_know;
    private TextView tv_product_know;
    private LinearLayout ll_shop;
    private TextView tv_shop_name;
    //轮播图
    private ViewPager viewPager;
    private ScreenNumView llDot;
    private JPNetRequest jpNetRequest;
    private List<String> mAlbum;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private int width;
    private Intent intent;
    public String sku, skuCount;
    public List<String> selectSkuList;
    public String getedId;
    private ActivityDetailTopFragment.onUpLoadErrorListener onUpLoadErrorListener;
    //---------------------------------------------------
    private TextView tv_jifen;
    private TextView tv_go_yhs;
    public TextView tv_container_jifen;
    private ImageView iv_classify;
    private LinearLayout ll_jfg;
    public TextView tv_container_juan_jfg;
    public TextView tv_container_question_jfg;
    //判断是哪个专区的标识
    public String regional_type;
    //运费
    public String freight_change;
    //运费说明的弹窗
    public PopupWindow mFregihtChargeWindow;

    /**
     * 之前这个价格为服务器返回的最外层的数据，现在改为先循环遍历对应的sku，再取这个sku的价格，奖券等属性
     */
    public String skuCostPriceFirst = "";//初始化，对应该SKU的价格
    public String skuReturnIntegralFirst = "";//初始化，对应的销售奖券
    public String skuMarketPriceFirst = "";//初始化，对应该SKU的价格
    public String skuIntegralFirst = "";//初始化，对应的SKU奖券
    public String skuRetailPriceFirst = ""; //初始化，对应的市场价（type = 2 时 现金价格）

    /**
     * 猜价格
     */
    public LinearLayout guessLayout;
    private TextView tvGuessPriceTop;
    private TextView tvGuessPriceBot;
    private LinearLayout layoutGuessCount;
    private TextView tvGuessCount;
    public TextView tvGuessHistory;

    /**
     * 碰运气
     */
    public LinearLayout touchLayout;
    private TextView tvTouchPriceTopOnce;
    private TextView tvTouchPriceTopRightOnce;
    private RandomTextView tvTouchPriceTop;
    private RandomTextView tvTouchPriceTopRight;
    private TextView tvTouchPriceBot;
    private LinearLayout layoutTouchCount;
    private TextView tvTouchCount;
    public TextView tvTouchClick;
    private ImageView imageView7;

    public String actOrderId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_activitydetail_top, null);
        activity = (ActivityDetailActivity) getActivity();
        context = getContext();

        initView();
        getCommodityDetails();
        getActRule();
        return rootView;
    }

    /**
     * 获取猜价格-碰运气规则  规则弹窗用
     */
    public String ruleContent;
    public String ruleDesc;

    public void getActRule() {
        RetrofitUtils2.getInstance(mContext).galRule(activity.activityType, new Callback<ActRuleEntity>() {
            @Override
            public void onResponse(Call<ActRuleEntity> call, Response<ActRuleEntity> response) {
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(context, body)) {
                    if (CommonUtils.serivceReturnCode(context, body.code, body.msg)) {
                        ActRuleEntity entity = response.body();
                        switch (entity.code) {
                            case 1:
                                Logger.i("2017年12月14日 11:09:39-走了成功");
                                ruleContent = entity.info.rule;
                                ruleDesc = entity.info.considerations;
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ActRuleEntity> call, Throwable t) {
                Logger.i("2017年12月14日 11:09:39-走了失败");
                if ("1".equals(activityType)) {
                    ruleContent = "您可以用您账户的奖券参与碰运气的活动，每次活动使用1个奖券，当开启碰运气时，商品的零售价开始滚动，待价格滚动停止，商品销售价格与碰运气的价格完全一致时，恭喜您，碰运气成功。该商品已在待付款查收并用等额的奖券做评论担保，商家开始发货。您确认收货并做出图文评价后，平台审核无误，奖券将自动退还您账户。";
                } else {
                    ruleContent = "您可以用您账户的奖券参与猜价格的活动，每次活动使用1个奖券，当开启猜价格时，商品的零售价开始滚动，待价格滚动停止，商品销售价格与猜价格的价格完全一致时，恭喜您，猜价格成功。该商品已在待付款查收并用等额的奖券做评论担保，商家开始发货。您确认收货并做出图文评价后，平台审核无误，奖券将自动退还您账户。";
                }
                ruleDesc = "1. 请您收到货物后180天内，及时做出图文评价后，系统将自动返还奖券。" + "\n" + "2. 如超出180天未作出图文评价，系统将默认您放弃评价，系统将终止奖券返还。";
            }
        });
    }

    /**
     * 请求商品详情信息
     */
    public void getCommodityDetails() {
        goodsId = activity.goodsId;
        activityId = activity.activityId;
        regional_type = activity.type;
        activityType = activity.activityType;

        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(context);
        }

        jpNetRequest.getActivityGoodInfo(activityId, activityType, goodsId, new RequestCallBack<ActivityDetailEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<ActivityDetailEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        if (null != onUpLoadErrorListener) {
                            onUpLoadErrorListener.isError(false);
                        }
                        goodInfo = responseInfo.result.data.goodInfo;
                        Logger.i("2017年12月18日 14:13:56-走了这里" + goodInfo.toString());
                        initData();
                        initActivityData();
                        break;
                    default:
                        if (null != onUpLoadErrorListener) {
                            onUpLoadErrorListener.isError(true);
                        }
                        showToast("返回码：" + responseInfo.result.code);
                        Logger.i("返回码：" + responseInfo.result.code);
                        break;
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                if (null != onUpLoadErrorListener) {
                    onUpLoadErrorListener.isError(true);
                }
            }
        });
    }


    /**
     * 加载数据
     */
    public void initData() {
        getedId = goodInfo.goodsId;
        //tag
        tagsName = goodInfo.tagsMsg;

        if (!TextUtils.isEmpty(goodInfo.goodsIntegral)) {
            guessFinalPrice = MoneyUtil.getLeXiangBiNoZero(goodInfo.goodsIntegral);
        } else {
            guessFinalPrice = "0";
        }

        if (TextUtils.isEmpty(tagsName) || tagsName == null) {
            tv_container_tag.setVisibility(View.GONE);
            //标题
            tv_container_title.setText(goodInfo.goodsName);
        } else {
            tv_container_tag.setVisibility(View.VISIBLE);
            tv_container_tag.setText(tagsName);
            //标题
            int length = tagsName.length();
            String space = "         ";
            for (int i = 0; i < length; i++) {
                space = space + " ";
            }
            tv_container_title.setText(space + goodInfo.goodsName);
        }

        /**
         *初始化先循环对应的sku
         */
        for (GoodsSkuPrice skuPrice : goodInfo.goodsSkuPrice) {
            if (skuPrice.skuInfo.equals(goodInfo.goodsSku)) {
                skuCostPriceFirst = skuPrice.skuCostPrice;
                skuReturnIntegralFirst = skuPrice.returnIntegral;
                skuMarketPriceFirst = skuPrice.skuMarketPrice;
                skuIntegralFirst = skuPrice.skuIntegralPrice;
                skuRetailPriceFirst = skuPrice.skuRetailPrice;
                skuCount = skuPrice.skuInventory;
            }
        }

        //先判断是线上商城还是易划算专区 假设0是线上商城 1为易划算 2为奖券购
        if ("1".equals(regional_type)) {
            iv_classify.setImageResource(R.mipmap.icon_yhs);
            //只有商品详情请价格显示后边2位00
            tv_container_price.setText(String.format("%.2f", NumberFormat.convertToDouble(skuIntegralFirst, 0.0) / 100));
            tv_container_price.setTextColor(getResources().getColor(R.color.color_green));
            tv_jifen.setVisibility(View.VISIBLE);
            tv_container_juan.setVisibility(View.GONE);
            tv_container_question.setVisibility(View.VISIBLE);
            tv_container_question.setText(activity.getResources().getString(R.string.yhs) + "交易说明？");
            tv_container_question.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            tv_container_question.getPaint().setAntiAlias(true);
            tv_go_yhs.setText(R.string.yhs);
            tv_go_yhs.setTextColor(activity.getResources().getColor(R.color.color_green));
            ll_jfg.setVisibility(View.GONE);
        } else if ("2".equals(regional_type)) {
            iv_classify.setImageResource(R.mipmap.icon_jfg);
            int jifen = Integer.parseInt(skuMarketPriceFirst) - Integer.parseInt(skuRetailPriceFirst);
            tv_container_price.setText(MoneyUtil.set¥Money(String.format("%.2f", NumberFormat.convertToDouble(skuRetailPriceFirst, 0.0) / 100)));
            ll_container_marker.setVisibility(View.GONE);
            tv_container_jifen.setVisibility(View.VISIBLE);
            tv_container_jifen.setText(" + " + String.format("%.2f", NumberFormat.convertToDouble(jifen, 0.0) / 100));
            tv_jifen.setVisibility(View.VISIBLE);
            ll_jfg.setVisibility(View.VISIBLE);
            tv_container_juan.setVisibility(View.GONE);
            tv_container_juan_jfg.setText("原价：¥" + MoneyUtil.getLeXiangBi(skuMarketPriceFirst));
            tv_container_juan_jfg.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            tv_container_juan.setVisibility(View.GONE);
            tv_container_question_jfg.setText(activity.getResources().getString(R.string.jfg) + "交易说明？");
            tv_container_question_jfg.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            tv_container_question_jfg.getPaint().setAntiAlias(true);
            tv_go_yhs.setText(activity.getResources().getString(R.string.jfg));
            tv_go_yhs.setTextColor(activity.getResources().getColor(R.color.color_red));
        } else {
            iv_classify.setVisibility(View.GONE);
            tv_container_price.setText(MoneyUtil.set¥Money(String.format("%.2f", NumberFormat.convertToDouble(skuCostPriceFirst, 0.0) / 100)));
            ll_container_marker.setVisibility(View.VISIBLE);
            tv_jifen.setVisibility(View.GONE);
            tv_container_marker.setText("¥" + MoneyUtil.getLeXiangBi(skuMarketPriceFirst));
            tv_container_marker.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            if (TextUtils.isEmpty(skuMarketPriceFirst) && TextUtils.isEmpty(skuCostPriceFirst)) {
                Logger.e("商品详情价格异常 goodsPrice " + skuMarketPriceFirst + " goodInfo.goodsCost " + skuCostPriceFirst);
                return;
            }
            tv_container_juan.setText(activity.getResources().getString(R.string.back_score) + MoneyUtil.getLeXiangBiNoZero(skuReturnIntegralFirst));
            tv_container_juan.setVisibility(View.VISIBLE);
            tv_container_question.setVisibility(View.VISIBLE);
            tv_container_question.setText(activity.getResources().getString(R.string.xiaofeijifen) + "说明？");
            tv_container_question.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            tv_container_question.getPaint().setAntiAlias(true);
            tv_go_yhs.setText("");
            ll_jfg.setVisibility(View.GONE);
        }

        tv_container_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WebViewActivity.class);
                if ("0".equals(regional_type)) {
                    intent.putExtra(Constants.SPKEY_URL, Constants.INTEGRAL_AGREEMENT);
                } else if ("1".equals(regional_type)) {
                    intent.putExtra(Constants.SPKEY_URL, Constants.YHS_AGREEMENT);
                }
                startActivity(intent);
            }
        });

        tv_container_question_jfg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WebViewActivity.class);
                intent.putExtra(Constants.SPKEY_URL, Constants.JFG_AGREEMENT);
                startActivity(intent);
            }
        });

        collectType = "0";//商品传“0”
        //评分
        float grade = (float) (Math.round((goodInfo.goodsGrade / 10.0f) * 10)) / 10;
        ratingBar.setRating(grade == 0 ? (float) 5.0 : grade);
        tv_container_grade.setText(grade == 0 ? "5.0分" : grade + "分");
        //销量
        tv_container_sale.setText("累计销量：" + goodInfo.goodsSale);
        //四个标签
        List<String> listTag = goodInfo.descTags;
        if (listTag.size() == 4) {
            tv_container_assure1.setVisibility(View.VISIBLE);
            tv_container_assure2.setVisibility(View.VISIBLE);
            tv_container_assure3.setVisibility(View.VISIBLE);
            tv_container_assure4.setVisibility(View.VISIBLE);
            tv_container_assure1.setText(listTag.get(0));
            tv_container_assure2.setText(listTag.get(1));
            tv_container_assure3.setText(listTag.get(2));
            tv_container_assure4.setText(listTag.get(3));
        } else {
            tv_container_assure1.setVisibility(View.GONE);
            tv_container_assure2.setVisibility(View.GONE);
            tv_container_assure3.setVisibility(View.GONE);
            tv_container_assure4.setVisibility(View.GONE);
        }
        //商品规格
        sku = goodInfo.goodsSku;
        String[] arraySku = sku.split(",");
        selectSkuList = new ArrayList<>(Arrays.asList(arraySku));

        ll_container_choose.setVisibility(View.VISIBLE);
        tv_container_choose.setVisibility(View.VISIBLE);
        if ("0:0".equals(sku) || TextUtils.isEmpty(sku)) {
            /**
             * Ray_L_Pain 2017-2-9 需求变为有规格的的产品也显示选择的数量
             */
            tv_container_choose.setText(noskuPropertyTxt(1));
        } else {
            tv_container_choose.setText(staticPropertyTxt(selectSkuList));
        }
        ll_container_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.mOperate = ActivityDetailActivity.OPERATE_BUY;
                activity.showPropertyWindow("normal");
            }
        });
        //商品信息
        ActivityDetailEntity.DataBean.GoodInfoBean.SupplierInfoBean suppBean = goodInfo.supplierInfo;
        //品牌
        tv_product_name.setText(goodInfo.goodsBrand);
        tv_product_from.setText(suppBean.supplierAddress);
        freight_change = suppBean.goodsFreightDesc;
        if (TextUtils.isEmpty(freight_change)) {
            tv_product_desc.setVisibility(View.INVISIBLE);
        } else if (freight_change.contains("\n")) {
            try {
                String freightStr = freight_change.split("\n")[0];
                tv_product_desc.setText(freightStr);
            } catch (Exception e) {
                tv_product_desc.setVisibility(View.INVISIBLE);
            }
        } else {
            tv_product_desc.setText(freight_change);
        }
        if (TextUtils.isEmpty(freight_change)) {
            tv_product_desc.setClickable(false);
        } else {
            tv_product_desc.setClickable(true);
            tv_product_desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFreightChargeWindow();
                }
            });
        }

        if (goodInfo.goodsType.equals("5")) {//零购券区商品,
            tv_shop_name.setText(R.string.linggouqu);
        } else {
            tv_shop_name.setText("店铺：" + suppBean.supplierName);
        }
        ll_product_know.setVisibility(View.GONE);
        ll_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(regional_type)) {
                    intent = new Intent(activity, JPFlagshipActivity.class);
                    intent.putExtra("index_id", goodInfo.goodsSupplier);
                } else {
                    intent = new Intent(activity, MallMainActivity.class);
                    intent.putExtra("title", ("1".equals(regional_type)) ? "易划算" : "奖券购");
                }
                startActivity(intent);
            }
        });
        //网址
        urlOne = Ip.getWechatURL(mContext) + "m/goods/contentPic.php?goods_id=" + goodInfo.goodsId + "&filiale_id=" + "0" + "&goods_supplier=" + goodInfo.goodsSupplier;
        Logger.i("url==" + urlOne);
        activity.urlOne = urlOne;
        //收藏
        if (goodInfo.yetCollect == 0) {
            isCollectTop = false;
        } else if (goodInfo.yetCollect == 1) {
            isCollectTop = true;
        }
        if (isCollectTop) {
            Drawable topDrawable = activity.getResources().getDrawable(R.mipmap.star_solid);
            topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
            activity.tvCollect.setCompoundDrawables(null, topDrawable, null, null);
            activity.tvCollect.setText(R.string.collected);
        } else {
            Drawable topDrawable = activity.getResources().getDrawable(R.mipmap.star_empty);
            topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
            activity.tvCollect.setCompoundDrawables(null, topDrawable, null, null);
            activity.tvCollect.setText(R.string.click_collect);
        }
        //轮播图
        mAlbum = goodInfo.goodsAlbum;
        albumAdapter = new AlbumAdapter();
        viewPager.setAdapter(albumAdapter);
        llDot.initScreen(albumAdapter.getCount());

        layoutGuessCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showActivityExplainWindow("guess");
            }
        });
        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showActivityExplainWindow("touch");
            }
        });
        layoutTouchCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActTouchListActivity.class);
                intent.putExtra("goods_id", activity.goodsId);
                intent.putExtra("act_type", "1");
                startActivity(intent);
            }
        });
        tvTouchClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchClick(v, true);
            }
        });
    }

    /**
     * 点击碰运气按钮后的操作
     */
    public void touchClick(View v, boolean flag) {
        activity.startMyDialog(false);

        RetrofitUtils2.getInstance(context).galLuckyPrize(activityId, new Callback<GALLuckyPrizeEntity>() {
            @Override
            public void onResponse(Call<GALLuckyPrizeEntity> call, Response<GALLuckyPrizeEntity> response) {
                activity.stopMyDialog();
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(context, body)) {
                    if (CommonUtils.serivceReturnCode(context, body.code, body.msg)) {
                        GALLuckyPrizeEntity entity = response.body();
                        switch (entity.code) {
                            case 1:
                                tvTouchPriceTopOnce.setVisibility(View.GONE);
                                tvTouchPriceTopRightOnce.setVisibility(View.GONE);
                                tvTouchPriceTop.setVisibility(View.VISIBLE);
                                tvTouchPriceTopRight.setVisibility(View.VISIBLE);

                                if (flag) {
                                    activity.goodView.setText("-1");
                                    activity.goodView.setTextColor(getResources().getColor(R.color.color_red));
                                    activity.goodView.setDistance(300);
                                    activity.goodView.show(v);
                                }

                                switch (entity.staus) {
                                    /**
                                     * 没中奖
                                     */
                                    case "0":
                                        tvTouchClick.setText("再来一次");

                                        tvTouchPriceTop.setText(String.valueOf(RandomUtil.getNum(0, Integer.parseInt(goodInfo.goodsIntegral) / 100)));
                                        tvTouchPriceTop.setPianyilian(RandomTextView.FIRSTF_FIRST);
                                        tvTouchPriceTop.start();
                                        tvTouchPriceTopRight.setText(String.valueOf(RandomUtil.getNum(0, 99)));
                                        tvTouchPriceTopRight.setPianyilian(RandomTextView.FIRSTF_FIRST);
                                        tvTouchPriceTopRight.start();
                                        break;
                                    /**
                                     * 中奖了
                                     */
                                    case "1":
                                        if (guessFinalPrice.contains(".")) {
                                            String[] arr = guessFinalPrice.split("\\.");
                                            tvTouchPriceTop.setText(arr[0]);
                                            if (arr[1].length() == 1) {
                                                tvTouchPriceTopRight.setText(arr[1] + "0");
                                            } else {
                                                tvTouchPriceTopRight.setText(arr[1]);
                                            }
                                        } else {
                                            tvTouchPriceTop.setText(guessFinalPrice);
                                            tvTouchPriceTopRight.setText("00");
                                        }

                                        tvTouchPriceTop.setPianyilian(RandomTextView.FIRSTF_FIRST);
                                        tvTouchPriceTop.start();
                                        tvTouchPriceTopRight.setPianyilian(RandomTextView.FIRSTF_FIRST);
                                        tvTouchPriceTopRight.start();

                                        ThreadUtil.getThreadPollProxy().execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Thread.sleep(3000);
                                                    activity.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            actOrderId = entity.orderId;
                                                            activity.showTouchResultWindow();
                                                        }
                                                    });
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GALLuckyPrizeEntity> call, Throwable t) {
                activity.stopMyDialog();
                showToast("网络繁忙");
            }
        });
    }

    public String guessFinalPrice;

    /**
     * 加载活动有关的数据
     */
    public void initActivityData() {
        /**
         * 碰运气
         */
        Logger.i("2017年12月18日 14:45:13-" + goodInfo.guessNum);
        if (TextUtils.isEmpty(goodInfo.guessNum) || "0".equals(goodInfo.guessNum)) {
            Logger.i("2017年12月18日 14:45:13-1111");
            if (guessFinalPrice.contains(".")) {
                String[] arr = guessFinalPrice.split("\\.");
                tvTouchPriceTopOnce.setText(arr[0]);
                if (arr[1].length() == 1) {
                    tvTouchPriceTopRightOnce.setText(arr[1] + "0");
                } else {
                    tvTouchPriceTopRightOnce.setText(arr[1]);
                }
            } else {
                tvTouchPriceTopOnce.setText(guessFinalPrice);
                tvTouchPriceTopRightOnce.setText("00");
            }
        } else {
            String[] arr = String.valueOf(Float.parseFloat(goodInfo.guessNum) / 100).split("\\.");
            Logger.i("2017年12月18日 14:45:13-2222");
            Logger.i("2017年12月18日 14:45:13-" + String.valueOf(Double.parseDouble(goodInfo.guessNum) / 100));
            tvTouchPriceTopOnce.setText(arr[0]);
            if (arr[1].length() == 1) {
                tvTouchPriceTopRightOnce.setText(arr[1] + "0");
            } else {
                tvTouchPriceTopRightOnce.setText(arr[1]);
            }
        }
        if (TextUtils.isEmpty(goodInfo.goodsIntegral)) {
            tvTouchPriceBot.setText("¥0");
        } else {
            tvTouchPriceBot.setText("¥" + MoneyUtil.getLeXiangBiNoZero(goodInfo.goodsIntegral));
        }
        tvTouchCount.setText(goodInfo.prizeNum + "人");

        /**
         * 猜价格
         */
        if (TextUtils.isEmpty(goodInfo.goodsIntegral)) {
            tvGuessPriceTop.setText("参考价：0-0");
        } else {
            tvGuessPriceTop.setText("参考价：0-" + MoneyUtil.getLeXiangBiNoZero(Float.parseFloat(goodInfo.goodsIntegral) * goodInfo.guess_price_gain_percent / 100));
        }
        tvGuessCount.setText(goodInfo.prizeNum + "人");
    }

    /**
     * 运费说明popwindow
     */
    public void showFreightChargeWindow() {
        if (mFregihtChargeWindow == null) {
            View contentView = activity.getLayoutInflater().inflate(R.layout.good_freight_charge_layout, null);
            mFregihtChargeWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mFregihtChargeWindow.setContentView(contentView);
            mFregihtChargeWindow.setAnimationStyle(R.style.AnimationSEX);
            mFregihtChargeWindow.setOutsideTouchable(true);
            View view_placeholder = contentView.findViewById(R.id.view_placeholder);
            view_placeholder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFregihtChargeWindow.dismiss();
                    activity.mShadowView.setVisibility(View.GONE);
                    activity.mShadowView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.library_module_anim_alpha_out));
                    return;
                }
            });
            TextView tv_content = (TextView) contentView.findViewById(R.id.tv_content);
            tv_content.setText(freight_change);
            TextView tv_ok = (TextView) contentView.findViewById(R.id.tv_ok);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFregihtChargeWindow.dismiss();
                    activity.mShadowView.setVisibility(View.GONE);
                    activity.mShadowView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.library_module_anim_alpha_out));
                }
            });
        }

        if (!mFregihtChargeWindow.isShowing()) {
            mFregihtChargeWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            activity.mShadowView.setVisibility(View.VISIBLE);
            activity.mShadowView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.library_module_anim_alpha_in));
        }
    }

    public void initView() {
        Logger.i("ray-" + "走了top的initView");
        if (activity.layoutNormal.getVisibility() == View.GONE) {
            activity.layoutNormal.setVisibility(View.VISIBLE);
        }
        if (activity.layoutQuestion.getVisibility() == View.VISIBLE) {
            activity.layoutQuestion.setVisibility(View.GONE);
        }
        screenWidth = ScreenUtils.getScreenWidth(context);
        customScrollView = (CustScrollView) rootView.findViewById(R.id.customScrollView);
        customScrollView.scrollTo(0, 1000);
        tv_container_tag = (TextView) rootView.findViewById(R.id.tv_container_tag);
        tv_container_title = (TextView) rootView.findViewById(R.id.tv_container_title);
        tv_container_price = (TextView) rootView.findViewById(R.id.tv_container_price);
        ll_container_marker = (LinearLayout) rootView.findViewById(R.id.ll_container_marker);
        tv_container_marker = (TextView) rootView.findViewById(R.id.tv_container_marker);
        tv_container_juan = (TextView) rootView.findViewById(R.id.tv_container_juan);
        tv_container_question = (TextView) rootView.findViewById(R.id.tv_container_question);
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        tv_container_grade = (TextView) rootView.findViewById(R.id.tv_container_grade);
        tv_container_sale = (TextView) rootView.findViewById(R.id.tv_container_sale);
        tv_container_assure1 = (TextView) rootView.findViewById(R.id.tv_container_assure1);
        tv_container_assure2 = (TextView) rootView.findViewById(R.id.tv_container_assure2);
        tv_container_assure3 = (TextView) rootView.findViewById(R.id.tv_container_assure3);
        tv_container_assure4 = (TextView) rootView.findViewById(R.id.tv_container_assure4);
        view_container_choose = rootView.findViewById(R.id.view_container_choose);
        ll_container_choose = (LinearLayout) rootView.findViewById(R.id.ll_container_choose);
        tv_container_choose = (TextView) rootView.findViewById(R.id.tv_container_choose);
        tv_product_name = (TextView) rootView.findViewById(R.id.tv_product_name);
        tv_product_from = (TextView) rootView.findViewById(R.id.tv_product_from);
        tv_product_desc = (TextView) rootView.findViewById(R.id.tv_product_desc);
        ll_product_know = (LinearLayout) rootView.findViewById(R.id.ll_product_know);
        tv_product_know = (TextView) rootView.findViewById(R.id.tv_product_know);
        ll_shop = (LinearLayout) rootView.findViewById(R.id.ll_shop);
        tv_shop_name = (TextView) rootView.findViewById(R.id.tv_shop_name);
        //轮播图
        viewPager = (ViewPager) rootView.findViewById(R.id.vp_comm_img);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(new ViewPager.LayoutParams());
        params.height = (int) (screenWidth / 1);
        viewPager.setLayoutParams(params);
        llDot = (ScreenNumView) rootView.findViewById(R.id.vp_indicator);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                llDot.snapToPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        width = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();// 获取屏幕宽度
        options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.login_module_default_jp)
                .showImageForEmptyUri(R.mipmap.login_module_default_jp).showImageOnFail(R.mipmap.login_module_default_jp)
                // 这里的三张状态用一张替代
                .cacheInMemory(true).imageScaleType(ImageScaleType.NONE).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        mSelectedProperty = new HashMap<>();
        mSelectedPropertyPrice = new HashMap<>();
        tv_jifen = (TextView) rootView.findViewById(R.id.tv_jifen);
        tv_go_yhs = (TextView) rootView.findViewById(R.id.tv_go_yhs);
        tv_container_jifen = (TextView) rootView.findViewById(R.id.tv_container_jifen);
        iv_classify = (ImageView) rootView.findViewById(R.id.iv_classify);
        ll_jfg = (LinearLayout) rootView.findViewById(R.id.ll_jfg);
        tv_container_juan_jfg = (TextView) rootView.findViewById(R.id.tv_container_juan_jfg);
        tv_container_question_jfg = (TextView) rootView.findViewById(R.id.tv_container_question_jfg);

        /**
         * 猜价格 碰运气
         */
        guessLayout = (LinearLayout) rootView.findViewById(R.id.layout_guess);
        tvGuessPriceTop = (TextView) rootView.findViewById(R.id.tv_guess_price_top);
        tvGuessPriceBot = (TextView) rootView.findViewById(R.id.tv_guess_price_bot);
        layoutGuessCount = (LinearLayout) rootView.findViewById(R.id.layout_guess_count);
        tvGuessCount = (TextView) rootView.findViewById(R.id.tv_guess_count);
        tvGuessHistory = (TextView) rootView.findViewById(R.id.tv_guess_history);

        touchLayout = (LinearLayout) rootView.findViewById(R.id.layout_touch);

        tvTouchPriceTopOnce = (TextView) rootView.findViewById(R.id.tv_touch_price_top_once);
        tvTouchPriceTopRightOnce = (TextView) rootView.findViewById(R.id.tv_touch_price_top_right_once);

        tvTouchPriceTop = (RandomTextView) rootView.findViewById(R.id.tv_touch_price_top);
        tvTouchPriceTopRight = (RandomTextView) rootView.findViewById(R.id.tv_touch_price_top_right);
        tvTouchPriceBot = (TextView) rootView.findViewById(R.id.tv_touch_price_bot);
        layoutTouchCount = (LinearLayout) rootView.findViewById(R.id.layout_touch_count);
        tvTouchCount = (TextView) rootView.findViewById(R.id.tv_touch_count);
        tvTouchClick = (TextView) rootView.findViewById(R.id.tv_touch_click);
        imageView7 = (ImageView) rootView.findViewById(R.id.imageView7);

        switch (activity.activityType) {
            case "1":
                touchLayout.setVisibility(View.VISIBLE);
                guessLayout.setVisibility(View.GONE);
                break;
            case "2":
                touchLayout.setVisibility(View.GONE);
                guessLayout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

        tvGuessHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActTouchListActivity.class);
                intent.putExtra("goods_id", activity.goodsId);
                intent.putExtra("act_type", "2");
                startActivity(intent);
            }
        });
    }

    public String noskuPropertyTxt(int count) {
        return "已选择 " + count + " 件";
    }

    public String staticPropertyTxt(List<String> selectSkuList) {
        String selectedValues = "";
        String selectedId = "";

        if (selectSkuList != null && selectSkuList.size() != 0 && selectSkuList != null) {
            for (int i = 0; i < selectSkuList.size(); i++) {
                String value = selectSkuList.get(i);

                for (GoodsSkuProperty property : goodInfo.goodsSkuValues) {
                    if (null != property.skuParentId && null != property.skuId) {
                        String item = property.skuParentId + ":" + property.skuId;
                        if (item.equals(value)) {
                            selectedValues += property.skuName + ", ";
                            selectedId += property.skuParentId + ":" + property.skuId + "，";
                        }
                    }
                }
            }
            Logger.i("goodsSkuValues:" + goodInfo.goodsSkuValues);
            Logger.i("selectedValues:" + selectedValues);
            Logger.i("selectedId:" + selectedId);
            return "已选择 " + selectedValues + "1件";
        }
        return null;
    }


    /**
     * 获取商品属性选择结果字串
     */
    public String getPropertyTxt(int count) {
        String selectedValues = "";

        if (goodInfo != null) {
            int selectedPropertyCount = mSelectedProperty.size(); //我的选择
            Logger.i("2016-12-15mSelectedProperty:" + mSelectedProperty);
            int propertyCount = goodInfo.goodsSkuProperty.size(); //商品选择--2
            if (propertyCount > 0) {
                if (selectedPropertyCount == propertyCount) {
                    for (GoodsSkuProperty property : goodInfo.goodsSkuProperty) {
                        selectedValues += mSelectedProperty.get(property.skuId).skuName + " ";
                    }
                    return "已选择 " + selectedValues + count + "件";
                } else if (selectedPropertyCount < propertyCount) {
                    for (GoodsSkuProperty property : goodInfo.goodsSkuProperty) {
                        if (null == mSelectedProperty.get(property.skuId)) {
                            selectedValues += property.skuName + " ";
                        }
                    }
                    return "请选择 " + selectedValues + count + "件";
                }
            }
        }
        return null;
    }

    /**
     * dialog 启动
     */
    public void startMyDialog() {
        Logger.i("getActivity  " + getActivity());
        if (isAdded() && getActivity() != null) {
            if (myloading == null) {
                myloading = MyLoading.createLoadingDialog(mContext);
                Logger.i(this.getClass().getName() + "  创建了了dialog  " + this.toString());
            }
            try {//捕获异常，处理 is your activity running的异常
                myloading.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(20000);
                            if (myloading != null && myloading.isShowing()) {
                                ((Activity) getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        stopMyDialog();
                                    }
                                });
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Logger.i(this.getClass().getName() + "  弹出了dialog  " + this.toString());
            } catch (Exception e) {
                Logger.i("异常信息：" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myloading != null) {
            if (myloading.isShowing()) {
                myloading.dismiss();
                myloading = null;
            }
        }
    }

    /**
     * dialog 销毁
     */
    public void stopMyDialog() {

        if (myloading != null) {
            if (null != getActivity()) {
                if (!getActivity().isFinishing()) {
                    myloading.dismiss();
                    Logger.i(this.getClass().getName() + "  取消了dialog  " + this.toString());
                }
            }
            myloading = null;
        }
    }

    public void showToast(String msg) {
        Logger.i("返回码：" + msg);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }

    class AlbumAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (mAlbum != null) {
                return mAlbum.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            // TODO Auto-generated method stub


            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            final ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageLoader.displayImage(Constants.ImageUrl + mAlbum.get(position) + Constants.ImageSuffix,
//                    imageView, options, new SimpleImageLoadingListener() {
//
//                        @Override
//                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                            // TODO Auto-generated method stub
//                            // imageView.setScaleType(ScaleType.CENTER);
//                        }
//                    });
            GlideUtil.showImageNoSuffix(context, mAlbum.get(position), imageView);
            container.addView(imageView, lp);

            int size = mAlbum.size();
            final String[] files = (String[]) mAlbum.toArray(new String[size]);
//            前缀 后缀取消 防止乐分商品无法显示
//            for (int i = 0; i < files.length; i++) {
//                files[i] = Constants.ImageUrl + files[i] + Constants.ImageSuffix;
//            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageBrower(position, files);
                }
            });
            return imageView;
        }
    }

    public interface onUpLoadErrorListener {
        void isError(boolean isError);
    }

    public void setOnUpLoadErorrListener(onUpLoadErrorListener onUpLoadErorrListener) {
        this.onUpLoadErrorListener = onUpLoadErorrListener;
    }

}
