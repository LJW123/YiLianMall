package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.leshan.ylyj.utils.SkipUtils;
import com.leshan.ylyj.view.activity.welfaremodel.WelfareLoveMsgActivity;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.luckypurchase.activity.LuckyActivityDetailActivity;
import com.yilian.luckypurchase.activity.LuckyMainActivity;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.activity.CtripHotelDetailActivity;
import com.yilian.mall.ctrip.util.JumpCtripActivityUtils;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mall.shoppingcard.utils.JumpCardActivityUtils;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.utils.Toast;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GoodsType;
import com.yilian.mylibrary.JsPayClass;
import com.yilian.mylibrary.NumberFormat;
import com.yilian.mylibrary.PullAliPayUtil;
import com.yilian.mylibrary.PullAliPayUtilSuccessListener;
import com.yilian.mylibrary.pay.PayFrom;
import com.yilian.mylibrary.utils.RecordListRetention;
import com.yilianmall.merchant.activity.MerchantComboManageActivity;
import com.yilianmall.merchant.activity.MerchantExchangeQrCodeActivity;
import com.yilianmall.merchant.activity.MerchantPayActivity;
import com.yilianmall.merchant.activity.MerchantTicketActivity;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * author Ray_L_Pain
 * Created by Administrator on 2016/11/14.
 * 该类用于 android与js的交互
 */
public class AndroidJs {
    private Context context;
    private Intent intent;

    private SharedPreferences sp;

    public AndroidJs(Context context) {
        this.context = context;
        sp = context.getSharedPreferences("UserInfor", 0);
    }

    /**
     * 给一个存在的页面的类名字符串，拼接上包名，组成全类名，然后跳转到该页面
     * 只能跳到Activity页面
     *
     * @param className
     */
    @JavascriptInterface
    public void fromEveryWhereToEveryWhere(String className) {
        try {
            Class<?> aClass = Class.forName("com.yilian.mall.ui." + className);
            context.startActivity(new Intent(context, aClass));
        } catch (ClassNotFoundException e) {
            Toast.makeText(context, "页面不存在", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 跳转基础一级页面
     *
     * @param type 根据该类型，区分跳转页面
     */
    @JavascriptInterface
    public void jumpToTabbarItem(String type) {
        intent = new Intent(context, JPMainActivity.class);
        boolean flag = false;
        switch (type) {
            case "home":
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                break;
            case "dikou":
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_DI_KOU_FRAGMENT);
                break;
            case "shopCar":
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_SHOPPING_CART_FRAGMENT);
                break;
            case "mine":
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                break;
            case "linggou":
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_LING_GOU_FRAGMENT);
                break;
            case "paihang":
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_RANKING_LIST);
                break;
            default://如果页面不存在，就跳到MainActivity
                flag = true;
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                break;
        }
        if (flag) {
            Toast.makeText(context, "页面不存在", Toast.LENGTH_SHORT).show();
        }
        //刷新个人页面标识
        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, context);
        context.startActivity(intent);
    }

    /**
     * 跳转到地区选择界面
     */
    @JavascriptInterface
    public void jumpToChooseProvinceList() {
        context.startActivity(new Intent(context, AreaSelectionActivity.class));
    }

    //  说明: 跳转到扫码页面
    @JavascriptInterface
    public void jumpToScanCode() {
        Intent intent = new Intent();
        intent.setClass(context, MipcaActivityCapture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    //        1.3 专题商品列表
//        说明: 专题商品列表页面(利用搜索接口 根据关键字 获取数据列表)
//        方法名: jumpToSearchGoods("key_word");
//        参数名:
//        key_word 搜索关键字
//        用例: jumpToSearchGoods("天王表");
    @JavascriptInterface
    public void jumpToSearchGoods(String keyWord) {
        intent = new Intent(context, SearchCommodityActivity.class);
        intent.putExtra("keyWord", keyWord);
        intent.putExtra("from", "webView");
        context.startActivity(intent);
    }

    /**
     * 搜索
     */
    @JavascriptInterface
    public void jumpToSearchPage(String type) {
        switch (type) {
            case "city":
                intent = new Intent(context, FindActivity.class);
                intent.putExtra("type", "city");
                break;
            case "merchant":
                intent = new Intent(context, FindActivity.class);
                intent.putExtra("type", "merchant");
                break;
            case "goods":
                intent = new Intent(context, SearchCommodityActivity.class);
                break;
            case "experimentalArea":
                intent = new Intent(context, SearchCommodityActivity.class);
                intent.putExtra("type", GoodsType.TYPE_TEST_GOODS);
                break;
            case "afterSale":
                intent = new Intent(context, FindActivity.class);
                if (isLogin()) {
                    intent.putExtra("type", "afterSale");
                } else {
                    context.startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
                }
                break;
            case "oneBuy":
                intent = new Intent(context, FindActivity.class);
                intent.putExtra("type", "oneBuy");
                break;
            default:
                break;
        }
        context.startActivity(intent);
    }

    /**
     * 判断是否登录
     */
    @JavascriptInterface
    public boolean isLogin() {
        return sp.getBoolean("state", false);
    }

//
//        B.商城模块
//        2.1 跳转到商品列表
//        说明: 跳转到商品列表页面(需要三个参数)
//        方法名: jumpToGoodsList("title","class","goods_type");
//        参数名:
//        title //分类标题
//                goods_type //0表示所有区域，3乐分区 4乐享区
//        class // 1彩妆护肤 2大小家电 3家纺 4食品 5洗护护肤 6保健服饰 7箱包皮具
//        8健康养生 9酒水茶叶 10家居用品 11手表饰品
//        用例: jumpToGoodsList("彩妆护肤","1","0");
//        注意: 当goods_type传0时 class起作用 其他情况不起作用
//        当goods_type传3或者4时 class需传0
//
//
//    @JavascriptInterface
//    public void jumpToGoodsList(String title, String cls, String goods_type) {
//        intent = new Intent(mContext, MallGoodsActivity.class);
//        intent.putExtra("title", title);
//        intent.putExtra("class", NumberFormat.convertToInt(cls, 0));
//        intent.putExtra("type", goods_type);
//        mContext.startActivity(intent);
//    }

    //        2.2 跳转到商品详情
//        说明: 跳转到商品详情页面
//        方法名: jumpToGoodsDetail("goods_id");
//        参数名:
//        goods_id //商品id
//        filiale_id //兑换中心id
//        用例: jumpToGoodsDetail("180");
//
    @JavascriptInterface
    public void jumpToGoodsDetail(String goodsId, String filialeId, String tagsName) {
        intent = new Intent(context, JPNewCommDetailActivity.class);
        intent.putExtra(Constants.INTENT_KEY_JP_GOODS_ID, goodsId);
        intent.putExtra(Constants.INTENT_KEY_JP_FILIALE_ID, filialeId);
        intent.putExtra(Constants.INTENT_KEY_JP_TAGS_NAME, tagsName);
        context.startActivity(intent);
    }

    //        2.2 跳转到商品详情
//        说明: 跳转到商品详情页面
//        方法名: jumpToGoodsDetails("goods_id","type");
//        参数名:
//        goods_id //商品id
//        type 0 普通商品 1易划算 2 奖券购  3普通益豆 4钜惠益豆 5益豆线上商城 6 京东商品详情
//        用例: jumpToGoodsDetail("180");
//
    @JavascriptInterface
    public void jumpToGoodsDetails(String goodsId, String type) {
        if (TextUtils.isEmpty(goodsId)) {
            Toast.makeText(context, "请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("6".equals(type)) {
//            跳转京东商品详情
            JumpJdActivityUtils.toGoodsDetail(context, goodsId);
        } else if ("7".equals(type)) {
//            跳转苏宁商品详情
            JumpSnActivityUtils.toSnGoodsDetailActivity(context, goodsId);
        } else {
            intent = new Intent(context, JPNewCommDetailActivity.class);
            intent.putExtra(Constants.INTENT_KEY_JP_GOODS_ID, goodsId);
            intent.putExtra("classify", type);
            context.startActivity(intent);
        }

    }

    /**
     * 2.2.3 跳转到购物卡专区商品详情
     * 说明: 跳转到购物卡专区详情
     * 方法名: jumpToScGoodsDetails("goods_id","type");
     * 参数名:
     * goods_id //商品id
     * type // 商品类型 1.京东商品 2.苏宁商品(暂定) 3.益联商品(暂定)
     * 注意: 时间戳为秒级时间戳
     * 用例: jumpToScGoodsDetails("180","1");
     *
     * @param goodsId 商品id
     * @param type    商品类型 1.京东商品 2.苏宁商品(暂定) 3.益联商品(暂定)
     */
    @JavascriptInterface
    public void jumpToScGoodsDetails(String goodsId, String type) {
        if (TextUtils.isEmpty(goodsId)) {
            Toast.makeText(context, "请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("1".equals(type)) {
            //跳转 京东商品 详情
            JumpCardActivityUtils.toGoodsDetail(context, goodsId);
        } else if ("2".equals(type)) {
            //跳转 益联商品 详情
        }
    }

    /**
     * 6.0 跳转购物卡首页
     * 说明： 跳转购物卡首页
     */
    @JavascriptInterface
    public void jumpToScMainPage() {
        JumpCardActivityUtils.toCardHomePage(context);
    }


    //        说明: 跳转到web专题页面(对于需要切换导航样式的web专题 例如 :适用于商城web首页向其他web专题的跳转)
//        方法名: jumpToMallWebTopic("url");
//        参数名:
//        url //url链接地址
//        用例: jumpToMallWebTopic("www.baidu.com");
//
    @JavascriptInterface
    public void jumpToMallWebTopic(String url) {
        intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constants.SPKEY_URL, url);
        context.startActivity(intent);
    }

//    //    2.4 跳转品牌旗舰店列表
////    说明: 品牌旗舰店列表
////    方法名: jumpToSuppliers();
////    用例: jumpToSuppliers();
//    @JavascriptInterface
//    public void jumpToSuppliers() {
//        mContext.startActivity(new Intent(mContext, MallProviderActivity.class));
//    }
//已删除
////    2.5 跳转省分商城商品分类列表或者品牌旗舰店商品列表
////    说明: 跳转到省分商城商品分类列表或者品牌旗舰店商品列表
////    方法名: jumpToProvinceGoodsList("supplier_id","class_id");
////    参数名:
////    supplier_id //旗舰店id
////            class_id //分类id
////    注意: 当为省分商城商品列表时 supplier_id传"0" 否则class_id传"0"
////    用例: jumpToProvinceGoodsList("0","100");
//
//    @JavascriptInterface
//    public void jumpToProvinceGoodsList(String supplierId, String classId) {
//        intent = new Intent(mContext, MallCategoryListActivity.class);
//        intent.putExtra("class", classId);
//        intent.putExtra("supplier_id", supplierId);
//        intent.putExtra("type", "mallCategory");
//        mContext.startActivity(intent);
//    }

    //    2.6 跳转品牌旗舰店详情
//    说明: 品牌旗舰店详情
//    方法名: jumpToSuppliersDetial("supplier_id");
//    参数名:
//    supplier_id //旗舰店id
//    用例: jumpToSuppliersDetial("10");
    @JavascriptInterface
    public void jumpToSuppliersDetial(int supplierId) {
        Logger.i("web跳转旗舰店详情2");
        intent = new Intent(context, JPFlagshipActivity.class);
        intent.putExtra("index_idz", String.valueOf(supplierId));
        context.startActivity(intent);
    }
//


//        3.5 跳转会员夺宝记录
//        说明: 跳转到会员夺宝记录页面
//        方法名: pushIntoMemberSnatchRecords("user_id");
//        参数名:
//        user_id //用户id
//        用例: pushIntoMemberSnatchRecords("10");
//


    //        D. 商家模块
//        4.1 跳转到商家详情
//        说明: 跳转到商家详情页面
//        方法名: jumpToMerchantDetail("merchant_id");
//        参数名:
//        merchant_id //商家id
//        用例: jumpToMerchantDetail("100");
//
    @JavascriptInterface
    public void jumpToMerchantDetail(String merchantId) {
        intent = new Intent(context, MTMerchantDetailActivity.class);
        intent.putExtra("merchant_id", merchantId);
        context.startActivity(intent);
    }

    //        4.2 跳转到商家乐分币/乐享币赠送详情
//        说明: 跳转到商家乐分币/乐享币赠送详情页面
//        方法名: jumpToMerchantDetailLB("merchant_id","type");
//        参数名:
//        merchant_id //商家id
//                type //0乐分币(抵扣券) 1乐享币
//        用例: jumpToMerchantDetailLB("100","0");
//
    @JavascriptInterface
    public void jumpToMerchantDetailLB(String merchantId, String type) {
        intent = new Intent(context, ShopsPresentOrConsumeDetailActivity.class);

        switch (type) {
            case "0":
                intent.putExtra("style", "present");
                break;
            case "1":
                intent.putExtra("style", "consume");
                break;
        }
        intent.putExtra("merchantId", merchantId);
        context.startActivity(intent);

    }

    //        4.3 跳转到兑换中心详情
//        说明: 跳转到兑换中心详情页面
//        方法名: jumpToExchangeDetail("exchange_id");
//        参数名:
//        exchange_id // 兑换中心id
//        recommend // "0"其他 "1"推荐
//        用例: jumpToExchangeDetail("100");
//
    @JavascriptInterface
    public void jumpToExchangeDetail(String exchangeId, String recommend) {
        intent = new Intent(context, JPLeFenHomeActivity.class);
        intent.putExtra("index_id", exchangeId);
        intent.putExtra("type", recommend);
        context.startActivity(intent);
    }

    //    4.4 跳转到兑换中心详情
//    说明: 跳转到兑换中心列表
//    方法名: jumpToExchangeList("type");
//    参数名:
//    type //  "3" 省份兑换中心 "2" 地区兑换中心
//    用例: jumpToExchangeList("1");
    @JavascriptInterface
    public void jumpToExchangeList(String type) {

    }

    //
//        E.个人中心
//
//        5.1 跳转乐享币乐分币抵扣券
//        说明: 跳转到乐享币乐分币抵扣券页面
//        方法名: jumpToMinePageLBRecord("type");
//        参数名:
//        type // 1奖券 0奖励
//        用例: jumpToMinePageLBRecord("0");
//
    @JavascriptInterface
    public void jumpToMinePageLBRecord(String type) {

        if (isLogin()) {
            intent = new Intent(context, V3MoneyActivity.class);
            intent.putExtra("type", NumberFormat.convertToInt(type, 0));
            context.startActivity(intent);
        } else {
            context.startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
        }
    }

    //        5.2 跳转订单列表
//        说明: 跳转到订单列表页面
//        方法名: jumpToMinePageOrderRecord("type");
//        参数名:
//        type //0全部 1待付款 2待收货 3待评价 4已完成 5售后
//        用例: jumpToMinePageOrderRecord("0");
//
    @JavascriptInterface
    public void jumpToMinePageOrderRecord(String type) {
        if (isLogin()) {
            switch (type) {
                case "1":
                case "2":
                case "3":
                case "4":
                    intent = new Intent(context, MyMallOrderListActivity2.class);
                    intent.putExtra("order_Type", type);
                    break;
                case "5":
                    intent = new Intent(context, AfterSaleActivity.class);
                    break;
            }
            context.startActivity(intent);
        } else {
            context.startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
        }
    }

    //        5.3 跳转会员充值
//        说明: 跳转到会员充值页面
//        方法名: jumpToMemberRecharge("type");
//        参数名:
//        type //0乐分币充值 1乐享币直充 2抵扣券充值
//        用例: jumpToMemberRecharge("0");
//
    @JavascriptInterface
    public void jumpToMemberRecharge(String type) {
        if (isLogin()) {
            switch (type) {
                case "0":
                    type = "lefenbi";
                    break;
                case "1":
                    type = "chooseCharge";
                    break;
                case "2":
                    type = "xianjinquan";
                    break;
            }
//            intent = new Intent(context, NMemberChargeActivity.class);
//            intent.putExtra("type", type);
//            context.startActivity(intent);
            intent = new Intent(context, RechargeActivity.class);
            context.startActivity(intent);
        } else {
            context.startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
        }

    }

    //        5.4 跳转会员转赠
//        说明: 跳转到会员转赠页面
//        方法名: jumpToMembergift("type");
//        参数名:
//        type //0奖励 1奖券
//        用例: jumpToMembergift("0");
//
    @JavascriptInterface
    public void jumpToMembergift(String type) {
        if (isLogin()) {
            intent = new Intent(context, V3MoneyActivity.class);
            intent.putExtra("type", NumberFormat.convertToInt(type, 0));
            context.startActivity(intent);
        } else {
            context.startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
        }
    }

    //        5.5 跳转中奖凭证详情
//        说明: 跳转到中奖凭证详情页面
//        方法名: jumpToWinRecordDetial("win_id");
//        参数名:
//        win_id //奖品编号id
//        用例: jumpToWinRecordDetial("10");
//
    @JavascriptInterface
    public void jumpToWinRecordDetial(String voucherId) {
        if (isLogin()) {
            intent = new Intent(context, PrizeVoucherDetailActivity.class);
            intent.putExtra("voucher_index", voucherId);
            context.startActivity(intent);
        } else {
            context.startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
        }
    }

    //
//    3. 通用方法调用
//    1.1 本地分享
//    说明: 调用本地分享方法:
//    方法名: share("title","content","imageUrl","url","callbackUrl");
//    参数名:
//    title: 分享出去的标题
//    content: 分享出去的副标题
//    imageUrl: 分享图片url
//    url: 分享的url
//    callbackUrl: 回调的url("0":不需要回调      "1":回调web的方法appCallback    "http://www.baidu.com":http开头的链接地址打开新的web页面)
//    用例: share("益联益家","一切尽在益联益家","http://www.mob.com/images/logo_black.png
//
//                      ","https://www.baidu.com","https://www.baidu.com");
//
    @JavascriptInterface
    public void share(final String title, final String content, String imageUrl, final String url, final String callBackUrl) {
        Logger.i("2018年1月4日 15:22:07-web-" + imageUrl);
        if (!imageUrl.startsWith("https:")) {
            imageUrl = "https:" + imageUrl;
        }
        Animation animBottom = AnimationUtils.loadAnimation(context, R.anim.anim_wellcome_bottom);
        UmengDialog dialog1 = new UmengDialog(context, animBottom, R.style.DialogControl,
                new UmengGloble().getAllIconModels());
        String finalImageUrl = imageUrl;
        Logger.i("2018年1月4日 15:22:07-web-" + finalImageUrl);
        dialog1.setItemLister(new UmengDialog.OnListItemClickListener() {

            @Override
            public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
//                String content = mContext.getResources().getString(R.string.appshare);
                new ShareUtile(context, ((IconModel) arg4).getType(), content, title, finalImageUrl, url, callBackUrl).share();
            }
        });
        dialog1.show();
    }

    /**
     * 直接分享
     *
     * @param platformType      分享出去的平台 0微信 1朋友圈 2QQ 3QQ空间
     * @param title             分享出去的标题
     * @param content           分享出去的副标题
     * @param imageUrl          分享图片url 如果不需要图片 可为空字符串 但需要传该参数
     * @param url               分享的url
     * @param callBackParameter 回调方法appCallback的参数 回调成功之后对web注入js方法 appCallback("callBackParameter");
     */
    @JavascriptInterface
    public void provokeShare(String platformType, String title, String content, String imageUrl, String url, String callBackParameter) {
//        type与分享不对应纠正
        if ("2".equals(platformType)) {
            platformType = "4";
        }
        new ShareUtile(context, NumberFormat.convertToInt(platformType, 0), content, title, imageUrl, url).share();
        PreferenceUtils.writeStrConfig(Constants.SHARE_CALLBACK, callBackParameter, context);
//        Logger.i("拉起分享： platformType:" + platformType + "  title:"+title+" content:"+content+" imageUrl: "+imageUrl+"  url:"+url+"  callBackParameter:"+callBackParameter);
//        new ShareUtile(NumberFormat.convertToInt(platformType, 0), content, title, imageUrl, url, callBackParameter, context).act_share_black();
    }

    //        1.2 跳转通用模块
//        说明: 跳转 乐分宝 兑换中心 邀请好友 天天夺宝 天天抽奖 常见问题 购物车 会员领奖励 我的夺宝 我的收藏 中奖凭证 设置 消息中心 会员领奖励
//        说明: 根据参数判断跳转
//        方法名: jumpToSecondLevelPage("type");
//        参数名:
//    lefenbao // 乐分宝
//    invite //邀请好友 
//    activity //活动列表 
//    choujiang //免费抽奖
//    choujiang//签到
//    question //常见问题
//    shoppingCar //购物车 
//    memberCash // 会员领奖励
//    memberEarn //会员领奖励 
//    mineSnatch //我的夺宝 
//    mineCollection //我的收藏 
//    mineWin //中奖凭证
//    setting //设置
//    message //消息中心
//    faceBack //意见反馈

    //        用例: jumpToSecondLevelPage("mineWin");
//
    @JavascriptInterface
    public void jumpToSecondLevelPage(String type) {
        switch (type) {
            case "lefenbao":
                if (isLogin()) {
                    context.startActivity(new Intent(context, BankActivity.class));
                } else {
                    context.startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
                }
                break;
            case "invite":
                context.startActivity(new Intent(context, JPInvitePrizeActivity.class));
                break;
            case "duobao":
                context.startActivity(new Intent(context, LuckySnatch.class));
                break;
            case "activity":
                intent = new Intent(context, JPLocationActivity.class);
                context.startActivity(intent);
                break;
            case "choujiang"://签到
                intent = new Intent(context, JPSignActivity.class);
                context.startActivity(intent);
                break;
            case "question":
                context.startActivity(new Intent(context, FAQActivity.class));
                break;
            case "shoppingCar":
                intent = new Intent(context, JPMainActivity.class);
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_SHOPPING_CART_FRAGMENT);
                context.startActivity(intent);
                break;
            case "memberCash":
                if (isLogin()) {
                    context.startActivity(new Intent(context, MembersCash.class));
                } else {
                    context.startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
                }
                break;
            case "memberEarn":
                context.startActivity(new Intent(context, MembersDetails.class));
                break;
            case "mineSnatch":
                if (isLogin()) {
                    context.startActivity(new Intent(context, MySnatchActivity.class));
                } else {
                    context.startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
                }
                break;
            case "mineCollection":
                if (isLogin()) {
                    context.startActivity(new Intent(context, FavoriteActivity.class));
                } else {
                    context.startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
                }
                break;
            case "mineWin":
                if (isLogin()) {
                    context.startActivity(new Intent(context, PrizeVoucherListActivity.class));
                } else {
                    context.startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
                }
                break;
            case "setting":
                context.startActivity(new Intent(context, SettingActivity.class));
                break;
            case "message":
                if (isLogin()) {
                    context.startActivity(new Intent(context, InformationActivity.class));
                } else {
                    context.startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
                }
                break;
            case "faceBack":
                intent = new Intent(context, FeedbackActivity.class);
                context.startActivity(intent);
//                if (isLogin()) {
//                } else {
//                    intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
//                }
                break;
        }
    }

    //        1.3 通用类跳转
//        说明: 限于两个端类名一致情况下 没有参数的情况
//        方法名: jumpToUniversalPage("class_name","is_login");
//        参数名:
//        class_name //类名
//                is_login // 0不需要登录 1需要登录
//        用例: jumpToUniversalPage("minePage","1");
//
    @JavascriptInterface
    public void jumpToUniversalPage(String className, String isLogin) {
        switch (isLogin) {
            case "0":
                try {
                    Class<?> aClass = Class.forName("com.yilian.mall.ui." + className);
                    context.startActivity(new Intent(context, aClass));
                } catch (ClassNotFoundException e) {
                    Toast.makeText(context, "页面不存在", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            case "1":
                if (isLogin()) {
                    try {
                        Class<?> aClass = Class.forName("com.yilian.mall.ui." + className);
                        context.startActivity(new Intent(context, aClass));
                    } catch (ClassNotFoundException e) {
                        Toast.makeText(context, "页面不存在", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    context.startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
                }
                break;
            default:
                break;
        }
    }

    //        1.4 获取用户信息接口
//        说明: 通过这个接口获取到用户的相关信息
//        1.4.1 获取token
//        说明: 获取token的值 当用户是未登录状态下时 token值为0 返回值为字符串
//        方法名: getUserToken();
    @JavascriptInterface
    public String getUserToken() {
//        String userInforToken = mContext.getSharedPreferences("UserInfor", 0).getString(Constants.SPKEY_TOKEN, "0");
        String token = RequestOftenKey.getToken(context);
        Logger.i("传递给Web的 token:" + token);
        return token;
    }

    //
//        1.4.2 获取device
//        说明: 获取device的值 正常设备都可以获取到 返回值为字符串
//        方法名: getUserDevice();
//
    @JavascriptInterface
    public String getUserDevice() {
//        String userInforDevice = mContext.getSharedPreferences("UserInfor", 0).getString(Constants.SPKEY_DEVICE_INDEX, "0");
//        Logger.i(userInforDevice);
        String deviceIndex = RequestOftenKey.getDeviceIndex(context);
        Logger.i("提供JS的deviceIndex :" + deviceIndex);
        return deviceIndex;
    }

    //        1.4.3 获取用户信息
//        说明: 客户端做是否登录验证 用的时候直接调用 返回值为json对象
//        方法名: getUserInfo();
    @JavascriptInterface
    public String getUserInfo() {
        Logger.i("用户信息:" + sp.getString(Constants.SPKEY_USER_INFO, "未获取到用户信息"));
        RequestOftenKey.getUserInfor(context, sp);//更新本地用户信息   在用户信息返回给web之前，先刷新本地数据
        return sp.getString(Constants.SPKEY_USER_INFO, "未获取到用户信息");
    }

    //
//        1.4.4 验证客户端是否登录
//        说明: 客户端是否登录 返回字符串 0登录状态 1未登录状态
//        方法名: getisLogin();
    @JavascriptInterface
    public String getisLogin() {
        if (isLogin()) {
            Logger.i("传递给WebView登录状态：" + 0);
            return "0";
        } else {
            Logger.i("传递给WebView登录状态：" + 1);
            return "1";
        }
    }

    //    1.4.5 获取版本号
//    说明: 获取版本号的值 正常设备都可以获取到 返回值为字符串 iOS 为0 安卓1 拼接方式: 0,2.4.5
//    方法名: getVersion();
    @JavascriptInterface
    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion() {
        return "1," + CommonUtils.getAppVersion(context);
    }


    //    1.5 跳转到我的乐友/从友/众友页面
//    说明: 跳转到我的乐友/从友/众友页面 客户端做是否登录判断
//    方法名: jumpToLevelMember("level");
//    参数名:
//    level //1 乐友 2 从友 3众友
//    用例: jumpToLevelMember("1");
//
    @JavascriptInterface
    public void jumpToLevelMember(String level) {
        if (isLogin()) {
            switch (level) {
                case "1":
                    context.startActivity(new Intent(context, MembersLevel1.class));
                    break;
                case "2":
                    context.startActivity(new Intent(context, MembersLevel1.class));
                    break;
                case "3":
                    context.startActivity(new Intent(context, MembersLevel1.class));
                    break;
            }
        } else {
            context.startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
        }
    }

    //    1.6 获取用户地理位置信息经纬度城市id区域id
//    说明: 获取用户地理位置信息经纬度城市id区域id 返回值为一个json对象
//    方法名: getUserLocationInfo();
//    返回值组合形式:
//    {
//        "lat":"", // 纬度
//            "long":"", // 经度
//            "city_id":"", // 城市id
//            "county_id":"" // 区县id
//    }
    @JavascriptInterface
    public String getUserLocationInfo() {
        JSONObject locationInfo = new JSONObject();
        String location = sp.getString(Constants.SPKEY_LOCATION, "获取位置信息失败");
        Logger.i("返回JS的：" + location);
        return location;
    }

    /**
     * 获取携程用户地理位置信息经纬度/城市/区域/商业区
     *
     * @return
     */
    @JavascriptInterface
    public String getXcUserLocationInfo() {
        JSONObject locationInfo = new JSONObject();
        String location = sp.getString(Constants.CTRIP_LOCATION, "获取位置信息失败");
        Logger.i("返回JS的：" + location);
        return location;
    }

    /**
     * 1.7登录 用户登录
     */
    @JavascriptInterface
    public void jumpToLogin() {
        context.startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
    }

    /**
     * 1.8跳转二级界面
     */
    @JavascriptInterface
    public void jumpToSecondTypePage(String idStr, String name, String filiale_id) {
        Intent intent = new Intent(context, OnLineMallGoodsListActivity.class);
        intent.putExtra("class_id", idStr);
        intent.putExtra("goods_classfiy", name);
        intent.putExtra("filiale_id", filiale_id);
        context.startActivity(intent);
    }

    /**
     * 1.9跳转旗舰店详情
     */
    @JavascriptInterface
    public void jumpToFlagShipDetialPage(String supplier_id) {
        Logger.i("webview跳转旗舰店详情1");
        Intent intent = new Intent(context, JPFlagshipActivity.class);
        intent.putExtra("index_id", supplier_id);
        context.startActivity(intent);
    }

    /**
     * 2.0跳转乐分币商品类表
     */
    @JavascriptInterface
    public void jumpToLeFeiGoodsListPage() {
        Intent intent = new Intent(context, JPleFenGoodsActivity.class);
        context.startActivity(intent);
    }


//    2.2 跳转到收银台
//    参数说明: 对于不是必须传的参数请传"空字符串"
//    payType:
//            3 商城订单(order_ids,lebi,xjq,gwq,payType必传参数)
//    4 商家套餐店内支付/扫码支付给商家(mer_id,lebi,xjq,gwq(注意：如果是消费送零购券这个值传 -1 ),payType,employee必传参数)
//            5 扫码支付给收银系统(order_ids,lebi,xjq,payType必传参数)
//    6 商家套餐在线支付订单(order_ids,lebi,xjq,payType必传参数)
//    order_ids: 单个订单号或者多个订单号 多个订单号时用,分割
//    lebi: 订单金额(分)
//    xjq: 订单抵扣券(分)
//    gwq: 订单零购券(分)
//    mer_id: 商家id
//    employee: 员工id
//    方法名: jumpCommonBalancePay("order_ids","lebi","xjq","gwq","payType","mer_id","employee");

    /**
     * 跳转到收银台
     *
     * @param orderIds 单个订单号或者多个订单号 多个订单号时用,分割
     * @param liBi     订单金额(分) 注: 最原始的订单金额
     * @param xjq      订单抵扣券(分)
     * @param payType
     * @param merId    商家id
     * @param employee 员工id
     */
    @JavascriptInterface
    public void jumpCommonBalancePay(String orderIds, String liBi, String xjq, String gwq, String payType, String merId, String employee) {
        RequestOftenKey.getUserInfor(context, sp);//刷新用户信息
        Intent intent = new Intent(context, CashDeskActivity2.class);
        intent.putExtra("orderIndex", orderIds);
        intent.putExtra("order_total_lebi", liBi);
        intent.putExtra("order_total_coupon", xjq);
        intent.putExtra("order_total_voucher", gwq);
        intent.putExtra("payType", payType);
        intent.putExtra("merId", merId);
        intent.putExtra("employee", employee);
        intent.putExtra(CashDeskActivity2.PAY_FROM_TAG, PayFrom.WEB_VIEW);
        intent.putExtra("user_coupon", sp.getString("coupon", ""));
        Logger.i("Web传递过来的跳转收银台数据： orderIds:" + orderIds + "  liBi:" + liBi + "  xjq:" + xjq + "  payType:" + payType + "  merId:" + merId + "  employee:" + employee);
        context.startActivity(intent);
    }

    /**
     * 2.3跳转设置密码
     * 说明：跳转设置密码页面
     * 方法名： jumpToSetPasswordPage();
     */
    @JavascriptInterface
    public void jumpToSetPasswordPage() {
        context.startActivity(new Intent(context, InitialPayActivity.class));
    }

    /**
     * 拉起微信或支付宝支付
     *
     * @param type 支付类型 1支付宝 2微信
     *             orderStr // 支付json字符串 接收过来后做转对象处理 格式样式例如:
     *             {
     *             orderString = "";
     *             "order_index" = 327;
     *             "payment_apply_time" = 1498027130;
     *             "payment_fee" = "0.01";
     *             "paymentIndex" = 2017062114385037463;
     *             }
     */
    @JavascriptInterface
    public void jumpToVendorPay(String type, String orderStr) {
        Gson gson = new Gson();
        JsPayClass jsPayClass = gson.fromJson(orderStr, JsPayClass.class);
        switch (type) {
            case "1"://拉起支付宝
                PullAliPayUtil.zhifubao(jsPayClass, context, new PullAliPayUtilSuccessListener() {
                    @Override
                    public void pullAliPaySuccessListener() {
                        //                    通知JS支付完成  appPayCallback()
                        com.yilian.mylibrary.PreferenceUtils.writeBoolConfig(Constants.SPKEY_HONG_BAO, true, context);
                    }
                });
                break;
            case "2"://拉起微信

                break;
        }
    }

    /**
     * 跳转易划算页面
     */
    @JavascriptInterface
    public void jumpToYiHuaSuanPage() {
        //易划算
        intent = new Intent(context, MallMainActivity.class);
        intent.putExtra("title", context.getResources().getString(R.string.yhs));
        context.startActivity(intent);
    }

    /**
     * 跳转奖券购页面
     */
    @JavascriptInterface
    public void jumpToJiFenGouPage() {
        //奖券购
        intent = new Intent(context, MallMainActivity.class);
        intent.putExtra("title", context.getResources().getString(R.string.jfg));
        context.startActivity(intent);
    }

    /**
     * 跳转到商家套餐订单列表界面
     */
    @JavascriptInterface
    public void jumpToPackageRecordPage() {
        intent = new Intent(context, MerchantComboManageActivity.class);
    }

    /**
     * 跳转到新版大家猜结果页面
     * id-活动id
     * isParticipate-1我猜过的 2已开奖
     */
    @JavascriptInterface
    public void jumpToNewGuessRecordsPage(String type, String activity_id) {
        intent = new Intent(context, NewGuessDetailActivity.class);
        intent.putExtra("id", activity_id);
        intent.putExtra("is_participate", type);
        context.startActivity(intent);
    }

    /**
     * 通过JS设置Title栏的背景色和字体、icon 颜色
     *
     * @param backgroundColor
     * @param tinColor        标题以及按钮颜色
     * @param statusBarType   电池条样式"0"默认 黑色 "1"白色  iOS使用，Android端不使用
     */
    @JavascriptInterface
    public void customNavColor(String backgroundColor, String tinColor, String statusBarType) {
        Logger.i("标题title走了这里1  backgroundColor：" + backgroundColor + " tinColor: " + tinColor);
        Intent intent = new Intent();
        intent.putExtra("backgroundColor", backgroundColor);
        intent.putExtra("titleTheme", 0);
        intent.putExtra("tinColor", tinColor);
        intent.setAction("com.yilian.webview.titlethemebroadcastreceiver");
        context.sendBroadcast(intent);
    }

    /**
     * 设置导航栏的背景透明度和滑动渐变类型
     *
     * @param alpha                   “0"透明 “1"不透明
     * @param themeType“0"向上滑动不变色保持透明 “1" 向上滑动变为白色 此参数在alpha为0时起作用
     * @param statusBarType           电池条样式"0"默认 黑色 "1"白色  iOS使用，Android端不使用
     */
    @JavascriptInterface
    public void customNavBackgroundAlpha(String alpha, String themeType, String statusBarType) {
        Logger.i("标题title走了这里2  alpha：" + alpha + "  themeType:" + themeType);
        Intent intent = new Intent();
        intent.putExtra("alpha", alpha);
        intent.putExtra("themeType", themeType);
        intent.putExtra("titleTheme", 1);
        intent.setAction("com.yilian.webview.titlethemebroadcastreceiver");
        context.sendBroadcast(intent);
    }

    /**
     * 设置导航栏右侧按钮显示
     *
     * @param iconType  0 只显示更多图标  1 显示特殊图标和更多图标 2只显示文字
     * @param defaul    0默认 1 不默认
     * @param rightText
     */
    @JavascriptInterface
    public void customNavButton(String iconType, String defaul, String rightText) {
        Logger.i("标题title走了这里3  iconType:" + iconType + "  defaul:" + defaul + "  rightText:" + rightText);
        Intent intent = new Intent();
        intent.putExtra("iconType", iconType);
        intent.putExtra("rightText", rightText);
        boolean isDefault = "0".equals(defaul);
        intent.putExtra("defaul", isDefault);
        intent.setAction("com.yilian.webview.iconthemebroadcastreceiver");
        context.sendBroadcast(intent);
    }

    /**
     * 3.5跳转幸运购首页
     * 说明：跳转幸运购首页
     * 方法名： jumpToLucyHomePage();
     */
    @JavascriptInterface
    public void jumpToLucyHomePage() {
        Intent intent = new Intent(context, LuckyMainActivity.class);
        context.startActivity(intent);
    }

    /**
     * 3.6跳转幸运购详情
     * 说明：跳转幸运购详情
     *
     * @param activityId
     */
    @JavascriptInterface
    public void jumpToLucyDetialPage(String activityId) {
        Logger.i("点击了：jumpToLucyDetialPage");
        Intent intent = new Intent(context, LuckyActivityDetailActivity.class);
        intent.putExtra("activity_id", activityId);
        intent.putExtra("type", "0");
        context.startActivity(intent);
    }

    void printMethodName() {
        Thread.currentThread().getStackTrace()[1].getMethodName();
    }

    /**
     * 3.7跳转猜价格-碰运气活动商品详情
     * goods_id //商品id
     * type  //1.碰运气 2.猜价格
     * act_id //活动ID
     * isShowView //进入详情是否自动弹出活动样式的sku 0不弹出 1弹出
     * order_id //从中奖显示界面跳转到详情需要传递此订单id,若普通跳转传递@"0"
     */
    @JavascriptInterface
    public void jumpToActiveGoodsDetails(String goods_id, String type, String act_id, String isShowView, String order_id) {
        Intent intent = new Intent(context, ActivityDetailActivity.class);
        intent.putExtra("goods_id", goods_id);
        intent.putExtra("activity_id", act_id);
        intent.putExtra("activity_type", type);
        intent.putExtra("is_show", isShowView);
        intent.putExtra("activity_orderId", order_id);

        Logger.i("2017年11月23日 15:30:42-" + goods_id);
        Logger.i("2017年11月23日 15:30:42-" + act_id);
        Logger.i("2017年11月23日 15:30:42-" + type);
        Logger.i("2017年11月23日 15:30:42-" + isShowView);
        Logger.i("2017年11月23日 15:30:42-" + order_id);
        context.startActivity(intent);
    }

    /**
     * 3.7 跳转到携程酒店详情
     *
     * hotel_id //酒店id
     * bookInTime // 入住时间 时间戳字符串,没有传空
     * bookOutTime // 离店时间 时间戳字符串,没有传空
     * type // 酒店类型 1.普通房酒店 2.钟点房酒店
     */
    @JavascriptInterface
    public void jumpToXcHotelDetails(String hotel_id , String bookInTime , String bookOutTime , String type) {
        String checkIn = null;
        String checkOut = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(!TextUtils.isEmpty(bookInTime) && !TextUtils.isEmpty(bookOutTime)){
            checkIn = sdf.format( Long.parseLong(bookInTime)*1000);
            checkOut = sdf.format( Long.parseLong(bookOutTime)*1000);
        }
        if(type.equals("1")) {
            JumpCtripActivityUtils.toCtripHotelDetail(context, hotel_id, checkIn,checkOut);
        }else {

        }
    }

    /**
     * 跳转捐赠成功的页面
     * 说明: 跳转捐赠成功的页面
     *
     * @param id
     */
    @JavascriptInterface
    public void jumpToLoveDonationMessage(String id, String msgId) {
        Intent intent = new Intent(context, WelfareLoveMsgActivity.class);
        intent.putExtra("proId", id);
        intent.putExtra("msg_id", msgId);
        if (context instanceof WebViewActivity) {
            ((WebViewActivity) context).startActivityForResult(intent, 1);
        }
    }

    /**
     * 查看更多公益跳转到公益列表
     */
    @JavascriptInterface
    public void jumpToLoveDonationList() {
        SkipUtils.toWelfareList(context);
        if (context instanceof WebViewActivity) {
            ((WebViewActivity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((WebViewActivity) context).finish();
                }
            });
        }
    }

    /**
     * 跳转公益列表页面 有两个方法
     * 说明：跳转公益列表页面
     */
    @JavascriptInterface
    public void jumpToGYPage() {
        SkipUtils.toWelfareList(context);
    }

    /**
     * 获取导航条高度
     * 说明：获取系统导航条高度 返回字符串 比如: "64"
     * 方法名： getNavitaionBarH();
     *
     * @return
     */
    @JavascriptInterface
    public String getNavitaionBarH() {
        if (context instanceof WebViewActivity) {
            return String.valueOf(((WebViewActivity) context).getNavitaionBarHeight() * 2);
        }
        return "100";
    }

    /**
     * 4.1 跳转我的钱包页面
     * 说明：跳转我的钱包页面
     * 方法名： jumpToMyWalletPage();
     */
    @JavascriptInterface
    public void jumpToMyWalletPage() {
        SkipUtils.toMyPurse(context);
    }

    /**
     * 跳转集分宝页面
     * 说明：跳转集分宝页面
     * 方法名： jumpToJfbPage();
     */
    @JavascriptInterface
    public void jumpToJfbPage() {
        SkipUtils.toIntegralTreasure(context);
    }

    /**
     * 跳转信用首页页面
     * 说明：跳转信用首页页面
     * 方法名： jumpToXYPage();
     */
    @JavascriptInterface
    public void jumpToXYPage() {
        SkipUtils.toMyCredit(context);
    }

    /**
     * 跳转健康首页页面
     * 说明：跳转健康首页页面
     */
    @JavascriptInterface
    public void jumpToJkPage() {
        SkipUtils.toMyHealth(context);
    }


    /**
     * 跳转实名认证页面
     * 说明：跳转实名认证页面
     */
    @JavascriptInterface
    public void jumpToSMRZPage() {
        SkipUtils.toCertificationViewImpl(context);
    }

    /**
     * 跳转个人基本信息页面
     * 说明：跳转个人基本信息页面
     */
    @JavascriptInterface
    public void jumpToJBXXPage() {
        SkipUtils.toEssentialInformation(context);
    }

    /**
     * 跳转游戏中心页面
     * 说明：跳转游戏中心页面
     */
    @JavascriptInterface
    public void jumpToYXZXPage() {
        context.startActivity(new Intent(context, GameHomePageActivity.class));
    }

    /**
     * 9 跳转互助计划详情页面
     * 说明： 跳转互助计划详情页面
     * 参数名:
     * projectId:  项目id
     */
    @JavascriptInterface
    public void jumpToHZJHPage(String projectId) {
        SkipUtils.toHelpEach(context, projectId, "", "", "", "", "", "", "");
    }

    /**
     * 跳转我的健康果页面
     * 说明： 跳转我的健康果页面
     */
    @JavascriptInterface
    public void jumpToMyJKGPage() {
        SkipUtils.toHealthFruit(context);
    }

    /**
     * 存储数据
     *
     * @param key
     * @param value
     */
    @JavascriptInterface
    public void jumpToUniversalWithKey(String key, String value) {
        PreferenceUtils.writeStrConfig(key, value, context);
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    @JavascriptInterface
    public String getLocalValueWithKey(String key) {
        return PreferenceUtils.readStrConfig(key, context, "");
    }

    /**
     * 删除数据
     *
     * @param key
     */
    @JavascriptInterface
    public void delLocalValueWithKey(String key) {
        PreferenceUtils.RemoveStrConfig(key, context);
    }

    /**
     * 跳转商家入驻/续费 支付界面
     *
     * @param payType
     * @param merchantId
     * @param merchantType
     * @param rechargeType 0缴费 1续费
     */
    @JavascriptInterface
    public void jumpMerchantAnnualPay(String payType, String merchantId, String merchantType, String rechargeType) {
        Intent intent = new Intent(context, MerchantPayActivity.class);
        intent.putExtra("merchantPayType", rechargeType);
        context.startActivity(intent);
    }

    /**
     * 跳转添加地址页面
     */
    @JavascriptInterface
    public void jumpAddAddressPage() {
        Intent intent = new Intent(context, AddressManageActivity.class);
        intent.putExtra(AddressManageActivity.TAG, AddressManageActivity.TAG_FROM_WEB_VIEW_ACTIVITY);
        context.startActivity(intent);
    }

    /**
     * 跳转服务中心收券二维码
     */
    @JavascriptInterface
    public void jumpToGetQuanCode() {
        Intent intent = new Intent(context, MerchantExchangeQrCodeActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转服务中心待核销兑换券
     */
    @JavascriptInterface
    public void jumpToWaitVerification() {
        Intent intent = new Intent(context, MerchantTicketActivity.class);
        intent.putExtra("recordType", RecordListRetention.EXCHANGE_VERIFICATION);
        context.startActivity(intent);
    }

    /**
     * 跳转兑换券收银台
     *
     * @param orderId
     * @param express
     * @param exchange
     */
    @JavascriptInterface
    public void jumpToExchangeCashDesk(String orderId, String express, String exchange) {
        Intent intent = new Intent(context, ExchangeCashDeskActivity.class);
        intent.putExtra(ExchangeCashDeskActivity.TAG_ORDER_ID, orderId);
        intent.putExtra(ExchangeCashDeskActivity.TAG_EXPRESS, express);
        intent.putExtra(ExchangeCashDeskActivity.TAG_EXCHANGE, exchange);
        context.startActivity(intent);
    }

    /**
     * 关闭webViewActivity
     */
    @JavascriptInterface
    public void closeCurrentWebView() {
        ((WebViewActivity) context).finish();
    }
}
