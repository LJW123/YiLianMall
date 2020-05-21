package com.yilian.mall.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GoodsType;
import com.yilian.mall.R;
import com.yilian.mall.adapter.BaseAdapter;
import com.yilian.mall.adapter.ImageDeleteAdapter;
import com.yilian.mall.entity.CreateServiceOrderEntity;
import com.yilian.mall.entity.CreateServiceOrderParameter;
import com.yilian.mall.entity.UploadImageEnity;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.retrofitutil.RetrofitUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mall.widgets.CircleImageView1;
import com.yilian.mall.widgets.NumberAddSubView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.FileUtils;
import com.yilian.mylibrary.GlideUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yilian.mylibrary.GoodsType.TYPE_TEST_GOODS;

/**
 * 提交售后服务申请界面
 */
public class ApplyAfterSaleActivity extends BaseActivity {
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int PHOTO_ALBUM = 2;
    public static final int APPLY_CAMERA_PERMISSION_REQUEST_CODE = 999;//请求相机权限使用请求码
    public static final int ALBUM_CAMERA_REQUEST_CODE = 1;//打开相机请求码
    public static final int APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE = 100;//读取权限的判断
    public static final int APPLY_WRITE_EXTERNAL_STORAGE_CODE = 888;
    static File img;
    private static String ImageName;
    @ViewInject(R.id.tv_goods_price2)
    /**
     * 奖券退款总价
     */

    public TextView tvGoodsIntegral2;
    @ViewInject(R.id.ll_back_money)
    LinearLayout llBackMoney;
    @ViewInject(R.id.ll_back_di_gou_quan)
    LinearLayout llBackDiGouQuan;
    @ViewInject(R.id.ll_back_money_type)
    LinearLayout llBackMoneyType;
    @ViewInject(R.id.ll_apply_count)
    LinearLayout llApplyCount;
    @ViewInject(R.id.rv_view)
    RecyclerView rvImg;
    BitmapUtils bu;
    MallNetRequest netRequest;
    CreateServiceOrderParameter parameters = new CreateServiceOrderParameter();
    ArrayList<String> imgList = new ArrayList<>();
    ArrayList<String> imgList1 = new ArrayList<>();
    String type = "退款";
    @ViewInject(R.id.tv_back_message)
    TextView tvBackMessage;
    @ViewInject(R.id.num_control)
    private NumberAddSubView numControl;
    //    @ViewInject(R.id.tv_icon_fen0)
//    private TextView tvIconFen0;
//    @ViewInject(R.id.tv_goods_coupon0)
//    private TextView tvGoodsCoupon0;
    @ViewInject(R.id.tv_back)
    private TextView tvBack;
    @ViewInject(R.id.iv_goods)
    private CircleImageView1 ivGoods;
    @ViewInject(R.id.iv_type)
    private ImageView ivType;
    @ViewInject(R.id.tv_goods_name)
    private TextView tvGoodsName;
    @ViewInject(R.id.tv_goods_norms)
    private TextView tvGoodsNorms;
    @ViewInject(R.id.tv_goods_price)
    private TextView tvGoodsPrice;
    /**
     * 商品信息奖券
     */
//    @ViewInject(R.id.tv_goods_integral)
//    private TextView tvGoodsIntegral;
    @ViewInject(R.id.tv_goods_count)
    private TextView tvGoodsCount;
    @ViewInject(R.id.rg_after_sale_type)
    private RadioGroup mRgAfterSaleType;
    @ViewInject(R.id.tv_back_money)
    private TextView tvBackMoney;
    @ViewInject(R.id.tv_score)
    private TextView tvIconFen;
    @ViewInject(R.id.tv_icon_integral)
    private TextView tvIconQuan;
    @ViewInject(R.id.tv_back_coupon)
    private TextView tvBackCoupon;
    @ViewInject(R.id.ed_question_describe)
    private EditText mEdQuestionDescribe; // 售后问题描述
    @ViewInject(R.id.btn_apply_after_sale)
    private Button btnApplyAfterSale;
    @ViewInject(R.id.iv_yhs_icon)
    private ImageView ivYhsIcon; //易划算icon
    @ViewInject(R.id.tv_back_integral)
    private TextView tvBackIntegral;//退款奖券
    @ViewInject(R.id.tv_goods_beans)
    private TextView tvGoodsBeans; // 送益豆
    @ViewInject(R.id.tv_subsidy)
    private TextView tvSubsidy; // 平台加赠益豆

    @ViewInject(R.id.tv_back_di_gou_quan)
    private TextView tvDiGouQuan;
    private PopupWindow popupWindow;
    private ImageDeleteAdapter adapter;
    private int maxCount = 1;
    private int price; //原价
    private String norms;
    private String goodsCost; //现价
    private boolean againAfterSale;
    private int after_sale_type;
    private String goodsReturnIntegral;
    private String goodsIntegral;
    private String payStyle;
    private String goodsRetail;
    private double integral;
    private String orderType;

    private double givenBean;
    private double subsidy;
    private String daiGouQuanMoney;
    private String eachDiGouQuanMoney;
    /**
     * 该商品所在订单的商品总价 不带代购券
     */
    private String orderGoodsTotalPrice;
    /**
     * 单件商品的支付价格
     */
    private String perGoodsPayMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_after_sale);
        ViewUtils.inject(this);
        netRequest = new MallNetRequest(mContext);

        bu = new BitmapUtils(mContext);
        initViews();

        setListener();
    }

    private void initViews() {

        Intent intent = getIntent();

        orderType = intent.getStringExtra("order_type");
        if (orderType == null) {
            tvBackMessage.setText("退款至账户奖励");
            tvBackMessage.setTextColor(getResources().getColor(R.color.color_666));
        } else {
            switch (orderType) {
                case "5":
                case "6":
                    tvBackMessage.setText("本活动商品为1奖券中奖，申请退货将为您退回1奖券，请谨慎选择!");
                    tvBackMessage.setTextColor(getResources().getColor(R.color.color_red));
                    break;
                default:
                    tvBackMessage.setText("退款至账户奖励");
                    tvBackMessage.setTextColor(getResources().getColor(R.color.color_666));
                    break;
            }

        }

        goodsIntegral = intent.getStringExtra("goodsIntegral");
        payStyle = intent.getStringExtra("payStyle");
        goodsRetail = intent.getStringExtra("goodsRetail");

        Logger.i("payStyle接受  " + payStyle + "  goodsRetail  " + goodsRetail);
        goodsReturnIntegral = intent.getStringExtra("goods_return_integral");
        //服务器返回的数据，支付那种类型的退换货的售后
        after_sale_type = intent.getIntExtra("after_sale_type", 0);
        againAfterSale = intent.getBooleanExtra("AgainAfterSale", false);
        maxCount = intent.getIntExtra("goods_count", 1);
        price = Integer.valueOf(intent.getStringExtra("goods_price"));
        norms = intent.getStringExtra("order_goods_norms");
//        单件商品原价格价钱
        goodsCost = intent.getStringExtra("goods_cost");

//        订单代购券总额
        daiGouQuanMoney = intent.getStringExtra("dai_gou_quan");
//订单总价
        orderGoodsTotalPrice = intent.getStringExtra("order_goods_total_price");
//        if (daiGouQuanMoney != null && orderGoodsTotalPrice != null) {
////        订单商品总价=商品总价+代购券
////        单件商品消耗代购券总额= 单件商品价格 *订单代购券总额/订单总价
//            BigDecimal multiply = new BigDecimal(goodsCost).multiply(new BigDecimal(daiGouQuanMoney));
//            BigDecimal divisor = new BigDecimal(orderGoodsTotalPrice).add(new BigDecimal(daiGouQuanMoney));
//            eachDiGouQuanMoney = String.valueOf(multiply.divide(divisor, 2, BigDecimal.ROUND_HALF_DOWN));
//        } else if (!TextUtils.isEmpty(intent.getStringExtra("each_di_gou_quan_money"))) {
//            eachDiGouQuanMoney = String.valueOf(new BigDecimal(intent.getStringExtra("each_di_gou_quan_money")));
//        } else {
            eachDiGouQuanMoney = "0";
//        }
//单件商品的支付价格
        perGoodsPayMoney = String.valueOf(new BigDecimal(goodsCost).subtract(new BigDecimal(eachDiGouQuanMoney)));
        String giveBeans = intent.getStringExtra("givenBean");
        if (TextUtils.isEmpty(giveBeans)) {
            giveBeans = "0";
            this.givenBean = Double.parseDouble(giveBeans);
        } else {
            this.givenBean = Double.parseDouble(giveBeans);
        }
        String subsidyStr = intent.getStringExtra("subsidy");
        if (TextUtils.isEmpty(subsidyStr)) {
            subsidyStr = "0";
            this.subsidy = Double.parseDouble(subsidyStr);
        } else {
            this.subsidy = Double.parseDouble(subsidyStr);
        }

        imgList1.add("add");
        adapter = new ImageDeleteAdapter(mContext, imgList1, imgList, bu);
        rvImg.setAdapter(adapter);
        rvImg.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        Drawable drawable = null;

        GlideUtil.showImageWithSuffix(mContext, intent.getStringExtra("goods_img_url"), ivGoods);
        switch (payStyle) {
            case "0":
//        上方价格设置
                ivYhsIcon.setVisibility(View.GONE);
                tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(goodsCost)));
//                tvIconFen0.setText(getString(R.string.back_score) + MoneyUtil.getLeXiangBi(goodsReturnIntegral));
                tvBackMoney.setVisibility(View.VISIBLE);
                tvBackMoney.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(String.valueOf(new BigDecimal(perGoodsPayMoney).multiply(new BigDecimal(maxCount))))));
                tvBackIntegral.setVisibility(View.GONE);
                break;
            case "1":
                ivYhsIcon.setVisibility(View.VISIBLE);
                ivYhsIcon.setImageResource(R.mipmap.icon_yhs);
                tvGoodsPrice.setVisibility(View.GONE);
//                tvIconFen0.setVisibility(View.GONE);
//                tvGoodsIntegral.setVisibility(View.VISIBLE);
                tvBackMoney.setVisibility(View.GONE);
                tvBackIntegral.setVisibility(View.VISIBLE);
                tvBackIntegral.setText(MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBi(Double.parseDouble(goodsIntegral) * maxCount)));
                break;
            case "2":
                ivYhsIcon.setVisibility(View.VISIBLE);
                ivYhsIcon.setImageResource(R.mipmap.icon_jfg);
                tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(goodsRetail)));
                tvBackMoney.setVisibility(View.VISIBLE);
                tvBackMoney.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(Double.parseDouble(goodsRetail) * maxCount)));
                tvBackIntegral.setVisibility(View.VISIBLE);
                tvBackIntegral.setText(MoneyUtil.setSmallIntegralMoney("+" + MoneyUtil.getLeXiangBi(integral * maxCount)));
                break;
            case "3":
            case "4":
            case GoodsType.CAL_POWER: //区块益豆专区商品
                tvGoodsPrice.setVisibility(View.VISIBLE);
                tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(goodsCost)));
                tvBackMoney.setVisibility(View.VISIBLE);
                tvBackMoney.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(String.valueOf(new BigDecimal(perGoodsPayMoney).multiply(new BigDecimal(maxCount))))));
                tvDiGouQuan.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(String.valueOf(
                        new BigDecimal(eachDiGouQuanMoney).multiply(new BigDecimal(maxCount))
                ))));
                tvGoodsBeans.setVisibility(View.VISIBLE);
                tvGoodsBeans.setText("送" + MoneyUtil.getLeXiangBi(givenBean));
                if (Double.valueOf(subsidy) != 0) {
                    tvSubsidy.setVisibility(View.VISIBLE);
                    tvSubsidy.setText("平台加赠" + Constants.APP_PLATFORM_DONATE_NAME + " " + MoneyUtil.getLeXiangBi(subsidy));
                } else {
                    tvSubsidy.setVisibility(View.GONE);
                }
                break;
            default:
                break;

        }


        tvBack.setText("提交售后服务申请");
        tvGoodsCount.setText(" × " + String.valueOf(maxCount));
        tvGoodsNorms.setText(norms);
        tvGoodsName.setText(intent.getStringExtra("goods_name"));
        Logger.i("申请数量：" + maxCount);
        if (againAfterSale) {//如果是再次申请提交售后，那么申请数量不能改变
            numControl.setMaxValue(maxCount - 1);
            numControl.setMinValue(maxCount);
            numControl.setValue(maxCount);
        } else {
            numControl.setMaxValue(maxCount - 1);
            numControl.setValue(maxCount);
        }

        tvBackCoupon.setText(" + " + MoneyUtil.getXianJinQuan(0));
        tvIconFen.setVisibility(View.GONE);
        Logger.i(String.valueOf(maxCount));
        parameters.order_index = intent.getStringExtra("order_id");
        parameters.order_goods_index = intent.getStringExtra("order_goods_index");
        parameters.order_goods_norms = norms;
        parameters.order_goods_sku = "0:0";
        parameters.service_type = 1;
        parameters.service_total_price = String.valueOf(price);
    }

    private void setListener() {
//        if (canAppayAfterSaleTime > 7) {//超过7天 不能退款、换货，只能返修
        if (after_sale_type == 3) {//不能退货 换货 只能返修
            mRgAfterSaleType.check(R.id.rb_maintain);
            llBackMoney.setVisibility(View.GONE);
            llBackDiGouQuan.setVisibility(View.GONE);
            llBackMoneyType.setVisibility(View.GONE);
            type = "返修";
            parameters.barter_type = 2;
            parameters.service_type = 0;
        } else if (after_sale_type > 1 || TYPE_TEST_GOODS.equals(payStyle)) {//不能退货  可换货 返修
            mRgAfterSaleType.check(R.id.rb_exchange);
            llBackMoney.setVisibility(View.GONE);
            llBackDiGouQuan.setVisibility(View.GONE);
            llBackMoneyType.setVisibility(View.GONE);
            type = "换货";
            parameters.barter_type = 1;
            parameters.service_type = 0;
        } else {//可退货 换货 返修
            mRgAfterSaleType.check(R.id.rb_back_money);
            type = "退款";
            parameters.service_type = 1;
        }

        mRgAfterSaleType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_back_money: // 退货
                        if (TYPE_TEST_GOODS.equals(payStyle)) {
                            mRgAfterSaleType.check(R.id.rb_exchange);
                            showToast("该商品不支持退款");
                            return;
                        }
                        if (after_sale_type > 1) {
                            mRgAfterSaleType.check(R.id.rb_maintain);
                            showToast("您已过了退款时间,您可以选择换货/返修");
                            return;
                        }
                        llApplyCount.setVisibility(View.VISIBLE);
                        llBackMoney.setVisibility(View.VISIBLE);
//                        llBackDiGouQuan.setVisibility(View.VISIBLE);
                        llBackDiGouQuan.setVisibility(View.GONE);
                        llBackMoneyType.setVisibility(View.VISIBLE);
                        parameters.service_type = 1;
                        type = "退款";
                        break;

                    case R.id.rb_exchange: // 换货
                        if (after_sale_type > 2) {
                            mRgAfterSaleType.check(R.id.rb_maintain);
                            showToast("已过了7天退款/换货时间,您可以选择返修");
                            return;
                        }
                        llBackMoney.setVisibility(View.GONE);
                        llBackDiGouQuan.setVisibility(View.GONE);
                        llBackMoneyType.setVisibility(View.GONE);
                        parameters.barter_type = 1;
                        parameters.service_total_price = "0";
                        parameters.service_type = 0;
                        type = "换货";
                        break;

                    case R.id.rb_maintain: // 返修
                        if (after_sale_type > 3) {
                            mRgAfterSaleType.check(R.id.rb_maintain);
                            showToast("您已过了退款/换货/返修时间,不可以再申请售后");
                            return;
                        }
                        llBackMoney.setVisibility(View.GONE);
                        llBackDiGouQuan.setVisibility(View.GONE);
                        llBackMoneyType.setVisibility(View.GONE);
                        parameters.barter_type = 2;
                        parameters.service_total_price = "0";
                        parameters.service_type = 0;
                        type = "返修";
                        break;
                    default:
                        break;
                }
            }
        });

        numControl.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                Logger.i("退款数量" + value);
                setBackMoney(value);
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                setBackMoney(value);
            }
        });

        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (imgList1.get(position).equals("add")) {
                    imgUpload(ApplyAfterSaleActivity.this.getWindow().getDecorView());
                }
            }
        });
    }

    private void setBackMoney(int count) {
        switch (payStyle) {

            case "1":
                tvBackIntegral.setText(MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBi(Double.parseDouble(goodsIntegral) * count)));
                break;
            case "2":
                tvBackMoney.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(Double.parseDouble(goodsRetail) * count)));
                tvBackIntegral.setText("+" + MoneyUtil.getLeXiangBi(integral * count));
                break;
            case "0":
            case "3":
            case "4":
            case "5":
                tvBackMoney.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(
                        String.valueOf(new BigDecimal(perGoodsPayMoney).multiply(new BigDecimal(String.valueOf(count)))))));
                tvDiGouQuan.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(
                        String.valueOf(new BigDecimal(eachDiGouQuanMoney).multiply(new BigDecimal(String.valueOf(count)))))));
                break;
            default:
                break;
        }
    }

    public void imgUpload(View view) {
        getPhotoPopupWindow(R.layout.library_module_popupwindow_amenduserphoto, -1, ViewGroup.LayoutParams.WRAP_CONTENT, R.style.merchant_AnimationFade);
        // 这里是位置显示方式,在屏幕的底部
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.2f);

    }

    /***
     * 获取头像PopupWindow实例
     */
    private void getPhotoPopupWindow(int resource, int width, int height, int animationStyle) {
        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            initPhotoPopuptWindow(resource, width, height, animationStyle);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 头像PopupWindow
     */
    protected void initPhotoPopuptWindow(int resource, int width, int height, int animationStyle) {
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(resource, null, false);
        popupWindow = new PopupWindow(popupWindow_view, width, height, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(animationStyle);
        backgroundAlpha(0.2f);
        // 点击其他地方消失
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void setData() {

    }

    //提交售后
    public void applyAfterSale(View view) {
        parameters.service_remark = mEdQuestionDescribe.getText().toString();
        if (parameters.service_remark == null || parameters.service_remark.length() == 0) {
            showToast("请填写问题描述");
            return;
        }
        parameters.service_goods_count = numControl.getValue();
        if (parameters.service_goods_count == 0) {
            showToast("售后数量过少");
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < imgList.size(); i++) {
            if (i < imgList.size() - 1) {
                sb.append(imgList.get(i) + ",");
            } else {
                sb.append(imgList.get(i));
            }
        }
        parameters.service_images = sb.toString();
        Logger.i("申请售后上传图片：" + parameters.service_images);
        if (parameters.service_images == null || parameters.service_images.length() == 0) {
            showToast("请上传售后商品图片");
            return;
        }
        netRequest.createServiceOrder2(parameters, new RequestCallBack<CreateServiceOrderEntity>() {
            @Override
            public void onStart() {
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<CreateServiceOrderEntity> responseInfo) {
                stopMyDialog();
                CreateServiceOrderEntity result = responseInfo.result;
                switch (result.code) {
                    case 1:
                        btnApplyAfterSale.setClickable(false);
                        Intent intent = new Intent(mContext, ApplyAfterSaleSuccessActivity.class);
                        intent.putExtra("orderId", result.orderId);
                        intent.putExtra("serviceOrder", result.serviceOrder);
                        intent.putExtra("time", StringFormat.formatDate(result.time, "yyyy-MM-dd"));
                        intent.putExtra("serviceRemark", parameters.service_remark);
                        intent.putExtra("type", type);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        btnApplyAfterSale.setClickable(true);
                        showToast(result.message);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                btnApplyAfterSale.setClickable(true);
                stopMyDialog();
                showToast("网络请求失败");
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case APPLY_CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            openCamera();
                        } else {
                            requestWrite_External_Storage();
                        }
                    } else {
                        showToast("拍照权限被拒绝");
                    }
                }
                break;
            case APPLY_WRITE_EXTERNAL_STORAGE_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        openCamera();
                    } else {
                        showToast("存储被拒绝,无法拍照");
                    }

                }

                break;
            case APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        albumSelect();
                    } else {
                        showToast("请开启读取SD卡权限");
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 开启照相机
     */
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ImageName = System.currentTimeMillis() + ".png";
        File file = new File(Environment.getExternalStorageDirectory(), ImageName);

        FileUtils.startActionCapture(this, file, ALBUM_CAMERA_REQUEST_CODE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        startActivityForResult(intent, ALBUM_CAMERA_REQUEST_CODE);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }

    /**
     * 申请存储卡写权限
     */
    private void requestWrite_External_Storage() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, APPLY_WRITE_EXTERNAL_STORAGE_CODE);
    }

    /**
     * 相册选择
     */
    private void albumSelect() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PHOTO_ALBUM);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == CAMERA_REQUEST_CODE) { //拍照回来
            if (resultCode == RESULT_CANCELED) {
                return;
            }
            if (data == null) {
                File picture = new File(Environment.getExternalStorageDirectory() + File.separator + ImageName);

                sendImage(picture);
                return;
            } else {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap bm = extras.getParcelable("data");
                    Uri uri = saveBitmap(bm);
                }
            }
        } else if (requestCode == PHOTO_ALBUM) { //相册选择回来
            if (resultCode == RESULT_CANCELED) {
                return;
            }
            if (data == null) {
                return;
            }
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = 0;
            try {
                columnIndex = c.getColumnIndex(filePathColumns[0]);
                String imagePath = c.getString(columnIndex);
                sendImage(new File(imagePath));
            } catch (Exception e) {
                e.printStackTrace();
                showToast("文件异常,请更换照片或拍照上传");
            } finally {
                c.close();
            }


        }
    }

    private void sendImage(File file) {
        startMyDialog(false);
        //在原来的基础上再对bitmap压缩一次
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("file", System.currentTimeMillis() + "environment", photoRequestBody);
        try {
            RetrofitUtils.getInstance(mContext).setContext(mContext)
                    .uploadFile(photo, new Callback<UploadImageEnity>() {
                        @Override
                        public void onResponse(Call<UploadImageEnity> call, Response<UploadImageEnity> response) {
                            UploadImageEnity body = response.body();
                            if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                                if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                    switch (body.code) {
                                        case 1:
                                            showToast("上传成功");
                                            imgList.add(body.filename);
                                            imgList1.clear();
                                            imgList1.addAll(imgList);
                                            if (imgList.size() < 3) {
                                                imgList1.add("add");
                                            }

                                            rvImg.setVisibility(View.VISIBLE);

                                            adapter.notifyDataSetChanged();
                                            break;
                                    }
                                }else{
                                    showToast(body.message);
                                }
                            }
                            stopMyDialog();

                        }

                        @Override
                        public void onFailure(Call<UploadImageEnity> call, Throwable t) {
                            stopMyDialog();
                            showToast("上传失败,重新上传");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Uri saveBitmap(Bitmap bm) {
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/com.yilian/userphoto/");
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        File img = new File(tmpDir.getAbsolutePath() + "11.png");
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(img);
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 拍照上传
     *
     * @param view
     */
    public void camera(View view) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
//申请权限，

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, APPLY_CAMERA_PERMISSION_REQUEST_CODE);
            return;
        }
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestWrite_External_Storage();
            return;
        }
//            有权限了
        openCamera();

    }

    /**
     * 跳转至相册选择
     *
     * @param view
     */
    public void photoalbum(View view) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE);
            return;
        }
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestWrite_External_Storage();
            return;
        }
        albumSelect();
    }

}
