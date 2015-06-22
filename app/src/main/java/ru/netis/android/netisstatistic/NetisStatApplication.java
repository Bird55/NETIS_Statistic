package ru.netis.android.netisstatistic;

import android.app.Application;

import java.net.CookieManager;
import java.net.CookiePolicy;

public class NetisStatApplication extends Application {

    public static final String TAG = NetisStatApplication.class.getSimpleName();

    private CookieManager mCookieManager;

    private static NetisStatApplication mInstance;

    public static synchronized NetisStatApplication getInstance() {
        return NetisStatApplication.mInstance;
    }

    public CookieManager getCookieManager() {
        if (mCookieManager == null) {
            mCookieManager = (CookieManager) CookieManager.getDefault();
            if (mCookieManager == null) {
                mCookieManager = new CookieManager();
                mCookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
                CookieManager.setDefault(mCookieManager);
            }
        }
        return mCookieManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NetisStatApplication.mInstance = this;
    }
}