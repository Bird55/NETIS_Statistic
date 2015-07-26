package ru.netis.android.netisstatistic;

import android.os.Bundle;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import ru.netis.android.netisstatistic.fragments.BaseSetFragment;
import ru.netis.android.netisstatistic.helpers.BaseSet;
import ru.netis.android.netisstatistic.tools.AsyncTaskListener;
import ru.netis.android.netisstatistic.tools.HttpHelper;
import ru.netis.android.netisstatistic.tools.SendHttpRequestTask;

public class SessionsActivity extends BaseActivity implements BaseSetFragment.OnBaseSetFragmentListener {

    private static final String URL_SESSIONS = "view/sessions.pl";
    private static final boolean DEBUG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consume);
        super.url = Constants.BASE_URL + URL_SESSIONS;
        super.tag = Constants.TAG_SESSIONS;
        start();
    }

    @Override
    public void onFragmentInteraction(BaseSet set) {
        AsyncTaskListener listener = this;
        HttpHelper helper = new HttpHelper(Constants.BASE_URL + URL_SESSIONS, Constants.GET);

        int ind = set.getIndOfService();
        helper.addFormUrlCode("contr_srv_id", set.getValue(ind));
        helper.addFormUrlCode("dtsy", Integer.toString(set.getDateFrom().year));
        helper.addFormUrlCode("dtsm", Integer.toString(set.getDateFrom().month + 1));
        helper.addFormUrlCode("dtsd", Integer.toString(set.getDateFrom().day));
        helper.addFormUrlCode("dtey", Integer.toString(set.getDateTo().year));
        helper.addFormUrlCode("dtem", Integer.toString(set.getDateTo().month + 1));
        helper.addFormUrlCode("dted", Integer.toString(set.getDateTo().day));
        helper.addFormUrlCode("out_fmt", "DEFAULT");
        helper.addFormUrlCode("order", "by-dts-asc");
        try {
            helper.addFormUrlCode("showlist", URLEncoder.encode("Вывести+список", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (DEBUG) Log.d(Constants.LOG_TAG, "SessionsActivity.onRestoreInstanceState set = " + set);

        SendHttpRequestTask t = new SendHttpRequestTask(helper, listener, progressDialog, Constants.TAG_GET_SESSIONS);
        t.execute();
    }

    
    @Override
    public void onAsyncTaskFinished(String data, int tag) {
        if (tag != Constants.TAG_GET_SESSIONS) {
            super.onAsyncTaskFinished(data, tag);
            return;
        }
        int i = 0;
        while (data.indexOf("<table class=\"data frame-small\"") > 0) {
            i++;
        }
        String s = data.substring(i);
        if (DEBUG) Log.d(Constants.LOG_TAG, "SessionsActivity.onAsyncTaskFinished i = " + i);
    }
}
