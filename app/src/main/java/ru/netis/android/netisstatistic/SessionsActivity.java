package ru.netis.android.netisstatistic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        if (DEBUG) Log.d(Constants.LOG_TAG, "SessionsActivity.onAsyncTaskFinished start: tag = " + tag);
        if (tag != Constants.TAG_GET_SESSIONS) {
            super.onAsyncTaskFinished(data, tag);
            return;
        }

        new ParseData().execute(data);
        if (DEBUG) Log.d(Constants.LOG_TAG, "SessionsActivity.onAsyncTaskFinished finish:");

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
