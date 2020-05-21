package com.yilian.mall.utils;/**
 * Created by  on 2017/3/4 0004.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.conversation.EServiceContact;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWMessageChannel;
import com.alibaba.mobileim.conversation.YWMessageSender;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.MyApplication;
import com.yilian.mylibrary.Constants;

import static android.content.Context.MODE_PRIVATE;
import static com.yilian.mall.MyApplication.sp;

/**
 * Created by  on 2017/3/4 0004.
 * 客关服相工具类
 */
public class AliCustomerServiceChatUtils {
    /**
     * 以activity的方式打开客服聊天界面
     *
     * @param customerServicePersonId 客服账号
     * @param customerServiceGroupId  组ID
     * @param id                      商品详情时此处为商品ID，旗舰店详情时此处为旗舰店ID，订单详情时此处为订单ID
     * @param fillieId                商品详情时此处为旗舰店ID， -1为订单详情
     */
    public static void openServiceChatByActivity(Context context, String customerServicePersonId, String customerServiceGroupId, String id, String fillieId) {
        if (context.getSharedPreferences(Constants.SP_FILE, MODE_PRIVATE).getBoolean(Constants.SPKEY_STATE, false)) {

            Logger.i("serviceId:" + customerServicePersonId);
            Logger.i("customerServiceGroupId:" + customerServiceGroupId);
            Logger.i("开启会话ID："+id+"开启会话filllieId:"+fillieId);
            //userid是客服账号，第一个参数是客服账号，第二个是组ID，如果没有，传0

            try {
                if (null != customerServicePersonId) {
                    EServiceContact  contact = new EServiceContact(customerServicePersonId, NumberFormat.convertToInt(customerServiceGroupId, 0));

                    //如果需要发给指定的客服账号，不需要Server进行分流(默认Server会分流)，请调用EServiceContact对象的setNeedByPass方法，参数为false。
                //contact.setNeedByPass(false);
                Intent intent = null;

                YWIMKit     ywimKit = MyApplication.getInstance().getYWIMKit();
                intent = ywimKit.getChattingActivityIntent(contact);
                context.startActivity(intent);
                if ("-1" == fillieId) {//订单消息
                    //通过调用如下代码完成交易id的提交。
                    YWMessage message = YWMessageChannel.createOrderFocusMessage(id);
                    IYWConversationService conversationService = ywimKit.getConversationService();
//获取会话对象
                    YWConversation conversation = conversationService.getConversation(contact);
//发送订单焦点消息，其中TIMEOUT为超时时间，单位为秒
                    YWMessageSender messageSender = conversation.getMessageSender();
                    Logger.i("order   messageSender:"+messageSender.toString());
                    messageSender.sendMessage(message, 2000, new IWxCallback() {

                        @Override
                        public void onSuccess(Object... arg0) {
                            //发送成功
                        }

                        @Override
                        public void onProgress(int arg0) {

                        }

                        @Override
                        public void onError(int arg0, String arg1) {
                            //发送失败
                        }
                    });
                } else {//宝贝消息和旗舰店消息都通过宝贝消息发送
                    // 通过调用如下代码完成宝贝id的提交(宝贝id可自定义，此处采用订单ID+FilileId的形式作为宝贝ID)
                    //创建一条宝贝焦点消息, 参数为宝贝id
                    YWMessage message;
                    if (TextUtils.isEmpty(fillieId)) {
                        message = YWMessageChannel.createGoodsFocusMessage(id);
                    } else {
                        message = YWMessageChannel.createGoodsFocusMessage(id + "×" + fillieId);
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                ((Activity)context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {//获取会话conversation的代码放到子线程里面，是为了防止第一次聊天时，由于没有建立聊天，获取的conversation的对象为null,就发送消息，会导致空指针（聊天的建立逻辑是在sdk的activity中，简单的打开activity后再获取conversation是不行的，需要再打开聊天activity一段时间后再获取）
                                        IYWConversationService conversationService = ywimKit.getConversationService();
                                        YWConversation conversation = conversationService.getConversation(contact);
//发送宝贝焦点消息，其中TIMEOUT为超时时间，单位为秒
                                        Logger.i(" 获取回话对象 " + conversation + "回话内容 " + message + " contanct " + contact);
                                        YWMessageSender messageSender = conversation.getMessageSender();
                                        Logger.i("goodDetail  messageSender:"+messageSender.toString());
                                        messageSender.sendMessage(message, 2000, new IWxCallback() {
                                            @Override
                                            public void onSuccess(Object... objects) {
                                                //                    宝贝信息发送成功
                                            }

                                            @Override
                                            public void onError(int i, String s) {
                                                //宝贝信息发送失败
                                            }

                                            @Override
                                            public void onProgress(int i) {

                                            }
                                        });
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
                } else {
                    Toast.makeText(context, "获取客服信息失败", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Logger.i(e.getMessage());
                e.printStackTrace();
            }
        } else {//只有登录状态才能打开聊天界面，否则跳转到登录界面
            context.startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
        }



    }

    /**
     * 判断是否登录
     */
    public boolean isLogin() {
        return sp.getBoolean(Constants.SPKEY_STATE, false);
    }
}
