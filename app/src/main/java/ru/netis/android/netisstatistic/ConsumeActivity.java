package ru.netis.android.netisstatistic;

import android.os.Bundle;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import ru.netis.android.netisstatistic.fragments.BaseSetFragment;
import ru.netis.android.netisstatistic.helpers.BaseSet;
import ru.netis.android.netisstatistic.tools.AsyncTaskListener;
import ru.netis.android.netisstatistic.tools.HttpHelper;

public class ConsumeActivity extends BaseActivity implements BaseSetFragment.OnBaseSetFragmentListener {

    private static final boolean DEBUG = true;
    private static final String URL_CONSUME = "view/consume.pl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consume);
        super.url = Constants.BASE_URL + URL_CONSUME;
        super.tag = Constants.TAG_CONSUME;
        start();
    }

    @Override
    public void onFragmentInteraction(BaseSet set) {
        AsyncTaskListener listener = this;
        HttpHelper helper = new HttpHelper(Constants.BASE_URL + URL_CONSUME, Constants.POST);

        int ind = set.getIndOfService();
        helper.addFormUrlCode("contr_srv_id", set.getValue(ind));
        helper.addFormUrlCode("dtsy", Integer.toString(set.getDateFrom().year));
        helper.addFormUrlCode("dtsm", Integer.toString(set.getDateFrom().month + 1));
        helper.addFormUrlCode("dtsd", Integer.toString(set.getDateFrom().day));
        helper.addFormUrlCode("dtey", Integer.toString(set.getDateTo().year));
        helper.addFormUrlCode("dtem", Integer.toString(set.getDateTo().month + 1));
        helper.addFormUrlCode("dted", Integer.toString(set.getDateTo().day));
        helper.addFormUrlCode("out_fmt", "DEFAULT");
        helper.addFormUrlCode("info", "value");
        helper.addFormUrlCode("unit", "3600");
        helper.addFormUrlCode("order", "by-dts-asc");
        try {
            helper.addFormUrlCode("showlist", URLEncoder.encode("Вывести список", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        helper.addFormUrlCode(".cgifields", "tax*");

        if (DEBUG) Log.d(Constants.LOG_TAG, "ConsumeActivity.onRestoreInstanceState url is " + helper.getUrl());

//        SendHttpRequestTask t = new SendHttpRequestTask(helper, listener, progressDialog, Constants.TAG_GET_CONSUME);
//        t.execute();
    }
}

/*
http://stat.netis.ru/view/consume.pl
?contr_srv_id=-380
&dtsy=2015
&dtsm=07
&dtsd=01
&dtey=2015
&dtem=07
&dted=31
&out_fmt=DEFAULT
&info=value
&unit=3600
&order=by-dts-asc
&showlist=%D0%92%D1%8B%D0%B2%D0%B5%D1%81%D1%82%D0%B8+%D1%81%D0%BF%D0%B8%D1%81%D0%BE%D0%BA
&.cgifields=tax*/
