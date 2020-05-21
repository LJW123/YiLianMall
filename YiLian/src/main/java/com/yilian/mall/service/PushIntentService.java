package com.yilian.mall.service;

import android.content.Context;
import android.content.Intent;
import android.os.Message;

import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.BindAliasCmdMessage;
import com.igexin.sdk.message.FeedbackCmdMessage;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.igexin.sdk.message.SetTagCmdMessage;
import com.igexin.sdk.message.UnBindAliasCmdMessage;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.luckypurchase.activity.LuckyActivityDetailActivity;
import com.yilian.luckypurchase.activity.LuckyAllCommentListActivity;
import com.yilian.luckypurchase.activity.LuckyAllPrizeListActivity;
import com.yilian.luckypurchase.activity.LuckyMainActivity;
import com.yilian.luckypurchase.activity.LuckyMyRecordActivity;
import com.yilian.luckypurchase.activity.LuckyWinPopWindowActivity;
import com.yilian.luckypurchase.activity.LuckyWinningListActivity;
import com.yilian.mall.MyApplication;
import com.yilian.mall.R;
import com.yilian.mall.entity.PushMsg;
import com.yilian.mall.ui.ActClockInActivity;
import com.yilian.mall.ui.AfterSaleActivity;
import com.yilian.mall.ui.AfterSaleOneActivity;
import com.yilian.mall.ui.DaZhuanPanDetailActivity;
import com.yilian.mall.ui.FavoriteActivity;
import com.yilian.mall.ui.GameHomePageActivity;
import com.yilian.mall.ui.GuaGuaKaActivity;
import com.yilian.mall.ui.GuessDetailActivity;
import com.yilian.mall.ui.InformationActivity;
import com.yilian.mall.ui.JPFlagshipListActivity;
import com.yilian.mall.ui.JPInvitePrizeActivity;
import com.yilian.mall.ui.JPLeFenHomeActivity;
import com.yilian.mall.ui.JPMainActivity;
import com.yilian.mall.ui.JPNewCommDetailActivity;
import com.yilian.mall.ui.JPOrderActivity;
import com.yilian.mall.ui.JPSignActivity;
import com.yilian.mall.ui.JTakeCashActivity;
import com.yilian.mall.ui.MTComboDetailsActivity;
import com.yilian.mall.ui.MTFindActivity;
import com.yilian.mall.ui.MTMerchantDetailActivity;
import com.yilian.mall.ui.MTNearActivity;
import com.yilian.mall.ui.MTOrderDetailActivity;
import com.yilian.mall.ui.MTOrderListActivity;
import com.yilian.mall.ui.MallMainActivity;
import com.yilian.mall.ui.MembersLevel3;
import com.yilian.mall.ui.MerchantSearchActivity;
import com.yilian.mall.ui.MipcaActivityCapture;
import com.yilian.mall.ui.MyMallOrderListActivity2;
import com.yilian.mall.ui.OnlineMallGoodsSortListActivity;
import com.yilian.mall.ui.PrizeVoucherDetailActivity;
import com.yilian.mall.ui.RechargeActivity;
import com.yilian.mall.ui.SearchCommodityActivity;
import com.yilian.mall.ui.V3MoneyActivity;
import com.yilian.mall.ui.WebViewActivity;
import com.yilian.mall.ui.YaoYiYaoActivity;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.SpeakUtills;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.activity.MerchantAfterSaleDetailsActivity;
import com.yilianmall.merchant.activity.MerchantCenterActivity;
import com.yilianmall.merchant.activity.MerchantComboManageActivity;
import com.yilianmall.merchant.activity.MerchantOrderDetailsActivity;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

//import com.baidu.tts.client.SpeechSynthesizer;
//import com.baidu.tts.client.TtsMode;


/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class PushIntentService extends GTIntentService {

    private static final String TAG = "PushIntentService  ";

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        Logger.e(TAG + "onReceiveServicePid -> " + "pid = " + pid);
    }

    @Override //获取的pid进行保存
    public void onReceiveClientId(Context context, String clientid) {
        Logger.e(TAG + "onReceiveClientId > " + "clientid = " + clientid);
        PreferenceUtils.writeStrConfig(Constants.SPKEY_CLIENTID, clientid, this);
        Logger.i(TAG + "存储pushId:" + clientid);
        sendMessage(clientid, 1);
    }

    @Override //接受推送的内容
    public void onReceiveMessageData(Context mContext, GTTransmitMessage msg) {
        Logger.i("接收到的pushMsg:" + msg.getMessageId() + "msg [ msg.getMessageId():" + msg.getMessageId()
                + "  msg.getPayloadId():" + msg.getPayloadId() + "  msg.getTaskId():" + msg.getTaskId() + "  msg.getPayload():" + new String(msg.getPayload()));
        String appid = msg.getAppid();
        String taskId = msg.getTaskId();
        String messageId = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkgName = msg.getPkgName();
        String clientId = msg.getClientId();

        //第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result = PushManager.getInstance().sendFeedbackMessage(mContext, taskId, messageId, 90001);
        Logger.i(TAG + " IntentService++call =  " + (result ? "success" : "failed"));

        Logger.i(TAG + " IntentService++onReceiveMessageData ->  " + "appid = " + appid + "\ntaskid " + taskId + "\nmessage" +
                messageId + "\npayload " + payload + "\npkgName " + pkgName + "\nclientid " + clientId);

        if (payload == null) {
            Logger.i(TAG + " receiver payload ==null ");
        } else {
            String data = new String(payload);
            Logger.i(TAG + "接收到数据receivee payload  " + data);
            PushMsg pushMsg = new Gson().fromJson(data, PushMsg.class);
            List<String> content = pushMsg.id;
            Intent intent = null;
            Logger.i(TAG + "PushMsg type " + pushMsg.type + " PushMsg.id " + pushMsg.id.toString());
            Logger.i("登录状态：" + isLogin());
            switch (pushMsg.type) {
                case "25":
                    Logger.i(TAG + " pushMsg.typeX1:" + pushMsg.type);
                    break;
                default:
                    Logger.i(TAG + " pushMsg.typeX2:" + pushMsg.type);
                    break;
            }
            switch (pushMsg.type) {
                case "0":
                    Logger.i(TAG + "推送消息走了这里0");
                    intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("url", content.get(1));
                    intent.putExtra("title_name", pushMsg.title);
                    break;
                case "1":
                    Logger.i(TAG + "推送消息走了这里1");
                    intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("url", content.get(1));
                    intent.putExtra("title_name", pushMsg.title);
                    break;
                case "2":
                    Logger.i(TAG + "推送消息走了这里2");
                    intent = new Intent(mContext, JPLeFenHomeActivity.class);
                    intent.putExtra("type", content.get(0));
                    intent.putExtra("index_id", content.get(1));
                    break;
                case "3": { //本地互动页面
                    Logger.i(TAG + "推送消息走了这里3");
                    switch (content.get(0)) {
                        case "1":
                            Logger.i(TAG + "推送消息走了这里3_1");
                            intent = new Intent(mContext, DaZhuanPanDetailActivity.class);
                            break;
                        case "2":
                            Logger.i(TAG + "推送消息走了这里3_2");
                            intent = new Intent(mContext, GuessDetailActivity.class);
                            break;
                        case "3":
                            Logger.i(TAG + "推送消息走了这里3_3");
                            intent = new Intent(mContext, GuaGuaKaActivity.class);
                            break;
                        case "4":
                            Logger.i(TAG + "推送消息走了这里3_4");
                            intent = new Intent(mContext, YaoYiYaoActivity.class);
                            break;
                        case "5":
                            Logger.i(TAG + "推送消息走了这里3_5");
                            intent = new Intent(mContext, JPSignActivity.class);
                            break;
                        default:
                            Logger.i(TAG + "推送消息走了这里3_default");
                            return;
                    }
                    Logger.i(TAG + "推送消息走了这里3_end");
                    intent.putExtra("activity_index", content.get(1));
                }
                break;
                case "4": //中奖凭证
                    Logger.i(TAG + "推送消息走了这里4");
                    intent = new Intent(mContext, PrizeVoucherDetailActivity.class);
                    intent.putExtra("voucher_index", content.get(1));
                    break;
                case "5"://商品详情
                    Logger.i(TAG + "推送消息走了这里5");
                    String str = content.get(1);
                    String filialeId = str.split(",")[0];
                    String goodsId = str.split(",")[1];
                    intent = new Intent(mContext, JPNewCommDetailActivity.class);
                    intent.putExtra("filiale_id", filialeId);
                    intent.putExtra("goods_id", goodsId);
                    break;
                case "6"://商家套餐详情界面
                    Logger.i(TAG + "推送消息走了这里6");
                    intent = new Intent(mContext, MTComboDetailsActivity.class);
                    intent.putExtra("package_id", content.get(1));
                    break;
                case "7"://本地商家详情界面
                    Logger.i(TAG + "推送消息走了这里7");
                    intent = new Intent(mContext, MTMerchantDetailActivity.class);
                    intent.putExtra("merchant_id", content.get(1));
                    break;
                case "8"://旗舰店详情界面
                    Logger.i(TAG + "推送消息走了这里8");
                    intent = new Intent(mContext, JPFlagshipListActivity.class);
                    intent.putExtra("type", "1");
                    intent.putExtra("index_id", content.get(1));
                    break;
                case "9":
                    Logger.i(TAG + "推送消息走了这里9");
                    switch (content.get(0)) {
                        case "0":
                            Logger.i(TAG + "推送消息走了这里9_0");
                            intent = new Intent(mContext, JPMainActivity.class);
                            intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                            break;
                        case "2":
                            Logger.i(TAG + "推送消息走了这里9_2");
                            intent = new Intent(mContext, JPMainActivity.class);
                            intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_SHOPPING_CART_FRAGMENT);
                            break;
                        case "3":
                            Logger.i(TAG + "推送消息走了这里9_3");
                            intent = new Intent(mContext, JPMainActivity.class);
                            intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                            break;
                        case "4":
                            Logger.i(TAG + "推送消息走了这里9_4");
                            intent = new Intent(mContext, V3MoneyActivity.class);
                            intent.putExtra("type", V3MoneyActivity.TYPE_MONEY);
                            break;
                        case "5"://暂时改成奖券页面跳转 iOS不支持 2017年8月21日16:56:42修改
                            Logger.i(TAG + "推送消息走了这里9_5");
                            intent = new Intent(mContext, V3MoneyActivity.class);
                            intent.putExtra("type", V3MoneyActivity.TYPE_INTEGRAL);
                            break;
                        case "6"://暂时改成奖券页面跳转 iOS不支持 2017年8月21日16:56:42修改
                            Logger.i(TAG + "推送消息走了这里9_6");
                            intent = new Intent(mContext, V3MoneyActivity.class);
                            intent.putExtra("type", V3MoneyActivity.TYPE_INTEGRAL);
                            break;
                        case "7":
                            Logger.i(TAG + "推送消息走了这里9_7");
                            intent = new Intent(mContext, FavoriteActivity.class);
                            break;
                        case "8":
                            Logger.i(TAG + "推送消息走了这里9_8");
                            intent = new Intent(mContext, WebViewActivity.class);
                            intent.putExtra(Constants.SPKEY_URL, PreferenceUtils.readStrConfig("yyCardUrl", mContext, ""));
                            break;
                        case "9":
                            Logger.i(TAG + "推送消息走了这里9_9");
                            if (isLogin()) {
                                intent = new Intent(mContext, JPInvitePrizeActivity.class);
                            } else {
                                intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                            }
                            break;
                        case "10":
                            Logger.i(TAG + "推送消息走了这里9_10");
//                            intent = new Intent(mContext, NMemberChargeActivity.class);
//                            intent.putExtra("type", "chooseCharge");
                            intent = new Intent(mContext, RechargeActivity.class);
                            break;
                        case "11":
                            Logger.i(TAG + "推送消息走了这里9_11");
                            intent = new Intent(mContext, MembersLevel3.class);
                            break;
                        case "12":
                            Logger.i(TAG + "推送消息走了这里9_12");
                            if (isLogin()) {
                                intent = new Intent(mContext, MipcaActivityCapture.class);
                            } else {
                                intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                            }
                            break;
                        case "13":
                            Logger.i(TAG + "推送消息走了这里9_13");
                            intent = new Intent(mContext, SearchCommodityActivity.class);
                            break;
                        case "14":
                            Logger.i(TAG + "推送消息走了这里9_14");
                            intent = new Intent(mContext, MTFindActivity.class);
                            break;
                        case "15":
                            Logger.i(TAG + "推送消息走了这里9_15");
                            if (isLogin()) {
                                intent = new Intent(mContext, InformationActivity.class);
                            } else {
                                intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                            }
                            break;
                        case "16":
                            Logger.i(TAG + "推送消息走了这里9_16");
                            intent = new Intent(mContext, MerchantSearchActivity.class);
                            break;
                        case "17"://意见反馈
                            Logger.i(TAG + "推送消息走了这里9_17");

                            break;
                        case "21":
                            Logger.i(TAG + "推送消息走了这里9_21");
                            intent = new Intent(mContext, JPMainActivity.class);
                            intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_DI_KOU_FRAGMENT);
                            break;
                        case "24":
                            Logger.i(TAG + "推送消息走了这里9_24");
                            intent = new Intent(mContext, JPMainActivity.class);
                            intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_DI_KOU_FRAGMENT);
                            break;
                        case "25":
                            Logger.i(TAG + "推送消息走了这里9_25");
                            if (isLogin()) {
                                intent = new Intent(mContext, JPInvitePrizeActivity.class);
                            } else {
                                intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                            }
                            break;
                        case "26":
                            Logger.i(TAG + "推送消息走了这里9_26");
                            if (isLogin()) {
                                intent = new Intent(mContext, JTakeCashActivity.class);
                            } else {
                                intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                            }
                            break;
                        case "27":
                            Logger.i(TAG + "推送消息走了这里9_27");
                            intent = new Intent(mContext, MallMainActivity.class);
                            intent.putExtra("type", "1");
                            break;
                        case "28":
                            Logger.i(TAG + "推送消息走了这里9_28");
                            intent = new Intent(mContext, MallMainActivity.class);
                            intent.putExtra("type", "2");
                            break;
                        case "29":
                            Logger.i(TAG + "推送消息走了这里9_29");
                            if (isLogin()) {
                                intent = new Intent(mContext, MerchantCenterActivity.class);
                            } else {
                                intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                            }
                            break;
                        case "30":
                            Logger.i(TAG + "推送消息走了这里9_30");
                            if (isLogin()) {
                                intent = new Intent(mContext, MerchantComboManageActivity.class);
                            } else {
                                intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                            }
                            break;
                        case "31":
                            Logger.i(TAG + "推送消息走了这里9_31");
                            if (isLogin()) {
                                intent = new Intent(mContext, GameHomePageActivity.class);
                            } else {
                                intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                            }
                            break;
                        case "32":
                            intent = new Intent(mContext, MTNearActivity.class);
                            intent.putExtra("categroy", "merchant");
                            intent.putExtra("has_package", "1");
                            break;
                        case "34":
                            intent = new Intent(mContext, LuckyMainActivity.class);
                            break;
                        case "37":
                            if (pushMsg.pushType == 0) {
//                                通知 奖券
                                intent = new Intent(mContext, V3MoneyActivity.class);
                                intent.putExtra("type", V3MoneyActivity.TYPE_INTEGRAL);
                            } else {
                                if (PreferenceUtils.readBoolConfig(Constants.ON_OFF_VOICE, mContext, false)) {
                                    //科大讯飞语音合成
                                    SpeakUtills.getInstance(mContext).initSpeeckSynthesizer().speak(pushMsg.content);
                                }
                            }
                            break;
                        case "38":
                            if (pushMsg.pushType == 0) {
//                                通知  奖励
                                intent = new Intent(mContext, V3MoneyActivity.class);
                                intent.putExtra("type", V3MoneyActivity.TYPE_MONEY);
                            } else {
                                if (PreferenceUtils.readBoolConfig(Constants.ON_OFF_VOICE, mContext, false)) {
                                    //科大讯飞语音合成
                                    SpeakUtills.getInstance(mContext).initSpeeckSynthesizer().speak(pushMsg.content);
                                }
                            }
                            break;
                        case "58":
                            //跳转商品分类列表
                            intent=new Intent(mContext, OnlineMallGoodsSortListActivity.class);
                            break;
                        default:
                            Logger.i(TAG + "推送消息走了这里9_end1");
                            break;
                    }
                    Logger.i(TAG + "推送消息走了这里9_end2");
                    break;
                case "10":
                    Logger.i(TAG + "推送消息走了这里10");
                    if (isLogin()) {
                        switch (content.get(0)) {
                            case "0":
                                Logger.i(TAG + "推送消息走了这里10_0");
                                intent = new Intent(mContext, MyMallOrderListActivity2.class);
                                intent.putExtra("order_Type", "0");
                                break;
                            case "1":
                                Logger.i(TAG + "推送消息走了这里10_1");
                                intent = new Intent(mContext, MyMallOrderListActivity2.class);
                                intent.putExtra("order_Type", "1");
                                break;
                            case "2":
                                Logger.i(TAG + "推送消息走了这里10_2");
                                intent = new Intent(mContext, MyMallOrderListActivity2.class);
                                intent.putExtra("order_Type", "2");
                                break;
                            case "3":
                                Logger.i(TAG + "推送消息走了这里10_3");
                                intent = new Intent(mContext, MyMallOrderListActivity2.class);
                                intent.putExtra("order_Type", "3");
                                break;
                            case "4":
                                Logger.i(TAG + "推送消息走了这里10_4");
                                intent = new Intent(mContext, MyMallOrderListActivity2.class);
                                intent.putExtra("order_Type", "4");
                                break;
                            case "5":
                                Logger.i(TAG + "推送消息走了这里10_5");
                                intent = new Intent(mContext, AfterSaleActivity.class);
                                break;
                            default:
                                Logger.i(TAG + "推送消息走了这里10_end");
                                break;
                        }
                    } else {
                        intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                    }
                    break;
                case "12"://商城订单详情页面
                    Logger.i(TAG + "推送消息走了这里12");
                    intent = new Intent(mContext, JPOrderActivity.class);
                    intent.putExtra("order_index", content.get(1));
                    break;
                case "13"://套餐订单列表页面
                    Logger.i(TAG + "推送消息走了这里13");
                    intent = new Intent(mContext, MTOrderListActivity.class);
                    switch (content.get(0)) {
                        case "0":
                            Logger.i(TAG + "推送消息走了这里13_0");
                            intent.putExtra("comboType", 0);
                            break;
                        case "1":
                            Logger.i(TAG + "推送消息走了这里13_1");
                            intent.putExtra("comboType", 1);
                            break;
                        case "2":
                            Logger.i(TAG + "推送消息走了这里13_2");
                            intent.putExtra("comboType", 2);
                            break;
                        case "3":
                            Logger.i(TAG + "推送消息走了这里13_3");
                            intent.putExtra("comboType", 3);
                            break;
                        case "4":
                            Logger.i(TAG + "推送消息走了这里13_4");
                            intent.putExtra("comboType", 4);
                            break;
                        default:
                            break;
                    }
                    break;
                case "14":
                    Logger.i(TAG + "推送消息走了这里14");
                    intent = new Intent(mContext, MTOrderDetailActivity.class);
                    intent.putExtra("index_id", content.get(1));
                    break;
                case "18": //0.新闻公告活动web页面 (id : ["0","url"])//不需要登录
                    Logger.i(TAG + "推送消息走了这里18");
                    if (isLogin()) {
                        intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra("url", content.get(1));
                        intent.putExtra("title_name", pushMsg.title);
                    } else {
                        intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                    }
                    break;
                case "19":
                    Logger.i(TAG + "推送消息走了这里19");
                    break;
                case "20":
                    Logger.i(TAG + "推送消息走了这里20");
                    break;
                case "21":
                    Logger.i(TAG + "推送消息走了这里21");
                    intent = new Intent(mContext, JPNewCommDetailActivity.class);
                    intent.putExtra("goods_id", content.get(1));
                    intent.putExtra("classify", content.get(2));
                    break;
                case "22":
                    Logger.i(TAG + "推送消息走了这里22" + " isLogin()" + isLogin());
                    if (isLogin()) {
                        intent = new Intent(mContext, MerchantComboManageActivity.class);
                    } else {
                        intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                    }
                    break;
                case "23":
                    Logger.i(TAG + "推送消息走了这里23、24");
                    if (isLogin()) {
                        intent = new Intent(mContext, AfterSaleOneActivity.class);
                        intent.putExtra("orderId", content.get(1));
                        intent.putExtra("type", 1);
                    } else {
                        intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                    }
                    break;
                case "24":
                    Logger.i(TAG + "推送消息走了这里23、24");
                    if (isLogin()) {
                        intent = new Intent(mContext, AfterSaleOneActivity.class);
                        intent.putExtra("orderId", content.get(1));
                        intent.putExtra("type", 0);
                    } else {
                        intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                    }
                    break;
                case "25":
                    Logger.i(TAG + "推送消息走了这里25" + " isLogin()" + isLogin());
                    if (isLogin()) { //系统消息
                        Logger.i(TAG + "推送消息走了这里25_1：" + pushMsg.type);
                        intent = new Intent(mContext, InformationActivity.class);
                    } else {
                        Logger.i(TAG + "推送消息走了这里25_2：" + pushMsg.type);
                        intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                    }
                    break;
//                商家中心订单详情 (id : [“0”,“订单id”]);
                case "26":
                    Logger.i(TAG + "推送消息走了这里26" + " isLogin()" + isLogin());
                    if (isLogin()) {
                        Logger.i(TAG + "推送消息走了这里26_1：" + pushMsg.type);

                        intent = new Intent(mContext, MerchantOrderDetailsActivity.class);
                        intent.putExtra("orderIndex", content.get(1));
                    } else {
                        Logger.i(TAG + "推送消息走了这里25_2：" + pushMsg.type);
                        intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                    }
                    break;
//                商家中心换修货售后订单详情(id : [“0”,“订单id”])
                case "27":
                    if (isLogin()) {
                        intent = new Intent(mContext, MerchantAfterSaleDetailsActivity.class);
                        intent.putExtra(Constants.SERVICE_INDEX, content.get(1));
                        intent.putExtra(Constants.SERVICE_TYPE, "0");//换货返修
                    } else {
                        intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                    }
                    break;
//                商家中心退货售后订单详情 (id : [“0”,“订单id”])
                case "28":
                    if (isLogin()) {
                        intent = new Intent(mContext, MerchantAfterSaleDetailsActivity.class);
                        intent.putExtra(Constants.SERVICE_INDEX, content.get(1));
                        intent.putExtra(Constants.SERVICE_TYPE, "1");//退货
                    } else {
                        intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                    }
                    break;
                //幸运购活动详情 (id : [“0”,“活动id”])
                case "29":
                    if (isLogin()) {
                        intent = new Intent(mContext, LuckyActivityDetailActivity.class);
                        intent.putExtra("activity_id", content.get(1));
                        intent.putExtra("type", "0");
                    } else {
                        intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                    }
                    break;
//                幸运购最新揭晓/我的纪录(中奖)/晒单/全部列表 (id : [“0”,“type”]) 0最新揭晓列表 1我的记录(中奖) 2全部晒单列表 3全部活动列表
                case "30":
                    switch (content.get(1)) {
                        case "0":
                            intent = new Intent(mContext, LuckyWinningListActivity.class);
                            break;
                        case "1":
                            if (isLogin()) {
                                intent = new Intent(mContext, LuckyMyRecordActivity.class);
                                intent.putExtra("current_position", 2);
                            } else {
                                intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                            }
                            break;
                        case "2":
                            intent = new Intent(mContext, LuckyAllCommentListActivity.class);
                            intent.putExtra("type", "2");
                            intent.putExtra("activity_id", "0");
                            break;
                        case "3":
                            intent = new Intent(mContext, LuckyAllPrizeListActivity.class);
                            break;
                        default:
                            break;
                    }
                    break;
                //                幸运购单个活动晒单列表 (content :“活动id”) 活动id不为空 则为单个活动的晒单列表
                case "31":
                    intent = new Intent(mContext, LuckyAllCommentListActivity.class);
                    intent.putExtra("type", "2");
                    intent.putExtra("activity_id", content.get(1));
                    break;
//                32.幸运购中奖给中奖人推送消息 跳转弹窗显示信息 (id : [“0”,“活动id”]) 活动id不为空字符串 调用mall.php?c=snatch/snatch_award_message 接口获取信息
                case "32":
                    Boolean isForeGround = PreferenceUtils.readBoolConfig(Constants.APP_IS_FOREGROUND, mContext, false);
                    Logger.i("isForeGround:" + isForeGround);
                    String activityId = content.get(1);
                    if (isForeGround) {
                        ThreadUtil.getThreadPollProxy().execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                    Intent intent = new Intent(mContext, LuckyWinPopWindowActivity.class);
                                    intent.putExtra("activityId", activityId);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    AppManager.getInstance().getTopActivity().overridePendingTransition(R.anim.library_module_dialog_enter, android.R.anim.fade_out);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    } else {
                        PreferenceUtils.writeStrConfig(Constants.APP_LUCKY_PRIZE_ACTIVITY_ID, activityId, mContext);
                    }
                    return;
                case "34":
                case "33":
//                      打卡活动中 求赏给被求赏人的推送消息 弹窗显示 (id : [“0”,“求赏id”]) 调用mall.php?c=seek_reward_info 接口获取 求赏人名称 求赏金额 信息
                    String rewardId = content.get(1);
                    String type = content.get(0);
                    intent = new Intent(mContext, ActClockInActivity.class);
                    intent.putExtra("reward_id", rewardId);
                    intent.putExtra("type", type);
                    break;
                default:
                    Logger.i(TAG + "推送消息走了这里default");
                    intent = new Intent(mContext, JPMainActivity.class);
                    intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                    break;
            }
            Logger.i(TAG + "推送消息走了这里 TWO：" + pushMsg.type);
            intent.putExtra("notice", true);
            intent.putExtra("push_msg", pushMsg);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent != null) {
                if (!"0".equals(pushMsg.pushId)) {
                    countPushInfo(mContext, pushMsg.pushId, intent);
                } else {
                    startActivity(intent);
                }
            }

        }
    }

    private CompositeSubscription compositeSubscription;

    public void addSubscription(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);

    }

    @SuppressWarnings("unchecked")
    void countPushInfo(Context context, String pushId, Intent intent) {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .countPushInfo("activity_click_into", pushId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer() {
                    @Override
                    public void onCompleted() {
                        startmIntent(context, intent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        startmIntent(context, intent);
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
        addSubscription(subscription);
    }

    /**
     * 推送事件点击后处理并解除注册
     *
     * @param context
     * @param intent
     */
    private void startmIntent(Context context, Intent intent) {
        context.startActivity(intent);
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void onDestroy() {
        Logger.i("");
        super.onDestroy();

    }

    private boolean isLogin() {
        boolean state = false;
        try {
            state = PreferenceUtils.readBoolConfig(Constants.SPKEY_STATE, this, false);
        } catch (Exception e) {
            Logger.i("获取登录状态时异常:" + e.getMessage());
            e.printStackTrace();
        } finally {
            Logger.i("获取了登录状态" + state);
        }
        return state;
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        Logger.e(TAG + "onReceiveOnlineState -> " + "online = " + online);
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {

        int action = cmdMessage.getAction();

        if (action == PushConsts.SET_TAG_RESULT) {
            setTagResult((SetTagCmdMessage) cmdMessage);
        } else if (action == PushConsts.BIND_ALIAS_RESULT) {//绑定别名回调
            bindAliasResult((BindAliasCmdMessage) cmdMessage);
        } else if (action == PushConsts.UNBIND_ALIAS_RESULT) {//解绑别名回调
            unbindAliasResult((UnBindAliasCmdMessage) cmdMessage);
        } else if ((action == PushConsts.THIRDPART_FEEDBACK)) {
            feedbackResult((FeedbackCmdMessage) cmdMessage);
        }
    }

    private void bindAliasResult(BindAliasCmdMessage bindAliasCmdMessage) {
        String sn = bindAliasCmdMessage.getSn();
        String code = bindAliasCmdMessage.getCode();


        int text = R.string.bind_alias_unknown_exception;
        switch (Integer.valueOf(code)) {
            case PushConsts.BIND_ALIAS_SUCCESS:
                text = R.string.bind_alias_success;
                break;
            case PushConsts.ALIAS_ERROR_FREQUENCY:
                text = R.string.bind_alias_error_frequency;
                break;
            case PushConsts.ALIAS_OPERATE_PARAM_ERROR:
                text = R.string.bind_alias_error_param_error;
                break;
            case PushConsts.ALIAS_REQUEST_FILTER:
                text = R.string.bind_alias_error_request_filter;
                break;
            case PushConsts.ALIAS_OPERATE_ALIAS_FAILED:
                text = R.string.bind_alias_unknown_exception;
                break;
            case PushConsts.ALIAS_CID_LOST:
                text = R.string.bind_alias_error_cid_lost;
                break;
            case PushConsts.ALIAS_CONNECT_LOST:
                text = R.string.bind_alias_error_connect_lost;
                break;
            case PushConsts.ALIAS_INVALID:
                text = R.string.bind_alias_error_alias_invalid;
                break;
            case PushConsts.ALIAS_SN_INVALID:
                text = R.string.bind_alias_error_sn_invalid;
                break;
            default:
                break;
        }
        Logger.i("bindAlias result sn = " + sn + ", code = " + code + ", text = " + getResources().getString(text));
    }

    private void unbindAliasResult(UnBindAliasCmdMessage unBindAliasCmdMessage) {
        String sn = unBindAliasCmdMessage.getSn();
        String code = unBindAliasCmdMessage.getCode();

        int text = R.string.unbind_alias_unknown_exception;
        switch (Integer.valueOf(code)) {
            case PushConsts.UNBIND_ALIAS_SUCCESS:
                text = R.string.unbind_alias_success;
                break;
            case PushConsts.ALIAS_ERROR_FREQUENCY:
                text = R.string.unbind_alias_error_frequency;
                break;
            case PushConsts.ALIAS_OPERATE_PARAM_ERROR:
                text = R.string.unbind_alias_error_param_error;
                break;
            case PushConsts.ALIAS_REQUEST_FILTER:
                text = R.string.unbind_alias_error_request_filter;
                break;
            case PushConsts.ALIAS_OPERATE_ALIAS_FAILED:
                text = R.string.unbind_alias_unknown_exception;
                break;
            case PushConsts.ALIAS_CID_LOST:
                text = R.string.unbind_alias_error_cid_lost;
                break;
            case PushConsts.ALIAS_CONNECT_LOST:
                text = R.string.unbind_alias_error_connect_lost;
                break;
            case PushConsts.ALIAS_INVALID:
                text = R.string.unbind_alias_error_alias_invalid;
                break;
            case PushConsts.ALIAS_SN_INVALID:
                text = R.string.unbind_alias_error_sn_invalid;
                break;
            default:
                break;
        }
        Logger.i("unbindAlias result sn = " + sn + ", code = " + code + ", text = " + getResources().getString(text));
    }

    private void setTagResult(SetTagCmdMessage setTagCmdMsg) {
        String sn = setTagCmdMsg.getSn();
        String code = setTagCmdMsg.getCode();

        String text = "设置标签失败, 未知异常";
        switch (Integer.valueOf(code)) {
            case PushConsts.SETTAG_SUCCESS:
                text = "设置标签成功";
                break;

            case PushConsts.SETTAG_ERROR_COUNT:
                text = "设置标签失败, tag数量过大, 最大不能超过200个";
                break;

            case PushConsts.SETTAG_ERROR_FREQUENCY:
                text = "设置标签失败, 频率过快, 两次间隔应大于1s且一天只能成功调用一次";
                break;

            case PushConsts.SETTAG_ERROR_REPEAT:
                text = "设置标签失败, 标签重复";
                break;

            case PushConsts.SETTAG_ERROR_UNBIND:
                text = "设置标签失败, 服务未初始化成功";
                break;

            case PushConsts.SETTAG_ERROR_EXCEPTION:
                text = "设置标签失败, 未知异常";
                break;

            case PushConsts.SETTAG_ERROR_NULL:
                text = "设置标签失败, tag 为空";
                break;

            case PushConsts.SETTAG_NOTONLINE:
                text = "还未登陆成功";
                break;

            case PushConsts.SETTAG_IN_BLACKLIST:
                text = "该应用已经在黑名单中,请联系售后支持!";
                break;

            case PushConsts.SETTAG_NUM_EXCEED:
                text = "已存 tag 超过限制";
                break;

            default:
                break;
        }

        Logger.i(TAG + "settag result sn = " + sn + ", code = " + code + ", text = " + text);
    }

    private void feedbackResult(FeedbackCmdMessage feedbackCmdMsg) {
        String appid = feedbackCmdMsg.getAppid();
        String taskid = feedbackCmdMsg.getTaskId();
        String actionid = feedbackCmdMsg.getActionId();
        String result = feedbackCmdMsg.getResult();
        long timestamp = feedbackCmdMsg.getTimeStamp();
        String cid = feedbackCmdMsg.getClientId();

        Logger.i(TAG + "onReceiveCommandResult -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nactionid = " + actionid + "\nresult = " + result
                + "\ncid = " + cid + "\ntimestamp = " + timestamp);
    }

    private void sendMessage(String data, int what) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = data;
        MyApplication.sendMessage(msg);
    }

}