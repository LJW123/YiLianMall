package rxfamily.net;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;


public class ResponseHandler {

    public static String get(Throwable e) {
        if (e instanceof HttpException) {

            HttpException httpException = (HttpException) e;

            int code = httpException.code();

            if (code == 500 || code == 404) {
                return "服务器出错";
            }
        } else if (e instanceof ConnectException) {
            return "网络断开,请打开网络!";
        } else if (e instanceof SocketTimeoutException) {
            return "网络连接超时!";
        }

        return "发生未知错误" + e.getMessage();
    }
}
