package com.yilian.mylibrary;
/**
 * Created by  on 2017/3/24 0024.
 */

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by  on 2017/3/24 0024.
 * 移动端type跳转类型定义
 * 数据结构样式 {
 * "type":"",//类型
 * "content":"",//需要向下个界面传的值
 * }
 */
public class JumpToOtherPage {

    private static SharedPreferences sp;
    private static Context mContext;
    private static JumpToOtherPage jumpToOtherPage;
    String clsName = "";

    private JumpToOtherPage(Context context) {
        mContext = context;
        sp = context.getSharedPreferences(Constants.SP_FILE, Context.MODE_PRIVATE);
    }

    public static synchronized JumpToOtherPage getInstance(Context context) {
        if (jumpToOtherPage == null) {
            jumpToOtherPage = new JumpToOtherPage(context);
        }
        return jumpToOtherPage;
    }

    public void jumpToOtherPage(int type, String contents) {
        try {
            String[] content = contents.split(",");
            Intent intent = new Intent();
            switch (type) {
                case 0: //0.新闻公告活动web页面 (id : ["0","url"])
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
                    intent.putExtra(Constants.SPKEY_URL, content[0]);
                    break;
                case 1://1.外部浏览器打开web页面 (id : ["0","url"])
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(content[0]));
                    break;
                case 2://2.本地商城详情页面
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPLeFenHomeActivity"));
                    intent.putExtra("index_id", content[0]);
                    break;
                case 3://3.本地活动页面
                    switch (content[0]) {
                        case "0":
                            //本地活动
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPLocationActivity"));
                            break;
                        case "1":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.DaZhuanPanDetailActivity"));
                            break;
                        case "2":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.GuessDetailActivity"));
                            break;
                        case "3":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.GuaGuaKaActivity"));
                            break;
                        case "4":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.YaoYiYaoActivity"));
                            break;
                        case "5":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPSignActivity"));
                            break;
                        case "6":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WheelOfFortuneActivity"));
                            break;
                        default://其余类型不做任何操作
                            return;
                    }
                    intent.putExtra("activity_index", content[1]);
                    break;
                case 4://4.中奖凭证页面
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.PrizeVoucherDetailActivity"));
                    intent.putExtra("voucher_index", content[0]);
                    break;
                case 5://5.商品详情页面
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPNewCommDetailActivity"));
                    intent.putExtra("goods_id", content[0]);
                    break;
                case 6://6.商家套餐详情页面
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MTComboDetailsActivity"));
                    intent.putExtra("package_id", content[0]);
                    break;
                case 7://7.本地商家详情页面
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MTMerchantDetailActivity"));
                    intent.putExtra("merchant_id", content[0]);
                    break;
                case 8://8.旗舰店详情页面
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPFlagshipActivity"));
                    intent.putExtra("type", "1");
                    intent.putExtra("index_id", content[0]);
                    break;
                case 9://打开通用页面
                    /**
                     * {
                     打开首页(content : "0")
                     本地(content : "1")
                     购物车(content : "2")
                     我的(content : "3")
                     我的奖励(content : "4")
                     我的零购券(content : "5")
                     我的抵扣券(content : "6")
                     我的收藏(content : "7")
                     我的一一卡(icontentd : "8")
                     邀请有奖(content : "9")
                     充值中心(content : "10")
                     我的团队(content : "11")
                     扫一扫(content : "12")
                     商品搜索(content : "13")
                     套餐搜索(content : "14")
                     系统消息列表(content : "15" )
                     商家搜索(content : "16" )
                     意见反馈(content : "17" )
                     限时抢购列表(本场)(content : "18")
                     零购商城(content : "19" )
                     抵扣商城(content : "20" )
                     附近商家列表(content : "21" )
                     本地套餐列表(content : "22" )
                     }
                     */
                    switch (content[0]) {
                        case "0":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPMainActivity"));
                            intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                            break;
                        case "2":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPMainActivity"));
                            intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_SHOPPING_CART_FRAGMENT);
                            break;
                        case "3":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPMainActivity"));
                            intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                            break;
                        case "4":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.BalanceActivity"));
                            intent.putExtra("type", "vouchers");
                            break;
                        case "5":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.BalanceActivity"));
                            intent.putExtra("type", "shopping");
                            break;
                        case "6":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.BalanceActivity"));
                            intent.putExtra("type", "subsidies");
                            break;
                        case "7":
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.FavoriteActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "8":
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
                                String yyCardUrl = PreferenceUtils.readStrConfig("yyCardUrl", mContext);
                                intent.putExtra(Constants.SPKEY_URL, yyCardUrl);
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "9":
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPInvitePrizeActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "10":
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.RechargeActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "11":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MembersLevel3"));
                            break;
                        case "12":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MipcaActivityCapture"));
                            break;
                        case "13":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.SearchCommodityActivity"));
                            break;
                        case "14":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MTFindActivity"));
                            break;
                        case "15":
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.InformationActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "16":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MerchantSearchActivity"));
                            break;
                        case "17":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.FeedbackActivity"));
                            break;
                        case "18":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.FlashSaleListActivity"));
                            break;
                        case "19":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPMainActivity"));
                            intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_LING_GOU_FRAGMENT);
                            break;
                        case "20":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPMainActivity"));
                            intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_DI_KOU_FRAGMENT);
                            break;
                        case "21":
                            //送券商家
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MTNearActivity"));
                            intent.putExtra("categroy", "merchant");
                            break;
                        case "22":
                            //套餐商家列表
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MTNearActivity"));
                            intent.putExtra("categroy", "combo");
                            break;
                        case "23"://拼团列表
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.SpellGroupListActiviy"));
                            break;
                        case "24":
                            //本地生活
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPMainActivity"));
                            intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_DI_KOU_FRAGMENT);
                            break;
                        case "25":
                            //分享好友
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPInvitePrizeActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "26":
                            //申请领奖励
                            if (isLogin()) {
                                if (isCert()) {
                                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JTakeCashActivity"));
                                } else {
                                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.mvp.view.CertificationViewImplActivity"));
                                }
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "27":
                            //易划算
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MallMainActivity"));
                            intent.putExtra("title", "易划算");
                            break;
                        case "28":
                            //奖券购
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MallMainActivity"));
                            intent.putExtra("title", "奖券购");
                            break;
                        case "29":
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantCenterActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "30":
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantComboManageActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "31":
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.GameHomePageActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "32":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MTNearActivity"));
                            intent.putExtra("categroy", "merchant");
                            intent.putExtra("has_package", "1");
                            break;
                        case "33":
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.ParadiseActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "34":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.luckypurchase.activity.LuckyMainActivity"));
                            break;
                        case "35":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.ApplyAgentDialogActivity"));
                            break;
                        case "36":
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.ActHomeActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "37":
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.ActClockInActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "38":
//                            我的钱包
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.leshan.ylyj.view.activity.bankmodel.MyPurseActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "39":
//                            集分宝
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.leshan.ylyj.view.activity.Integraltreasuremodel.IntegralTreasureActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "40":
//                            信用首页
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.leshan.ylyj.view.activity.creditmodel.MyCreditActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "41":
//                            健康首页
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.leshan.ylyj.view.activity.healthmodel.HealthHomeActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "42":
//                            公益列表
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.leshan.ylyj.view.activity.welfaremodel.WelfareDonationListActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "43":
//                            实名认证
                            if (isLogin()) {
                                if (isCert()) {
                                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.mvp.view.CertificationViewImplActivity"));
                                } else {
                                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.mvp.view.CertificationResultImplViewActivity"));
                                }
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "44":
//                            个人基本信息
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.mvp.view.BasicInfomationViewImplActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "45":
                            //奖券
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.V3MoneyActivity"));
                                intent.putExtra("type", 1);
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "46":
                            //奖励
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.V3MoneyActivity"));
                                intent.putExtra("type", 0);
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "47":
                            //银行卡
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.leshan.ylyj.view.activity.bankmodel.MyBankCardsActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "48":
                            //我要开店
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
                                intent.putExtra(Constants.SPKEY_URL, content[0]);
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;

                        case "49":
                            //云图
                            if (isLogin()) {
//                                intent.setComponent(new ComponentName(mContext, ""));
                                ToastUtil.showMessage(mContext, "跳转云图");
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "51":
                            //收款码
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantQrCodeActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "52":
                            //折扣设置
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantChangeDiscountActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "53":
                            //收款记录
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantMoneyRecordActivity2"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "54":
                            //商品管理
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantManagerActivity"));
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "56":
//                            送益豆区首页
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.mvp.view.LeDouHomePageViewImplActivity"));
                            break;
                        case "57":
//                            区块算力幸运购首页
                            intent.setComponent(new ComponentName(mContext, "com.yilian.luckypurchase.activity.LuckyMainActivity"));
                            break;
                        case "58":
//                            商品分类页面
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.OnlineMallGoodsSortListActivity"));
                            break;
                        case "59":
                            //跳转京东首页
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.jd.activity.JdHomePageActivity"));
                            break;
                        case "60":
                            //苏宁政企入口
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.suning.activity.SnHomePageActivity"));
                            break;
                        case "61":
                            //携程政企入口
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ctrip.activity.CtripHomePageActivity"));
                            break;

                        case "62":
                            //购物卡京东品牌精选入口

                            break;
                        case "63":
                            //购物卡首页入口
                            JumpToOtherPageUtil.getInstance().jumToCardHomePage(mContext);
                            return;
                        default:
                            break;
                    }
                    break;
                case 10://商城订单列表页面
                    if (isLogin()) {
                        switch (content[0]) {
                            case "0":
                                Logger.i("new mine-setMallOrder00000000000000");
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MyMallOrderListActivity2"));
                                intent.putExtra("order_Type", 0);
                                break;
                            case "1":
                                Logger.i("new mine-setMallOrder111111111111111");
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MyMallOrderListActivity2"));
                                intent.putExtra("order_Type", 1);
                                break;
                            case "2":
                                Logger.i("new mine-setMallOrder2222222222222");
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MyMallOrderListActivity2"));
                                intent.putExtra("order_Type", 2);
                                break;
                            case "3":
                                Logger.i("new mine-setMallOrder33333333333333");
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MyMallOrderListActivity2"));
                                intent.putExtra("order_Type", 3);
                                break;
                            case "4":
                                Logger.i("new mine-setMallOrder44444444444444");
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MyMallOrderListActivity2"));
                                intent.putExtra("order_Type", 4);
                                break;
                            case "5":
                                Logger.i("new mine-setMallOrder5555555555555");
                                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.AfterSaleActivity"));
                                break;
                            default:
                                break;
                        }
                    } else {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                    }

                    break;
                case 11://推送提示升级app消息(不需要参数 限iOS版)
                    break;
                case 12://商城订单详情页面
                    if (isLogin()) {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPOrderActivity"));
                        intent.putExtra("order_index", content[0]);
                    } else {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                    }

                    break;
                case 13://套餐订单列表页面
                    if (isLogin()) {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MTOrderListActivity"));
                        switch (content[0]) {
                            case "0":
                                intent.putExtra("comboType", "0");
                                break;
                            case "1":
                                intent.putExtra("comboType", "1");
                                break;
                            case "2":
                                intent.putExtra("comboType", "2");
                                break;
                            case "3":
                                intent.putExtra("comboType", "3");
                                break;
                            case "4":
                                intent.putExtra("comboType", "4");
                                break;
                            default:
                                break;
                        }
                    } else {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                    }

                    break;
                case 14:
                    if (isLogin()) {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MTOrderDetailActivity"));
                        intent.putExtra("index_id", content[0]);
                    } else {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                    }

                    break;
                case 15://15.身边好货页面 (content : "filiale_id")//TODO
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.NearbyGoodsActivity"));
                    intent.putExtra("filialeId", content[0]);
                    break;
                case 16://16.商家行业分类列表页面(原送券商家) (content : "industry_id")//TODO
                    //送券商家
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MTNearActivity"));
                    intent.putExtra("mIndustryTop", content[0]);
                    intent.putExtra("categroy", "merchant");
                    break;
                case 17://17.限时抢购详情 (content :"商品id")
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.FlashSaleDetailsActivity"));
                    intent.putExtra("goods_id", content[0]);
                    break;
                case 18://18.app内web页面(需要登录) (content : "url")
                    if (isLogin()) {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
                        intent.putExtra(Constants.SPKEY_URL, content[0]);
                    } else {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                    }
                    break;
                case 19://本地商城列表/详情页面(content: "filiale_count,filiale_id") ps:count=1跳转至兑换中心详情,count>1跳转至兑换中心列表
                    if (NumberFormat.convertToInt(content[0], 0) <= 0) {
                        Toast.makeText(mContext, "本地商城暂未开放!", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (NumberFormat.convertToInt(content[0], 0) == 1) {//跳转到兑换中心详情
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPLeFenHomeActivity"));
                        intent.putExtra("type", "0");
                        intent.putExtra("index_id", content[1]);
                    } else if (NumberFormat.convertToInt(content[0], 0) > 1) {//跳转到兑换中心列表
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.FilialeListNewActivity"));
                        intent.putExtra("type", "2");//0表示全部
                    }
                    break;
                case 20://拼团商品详情(contentid:拼团活动id)
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.SpellGroupDetailsActivity"));
                    intent.putExtra("index", content[0]);
                    break;
                case 21:
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPNewCommDetailActivity"));
                    //商品类别 0 普通商品 1yhs 2jfg 3/4益豆
                    intent.putExtra("goods_id", content[0]);
                    intent.putExtra("classify", content[1]);
                    break;
                case 22:
                    if (isLogin()) {
                        intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantComboManageActivity"));
                    } else {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                    }
                    break;
                case 23:
                    if (isLogin()) {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.AfterSaleOneActivity"));
                        intent.putExtra("orderId", content[0]);
                        intent.putExtra("type", 1);
                    } else {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                    }
                    break;
                case 24:
                    if (isLogin()) {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.AfterSaleOneActivity"));
                        intent.putExtra("orderId", content[0]);
                        intent.putExtra("type", 0);
                    } else {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                    }
                    break;
                case 25:
                    if (isLogin()) {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.InformationActivity"));
                    } else {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                    }
                    break;
                case 26:
                    if (isLogin()) {
                        intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantOrderDetailsActivity"));
                        intent.putExtra("orderIndex", content[0]);
                    } else {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                    }
                    break;
                case 27:
                    if (isLogin()) {
                        intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantAfterSaleDetailsActivity"));
                        intent.putExtra(Constants.SERVICE_INDEX, content[0]);
                        intent.putExtra(Constants.SERVICE_TYPE, "0");//换货返修
                    } else {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                    }
                    break;
                case 28:
                    if (isLogin()) {
                        intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantAfterSaleDetailsActivity"));
                        intent.putExtra(Constants.SERVICE_INDEX, content[0]);
                        intent.putExtra(Constants.SERVICE_TYPE, "1");//退货
                    } else {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                    }
                    break;
                //幸运购活动详情 (content :“活动id”)
                case 29:
                    intent.setComponent(new ComponentName(mContext, "com.yilian.luckypurchase.activity.LuckyActivityDetailActivity"));
                    intent.putExtra("activity_id", content[0]);
                    intent.putExtra("type", "0");
                    break;
//                幸运购最新揭晓/我的纪录(中奖)/晒单/全部列表 (content :“0”) 0最新揭晓列表 1我的记录(中奖) 2全部晒单列表 3全部活动列表
                case 30:
                    switch (content[0]) {
                        case "0":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.luckypurchase.activity.LuckyWinningListActivity"));
                            break;
                        case "1":
                            if (isLogin()) {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.luckypurchase.activity.LuckyMyRecordActivity"));
                                intent.putExtra("current_position", 2);
                            } else {
                                intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            }
                            break;
                        case "2":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.luckypurchase.activity.LuckyAllCommentListActivity"));
                            intent.putExtra("type", "2");
                            intent.putExtra("activity_id", "0");
                            break;
                        case "3":
                            intent.setComponent(new ComponentName(mContext, "com.yilian.luckypurchase.activity.LuckyAllPrizeListActivity"));
                            break;
                        default:
                            break;
                    }
                    break;
//                幸运购单个活动晒单列表 (content :“活动id”) 活动id不为空 则为单个活动的晒单列表
                case 31:
                    intent.setComponent(new ComponentName(mContext, "com.yilian.luckypurchase.activity.LuckyAllCommentListActivity"));
                    intent.putExtra("type", "2");
                    intent.putExtra("activity_id", content[0]);
                    break;
//                    本地幸运购列表
                case 32:
                    intent.setComponent(new ComponentName(mContext, "com.yilian.luckypurchase.activity.LuckyLocalListActivity"));
                    intent.putExtra("merchantId", content[0]);
                    break;
//                    外部浏览器打开web页面 (需要登录) (id : ["0","url"])
                case 33:
                    if (isLogin()) {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(content[0]));
                    } else {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                    }
                    break;
                //商家中心订单列表页面
                case 34:
                    switch (content[0]) {
                        case "0":
                            intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantOrderListActivity"));
                            intent.putExtra("index", 3);
                            break;
                        case "1":
                            intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantOrderListActivity"));
                            intent.putExtra("index", 0);
                            break;
                        case "2":
                            intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantOrderListActivity"));
                            intent.putExtra("index", 1);
                            break;
                        case "3":
                            intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantOrderListActivity"));
                            intent.putExtra("index", 2);
                            break;
                        case "4":
                            intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantAfterSaleListActivity"));
                            break;
                        default:
                            break;
                    }
                    break;
                //现金交易
                case 35:
                    intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantOffLineDealActivity"));
                    intent.putExtra(Constants.MERCHANT_ID, content[0]);
                    break;
                //商超零售
                case 36:
                    intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantChooseDealTypeActivity"));
                    intent.putExtra(Constants.MERCHANT_ID, content[0]);
                    intent.putExtra("showText1", "条码交易");
                    intent.putExtra("showText2", "整单交易");
                    break;
                //我的钱柜 type:1线上 2线下
                case 37:
                    ToastUtil.showMessage(mContext, "跳转我的钱柜");
                    break;
                //信用通 type:1线上 2线下
                case 38:
                    intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantXytActivity"));
                    intent.putExtra(Constants.MERCHANT_ID, content[0]);
                    intent.putExtra("merchantPayType", content[1]);
                    break;
                //年费缴纳 0续费 1缴费
                case 39:
                    intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantPayActivity"));
                    intent.putExtra(Constants.MERCHANT_ID, content[0]);
                    intent.putExtra("merchantPayType", content[1]);
                    break;
                case 41:
//                    益豆区二级分类
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.mvp.view.SpecialLeDouViewImplActivity"));
                    intent.putExtra("ledou_type", content[0]);
                    intent.putExtra("ledou_type_name", content[1]);
                    break;
                case 45:
//                    京东政企品牌商品列表(content : "品牌名称")
//                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.jd.activity.JdBrandGoodsListActivity"));
//                    intent.putExtra("brand_name", content[0]);
                    JumpToOtherPageUtil.getInstance().jumToJdBrandGoodsList(mContext, content[0]);
                    return;
                case 46:
//                    京东政企商品详情
//                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.jd.activity.goodsdetail.JDGoodsDetailActivity"));
//                    intent.putExtra("JDGoodsDetailActivity", content[0]);
                    JumpToOtherPageUtil.getInstance().jumToJdGoodsDetail(mContext, content[0], JumpToOtherPageUtil.JD_GOODS_TYPE_COMMON);
                    return;
                case 47:
//                    京东政企三级分类商品列表
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.jd.activity.JdThirdClassifyGoodsListActivity"));
                    intent.putExtra("catId", content[1]);
                    intent.putExtra("title", content[0]);
                    break;
                case 48:
//                    苏宁政企品牌商品列表(content : "品牌名称")
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.suning.activity.SnBrandGoodsListActivity"));
                    intent.putExtra("brand_name", content[0]);
                    break;
                case 49:
//                    苏宁政企商品详情(content : "商品id (sku)")
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.suning.activity.goodsdetail.SnGoodsDetailActivity"));
                    intent.putExtra("SnGoodsDetailActivity", content[0]);
                    break;
                case 50:
//                    苏宁政企三级分类商品列表(content : "分级分类名称,三级分类id") 注意 逗号分隔
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.suning.activity.SnThirdClassifyGoodsListActivity"));
                    intent.putExtra("catId", content[1]);
                    intent.putExtra("title", content[0]);
                    break;
                case 51:
                    // 携程政企普通房型酒店详情(content : "酒店ID,入住时间戳(没有传空),离店时间戳(没有传空)") 注意 逗号分隔
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ctrip.activity.CtripHotelDetailActivity"));
                    intent.putExtra("tag_hotel_id", content[0]);
                    //**********处理 入住 离店 时间*******************
                    String checkIn,checkOut;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    if(!TextUtils.isEmpty(content[1]) && !TextUtils.isEmpty(content[2])){
                        checkIn = sdf.format( Integer.parseInt(content[1])*1000);
                        checkOut = sdf.format( Integer.parseInt(content[2])*1000);
                    }else {
                        checkIn = sdf.format(new Date(System.currentTimeMillis()));
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        checkOut = sdf.format(calendar.getTime());
                    }
                    intent.putExtra("checkIn", checkIn);
                    intent.putExtra("checkOut", checkOut);
                    break;
                case 52:
                    // 携程政企钟点房型酒店详情(content : "酒店ID,入住时间戳(没有传空),离店时间戳(没有传空)") 注意 逗号分隔
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ctrip.activity.CtripHotelDetailActivity"));
                    intent.putExtra("tag_hotel_id", content[0]);
                    //**********处理 入住 离店 时间*******************
                    String checkInByHour,checkOutByHour;
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    if(!TextUtils.isEmpty(content[1]) && !TextUtils.isEmpty(content[2])){
                        checkInByHour = sdf1.format( Integer.parseInt(content[1])*1000);
                        checkOutByHour = sdf1.format( Integer.parseInt(content[2])*1000);
                    }else {
                        checkInByHour = sdf1.format(new Date(System.currentTimeMillis()));
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        checkOutByHour = sdf1.format(calendar.getTime());
                    }
                    intent.putExtra("checkIn", checkInByHour);
                    intent.putExtra("checkOut", checkOutByHour);
                    break;
                case 53:
                    //购物卡 京东三级分类商品列表(content : "分级分类名称,三级分类id") 注意 逗号分隔
                    JumpToOtherPageUtil.getInstance().jumToCardJdGoodsList(mContext, content[1], content[0]);
                    break;
                case 54:
                    //购物卡 京东商品详情(content : "商品id (sku)")
                    JumpToOtherPageUtil.getInstance().jumToJdGoodsDetail(mContext, content[0], JumpToOtherPageUtil.JD_GOODS_TYPE_CARD);
                    return;
                case 55:
                    //购物卡 京东品牌商品列表(content : "品牌名称")
                    JumpToOtherPageUtil.getInstance().jumToCardJdBrandGoodsList(mContext, content[0]);
                    return;
                case 56:
                    //跳转购物卡页面
                    /**
                     *  content [1,0] 未关联
                     *  content[2,0001006] 已经关联卡号
                     */
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.shoppingcard.activity.CardMyCardActivity"));
                    switch (content[0]) {
                        case "1":
                            intent.putExtra("cardNumber", "");
                            break;
                        case "2":
                            intent.putExtra("cardNumber", content[1]);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
            if (intent == null) {
                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPMainActivity"));
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
            }
            mContext.startActivity(intent);
        } catch (Exception e) {
            Logger.i("跳转页面时发生了异常");
//            如果发生异常则跳转到首页
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPMainActivity"));
            intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
            mContext.startActivity(intent);
            e.printStackTrace();
        }

    }

    public static boolean isLogin() {
        return sp.getBoolean(Constants.SPKEY_STATE, false);
    }

    private static boolean isCert() {
        String isCert = PreferenceUtils.readStrConfig(Constants.IS_CERT, mContext);
        if ("1".equals(isCert)) {
            return true;
        }
        return false;
    }
}
