package rxfamily.net;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by  on 2017/8/14 0014.
 * Rxjava请求网络时使用
 */

public class NetGsonConverterFactory extends Converter.Factory {
    private static Context context;
    private final Gson gson;

    private NetGsonConverterFactory(Gson gson) {
        if (gson == null) {
            throw new NullPointerException("gson==null");
        }
        this.gson = gson;
    }

    public static NetGsonConverterFactory create(Context context) {
        NetGsonConverterFactory.context = context;
        return create(new Gson());
    }

    public static NetGsonConverterFactory create(Gson gson) {
        return new NetGsonConverterFactory(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new CustomGsonResponseBodyConverter2<>(gson, adapter, context);

    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }
}
