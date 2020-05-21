package com.yilian.networkingmodule.entity.suning;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by zhaiyaohua on 2018/5/22.
 */

public class SnGoodsListEntity extends HttpResultBean {

    /**
     * data : [{"id":"22823","sku":"5256819","name":"水星家纺 床上四件套纯棉 全棉斜纹床品套件 简约被套床单被罩 倾颜倾羽 加大双人1.8米床","weight":"2.67","imagePath":"jfs/t10453/180/1197521679/146844/3404cdbc/59dda53aN39175e5c.jpg","state":"1","brandName":"水星（MERCURY）","productArea":"上海","upc":"6933955907596","saleUnit":" ","one_category":"15248","two_category":"15249","three_category":"15252","price":"299.00","jdPrice":"1089.00","marketPrice":"1199.00","return_bean":"790.00","sale":"1014","rate":"72.54"}]
     * page : 46
     */

    @SerializedName("page")
    public int page;
    @SerializedName("data")
    public List<SnGoodsAbstractInfoEntity> data;

    public List<SnGoodsAbstractInfoEntity> getData() {
        if (data == null) {
            return new ArrayList<>();
        }
        return  data;
    }
}
