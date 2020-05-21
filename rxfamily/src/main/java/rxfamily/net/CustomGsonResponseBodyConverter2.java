package rxfamily.net;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.yilian.mylibrary.CodeException;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import rxfamily.entity.BaseEntity;

/**
 * Created by  on 2017/8/14 0014.
 * Rxjava请求网络时使用
 */

final class CustomGsonResponseBodyConverter2<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private Context context;

    CustomGsonResponseBodyConverter2(Gson gson, TypeAdapter<T> adapter, Context context) {
        this.gson = gson;
        this.adapter = adapter;
        this.context = context;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        BaseEntity httpStatus = gson.fromJson(response, BaseEntity.class);
        int code = httpStatus.code;
        if (code != 1) {
            if (code == -4 || code == -23) {
                //刷新个人页面标识
                PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, context);
            }
            value.close();
            throw new CodeException(httpStatus.msg, httpStatus.code);
        }

        MediaType contentType = value.contentType();
        Charset charset = contentType != null ? contentType.charset(Charset.forName("UTF-8")) : Charset.forName("UTF-8");
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);

        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}

