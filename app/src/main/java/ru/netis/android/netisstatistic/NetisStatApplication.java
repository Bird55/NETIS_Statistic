package ru.netis.android.netisstatistic;

import android.app.Application;

import ru.netis.android.netisstatistic.util.MyVolley;

/**
 * Application class for the demo. Used to ensure that MyVolley is initialized. {@see MyVolley}
 * @author Ognyan Bankov
 *
 */
public class NetisStatApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }


    private void init() {
        MyVolley.init(this);
    }
}
