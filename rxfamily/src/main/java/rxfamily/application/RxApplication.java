package rxfamily.application;


import android.app.Application;


public class RxApplication extends Application {

    private static RxApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public synchronized static RxApplication getInstance() {
        return sInstance;
    }
}
