package com.yilian.mall.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author xiaofo on 2018/9/14.
 */

public class CtripIconUtil {
    public static String[] imgs = new String[]{
            "ctripIcon/ss1.png",
            "ctripIcon/ss2.png",
            "ctripIcon/ss3.png",
            "ctripIcon/ss4.png",
            "ctripIcon/ss5.png",
            "ctripIcon/ss6.png",
            "ctripIcon/ss7.png",
            "ctripIcon/ss8.png",
            "ctripIcon/ss9.png",
            "ctripIcon/ss10.png",
            "ctripIcon/ss11.png",
            "ctripIcon/ss12.png",
            "ctripIcon/ss13.png",
            "ctripIcon/ss14.png",
            "ctripIcon/ss15.png",
            "ctripIcon/ss16.png",
            "ctripIcon/ss17.png",
            "ctripIcon/ss18.png",
            "ctripIcon/ss19.png",
            "ctripIcon/ss20.png",
            "ctripIcon/ss21.png",
            "ctripIcon/ss22.png",
            "ctripIcon/ss23.png",
            "ctripIcon/ss24.png",
            "ctripIcon/ss25.png",
            "ctripIcon/ss26.png",
            "ctripIcon/ss27.png",
            "ctripIcon/ss28.png",
            "ctripIcon/ss29.png",
            "ctripIcon/ss30.png",
            "ctripIcon/ss31.png",
            "ctripIcon/ss32.png",
            "ctripIcon/ss33.png",
            "ctripIcon/ss34.png",
            "ctripIcon/ss35.png",
            "ctripIcon/ss36.png",
            "ctripIcon/ss37.png",
            "ctripIcon/ss38.png",
            "ctripIcon/ss39.png",
            "ctripIcon/ss40.png",
            "ctripIcon/ss41.png",
            "ctripIcon/ss42.png",
            "ctripIcon/ss43.png",
            "ctripIcon/ss44.png",
            "ctripIcon/ss45.png",
            "ctripIcon/ss46.png",
            "ctripIcon/ss47.png",
            "ctripIcon/ss48.png",
            "ctripIcon/ss49.png",
            "ctripIcon/ss50.png",
            "ctripIcon/ss51.png",
            "ctripIcon/ss52.png",
            "ctripIcon/ss53.png",
            "ctripIcon/ss54.png",
            "ctripIcon/ss55.png",
            "ctripIcon/ss56.png",
            "ctripIcon/ss57.png",
            "ctripIcon/ss58.png",
            "ctripIcon/ss59.png",
            "ctripIcon/ss60.png",
            "ctripIcon/ss61.png",
            "ctripIcon/ss62.png",
            "ctripIcon/ss63.png",
            "ctripIcon/ss64.png",
            "ctripIcon/ss65.png",
            "ctripIcon/ss66.png",
            "ctripIcon/ss67.png",
            "ctripIcon/ss68.png",
            "ctripIcon/ss69.png",
            "ctripIcon/ss70.png",
            "ctripIcon/ss71.png",
            "ctripIcon/ss72.png",
            "ctripIcon/ss73.png",
            "ctripIcon/ss74.png",
            "ctripIcon/ss75.png",
            "ctripIcon/ss76.png",
            "ctripIcon/ss77.png",
            "ctripIcon/ss78.png",
            "ctripIcon/ss79.png",
            "ctripIcon/ss80.png",
            "ctripIcon/ss81.png",
            "ctripIcon/ss82.png",
            "ctripIcon/ss83.png",
            "ctripIcon/ss84.png",
            "ctripIcon/ss85.png",
            "ctripIcon/ss86.png",
            "ctripIcon/ss87.png",
            "ctripIcon/ss88.png",
            "ctripIcon/ss89.png",
            "ctripIcon/ss90.png",
            "ctripIcon/ss91.png",
            "ctripIcon/ss92.png",
            "ctripIcon/ss93.png",
            "ctripIcon/ss94.png",
            "ctripIcon/ss95.png",
            "ctripIcon/ss96.png",
            "ctripIcon/ss97.png",
            "ctripIcon/ss98.png",
            "ctripIcon/ss99.png",
            "ctripIcon/ss100.png",
            "ctripIcon/ss101.png",
            "ctripIcon/ss102.png",
            "ctripIcon/ss103.png",
            "ctripIcon/ss104.png",
            "ctripIcon/ss105.png",
            "ctripIcon/ss106.png",
            "ctripIcon/ss107.png",
            "ctripIcon/ss108.png",
            "ctripIcon/ss109.png",
            "ctripIcon/ss110.png",
            "ctripIcon/ss111.png",
            "ctripIcon/ss112.png",
            "ctripIcon/ss113.png",
            "ctripIcon/ss114.png",
            "ctripIcon/ss115.png",
            "ctripIcon/ss116.png",
            "ctripIcon/ss117.png",
            "ctripIcon/ss118.png",
            "ctripIcon/ss119.png",
            "ctripIcon/ss120.png",
            "ctripIcon/ss121.png",
            "ctripIcon/ss122.png",
            "ctripIcon/ss123.png",
            "ctripIcon/ss124.png",
            "ctripIcon/ss125.png",
            "ctripIcon/ss126.png",
            "ctripIcon/ss127.png",
            "ctripIcon/ss128.png",
            "ctripIcon/ss129.png",
            "ctripIcon/ss130.png",
            "ctripIcon/ss131.png",
            "ctripIcon/ss132.png",
            "ctripIcon/ss133.png",
            "ctripIcon/ss134.png",
            "ctripIcon/ss135.png",
            "ctripIcon/ss136.png",
            "ctripIcon/ss137.png",
            "ctripIcon/ss138.png",
            "ctripIcon/ss139.png",
            "ctripIcon/ss140.png",
            "ctripIcon/ss141.png",
            "ctripIcon/ss142.png",
            "ctripIcon/ss143.png",
            "ctripIcon/ss144.png",
            "ctripIcon/ss145.png",
            "ctripIcon/ss146.png",
            "ctripIcon/ss147.png",
            "ctripIcon/ss148.png",
            "ctripIcon/ss149.png",
            "ctripIcon/ss150.png",
            "ctripIcon/ss151.png",
            "ctripIcon/ss152.png",
            "ctripIcon/ss153.png",
            "ctripIcon/ss154.png",
            "ctripIcon/ss155.png",
            "ctripIcon/ss156.png",
            "ctripIcon/ss157.png",
            "ctripIcon/ss158.png",
            "ctripIcon/ss159.png",
            "ctripIcon/ss160.png",
            "ctripIcon/ss161.png",
            "ctripIcon/ss162.png",
            "ctripIcon/ss163.png",
            "ctripIcon/ss164.png",
            "ctripIcon/ss165.png",
            "ctripIcon/ss166.png",
            "ctripIcon/ss167.png",
            "ctripIcon/ss168.png",
            "ctripIcon/ss169.png",
            "ctripIcon/ss170.png",
            "ctripIcon/ss171.png",
            "ctripIcon/ss172.png",
            "ctripIcon/ss173.png",
            "ctripIcon/ss174.png",
            "ctripIcon/ss175.png",
            "ctripIcon/ss176.png",
            "ctripIcon/ss177.png",
            "ctripIcon/ss178.png",
            "ctripIcon/ss179.png",
            "ctripIcon/ss180.png",
            "ctripIcon/ss181.png",
            "ctripIcon/ss182.png",
            "ctripIcon/ss183.png",
            "ctripIcon/ss184.png",
            "ctripIcon/ss185.png",
            "ctripIcon/ss186.png",
            "ctripIcon/ss187.png",
            "ctripIcon/ss188.png",
            "ctripIcon/ss189.png",
            "ctripIcon/ss190.png",
            "ctripIcon/ss191.png",
            "ctripIcon/ss192.png",
            "ctripIcon/ss193.png",
            "ctripIcon/ss194.png",
            "ctripIcon/ss195.png",
            "ctripIcon/ss196.png",
            "ctripIcon/ss197.png",
            "ctripIcon/ss198.png",
            "ctripIcon/ss199.png",
            "ctripIcon/ss200.png",
            "ctripIcon/ss201.png",
            "ctripIcon/ss202.png",
            "ctripIcon/ss203.png",
            "ctripIcon/ss204.png",
            "ctripIcon/ss205.png",
            "ctripIcon/ss206.png",
            "ctripIcon/ss207.png",
            "ctripIcon/ss208.png",
            "ctripIcon/ss209.png",
            "ctripIcon/ss210.png",
            "ctripIcon/ss211.png",
            "ctripIcon/ss212.png",
            "ctripIcon/ss213.png",
            "ctripIcon/ss214.png",
            "ctripIcon/ss215.png",
            "ctripIcon/ss216.png",
            "ctripIcon/ss217.png",
            "ctripIcon/ss218.png",
            "ctripIcon/ss219.png",
            "ctripIcon/ss220.png",
            "ctripIcon/ss221.png",
            "ctripIcon/ss222.png",
            "ctripIcon/ss223.png",
            "ctripIcon/ss224.png",
            "ctripIcon/ss225.png",
            "ctripIcon/ss226.png",
            "ctripIcon/ss227.png",
            "ctripIcon/ss228.png",
            "ctripIcon/ss229.png",
            "ctripIcon/ss230.png",
            "ctripIcon/ss231.png",
            "ctripIcon/ss232.png",
            "ctripIcon/ss233.png",
            "ctripIcon/ss234.png",
            "ctripIcon/ss235.png",
            "ctripIcon/ss236.png",
            "ctripIcon/ss237.png",
            "ctripIcon/ss238.png",
            "ctripIcon/ss239.png",
            "ctripIcon/ss240.png",
            "ctripIcon/ss241.png",
            "ctripIcon/ss242.png",
            "ctripIcon/ss243.png",
            "ctripIcon/ss244.png",
            "ctripIcon/ss245.png",
            "ctripIcon/ss246.png",
            "ctripIcon/ss247.png",
            "ctripIcon/ss248.png",
            "ctripIcon/ss249.png",
            "ctripIcon/ss250.png",
            "ctripIcon/ss251.png",
            "ctripIcon/ss252.png",
            "ctripIcon/ss253.png",
            "ctripIcon/ss254.png",
            "ctripIcon/ss255.png",
            "ctripIcon/ss256.png",
            "ctripIcon/ss257.png",
            "ctripIcon/ss258.png",
            "ctripIcon/ss259.png",
            "ctripIcon/ss260.png",
            "ctripIcon/ss261.png",
            "ctripIcon/ss262.png",
            "ctripIcon/ss263.png",
            "ctripIcon/ss264.png",
            "ctripIcon/ss265.png",
            "ctripIcon/ss266.png",
            "ctripIcon/ss267.png",
            "ctripIcon/ss268.png",
            "ctripIcon/ss269.png",
            "ctripIcon/ss270.png",
            "ctripIcon/ss271.png",
            "ctripIcon/ss272.png",
            "ctripIcon/ss273.png",
            "ctripIcon/ss274.png",
            "ctripIcon/ss275.png",
            "ctripIcon/ss276.png",
            "ctripIcon/ss277.png",
            "ctripIcon/ss278.png",
            "ctripIcon/ss279.png",
            "ctripIcon/ss280.png",
            "ctripIcon/ss281.png",
            "ctripIcon/ss282.png",
            "ctripIcon/ss283.png",
            "ctripIcon/ss284.png",
            "ctripIcon/ss285.png",
            "ctripIcon/ss286.png",
            "ctripIcon/ss287.png",
            "ctripIcon/ss288.png",
            "ctripIcon/ss289.png",
            "ctripIcon/ss290.png",
            "ctripIcon/ss291.png",
            "ctripIcon/ss292.png",
            "ctripIcon/ss293.png",
            "ctripIcon/ss294.png",
            "ctripIcon/ss295.png",
            "ctripIcon/ss296.png",
            "ctripIcon/ss297.png",
            "ctripIcon/ss298.png",
            "ctripIcon/ss299.png",
            "ctripIcon/ss300.png",
            "ctripIcon/ss301.png",
            "ctripIcon/ss302.png",
            "ctripIcon/ss303.png",
            "ctripIcon/ss304.png",
            "ctripIcon/ss305.png",
            "ctripIcon/ss306.png",
            "ctripIcon/ss307.png",
            "ctripIcon/ss308.png",
            "ctripIcon/ss309.png",
            "ctripIcon/ss310.png",
            "ctripIcon/ss311.png",
            "ctripIcon/ss312.png",
            "ctripIcon/ss313.png",
            "ctripIcon/ss314.png",
            "ctripIcon/ss315.png",
            "ctripIcon/ss316.png",
            "ctripIcon/ss317.png",
            "ctripIcon/ss318.png",
            "ctripIcon/ss319.png",
            "ctripIcon/ss320.png",
            "ctripIcon/ss321.png",
            "ctripIcon/ss322.png",
            "ctripIcon/ss323.png",
            "ctripIcon/ss324.png",
            "ctripIcon/ss325.png",
            "ctripIcon/ss326.png",
            "ctripIcon/ss327.png",
            "ctripIcon/ss328.png",
            "ctripIcon/ss329.png",
            "ctripIcon/ss330.png",
            "ctripIcon/ss331.png",
            "ctripIcon/ss332.png",
            "ctripIcon/ss333.png",
            "ctripIcon/ss334.png",
    };

    /**
     * 从Assets中读取图片
     *
     * @param index
     * @param context
     * @return
     */
    public static Bitmap getImageFromAssetsFile(int index, Context context) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(imgs[index]);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }
}
