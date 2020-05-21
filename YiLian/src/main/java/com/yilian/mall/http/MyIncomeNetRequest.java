package com.yilian.mall.http;

import android.content.Context;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.entity.AllImcomeEntity;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.CashURLEntity;
import com.yilian.mall.entity.ChargeDisplayEntity;
import com.yilian.mall.entity.ChargeSuccessEntity;
import com.yilian.mall.entity.DetailsEntity;
import com.yilian.mall.entity.MemberLevel1Entity;
import com.yilian.mall.entity.MyIncome;
import com.yilian.mall.entity.PaymentIndexEntity;
import com.yilian.mall.entity.SearchEntity;
import com.yilian.mall.entity.SearchMemEntity;
import com.yilian.mall.entity.TiXianEntity;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mylibrary.Ip;

/**
 * Created by Administrator on 2016/3/28.
 */
public class MyIncomeNetRequest extends BaseNetRequest {

    private String URL;

    public MyIncomeNetRequest(Context context) {
        super(context);
        URL = Ip.getURL(context) + "mall.php";
    }

    public void MyIncomeNet(RequestCallBack<MyIncome> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "account/member_center");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));

        postRequest(URL, params, MyIncome.class, callBack);
    }

    /**
     * 领奖励金额订单
     */
    public void CashNet(String money, String type, String pwd, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "account/extract_order");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("money", String.valueOf(NumberFormat.convertToFloat(money, 0) * 100));//领奖励金额,单位：分
        params.addBodyParameter("type", type);//领奖励类型 0公众号 1微信 2 支付宝
        params.addBodyParameter("pwd", pwd);//md5 密码
        Logger.i("领奖励参数： money:" + money + "00" + "  token:" + RequestOftenKey.getToken(mContext) +
        "  deviceIndex:"+RequestOftenKey.getDeviceIndex(mContext)+" type:"+type+"  pwd:"+pwd);
        postRequest(URL, params, BaseEntity.class, callBack);
    }

    /**
     * 会员领奖励明细
     *
     * @param begin//开始时间     时间戳
     * @param end//结束时间       时间戳
     * @param type//默认0我的领奖励明细 等级1 2 3
     * @param page
     * @param callBack
     */

    public void AllIncomeNet(String begin, String end, String type, int page, RequestCallBack<AllImcomeEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "account/income_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("begin", begin);
        params.addBodyParameter("end", end);
        params.addBodyParameter("type", type);
        params.addBodyParameter("page", page + "");

        postRequest(URL, params, AllImcomeEntity.class, callBack);
    }

    public void MemberLevel1Net(String lev, RequestCallBack<MemberLevel1Entity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "account/member_lev");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("lev", lev);//会员等级1.2.3

        postRequest(URL, params, MemberLevel1Entity.class, callBack);
    }


    /**
     * 请求充值订单
     *
     * @param pay_type//1支付宝 2微信 3微信公共账号 4网银 6套餐  7团购转配送
     * @param type           1商城订单支付,2商家入驻或续费,3商家扫码支付 4 线下交易 5奖励充值
     * @param paymentFree    //支付总价
     * @param callBack
     */
    public void NPaymentIndexNet(String pay_type, String type, String paymentFree, RequestCallBack<PaymentIndexEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "recharge/recharge_order");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("pay_type", pay_type);//0支付宝 1微信 2微信公共账号
        params.addBodyParameter("type", type);//1商城订单支付,2商家入驻或续费,3商家扫码支付 4 线下交易 5奖励充值
        params.addBodyParameter("payment_fee", paymentFree);//支付总价
        params.addBodyParameter("consumer_openid", "0");//微信唯一id app情况传0
        params.addBodyParameter("order_index", "0");
        postRequest(URL, params, PaymentIndexEntity.class, callBack);
    }

    /**
     * 请求充值订单
     *
     * @param pay_type    //1支付宝 2微信 3微信公共账号 4网银  7团购转配送
     * @param type        //0正常充值 1乐享币直充
     * @param paymentFree //支付总价 单位元
     * @param callBack
     */
    public void NPaymentIndexNet(String pay_type, String type, String paymentFree, String orderId, RequestCallBack<PaymentIndexEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "recharge/recharge_order");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("pay_type", pay_type);//1支付宝 2微信 3微信公共账号 4网银
        params.addBodyParameter("type", type);//0正常充值 1乐享币直充
        params.addBodyParameter("payment_fee", paymentFree);//支付总价
        params.addBodyParameter("consumer_openid", "0");//微信唯一id app情况传0
        params.addBodyParameter("order_index", orderId);
        postRequest(URL, params, PaymentIndexEntity.class, callBack);
    }

    /**
     * 请求充值订单
     *
     * @param pay_type    //1支付宝 2微信 3微信公共账号 4网银  7团购转配送
     *                    0
     * @param type        //0正常充值 1乐享币直充
     * @param paymentFree //支付总价 单位元
     * @param addressId   在团购转配送时要传次字段
     * @param callBack
     */
    public void NPaymentIndexNet(String pay_type, String type, String paymentFree, String orderId, String addressId, RequestCallBack<PaymentIndexEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "recharge/recharge_order");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("pay_type", pay_type);//1支付宝 2微信 3微信公共账号 4网银
        params.addBodyParameter("type", type);//0正常充值 1乐享币直充
        params.addBodyParameter("payment_fee", paymentFree);//支付总价
        params.addBodyParameter("consumer_openid", "0");//微信唯一id app情况传0
        params.addBodyParameter("order_index", orderId);
        params.addBodyParameter("address_id", addressId);
        postRequest(URL, params, PaymentIndexEntity.class, callBack);
    }


    public void TiXianNet(RequestCallBack<TiXianEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "account/user_case_info");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));

        postRequest(URL, params, TiXianEntity.class, callBack);
    }

    public void DetailsNet(String useid, RequestCallBack<DetailsEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "member_info");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("user_id", useid);

        postRequest(URL, params, DetailsEntity.class, callBack);
    }

    public void WeiXinBind(RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "account/app_bind_wx");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL, params, BaseEntity.class, callBack);
    }

    public void ShareNet(RequestCallBack<CashURLEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "share");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));

        postRequest(Ip.getURL(mContext) + "activity.php", params, CashURLEntity.class, callBack);
    }

    /**
     * 根据用户id查询单一会员信息
     *
     * @param user_id
     * @param callBack
     */
    public void searchByIdNet(String user_id, RequestCallBack<SearchMemEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "member_info");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("user_id", user_id);//用户 id

        postRequest(URL, params, SearchMemEntity.class, callBack);
    }

    /**
     * 会员模糊查询
     *
     * @param key
     * @param is_find
     * @param callBack
     */
    public void searchNet(String key, int is_find, String userId, RequestCallBack<SearchEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "find_members");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("key", key);//用户名，手机号
        params.addBodyParameter("is_find", is_find + "");//0用户名 1 手机号
        params.addBodyParameter("user_id", userId);
        Logger.i("Url  "+URL+" deviceindex "+RequestOftenKey.getDeviceIndex(mContext)+" token  "+RequestOftenKey.getToken(mContext)+" key "+key+" isfind "+is_find+" parentUserId:"+userId);
        postRequest(URL, params, SearchEntity.class, callBack);
    }



    /**
     * 会员转赠奖励
     */
    public void sendMoney(String phone, String lefen, String lebi, String pwd, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "transfer/send_money");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("phone", phone);//用户名，手机号
        params.addBodyParameter("lefen", "0");//乐分币取消后，该字段设置为0
        params.addBodyParameter("lebi", lebi);
        params.addBodyParameter("pwd", pwd);

        postRequest(URL, params, BaseEntity.class, callBack);
    }


    /**
     * 充值成功后判断会员等级
     *
     * @param callBack
     */
    public void chargeSuccess(RequestCallBack<ChargeSuccessEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "account/recharge_lebi");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));

        postRequest(URL, params, ChargeSuccessEntity.class, callBack);
    }

    //    支付修改数据接口 http://test.lefenmall.com/app/mall.php?c=recharge/paytype&device_index=0&token=3308425121544761008    {"code":"1","msg":"成功","type":"0"}
    public void weixinDisplay(RequestCallBack<ChargeDisplayEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "recharge/paytype");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));

        postRequest(URL, params, ChargeDisplayEntity.class, callBack);
    }
}