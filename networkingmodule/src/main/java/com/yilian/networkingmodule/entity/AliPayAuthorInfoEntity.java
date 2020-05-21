package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ASUS on 2017/4/12 0012.
 */

public class AliPayAuthorInfoEntity extends BaseEntity {

    /**
     * data : {"infoStr":"apiname=com.alipay.account.auth&app_id=2016092700604265&app_name=mc&auth_type=AUTHACCOUNT&biz_type=openservice&method=alipay.open.auth.sdk.code.get&pid=2088121659453679&product_id=APP_FAST_LOGIN&scope=kuaijie&sign_type=RSA2&sign=pK%2B65iJ3GIS3x%2BgepsZe6HRUifeKBkhbuLHdIYob9%2BPX2pxn3UvqDQq5Dt1RT%2BxkyE5d%2BuC4qrZ5GZ2hz%2FlmUJJkAwV21b%2BbhKJR3om2S7uJU3KA2fSy%2BWf%2BLLzH0JdgIMEMvwfmVNWix6fhTU%2FYowL2jDawuQ08TUkEOnADUqDKAwhbv1xAbXpc0Q%2BYtuIXYVg9mgtFYYJ4UbCkbceNKHqIVqw1uCh5fiMlaMNXe9JCY%2FawB5wGtvvHbPloN2sBqKHsx9fUCY9Kf1nhJwGwEsP86Xs24mT%2Bo8AOL2ONVrg4g%2FdOo%2BFOCzxJXvrexYFsVySGzO9qBuNsLhu%2BJPozpw%3D%3D"}
     * request : GET /app/account.php?c=alipay_oauth_request
     */

    @SerializedName("data")
    public DataBean data;
    @SerializedName("request")
    public String request;

    public static class DataBean {
        /**
         * infoStr : apiname=com.alipay.account.auth&app_id=2016092700604265&app_name=mc&auth_type=AUTHACCOUNT&biz_type=openservice&method=alipay.open.auth.sdk.code.get&pid=2088121659453679&product_id=APP_FAST_LOGIN&scope=kuaijie&sign_type=RSA2&sign=pK%2B65iJ3GIS3x%2BgepsZe6HRUifeKBkhbuLHdIYob9%2BPX2pxn3UvqDQq5Dt1RT%2BxkyE5d%2BuC4qrZ5GZ2hz%2FlmUJJkAwV21b%2BbhKJR3om2S7uJU3KA2fSy%2BWf%2BLLzH0JdgIMEMvwfmVNWix6fhTU%2FYowL2jDawuQ08TUkEOnADUqDKAwhbv1xAbXpc0Q%2BYtuIXYVg9mgtFYYJ4UbCkbceNKHqIVqw1uCh5fiMlaMNXe9JCY%2FawB5wGtvvHbPloN2sBqKHsx9fUCY9Kf1nhJwGwEsP86Xs24mT%2Bo8AOL2ONVrg4g%2FdOo%2BFOCzxJXvrexYFsVySGzO9qBuNsLhu%2BJPozpw%3D%3D
         */

        @SerializedName("infoStr")
        public String infoStr;
    }
}
