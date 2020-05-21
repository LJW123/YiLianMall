package com.yilian.mylibrary;

import android.os.Environment;

import java.io.File;
import java.nio.charset.Charset;

import static com.yilian.mylibrary.Ip.getHelpURL;


public class Constants {

    public static final String PAY_PASSWORD = "pay_password";//是否有支付密码
    /**
     * sp key-下拉刷新操作时间
     **/
    public static final String FILE_USERPHOTO_PATH = "pull_refresh_time";
    /**
     * sp key-下拉刷新操作时间
     **/
    public static final String SPKEY_PULL_REFRESH_TIME = "pull_refresh_time";
    /**
     * sp key-上拉加载操作时间
     **/
    public static final String SPKEY_LOAD_MORE_TIME = "load_more_time";
    /**
     * sp key-此用户已参加大转盘活动的次数 -存取时后接活动id
     **/
    public static final String SPKEY_TURN_TABLE_COUNT = "turntable_count";
    /**
     * sp key-用户搜索商家历史
     **/
    public static final String SPKEY_MERCHANT_SEARCH_HISTORY = "merchant_search_history";
    /**
     * sp key-用户搜索产品历史
     **/
    public static final String SPKEY_GOODS_SEARCH_HISTORY = "goods_search_history";
    /**
     * sp key-用户搜索城市
     **/
    public static final String SPKEY_CITY_SEARCH_HISTORY = "city_search_history";
    /**
     * sp key-最新版本名称
     **/
    public static final String SPKEY_LATEST_VERSION = "latset_version";
    /**
     * sp key-盐值
     **/
    public static final String SPKEY_SERVER_SALT = "server_salt";
    /**
     * sp key-设备id
     **/
    public static final String SPKEY_DEVICE_INDEX = "device_index";
    /**
     * sp key-是否是首次安装
     **/
    public static final String SPKEY_IS_FIRST = "isFirst";
    /**
     * sp key-个推 推送标示 id
     **/
    public static final String SPKEY_CLIENTID = "clientid";
    /**
     * sp key-判断用户是否在登录状态
     **/
    public static final String SPKEY_STATE = "state";
    /**
     * sp key- token
     **/
    public static final String SPKEY_TOKEN = "token";
    /**
     * sp key- 用户账号
     **/
    public static final String SPKEY_PHONE = "phone";
    /**
     * 手机号 密码登录时，把手机号和密码以逗号分隔的形式存下来，在登录时，根据手机号判断密码是否对应，如果对应则自动输入到密码框
     */
    public static final String SPKEY_PHONE_PASSWORD = "phone_password";
    /**
     * sp key- 用户完整个人信息
     **/
    public static final String SPKEY_USER_INFO = "userInfo";
    /**
     * spkey- 完整定位信息
     **/
    public static final String SPKEY_LOCATION = "location";
    /**
     * sp key- **************携程用户地理位置信息经纬度/城市/区域/商业区***********************************
     **/
    public static final String CTRIP_LOCATION = "ctrip_location";
    /**
     * sp key- 定位省份id
     **/
    public static final String SPKEY_LOCATION_PROVINCE_ID = "locationProvinceId";
    /**
     * sp key- 定位省份名字
     **/
    public static final String SPKEY_LOCATION_PROVINCE_NAME = "locationProvinceName";
    /**
     * sp key- 定位城市id
     **/
    public static final String SPKEY_LOCATION_CITY_ID = "locationCityId";
    /**
     * sp key- 定位城市name
     **/
    public static final String SPKEY_LOCATION_CITY_NAME = "locationCityName";
    /**
     * sp key- 定位区县id
     **/
    public static final String SPKEY_LOCATION_COUNTY_ID = "locationCountyId";
    /**
     * sp key- 定位区县name
     **/
    public static final String SPKEY_LOCATION_COUNTY_NAME = "locationCountyName";
    /**
     * sp key- 定位位置详情
     **/
    public static final String SPKEY_LOCATION_ADDRESS = "locationAddress";
    /**
     * sp key- 选择省份id
     **/
    public static final String SPKEY_SELECT_PROVINCE_ID = "selectProvinceId";
    /**
     * sp key- 选择省份name
     **/
    public static final String SPKEY_SELECT_PROVINCE_NAME = "selectProvinceName";
    /**
     * sp key- 选择城市id
     **/
    public static final String SPKEY_SELECT_CITY_ID = "selectCityId";
    /**
     * sp key- 选择城市name
     **/
    public static final String SPKEY_SELECT_CITY_NAME = "selectCityName";
    /**
     * sp key- 选择区县id
     **/
    public static final String SPKEY_SELECT_COUNTY_ID = "selectCountyId";
    /**
     * sp key- 选择区县name
     **/
    public static final String SPKEY_SELECT_COUNTY_NAME = "selectCountyName";
    /**
     * sp key- 最近访问省份id 保存的多个省份用","隔开
     **/
    public static final String SPKEY_LATEST_PROVINCE_ID = "latestProvinceId";
    /**
     * sp key- 最近访问区县id 保存的多个区县用","隔开
     **/
    public static final String SPKEY_LATEST_COUNTY_ID = "latestCountyId";
    /**
     * sp key- 最近访问区县名称   保存的多个区县用","隔开
     **/
    public static final String SPKEY_LATEST_COUNTY_NAME = "latestCountyName";
    /**
     * sp key- 最近访问区县所在市id   保存的多个用","隔开
     **/
    public static final String SPKEY_LATEST_CITY_ID = "latestCityId";
    /**
     * sp key- 最近访问区县编号，从1开始, int,不计重复的区县
     **/
    public static final String SPKEY_LATEST_COUNTY_NUM = "latestCountyNum";
    /**
     * sp key- 选择城市是否和定位城市EA_BOOLEAN = "spkeyAreaBoolean";
     * <p/>
     * /** sp key- 用户是否手动设置过城市 boolean
     **/
    public static final String SPKEY_USER_SELECT_CITY = "userselectcity";
    /**
     * sp key- 首页加载完成后缓存数据 boolean
     **/
    public static final String MAIN_DATA = "mainData";
    /**
     * sp key- 首页加载完成后缓存数据是否成功 boolean
     **/
    public static final String MAIN_DATA_OK = "mainDataOk";
    //商家我的营收说明
    public static final String MERCHANT_MAY_REVENUE = getHelpURL() + "html/yingshou.html";
    /**
     * sp key- 广告信息
     */
    public static final String SPKEY_HAVEAD = "haveAD";
    public static final String SPKEY_AD_TYPE = "adType";//1URL 2商品详情页 3商家详情页 4幸运购活动详情 5兑换中心详情 6幸运购首页
    public static final String SPKEY_AD_IMAGE_URL = "imageUrl";//广告图片url
    public static final String SPKEY_AD_CONTENT = "adContent";//url/goods_id/merchant_id/activity_id/兑换中心_id/
    public static final String SPKEY_AD_SHOWTIME = "adShowTime";//url/goods_id/merchant_id/activity_id/兑换中心_id/
    /**
     * sp key- 用户积发奖励包
     **/
    public static final String SPKEY_INTEGRAL = "integral";
    /**
     * sp key- 用户乐币奖励
     **/
    public static final String SPKEY_LEBI = "lebi";
    /**
     * sp key- 用户乐券奖励
     **/
    public static final String SPKEY_COUPON = "coupon";
    /**
     * sp key- 签到大转盘
     */
    public static final String SPKEY_SIGN = "sign_count";
    /**
     * 全部
     **/
    public static final String ACTIVITY_TYPE_ALL = "0";
    /**
     * 大转盘
     **/
    public static final String ACTIVITY_TYPE_TURN = "1";
    /**
     * 大家猜
     **/
    public static final String ACTIVITY_TYPE_GUESS = "2";
    /**
     * 刮刮卡
     **/
    public static final String ACTIVITY_TYPE_SCRAPING = "3";
    /**
     * 摇一摇
     **/
    public static final String ACTIVITY_TYPE_SHAKE = "4";
    /**
     * 活动状态-未开始
     **/
    public static final String ACTIVITY_STATE_NOT_START = "0";
    /**
     * 活动状态-正在进行
     **/
    public static final String ACTIVITY_STATE_GOING = "1";
    /**
     * 活动状态-结束
     **/
    public static final String ACTIVITY_STATE_END = "3";
    /**
     * 兑换中心商品分区-乐换区
     **/
    public static final int FILIALE_GOOD_REGION_LEHUAN = 1;
    /**
     * 兑换中心商品分区-乐选区
     **/
    public static final int FILIALE_GOOD_REGION_LEXUAN = 2;
    /**
     * 兑换中心商品分区-乐购区
     **/
    public static final int FILIALE_GOOD_REGION_LEGOU = 3;
    /*下单选择地址的address_id*/
    public static final String ADDRESS_ID_SELECTED = "99";
    // SDCard路径
    public static final String SD_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + File.separatorChar;
    /**
     * 文件根目录
     */
    public static final String BASE_PATH = "/com.yilian/";
    /**
     * 游戏下载文件目录
     */
    public static final String GAME_PATH = "/com.yilian/game/";
    // 缓存图片路径
    public static final String BASE_IMAGE_CACHE = BASE_PATH + "cache/images/";
    // 需要分享的图片
    public static final String SHARE_FILE = BASE_PATH + "QrShareImage.png";
    //分享的幸运购活动的ID
    public static final String One_Buy_Share_Activity_ID = "OneBuyShareActivityId";
    // 分享成功
    public static final int SHARE_SUCCESS = 0X1000;
    // 分享取消
    public static final int SHARE_CANCEL = 0X2000;
    // 分享失败
    public static final int SHARE_ERROR = 0X3000;
    // 开始执行
    public static final int EXECUTE_LOADING = 0X4000;
    // 正在执行
    public static final int EXECUTE_SUCCESS = 0X5000;
    // 执行完成
    public static final int EXECUTE_FAILED = 0X6000;
    // 加载数据成功
    public static final int LOAD_DATA_SUCCESS = 0X7000;
    // 加载数据失败
    public static final int LOAD_DATA_ERROR = 0X8000;
    // 动态加载数据
    public static final int SET_DATA = 0X9000;
    // 未登录
    public static final int NONE_LOGIN = 0X10000;
    //app 分享key
    //public static final String APP_ID = "wx54828af084eabb18";
    /**
     * 微信    APP_ID  APP_SECRET
     */
    public static final String APP_ID = "wxa433b92d5753519f";
    public static final String WEIXIN_APPSECRET = "9c6686e80554c3750e5179893847bcad";
    /**
     * QQ APPID  APPKEY
     */
    public static final String QQ_APPID = "1106230726";
    public static final String QQ_APPKEY = "uutWettPhIVkNAdp";

    /**
     ******************************************* 参数设置信息结束 ******************************************
     */
    /**
     * 支付宝
     */
    //商户PID
    public static final String PARTNER = "";
    //商户收款账号
    public static final String SELLER = "";
    //商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "";
    //支付宝公钥
    public static final String RSA_PUBLIC = "";
    //	public static String ObtainLefen = "http://test.58lefen.com/app/agreementforios/problem9.html";
//首页
    public static final String INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT = "jpMTHomeFragment";
    //    线上商城
    public static final String INTENT_VALUE_JUMP_TO_DI_KOU_FRAGMENT = "jpDiKouFragment";
    //    购物车
    public static final String INTENT_VALUE_JUMP_TO_SHOPPING_CART_FRAGMENT = "jpShoppingCartFragment";
    //    我的
    public static final String INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT = "jpPersonCenterFragment";
    //
    public static final String INTENT_VALUE_JUMP_TO_LING_GOU_FRAGMENT = "jpLingGouFragment";
    //    奖励
    public static final String INTENT_VALUE_JUMP_TO_RANKING_LIST = "rankingList";
    public static final String JPMAINREGISTER = "jpmainregister";
    public static final String PAY_TYPE = "pay_type";//掉期支付广播的时候的type
    public static final String MERSTATUS = "merstatus";
    public static final String MALL_INTEGRAL = "mall_integral";// 商城奖券
    public static final String INTEGRAL_NUMBER = "integral_number";//奖券指数
    public static final String TOTAL_INTEGRAL_BONUS = "total_integral_bonus";//累计奖券发奖励
    public static final String BALANCE_NUM = "balance_num";//累计领奖励
    public static final String PHOTO = "photo";
    public static final String MERCHANT_STATUS = "merchant_status";//商家缴费的状态是缴费还是续费
    public static final String USERURL = "user_url";//返回的分享链接
    public static final String MERCHANT_STATUS_FLAG = "merchant_status_flag";//标记商家此次缴费是要成为个体商家（"3"）还是实体商家（"2"）
    public static final String SHOW_MONTH = "show_month";//标记当前奖励/奖券列表是否显示月份字段
    public static final String SHOW_HOME_PAGE_NOTICE_BEAN = "show_home_page_notice_bean";//标记是否显示首页弹屏公告
    public static final String NOT_SHOW_HOME_PAGE_NOTICE_BEAN_ID = "show_home_page_notice_bean_id";//标记是否显示的首页弹屏公告的ID
    public static final String JWT = "jwt";//登录游戏传递给游戏的用户信息字段
    public static final String JWT_EXPIRES_IN = "jwt_expires_in";//登录游戏用户信息字段有效时长
    public static final String USER_ID = "user_id";
    public static final String SERVICE_TYPE = "service_type";//商家售后订单审核功能需要字段 0换货 返修 1退货
    public static final String CHECK_ID = "check_id";//商家售后订单审核功能需要字段 短的订单号
    public static final String LIST_POSITION = "list_position";
    public static final String SERVICE_INDEX = "service_index";
    public static final String ORDER_INDEX = "order_index";
    /**
     * 售后订单列表的刷新
     */
    public static final String REFRESH_AFTER_lIST = "refresh_after_List";
    public static final String ORDER_GOODS_INDEX = "order_goods_index";//订单下商品的自增Id,如果合并发货的话用“,”拼接
    public static final String JUMP_STATUS = "jump_status";
    public static final String JUMP_MIPCA = "jump_mipca";//跳转二维码扫描界面用语区分当前是商品条码扫描还是快递单号扫描
    public static final String EXPRESS_SELECT = "express_select";//快递扫描跳转标识
    public static final int COMMIT_EXPRESS_NUM_ACTIVITY = 100;
    public static final int AFTER_SALE_ROUTE_ACTIVITY = 101;
    public static final String SPKEY_WX_LOGIN = "isWXLogin";
    /**
     * 帮助
     */
//    public static String UseHelp = Ip.HELP_URL + "agreementforios/usehelp.html";
    /**
     * 使用协议
     */
//    public static String UseAgreement = Ip.HELP_URL + "agreementforios/useagreement.html";
    /**
     * 活动规则
     */
//    public static String ActivityRules = Ip.HELP_URL + "agreementforios/activityrules.html";
    /**
     * 如何获得乐分
     */
//    public static String ObtainLefen = Ip.HELP_URL + "agreementforios/problem9.html ";
    /**
     * 什么是乐分宝
     */
//    public static String LefenBaoWhy = Ip.HELP_URL + "agreementforios/lefenbao.html";
    /**
     * 现金支付
     */
//    public static String PayCash = Ip.BASE_PAY_URL + "WapPayment/payRequest.php?";

    /**
     * 如何获取抵扣券
     */
//    public static String HowToGetCoupon = Ip.HELP_URL + "agreementforios/QAforLocal.html";

    /**
     * 如何获取零购券
     */
//    public static String HowToGetVoucher = Ip.HELP_URL + "agreementforios/voucher.html";

    /**
     * 签到规则
     */
//    public static String signRule = Ip.HELP_URL + "agreementforios/user_sign.html";
    public static final String SPKEY_ALIPAY_LOGIN = "isAliPayLogin";
    public static final int SMS_COUNT_TIME = 60;// 两次获取短信间隔时长
    public static final String JP_MAIN_ACTIVITY_ADV = "JPMainActivityAD";//主页推广活动
    public static final String JP_MAIN_ACTIVITY_HAVE_AD = "haveNewAd";
    public static final String PAY_SOURCE = "pay_source";//支付订单来源
    //    public static String Url = Ip.URL;
    public static final String PAY_MONEY = "pay_money";//订单实际支付金额（除去抵扣券）
    public static final String REFERRER_PHONE = "referrer_phoen";
    //店内消费/扫码支付分享的order_id
    public static final String Order_Id = "order_id";
    public static final String VOUCHERAUTH = "voucherAuth";//用户零购券权限标识
    public static final String REFRESHUSERINFOBROADCASTRECEIVER = "com.yilian.mall.receiver.refreshuserinfor";
    /**
     * 阿里云有关----------------------------------------------------------------------------------------------------------------------
     */
    //HTTP
    public static final String CLOUDAPI_HTTP = "http://";
    //HTTPS
    public static final String CLOUDAPI_HTTPS = "https://";
    /**
     * Api绑定的的AppKey，可以在“阿里云官网”->"API网关"->"应用管理"->"应用详情"查看
     */
    public static final String APP_KEY = "23649287";
    /**
     * Api绑定的的AppSecret，用来做传输数据签名使用，可以在“阿里云官网”->"API网关"->"应用管理"->"应用详情"查看
     */
    public static final String APP_SECRET = "0a35644d3099109faf223c90de0b1c54";
    /**
     * 是否以HTTPS方式提交请求
     * 本SDK采取忽略证书的模式,目的是方便大家的调试
     * 为了安全起见,建议采取证书校验方式
     */
    public static final boolean IS_HTTPS = true;
    /**
     * Api绑定的域名
     */
    public static final String APP_HOST = "dm-51.data.aliyun.com";
    //GET
    public static final String CLOUDAPI_GET = "GET";
    //POST
    public static final String CLOUDAPI_POST = "POST";
    //PUT
    public static final String CLOUDAPI_PUT = "PUT";
    //DELETE
    public static final String CLOUDAPI_DELETE = "DELETE";
    //请求Header Accept
    public static final String CLOUDAPI_HTTP_HEADER_ACCEPT = "Accept";
    //请求Body内容MD5 Header
    public static final String CLOUDAPI_HTTP_HEADER_CONTENT_MD5 = "Content-MD5";
    //请求Header Content-Type
    public static final String CLOUDAPI_HTTP_HEADER_CONTENT_TYPE = "Content-Type";
    //请求Header UserAgent
    public static final String CLOUDAPI_HTTP_HEADER_USER_AGENT = "User-Agent";
    //请求Header Date
    public static final String CLOUDAPI_HTTP_HEADER_DATE = "Date";
    //请求Header Host
    public static final String CLOUDAPI_HTTP_HEADER_HOST = "Host";
    //签名Header
    public static final String CLOUDAPI_X_CA_SIGNATURE = "X-Ca-Signature";
    //所有参与签名的Header
    public static final String CLOUDAPI_X_CA_SIGNATURE_HEADERS = "X-Ca-Signature-Headers";
    //请求时间戳
    public static final String CLOUDAPI_X_CA_TIMESTAMP = "X-Ca-Timestamp";
    //请求放重放Nonce,15分钟内保持唯一,建议使用UUID
    public static final String CLOUDAPI_X_CA_NONCE = "X-Ca-Nonce";
    //APP KEY
    public static final String CLOUDAPI_X_CA_KEY = "X-Ca-Key";
    //签名版本号
    public static final String CLOUDAPI_X_CA_VERSION = "CA_VERSION";
    //编码UTF-8
    public static final Charset CLOUDAPI_ENCODING = Charset.forName("UTF-8");
    //Header头的编码
    public static final Charset CLOUDAPI_HEADER_ENCODING = Charset.forName("ISO-8859-1");
    //UserAgent
    public static final String CLOUDAPI_USER_AGENT = "ALIYUN-ANDROID-DEMO";
    //换行符
    public static final String CLOUDAPI_LF = "\n";
    //参与签名的系统Header前缀,只有指定前缀的Header才会参与到签名中
    public static final String CLOUDAPI_CA_HEADER_TO_SIGN_PREFIX_SYSTEM = "X-Ca-";
    //签名版本号
    public static final String CLOUDAPI_CA_VERSION_VALUE = "1";

    //web网页穿参数  ？ &
//    public static String PHOTO_TEXT_URL = Ip.WECHAT + "merchant/packageImageIntro.php?package_id=";
    //表单类型Content-Type
    public static final String CLOUDAPI_CONTENT_TYPE_FORM = "application/x-www-form-urlencoded; charset=UTF-8";
    // 流类型Content-Type
    public static final String CLOUDAPI_CONTENT_TYPE_STREAM = "application/octet-stream; charset=UTF-8";
    //JSON类型Content-Type
    public static final String CLOUDAPI_CONTENT_TYPE_JSON = "application/json; charset=UTF-8";
    //XML类型Content-Type
    public static final String CLOUDAPI_CONTENT_TYPE_XML = "application/xml; charset=UTF-8";
    //文本类型Content-Type
    public static final String CLOUDAPI_CONTENT_TYPE_TEXT = "application/text; charset=UTF-8";
    /**
     * 阿里云有关结束-----------------------------------------------------------------------------------------------------------
     */

    //身份证正面的URL
    public static final String IDCARD_FACE_IMGURL = "face_imageUrl";
    //身份证反面的URL
    public static final String IDCARD_BACK_IMGURL = "back_imageUrl";
    //隐藏新手指引
    public static final String HIDE_DOUBLE_CLICK = "hide_double_click";
    //    阿里百川 AppKey:
    public static final String ALI_APP_KEY = "23658663";
    //    阿里百川 AppSecret:
    public static final String ALI_APP_SECRET = "acdcc1805858ff58693346a2389d8183";
    //    阿里百川 UserId：
    public static final String ALI_USER_ID = "ali_user_id";
    public static final String MERCHNAT_SEARCH_HISTORY = "merchant_search_history";
    /**
     * 京东搜索历史记录key
     */
    public static final String JD_GOODS_SEARCH_RECORDER = "jd_goods_search_history_list";
    public static final String SN_GOODS_SEARCH_RECORDER = "sn_goods_search_history_list";
    public static final String CTRIP_KEYWORD_SEARCH_RECORDER = "ctrip_keyword_search_history_list";
    public static final String MERCHNAT_SEARCH_COMMODITY = "search_commodity_history";
    public static final String WXCHARGEFORPAY = "WXChargeForPay";//微信的支付类型，该笔支付是用于直接支付订单的，而不是充值的
    public static final String WXPAYORDER = "WXPayOrder";//微信支付的订单ID
    public static final String WXPAYTYPE1 = "WXPayType1";//微信支付的类型  商品订单1 商家入驻缴费2 扫码支付3 线下交易4
    public static final String WXPAYORDERNUMBER = "WXPayOrderNumber";//该笔微信支付的订单数量
    public static final String HAS_MESSAGE = "hasMessage";
    public static final String FLASHSALEPAY = "FlashSalePay";//限时抢购 支付标记
    public static final String SPELL_GROUP = "spellGroup";//限时购和拼团支付区别Key
    public static final String MY_BALANCE = "myBalance";//我的奖券
    public static final String MY_CASH = "myCash";//我的奖励
    public static final String PENDINTEGRAL = "pendIntegral";//待结算奖券
    public static final String CITYID = "cityId";
    public static final String COUNTYID = "countyId";
    public static final String JIFEN = "jifen";
    public static final String MERCHANT_LEV = "lev";
    public static final String LEV_NAME = "lev_name";
    public static final String MERCHANT_ID = "merchant_id";
    public static final String MERCHANT_TYPE = "merchant_type";
    public static final String AGENT_ID = "agent_id";
    public static final String AGENT_URL = "agent_url";
    public static final String REQUIREDCASH = "required_cash";
    public static final String QYURL = "qy_url";
    public static final String MER_STATUS = "mer_status";//当前的商家的状态
    public static final String SELECT_MER_STATUS = "mer_status";//当前的商家的状态
    public static final String MER_REFUSE_REASON = "mer_refuse_reason";
    public static final String AGENT_STATUS = "agent_status";//1代表已申请未通过
    public static final String AGENT_REGION = "agent_region";//申请服务中心所在区域
    public static final String YI_DOU_BAO = "yi_dou_bao"; //0不显示  1显示
    public static final String YI_DOU_BAO_URL = "yi_dou_bao_url"; //易豆宝URL
    //   为0的情况下 跳转APP内部打开网页 1的情况下 跳转到外部浏览器打开
    public static final String YI_DOU_JUMP = "yi_dou_jump";
    //实体商家入驻协议
    public static final String MERCHANT_REGISTE = getHelpURL() + "agreementforios/agreement/registeEntityProtocol.html";
    //个体商家入驻协议
    public static final String PERSONAGE_MERCHANT = getHelpURL() + "agreementforios/agreement/registeUnityProtocol.html";
    //    VIP会员注册协议
    public static final String VIP_REGISTE = getHelpURL() + "agreementforios/agreement/registeVipProtocol.html";
    //个人身份信息注意事项：
    public static final String PERSONAGE_IDCARD = getHelpURL() + "agreementforios/idcardNotice.html";
    //认证资料注意事项
    public static final String CERTIFICATED_INFORMATION = getHelpURL() + "agreementforios/licenceNotice.html";
    //线下门店说明
    public static final String OFFLINE_SHOP = getHelpURL() + "agreementforios/offlineShop.html";
    //线上商城旗舰店说明
    public static final String ONLINE_SHOP = getHelpURL() + "agreementforios/onlineShop.html";
    //认证资料认证注意事项
    public static final String PERSONAL_INFO_NOTICE = getHelpURL() + "agreementforios/personalInfoNotice.html";
    //VIP权益：
    public static final String VIP_RIGHES = getHelpURL() + "agreementforios/vipRights.html";
    //注册页的注册协议连接
    public static final String REGISTER_HTTP = Ip.getFileUrl() + "news/20170918112633.html";
    //常见问题地址
    public static final String APP_FAQ = getHelpURL() + "agreementforios/app_faq.html";
    //奖券说明地址
    public static final String INTEGRAL_AGREEMENT = getHelpURL() + "agreementforios/integral.html";
    //奖励说明地址
    public static final String VOUCHER_AGREEMENT = getHelpURL() + "agreementforios/lebi.html";
    //帮助中心
    public static final String HELP_CENTER = "m/activity/readMusic/readIndex1.php";
    //商户入驻须知
    public static final String MERCHANT_INSTRUCTIONS = Ip.getFileUrl() + "news/20170901052328.html";
    public static final int GET_SMS_CODE_GAP = 30;
    public static final int GET_SMS_CODE_COUNT_DOWN_TIME = 29;//获取验证码倒计时
    public static final String RE_LOCATION = "re_location";//首页重新定位
    public static final String SELF_LOCATION_LAT = "sel_location_lat";
    public static final String SELF_LOCATION_LNG = "sel_location_lng";
    public static final String ACCOUNTHISTORY = "accountHistory";
    public static final String HOME_PAGE_NOTICE = "home_page_notice";
    //易划算说明地址
    public static final String YHS_AGREEMENT = getHelpURL() + "agreementforios/yihuasuan.html";
    //奖券购说明地址
    public static final String JFG_AGREEMENT = getHelpURL() + "agreementforios/jifengou.html";
    //区块益豆说明地址
    public static final String LEDOU_AGREEMENT = Ip.BASE_DEFAULT_WEB_URL + "wechat/xieyi/xieyi.html";
    public static final String LEDOU_TAKE = Ip.BASE_DEFAULT_WEB_URL + "wechat/xieyi/tqsm.html";
    //收藏默认的type
    public static final String COLLECT_TYPE = "0";
    //是否显示商家线下门店资料被拒绝原因
    public static final String SHOW_MERCHANT_REFUSE_REASON = "show_merchant_refuse_reason";
    /**
     * 页面刷新有关
     */
    public static final String REFRESH_USER_FRAGMENT = "refresh_user_fragment";
    public static final String REFRESH_USER_FRAGMENT_LOADING = "refresh_user_fragment_loading";
    public static final String REFRESH_SHOP_FRAGMENT = "refresh_shop_fragment";
    public static final String REFRESH_HOME_FRAGMENT = "refresh_home_fragment";
    public static final int PAGE_INDEX = 0;//起始页数
    public static final int PAGE_COUNT = 30;//每页数量 30
    public static final int PAGE_COUNT_20 = 20;//每页数量 20
    public static final int PAGE_COUNT_10 = 10;//每页数量 20
    /**
     * 领奖励
     */
    public static final int TAKE_CASH = 102;
    public static final int TAKE_CASH_SUCCESS = 103;
    public static final int MEMBER_GIFT = 104;
    public static final int OFFLINE_TRANSFER_RECORD = 105;
    /**
     * 储存文件地址
     */
    public static final String FILE_PATH = "com.yilian.mall";
    /**
     * 储存文件名字
     */
    public static final String FILE_NAME = "yilian.txt";
    /**
     * 语言包获取的ip字段
     */
    public static final String LANGUAGE_PACKAGE_IP = "ip";
    /**
     * 商家入驻入口
     */
    public static final String MERCHANT_ENTER = Ip.getWechatURL(LibApplication.getContextObject()) + "m/activity/suNing/shopEnter.php";
    //奖券商城建设中
    public static final String INTEGRAL_MALL_DEVELOPING = Ip.getWechatURL(LibApplication.getContextObject()) + "m/integralMallDeveloping.html";
    //益划算商城建设中
    public static final String MALL_DEVELOPING = Ip.getWechatURL(LibApplication.getContextObject()) + "m/mallDeveloping.html";
    public static final String AFTER_SALE_GOODS = Ip.getWechatURL(LibApplication.getContextObject()) + "/m/goods/afterSale.html";//售后保障
    /**
     * 游戏中心
     */
    public static final String GAME_CENTER_NAME = "game_name";
    public static final String GAME_CENTER_DESC = "game_desc";
    public static final String GAME_CENTER_ICON = "game_icon";
    public static final String GAME_CENTER_TYPE = "game_type";
    public static final String GAME_CENTER_CONTENT = "game_content";
    /**
     * 乐淘币
     */
    public static final String LE_TAO_BI = "ltb";
    public static final String LE_TAO_BI_ICON = "ltb_icon";
    public static final String LE_TAO_BI_DESC = "ltb_desc";
    public static final String LE_TAO_BI_URL = "ltb_url";
    public static final String LE_TAO_BI_TYPE = "ltb_type";
    public static final String LE_TAO_BI_CONTENT = "ltb_content";
    public static final String REFRESH_MERCHANT_CENTER = "refresh_merchant_center";//是否刷新商家中心页面数据
    public static final String REFRESH_MERCHANT_MANAGER_LIST = "refresh_merchant_manager_list";//是否刷新商家中心页面数据
    /**
     * webView右上角说明icon是否显示的常量
     */
    public static final String WEBVIEW_ICON_EXPLAIN = "icon_explain";
    /**
     * 奖券乐园-中奖纪录地址
     */
    public static final String PARADISE_RECORD = Ip.getWechatURL(LibApplication.getContextObject()) + "m/activity/prizeTicketList.php";
    /**
     * 奖券乐园-大家猜
     */
    public static final String PARADISE_GUESS = Ip.getWechatURL(LibApplication.getContextObject()) + "m/activity/togetherGuestGame.php";
    /**
     * 奖券乐园-大转盘
     */
    public static final String PARADISE_WHEEL = Ip.getWechatURL(LibApplication.getContextObject()) + "m/activity/aquareWheelOfFortune.php";
    /**
     * 信用账单
     */
    public static final String CREDIT_BILL = Ip.getWechatURL(LibApplication.getContextObject()) + "m/activity/monthOrder/month_bill.php";
    /**
     * 信用账单
     */
    public static final String PROMOTE_CREDIT = Ip.getWechatURL(LibApplication.getContextObject()) + "m/activity/credit/creditPromote.php";
    /**
     * 奖券乐园-刮刮乐
     */
    public static final String PARADISE_SCRAPE = Ip.getWechatURL(LibApplication.getContextObject()) + "m/activity/scratch.php";
    //    集分宝相关协议前缀
    public static final String INTEGRAL_SET_PROTOCOL = Ip.getWechatURL(LibApplication.getContextObject()) + "m/activity/protocol/protocol.php?key=";
    //公益爱心捐赠
    public static final String WELFARE_LOVE_DONATION = Ip.getWechatURL(LibApplication.getContextObject()) + "m/activity/loveDonation/loveDonation.php?project_id=";
    //实名授权
    public static final String REAL_NAME_AUTH = INTEGRAL_SET_PROTOCOL + "realNameAuth";
    //    上传身份证拍照要求
    public static final String PHOTO_DESCRIPTION = INTEGRAL_SET_PROTOCOL + "photoDescription";
    //    限额说明
    public static final String QUOTAS = INTEGRAL_SET_PROTOCOL + "quotas";
    //    互助条款
    public static final String MUTUAL_TERMS = INTEGRAL_SET_PROTOCOL + "MutualTerms";
    //    信息介绍
    public static final String MESSAGE_INTRODUCE = INTEGRAL_SET_PROTOCOL + "messageIntroduce";
    //    奖券捐赠协议
    public static final String DONATION_INTEGRAL_AGREEMENT = INTEGRAL_SET_PROTOCOL + "donationIntegralAgreement";
    //    账单说明
    public static final String BILL_EXPLAIN = INTEGRAL_SET_PROTOCOL + "billExplain";
    //    益家公益用户协议
    public static final String YLYJ_USER_AGREEMENT = INTEGRAL_SET_PROTOCOL + "ylyjUserAgreement";
    //    益联益家益点信用协议
    public static final String YLYJ_CREDIT_INFO = INTEGRAL_SET_PROTOCOL + "ylyjCreditInfo";
    //    奖券赚不停
    public static final String GAIN_INTEGRAL = Ip.getWechatURL(LibApplication.getContextObject()) + "m/activity/gainIntegral/gainIntegral.php";
    //    奖券超值GO
    public static final String INTERGRAL_GO = Ip.getWechatURL(LibApplication.getContextObject()) + "m/activity/gainIntegral/integralGo.php";
    //    奖券小游戏
    public static final String LOVE_DONATION = Ip.getWechatURL(LibApplication.getContextObject()) + "m/activity/gainIntegral/integralGames.php";
    /**
     * WebView右上角说明的js方法
     */
    public static final String JS_APPSHOWRULER = "javascript:" + "appShowRuler()";
    public static final String REFRESH_GUESS_GOING_ONE = "refresh_guess_going_1";//是否刷新大家猜进行中列表
    public static final String REFRESH_GUESS_GOING_TWO = "refresh_guess_going_2";//是否刷新大家猜进行中列表
    public static final String REFRESH_GUESS_GOING_THREE = "refresh_guess_going_3";//是否刷新大家猜进行中列表
    public static final String REFRESH_GUESS_DETAIL = "refresh_guess_detail";//是否刷新大家猜结果列表
    public static final String APP_CALLBACK_NO_PARAMETER = "call_back_url";
    public static final String APP_CALLBACK_PARAMETER = "call_back_str";
    public static final String REFRESH_LUCKY_DETAIL = "refresh_lucky_detail";//刷新幸运购标识
    public static final String REFRESH_PRZIE_DETAIL = "refresh_prize_detail";//是否刷新大家猜结果列表
    public static final int LUCKY_SHOW = 106;
    public static final String LUCKY_SHARE_URL = "http://a.app.qq.com/o/simple.jsp?pkgname=com.yilian.mall";
    /**
     * 网页分享后的回调方法的参数
     */
    public static final String SHARE_CALLBACK = "share_callback";
    /**
     * APP是否在前台
     */
    public static final String APP_IS_FOREGROUND = "app_is_foreground";
    /**
     * 用户幸运购最近一次中奖活动ID
     */
    public static final String APP_LUCKY_PRIZE_ACTIVITY_ID = "app_lucky_prize_activity_id";
    public static final String ACTIVITY_DETAIL_ENTITY = "activity_detail_entity";
    /**
     * 每次打开app时控制是否弹窗的flag
     */
    public static final String RED_PACKET_FIRST = "red_packet_first";
    /**
     * 是否弹出每日奖励弹窗
     */
    public static final String RED_PACKET_SHOW = "red_packet_show";
    /**
     * 弹出奖励时的奖励id
     */
    public static final String RED_PACKET_ID = "red_packet_id";
    /**
     * 奖励Fragment是否刷新的标识
     */
    public static final String REFRESH_RED_FRAGMENT = "refresh_red_fragment";
    /**
     * 奖励Fragment获取的金额
     */
    public static final String RED_DIALOG_MONEY = "red_dialog_money";
    /**
     * 奖励Fragment获取的时间
     */
    public static final String RED_DIALOG_TIME = "red_dialog_time";
    /**
     * sp存储的用户姓名
     */
    public static final String USER_NAME = "user_name";
    /**
     * 分享时是否是奖励分享的字段
     */
    public static final String RED_SHARE = "red_share";
    public static final String IS_CHECKED_UPDATE = "is_checked_update";
    public static final String USER_LEVEL = "user_level";
    /**
     * 24小时的毫秒数
     */
    public static final int ONE_DAY = 86400000;
    public static final String ON_OFF_VOICE = "on_off_voice";
    /**
     * 线上商城MD5
     */
    public static final String KEY_ONLINE_MALL_BANNER_MD5 = "online_mall_md5_str";
    /**
     * 线上商城json文件路径
     */
    public static final String FILE_NAME_MANLL_BANNER = "onlineMallBannerJson.txt";
    public static final String KEY_ONLINE_MALL_BANNER_DATA = "onlineBannerData";
    /**
     * 用户是否认证
     */
    public static final String IS_CERT = "is_cert";
    /**
     * 运营加入后的新版银行卡
     */
    public static final String CARD_ID = "card_id";
    /**
     * 心情
     */
    public static final String STATE_OF_MIND = "stateOfMind";
    /**
     * 健康有关参数key
     */
    public static final String HEALTH_HELP_OTHER_ID = "health_help_other_id";
    public static final String HEALTH_HELP_OTHER_TYPE = "health_help_other_type";
    public static final String HEALTH_HELP_OTHER_TAG = "health_help_other_tag";
    public static final String HEALTH_HELP_OTHER_ID_NUM = "health_help_other_id_num";
    public static final String HEALTH_HELP_OTHER_NAME = "health_help_other_name";
    public static final String HEALTH_HELP_OTHER_BIRTHDAY = "health_help_other_birthday";
    public static final String HEALTH_HELP_OTHER_JOIN_ID = "health_help_other_join_id";
    public static final String HEALTH_HELP_OTHER_TITLE = "health_help_other_title";
    public static final String REFRESH_HEALTH_FAMILY_LIST = "refresh_health_family_list";
    public static final String REFRESH_HEALTH_HOME = "refresh_health_home";
    /**
     * AppKey：24784942     AppSecret：6ae2b8479dba6b71aabc5e797a9c5974
     * AppCode：c9f4bf5577184399a7d2ce04bba36bcc
     * 身份证识别key
     */
    public static final String OCR_IDCARD_APP_CODE = "c9f4bf5577184399a7d2ce04bba36bcc";
    /**
     * AppKey：24784942     AppSecret：6ae2b8479dba6b71aabc5e797a9c5974
     * AppCode：c9f4bf5577184399a7d2ce04bba36bcc
     * 驾驶证识别key
     */
    public static final String OCR_DRIVER_LICENSE = "c9f4bf5577184399a7d2ce04bba36bcc";
    /**
     * 印刷文字识别-行驶证识别（限时5折）
     * AppKey：24784942     AppSecret：6ae2b8479dba6b71aabc5e797a9c5974
     * AppCode：c9f4bf5577184399a7d2ce04bba36bcc
     * 行驶证识别key
     */
    public static final String OCR_VEHICLE = "c9f4bf5577184399a7d2ce04bba36bcc";


    //-----------------------------------------------------------------------------
    /**
     * 科大讯飞App_id
     */
    public static final String SPEAKE_APP_ID = "5a39b919";
    /**
     * 手机号码正则
     */
    public static final String PHONE_REGEX = "^1([3456789]\\d{9})$";
    /**
     * 商家入驻企业、个人、个体类型
     */
    public static final String MERCHANT_ENTER_STATUS = "merchant_enter_status";
    /**
     * 商家入驻线上或线下店铺
     */
    public static final String MERCHANT_ENTER_ON_OFF_LINE = "merchant_enter_on_off_line";
    /**
     * 信用通
     */
    public static final String XYT_JUMP_TO_RENEW = "xyt_jump_to_renew";
    public static final String XYT_JUMP_TO_RENEW_YEAR_XYT = "xyt_jump_to_renew_year_xyt";
    public static final String XYT_JUMP_TO_RENEW_YEAR_TECH = "xyt_jump_to_renew_year_tech";
    public static final String XYT_JUMP_TO_RENEW_MONEY_XYT = "xyt_jump_to_renew_money_xyt";
    public static final String XYT_JUMP_TO_RENEW_MONEY_TECH = "xyt_jump_to_renew_money_tech";
    public static final String XYT_RENEW_ALL = "xyt_renew_all";
    public static final String XYT_RENEW_XYT = "xyt_renew_xyt";
    public static final String XYT_RENEW_SERVICE = "xyt_renew_service";
    /**
     * 开店申请id
     */
    public static final String MERCHANT_APPLY_ID = "merchant_apply_id";
    /**
     * 审核状态 1表示已付费待审核，2表示审核通过，3表示审核拒绝
     */
    public static final String MERCHANT_CHECK_STATUS = "merchant_check_status";
    /**
     * 拒绝原因
     */
    public static final String MERCHANT_REFUSE_REASON = "merchant_refuse_reason";
    /**
     * 百度-高德-腾讯-搜狗的地图的包名
     */
    public static final String PACKAGE_BAIDU_MAP = "com.baidu.BaiduMap";
    public static final String PACKAGE_AMAP_MAP = "com.autonavi.minimap";
    public static final String PACKAGE_TENCENT_MAP = "com.tencent.map";
    public static final String PACKAGE_SOGOU_MAP = "com.sogou.map";
    /**
     * 状态栏透明度
     * 60的透明度
     */
    public static final int STATUS_BAR_ALPHA_60 = 60;
    /**
     * 状态栏透明度
     * 0透明度
     */
    public static final int STATUS_BAR_ALPHA_0 = 0;
    /**
     * JD商品详情是否刷新 在提交订单前会检查价格,若检查价格与原价格不符,则回到订单详情需要刷新详情数据
     */
    public static final String REFRESH_JD_GOODS_DETAIL = "refresh_jd_goods_detail";
    /**
     * 益联商家中心通知共享文件key值
     */
    public static final String KEY_MERCHANT_NOTICE = "key_merchant_notice";
    /**
     * 京东首页通知共享文件key值
     */
    public static final String KEY_JD_HOME_PAGE_NOTICE = "key_jd_home_page_notice";
    /**
     * 苏宁首页通知共享文件key值
     */
    public static final String KEY_SN_HOME_PAGE_NOTICE = "key_sn_home_page_notice";
    /**
     * 兑换券说明
     */
    public static final String EXCHANGE_TICKET_RECOMMEND = Ip.BASE_DEFAULT_WEB_URL + "help/agreementforios/dhq_note.html";
    /**
     * 兑换券常见问题
     */
    public static final String EXCHANGE_TICKET_FAQ = Ip.BASE_DEFAULT_WEB_URL + "help/agreementforios/dhq_questions.html";
    /**
     * 核销兑换券说明
     */
    public static final String VERIFICATION_EXCHANGE_TICKET_RECOMMEND = Ip.BASE_DEFAULT_WEB_URL + "help/agreementforios/hexiao_note.html";
    /**
     * 核销兑换券常见问题
     */
    public static final String VERIFICATION_EXCHANGE_TICKET_FAQ = Ip.BASE_DEFAULT_WEB_URL + "help/agreementforios/heixao_questions.html";
    /**
     * 待提取天使说明
     */
    public static final String EXTRACT_ANGEL_RECOMMEND = Ip.BASE_DEFAULT_WEB_URL + "help/agreementforios/angelExtractinStructions.html";
    /**
     * 待提取天使常见问题
     */
    public static final String EXTRACT_ANGEL_FAQ = Ip.BASE_DEFAULT_WEB_URL + "help/agreementforios/angleQuestion.html";

    public static final String DAI_GOU_QUAN_RECOMMEND = Ip.BASE_DEFAULT_WEB_URL + "/wechat/xieyi/archivesPrize.html";
    /**
     * ******************************************
     * ******************************************  携程  ******************************************
     * ******************************************
     */
    /**
     * 携程 携程说明文档
     */
    public static final String CTRIP_INSTRUCTIONS_TEXT = Ip.BASE_DEFAULT_WEB_URL + "help/html/xieSpecification.html";
    /**
     * 携程 发票
     */
    public static final String CTRIP_INVOICE = Ip.BASE_DEFAULT_WEB_URL + "help/html/xiechengFaPiao.html ";
    /**
     * 携程 收银台 温馨提示
     */
    public static final String CTRIP_PEYMENT_WARM_PROMPT = Ip.BASE_DEFAULT_WEB_URL + "help/html/xiechengWarmPrompt.html";
    /**
     * 携程 提交订单 联系益联客服
     */
    public static final String CTRIP_CONTACT_SERVICE = Ip.BASE_DEFAULT_WEB_URL + "help/html/xiechengFaPiao.html";

    /**
     * ****************************************** 参数设置信息开始 ******************************************
     */

    // 应用名称
    public static String APP_NAME = "";
    // 手机IMEI号码
    public static String IMEI = "";
    // 手机号码
    public static String TEL = "";
    /**
     * 现金支付
     */
    public static String PayCash = Ip.getBaseURL(LibApplication.getContextObject()) + "WapPayment/payRequest.php?";
    /**
     * 用户协议
     */
//    public static String UserAgreement = Ip.HELP_URL + "agreementforios/useragreement.html";
    public static String ImageUrl = "http://img.yilian.lefenmall.com/";//图片url
    public static String ImageSuffix = "?x-oss-process=image/resize,limit_0,w_120,h_120,m_fill";//图片后缀 ，m model ,w 和 h 的值控制图片清晰度 越大越清晰 相应的占用内存也越高
    //web网页穿参数  ？ &
    public static String PHOTO_TEXT_URL = Ip.getWechatURL(LibApplication.getContextObject()) + "merchant/packageImageIntro.php?package_id=";
    /**
     * 帮助
     */
    public static String UseHelp = getHelpURL() + "agreementforios/usehelp.html";
    /**
     * 融云Token
     **/
    public static String SPKEY_IM_Token = "imToken";
    public static String SPKEY_IM_USERID = "imUserID";
    /**
     * 推荐人昵称、ID
     */
    public static String SPKEY_REFERRER_NAME = "referrerName";
    public static String SPKEY_REFERRER_ID = "referrerID";
    /**
     * 自己的乐友群
     */
    public static String SPKEY_LOWER_GROUP_ID = "lowerGroupID";
    /**
     * 推荐人得乐友群
     */
    public static String SPKEY_HIGHER_GROUP_ID = "higherGropuID";
    /**
     * 是否有上级群
     */
    public static String SPKEY_HAVE_SUPER_GROUP = "haveSuperGroup";
    /**
     * 是否有下级群
     */
    public static String SPKEY_HAVE_LOWER_GROUP = "haveLowerGroup";
    /**
     * 新版商城一级分类 类别对象
     */
    public static String SPKEY_JP_CATEGORY_OBJECT = "categoryObject";
    /**
     * 什么是乐分宝
     */
    public static String LefenBaoWhy = getHelpURL() + "agreementforios/lefenbao.html";
    /**
     * 签到规则
     */
    public static String signRule = getHelpURL() + "agreementforios/user_sign.html";
    /**
     * 如何获得乐分
     */
    public static String ObtainLefen = getHelpURL() + "agreementforios/problem9.html ";
    /**
     * 活动规则
     */
    public static String ActivityRules = getHelpURL() + "agreementforios/activityrules.html";
    /**
     * 打卡活动规则
     */
    public static String ActClockRules = getHelpURL() + "agreementforios/activityRuler/clockIn.html";
    /**
     * 如何获取零购券
     */
    public static String HowToGetVoucher = getHelpURL() + "agreementforios/voucher.html";
    /**
     * 什么是销售奖券
     */
    public static String HowToGetCoupon = getHelpURL() + "agreementforios/coupon.html";
    /**
     * 拼团的退款说明
     */
    public static String spellGroupRefundExplain = Ip.getWechatURL(LibApplication.getContextObject()) + "/group/process.html";
    /**
     * 打开webview界面的url
     */
    public static String SPKEY_URL = "url";
    public static String SPKEY_JP_MAIN_HEADER_DATA = "jpMainFragmentHeaderData";
    public static String INTENT_KEY_JP_FILIALE_ID = "filiale_id";
    public static String INTENT_KEY_JP_GOODS_ID = "goods_id";
    public static String INTENT_KEY_JP_TAGS_NAME = "tags_name";
    public static String INTENT_KEY_ONE_BUY_ACTIVITY_ID = "activity_id";
    public static String INTENT_KEY_FLAGS_SHIP_ID = "index_id";
    /**
     * 一个键对应的四个值，用来判断跳转到主页时的目的地
     */
    public static String INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY = "jpMainActivity";
    /**
     * 存储首页一个活动banner和三个活动对象的sp键
     */
    public static String SPKEY_JP_MAIN_ACTIVITY = "jpMainActivity";
    public static String SPKEY_JP_MAIN_ONE_ACTIVITY = "jpMainOneActivity";
    public static String SPKEY_JP_MAIN_THREE_ACTIVITY = "jpMainThreeActivity";
    public static String SPKEY_JP_MAIN_NEWS_ACTIVITY = "jpMainNewsActivity";
    public static String SPKEY_JP_CATEGORY_GOODS = "jpCategoryGoods";
    public static String SP_FILE = "UserInfor";
    public static String SP_FILE2 = "UserInfor2";
    /**
     * 奖励分享时，微信分享回调的webview对象/支付回调字段
     */
    public static String SPKEY_HONG_BAO = "hongbao";
    public static String SPKEY_MT_PACKAGE_ORDER_ID = "mt_package_order_id";
    /**
     * 套餐评论分享
     */
    public static String SPKEY_COMMENT_SUCCESS = "comment_success";
    /**
     * 店内消费/扫码支付分享
     */
    public static String SPKEY_PAY_SUCCESS = "pay_success";
    /**
     * 易划算/奖券购 分类列表存储的key
     */
    public static String YHS_CATEGORY_LIST = "yhs_category_list";
    public static String JFG_CATEGORY_LIST = "jfg_category_list";
    /**
     * 京东首页底部tab分类角标
     */
    public static int JD_INDEX_CLASSIFY_FRAGMENT = 1;
    /**
     * 京东首页底部tab品牌精选角标
     */
    public static int JD_INDEX_BRAND_SELECTED_FRAGMENT = 2;
    /**
     * 京东首页底部tab购物车角标
     */
    public static int JD_INDEX_SHOPPING_CAR = 3;
    /**
     * app平台赠送名称
     */
    public static String APP_PLATFORM_DONATE_NAME = "益豆";
    /**
     * 提取营收列表页面类型，提取营收记录列表
     */
    public static int TYPE_EXTRACT_REVENUE_108 = 108;
    /**
     * 提取营收明细列表
     */
    public static int TYPE_EXTRACT_REVENUE_0 = 0;
}

