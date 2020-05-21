package com.yilian.mall.jd.utils;

/**
 * @author Created by  on 2018/5/22.
 *         京东图片
 *         http://img13.360buyimg.com/n0/
 *         其中 n0(最大图 800*800px)、n1(350*350px)、n2(160*160px)、n3(130*130px)、n4(100*100px) 为图片 大小。
 *         例如接口返回 imagePath 为
 *         g8/M03/0E/06/rBEHaFCg5wQIAAAAAAGhG73oiLUAACxswH4MBwAAaEz619.jpg 拼接后的
 *         url 为:
 *         http://img13.360buyimg.com/n0/g8/M03/0E/06/rBEHaFCg5wQIAAAAAAGhG73oiLUAACxswH4MBwAA aEz619.jpg
 */

public class JDImageUtil {
    /**
     * n0(最大图 800*800px)、n1(350*350px)、n2(160*160px)、n3(130*130px)、n4(100*100px) 为图片 大小。
     */
    static String[] size = new String[]{"n0", "n1", "n2", "n3", "n4"};
    /**
     * 图片前缀
     */
    static String JDImageUrlPrefix = "http://img13.360buyimg.com/";

    /**
     * 获取N0尺寸图片
     *
     * @param imageUrl
     * @return
     */
    public static String getJDImageUrl_N0(String imageUrl) {
        return getImageUrl(size[0], imageUrl);
    }

    private static String getImageUrl(String size, String imageUrl) {
        return getJDImageUrlPrefix() + size +"/"+ imageUrl;
    }

    private static String getJDImageUrlPrefix() {
        return JDImageUrlPrefix;
    }

    /**
     * 获取N1尺寸图片
     *
     * @param imageUrl
     * @return
     */
    public static String getJDImageUrl_N1(String imageUrl) {
        return getImageUrl(size[1], imageUrl);

    }

    /**
     * 获取N2尺寸图片
     *
     * @param imageUrl
     * @return
     */
    public static String getJDImageUrl_N2(String imageUrl) {
        return getImageUrl(size[2], imageUrl);

    }

    /**
     * 获取N3尺寸图片
     *
     * @param imageUrl
     * @return
     */
    public static String getJDImageUrl_N3(String imageUrl) {
        return getImageUrl(size[3], imageUrl);

    }

    /**
     * 获取N40尺寸图片
     *
     * @param imageUrl
     * @return
     */
    public static String getJDImageUrl_N4(String imageUrl) {
        return getImageUrl(size[4], imageUrl);
    }
}
