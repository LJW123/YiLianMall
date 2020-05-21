package com.yilian.mall.utils;

import android.annotation.SuppressLint;
import android.text.Html;
import android.text.Spanned;

import com.orhanobut.logger.Logger;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * author XiuRenLi  PRAY NO BUG
 * Created by Administrator on 2016/8/8.
 */
public class MoneyUtil {

    /**
     * 把服务器返回的乐享币，除以100，并保留两位小数
     * 2017年7月27日17:13:14 因需求改变 所有货币显示都保留到小数点最后一位不为0的位数
     *
     * @param serverLeBi
     * @return
     */
    @Deprecated
    public static String getLeXiangBi(String serverLeBi) {
//        return String.format("%.2f", NumberFormat.convertToDouble(serverLeBi, 0.0) / 100);
        return getLeXiangBiNoZero(serverLeBi);
    }

    /**
     * 把服务器返回的乐享币，除以100，取得保留小数点后两位的值，之后再保留到小数点后最后一位不为0的数值
     *
     * @param serverLeBi
     * @return
     */
    public static String getLeXiangBiNoZero(String serverLeBi) {
        String format = String.format("%.2f", NumberFormat.convertToDouble(serverLeBi, 0.0) / 100);
        String[] split = format.split("\\.");
        if (split[1].equals("00")) {//如果小数点后两位都是0，则返回整数
            return split[0];
        } else {//如果小数点后两位不全是0
            if (Integer.valueOf(split[1]) % 10 == 0) {//如果小数点后两位组成的十位数除以10，余数为0，则返回小数点后一位
                return split[0] + "." + Integer.valueOf(split[1]) / 10;
            } else {//如果小数点后两位组成的数除以10，余数不为0，则返回小数点后两位。
                return format;
            }
        }
    }

    @Deprecated
    public static String getLeXiangBi(int serverLeBi) {
//         *2017年7月27日17:13:14 因需求改变 所有货币显示都保留到小数点最后一位不为0的位数

//        return String.format("%.2f", NumberFormat.convertToDouble(serverLeBi, 0.0) / 100);
        return getLeXiangBiNoZero(serverLeBi);
    }

    public static String getLeXiangBiNoZero(int serverLeBi) {
        String format = String.format("%.2f", NumberFormat.convertToDouble(serverLeBi, 0.0) / 100);
        String[] split = format.split("\\.");
        if (split[1].equals("00")) {//如果小数点后两位都是0，则返回整数
            return split[0];
        } else {//如果小数点后两位不全是0

            if (Integer.valueOf(split[1]) % 10 == 0) {//如果小数点后两位组成的十位数除以10，余数为0，则返回小数点后一位
                return split[0] + "." + Integer.valueOf(split[1]) / 10;
            } else {//如果小数点后两位组成的数除以10，余数不为0，则返回小数点后两位。
                return format;
            }
        }
    }

    @Deprecated
    public static String getLeXiangBi(double serverLeBi) {
        //         *2017年7月27日17:13:14 因需求改变 所有货币显示都保留到小数点最后一位不为0的位数

//        return String.format("%.2f", NumberFormat.convertToDouble(serverLeBi, 0.0) / 100);
        return getLeXiangBiNoZero(serverLeBi);
    }

    public static String getLeXiangBiNoZero(double serverLeBi) {
        String format = String.format("%.2f", NumberFormat.convertToDouble(serverLeBi, 0.0) / 100);
        String[] split = format.split("\\.");
        if (split[1].equals("00")) {//如果小数点后两位都是0，则返回整数
            return split[0];
        } else {//如果小数点后两位不全是0

            if (Integer.valueOf(split[1]) % 10 == 0) {//如果小数点后两位组成的十位数除以10，余数为0，则返回小数点后一位
                return split[0] + "." + Integer.valueOf(split[1]) / 10;
            } else {//如果小数点后两位组成的数除以10，余数不为0，则返回小数点后两位。
                return format;
            }
        }
    }

    /**
     * 把服务器返回的乐享币，除以100，不保留两位小数
     *
     * @param serverLeBi
     * @return
     */
    public static String getLeXiangBiNoDecimal(String serverLeBi) {
        return String.valueOf(NumberFormat.convertToInt(serverLeBi, 0) / 100);
    }

    public static String getLeXiangBiNoDecimal(int serverLeBi) {
        return String.valueOf(NumberFormat.convertToInt(serverLeBi, 0) / 100);
    }

    public static String getLeXiangBiNoDecimal(double serverLeBi) {
        return String.valueOf(NumberFormat.convertToInt(serverLeBi, 0) / 100);
    }

    /**
     * 把服务器返回的抵扣券，除以100，并保留两位小数
     *
     * @param serverXianJin
     * @return
     */
    @Deprecated
    public static String getXianJinQuan(String serverXianJin) {
        //2017年7月27日17:29:22 需求修改 货币金额保留到小数点后最后一位不为零的数
        return getLeXiangBiNoZero(serverXianJin);
//        return String.format("%.2f", NumberFormat.convertToDouble(serverXianJin, 0.0) / 100);

    }

    @Deprecated
    public static String getXianJinQuan(int serverXianJin) {
        //2017年7月27日17:29:22 需求修改 货币金额保留到小数点后最后一位不为零的数
        return getLeXiangBiNoZero(serverXianJin);
//        return String.format("%.2f", NumberFormat.convertToDouble(serverXianJin, 0.0) / 100);
    }

    public static String getXianJinQuan(double serverXianJin) {
        //2017年7月27日17:29:22 需求修改 货币金额保留到小数点后最后一位不为零的数
        return getLeXiangBiNoZero(serverXianJin);
//        return String.format("%.2f", NumberFormat.convertToDouble(serverXianJin, 0.0) / 100);
    }

    /**
     * 把服务器返回的抵扣券，除以100，不保留两位小数
     *
     * @param serverXianJin
     * @return
     */
    public static String getXianJinQuanNoDecimal(String serverXianJin) {
        return String.valueOf(NumberFormat.convertToInt(serverXianJin, 0) / 100);
    }


    public static String getXianJinQuanNoDecimal(int serverXianJin) {
        return String.valueOf(NumberFormat.convertToInt(serverXianJin, 0) / 100);
    }

    public static String getXianJinQuanNoDecimal(double serverXianJin) {
        return String.valueOf(NumberFormat.convertToInt(serverXianJin, 0) / 100);
    }


    /**
     * 把服务器返回的乐分币后面添加上“.00”
     *
     * @param serverLeFen
     * @return
     */
    public static String getLeFenBi(String serverLeFen) {
        return serverLeFen + ".00";
    }


    public static String getLeFenBi(double serverLeFen) {
        return String.valueOf(serverLeFen) + ".00";
    }

    public static String getLeFenBi(int serverLeFen) {
        return String.valueOf(serverLeFen) + ".00";
    }

    /**
     * 货币小数点后两位字体缩小
     *
     * @param money
     * @return
     */
    public static Spanned setMoney(String money) {
        if (money.contains(".")) {
            Logger.i("money：" + money);
            String[] split = money.split("\\.");
            Logger.i("split To String:" + split[0] + "   " + split[1]);
            return Html.fromHtml("<font  ><small><small></small></small></font>" + split[0] + "." + "<font  ><small><small>" + split[1] + "</small></small></font>");
        }
        return Html.fromHtml("<font  ><small><small>¥</small></small></font>" + money);
    }

    /**
     * 修改货币后奖券字体变小
     *
     * @param money
     * @return
     */
    public static Spanned setSmallIntegralMoney(String money) {
        if (money.contains(".")) {
//            Logger.i("money：" + money);
            String[] split = money.split("\\.");
//            Logger.i("split To String:" + split[0] + "   " + split[1]);
            return Html.fromHtml(split[0] + "." + split[1] + " <font  ><small><small>奖券</small></small></font>");
        }
        return Html.fromHtml(money + " <font><small><small>奖券</small></small></font>");
    }

    /**
     * 货币小数点后两位字体缩小
     * 因需求改变，小数点后两位数字不再缩小
     * 使用setNoSmall¥Money替换
     *
     * @param money
     * @return
     */
    @Deprecated
    public static Spanned set¥Money(String money) {
//       2017年7月27日17:14:27 因需求改变，小数点后两位数字不再缩小
//        if (money.contains(".")) {
////            Logger.i("money：" + money);
//            String[] split = money.split("\\.");
////            Logger.i("split To String:" + split[0] + "   " + split[1]);
//            return Html.fromHtml("<font  ><small><small>¥</small></small></font>" + split[0] + "." + "<font  ><small><small>" + split[1] + "</small></small></font>");
//        }
//        return Html.fromHtml("<font  ><small><small>¥</small></small></font>" + money);
        return setNoSmall¥Money(money);
    }


    /**
     * 货币小数点后两位字体不变
     *
     * @param money
     * @return
     */
    public static Spanned setNoSmall¥Money(String money) {
        String defaultMoney = "";
        if (money.contains(".")) {
//            Logger.i("money：" + money);
            String[] split = money.split("\\.");
//            Logger.i("split To String:" + split[0] + "   " + split[1]);
            defaultMoney = "<font  ><small>¥</small></font>" + split[0] + "." + split[1];
            return Html.fromHtml(defaultMoney);
        }
        defaultMoney = "<font  ><small>¥</small></font>" + money;
        return Html.fromHtml(defaultMoney);
    }

    /**
     * 符号和数字一样大
     *
     * @param money
     * @return
     */
    public static Spanned setNoSmallMoney(String money) {
        if (money.contains(".")) {
//            Logger.i("money：" + money);
            String[] split = money.split("\\.");
//            Logger.i("split To String:" + split[0] + "   " + split[1]);
            return Html.fromHtml("¥" + split[0] + "." + split[1]);
        }
        return Html.fromHtml("¥" + money);
    }

    /**
     * 改变小数点前面的数字大小
     *
     * @param money
     * @return
     */
    public static Spanned setBigBeforeDecimal(String money) {
        if (money.contains(".")) {
            String[] split = money.split("\\.");
            return Html.fromHtml("<font><big><big>" + split[0] + "</big></big></font>." + split[1]);
        }
        return Html.fromHtml("<big><big>" + money + "</big></big>");
    }

    /**
     * 添加人民币符号并保留小数点后两位 不四舍五入
     *
     * @param serverMoney
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String getMoneyWith¥Floor(String serverMoney) {
        final DecimalFormat formatter = new DecimalFormat("#0.##");
        formatter.setRoundingMode(RoundingMode.FLOOR);
        double v = NumberFormat.convertToDouble(serverMoney, 0.0);
        String format1 = String.format("%.2f", v / 100);
        return "¥" + formatter.format(NumberFormat.convertToDouble(format1, 0d));
    }

    @SuppressLint("DefaultLocale")
    public static String getMoneyWith¥(String serverLeBi) {
        return "¥" + String.format("%.2f", NumberFormat.convertToDouble(serverLeBi, 0.0) / 100);
    }
}
