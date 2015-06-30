package ru.netis.android.netisstatistic.tools;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import ru.netis.android.netisstatistic.Constants;

public class SendHttpRequestTask extends AsyncTask<Void, Void, String> {

    HttpHelper helper;
    AsyncTaskListener listener;
    private ProgressBar bar = null;
    private int tag;

    public SendHttpRequestTask(HttpHelper helper, AsyncTaskListener listener, ProgressBar bar, int tag) {
        this(helper, listener, tag);
        this.bar = bar;
    }

    public SendHttpRequestTask(HttpHelper helper, AsyncTaskListener listener, int tag) {
        this.helper = helper;
        this.listener = listener;
        this.tag = tag;
    }

    @Override
    protected void onPreExecute() {
        if (bar != null) {
            bar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        String data = null;

        try {
            helper.connectForMultipart();
            helper.finishMultipart();
            data = helper.getResponse();
//            Log.d(Constants.LOG_TAG, "\r\n" + helper.getHeaders());
//            Log.d(Constants.LOG_TAG, "\r\n" + helper.getCookies());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(String data) {
        if (bar != null) {
            bar.setVisibility(View.GONE);
        }
        try {
            helper.disConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.d(Constants.LOG_TAG, "\r\n" + data);
        listener.onAsyncTaskFinished(data, tag);
    }
}
