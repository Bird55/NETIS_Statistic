package ru.netis.android.netisstatistic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import ru.netis.android.netisstatistic.fragments.BaseSetFragment;
import ru.netis.android.netisstatistic.helpers.BaseSet;
import ru.netis.android.netisstatistic.tools.AsyncTaskListener;
import ru.netis.android.netisstatistic.tools.HttpHelper;
import ru.netis.android.netisstatistic.tools.SendHttpRequestTask;

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
        HttpHelper helper = new HttpHelper(Constants.BASE_URL + URL_CONSUME, Constants.GET);

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
        helper.addFormUrlCode(".cgifields", "tax");

        if (DEBUG) Log.d(Constants.LOG_TAG, "ConsumeActivity.onRestoreInstanceState set = " + set);

        SendHttpRequestTask t = new SendHttpRequestTask(helper, listener, progressDialog, Constants.TAG_GET_CONSUME);
        t.execute();
    }

    @Override
    public void onAsyncTaskFinished(String data, int tag) {
        if (DEBUG) Log.d(Constants.LOG_TAG, "ConsumeActivity.onAsyncTaskFinished start: tag = " + tag);
        if (tag != Constants.TAG_GET_CONSUME) {
            super.onAsyncTaskFinished(data, tag);
            return;
        }

        new ParseData().execute(data);
        if (DEBUG) Log.d(Constants.LOG_TAG, "ConsumeActivity.onAsyncTaskFinished finish:");

    }

    class ParseData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Document doc;
            StringBuilder builder = new StringBuilder();
            doc = Jsoup.parse(params[0]);
//
//            Elements trs = doc.select("table.data tr");
//            Log.d(Constants.LOG_TAG, "SessionsActivity.ParseData.onInBackground: length = " + trs.size());
//            trs.remove(0);
//            for (Element tr: trs) {
//                Elements tds = tr.getElementsByTag("td");
//                for (Element td: tds) {
//                    builder.append(td.text()).append(":");
//                }
//                builder.append("/r/n");
//            }
//            return builder.toString();
            return doc.title();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(Constants.LOG_TAG, "SessionsActivity.ParseData.onPostExecute: table = " + s);
        }
    }
}
