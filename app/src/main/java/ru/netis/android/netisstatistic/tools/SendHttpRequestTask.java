package ru.netis.android.netisstatistic.tools;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

public class SendHttpRequestTask extends AsyncTask<Void, Void, String> {

    HttpHelper helper;
    AsyncTaskListener listener;
    private ProgressBar bar = null;

    public SendHttpRequestTask(HttpHelper helper, AsyncTaskListener listener, ProgressBar bar) {
        this(helper, listener);
        this.bar = bar;
    }

    public SendHttpRequestTask(HttpHelper helper, AsyncTaskListener listener) {
        this.helper = helper;
        this.listener = listener;
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
//        Log.d(MainActivity.LOG_TAG, "\r\n" + data);
        listener.onAsyncTaskFinished(data);
    }
}
