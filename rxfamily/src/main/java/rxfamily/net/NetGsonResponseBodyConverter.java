package rxfamily.net;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import rxfamily.entity.BaseEntity;

/**
 *
 * @author
 * @date 2017/8/14 0014
 * Rxjava请求网络时使用
 */

public class NetGsonResponseBodyConverter<T extends BaseEntity> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    public NetGsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        BaseEntity httpResultBean = gson.fromJson(response, BaseEntity.class);
        try {
            int code = httpResultBean.code;
            if (code == 1) {
                return gson.fromJson(response, type);
            } else if (code == -4 || code == -23) {
                throw new IOException("账号已掉线,请重新登录");
            } else {
                throw new IOException(httpResultBean.msg);
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        } finally {
            value.close();
        }
    }
}
