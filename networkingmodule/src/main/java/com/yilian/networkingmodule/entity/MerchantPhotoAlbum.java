package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuyuqi on 2017/5/5 0005.
 */
public class MerchantPhotoAlbum extends BaseEntity {

    /**
     * data : {"album":["/merchant/images/d755e158030a8ca091c3b2e2cfb2f2dc75f090cf_2 - 副本.jpg","/merchant/images/322472d5fc0c382eb0d0d7362b0cca87ece66c77_1.jpg","/merchant/images/dbdd030b01541b95f4ece77fc11c5d779f835bbe_2.jpg","/merchant/images/90a2b62844ff3af722b6b53780577a7bb807cdab_3 - 副本.jpg","/merchant/images/57269a03458d27702ec597b0be416e040395f3c0_11_04211751_03s.jpg","/merchant/images/f09d0a074d0088879fbfe1974b38070047a0305c_3.jpg","/merchant/images/8fd1eca2c9a3801f2428c141a3b3fb11334da1ae_11_04211751_35s.jpg","/merchant/images/3d89b4b1f728572ae87d7c37f6b91ce1b307767a_1000 (1).jpg","/merchant/images/2cc11654febf1d06ee693425d16fea7eeaab9df9_4 - 副本.jpg","/merchant/images/ad78eed5846fa9b646bfe215c334df584cebfbe9_2 - 副本.jpg","/merchant/images/41d1730ef87980c697e695b4f526ae16182ac888_1.jpg","/merchant/images/5b527454ffea9cec80db6f889f4520defb70e3b8_2.jpg","/merchant/images/c7a15c4a3449be88d5c252b6274a77b37981df0e_3 - 副本.jpg","/merchant/images/e69148e14360888703eda049eb7666108d3e6af7_3.jpg","/merchant/images/a495518b51a50c3b00bf363000c59615f820abd6_5 (1)_b.jpg","/merchant/images/387f2ce78bd925a0636019e9717eb8c79d4d112e_4是是是(1).jpg","/merchant/images/128181a532e4ef25a973360414a5d49b22079970_9.jpg","/merchant/images/cf73ed351127b2dd94c431436b9366175cdb98ea_10 (1).jpg","/merchant/images/37166e40a16ba5c26f4d9171e5c4600ccfc7c9ea_11_04211751_03s.jpg","/merchant/images/4c94878ce4f6da28a8761767819aed6673249e11_11_04211751_35s.jpg"]}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        @SerializedName("album")
        public List<String> album;
    }
}
