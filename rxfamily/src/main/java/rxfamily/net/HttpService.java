package rxfamily.net;

import android.content.Context;

import com.yilian.mylibrary.RequestOftenKey;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rxfamily.BuildConfig;
import rxfamily.application.RxApplication;
import rxfamily.utils.NetWorkUtils;

/**
 * 初始化Http请求实例的类
 *
 * @author ZYH
 */

public class HttpService {

    public final static int CONNECT_TIMEOUT = 30;
    public final static int READ_TIMEOUT = 30;
    public final static int WRITE_TIMEOUT = 30;

    private volatile static HttpService INSTANCE;
    private Retrofit httpService;
    private Context mContext;


    public HttpService(final Context mContext, String base_url, Boolean use_cache) {
        this.mContext = mContext;

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        //RequestOftenKey.getToken(mContext)
        //RequestOftenKey.getDeviceIndex(mContext)
        httpClient.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging)//开启OKHttp的日志拦截
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request oldRequest = chain.request();
                        HttpUrl.Builder builder = oldRequest.url().newBuilder()
                                .scheme(oldRequest.url().scheme())
                                .host(oldRequest.url().host())
                                .addQueryParameter("token", RequestOftenKey.getToken(mContext))
                                .addQueryParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
                        Request newRequest = oldRequest.newBuilder().method(oldRequest.method(), oldRequest.body())
                                .url(builder.build())
                                .build();
                        return chain.proceed(newRequest);
                    }
                });

        if (use_cache) {
            //设置缓存路径`
            final File httpCacheDirectory = new File(RxApplication.getInstance().getCacheDir(), "okhttpCache");
            //Log.d("httpCacheDirectory", RxApplication.getInstance().getCacheDir().getAbsolutePath());
            Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);//缓存可用大小为10M

            httpClient.addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                    .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                    .cache(cache);
        }

        httpService = new Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(NetGsonConverterFactory.create(mContext))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient.build())
                .build();
    }

    public static HttpService getInstance(Context mContext, String base_url, Boolean use_cache) {
        if (INSTANCE == null) {
            synchronized (HttpService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpService(mContext, base_url, use_cache);
                }
            }
        }
        return INSTANCE;
    }

    public Retrofit getHttpService() {
        return httpService;
    }


    /**
     * 缓存
     */
    private final static Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            //获取网络状态
            int netWorkState = NetWorkUtils.getNetworkState(RxApplication.getInstance());
            //无网络，请求强制使用缓存
            if (netWorkState == NetWorkUtils.NETWORK_NONE) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response originalResponse = chain.proceed(request);

            switch (netWorkState) {
                case NetWorkUtils.NETWORK_MOBILE://mobile network 情况下缓存一分钟
                    int maxAge = 60;
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();

                case NetWorkUtils.NETWORK_WIFI://wifi network 情况下不使用缓存
                    maxAge = 0;
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();

                case NetWorkUtils.NETWORK_NONE://none network 情况下离线缓存4周
                    int maxStale = 60 * 60 * 24 * 4 * 7;
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                default:
                    throw new IllegalStateException("network state is Erro!");
            }
        }
    };
}
