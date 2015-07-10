package ru.netis.android.netisstatistic.tools;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import ru.netis.android.netisstatistic.Constants;

public class SendHttpRequestTask extends AsyncTask<Void, Void, String> {


    private static final boolean DEBUG = false;
    HttpHelper helper;
    AsyncTaskListener listener;
    private ProgressDialog progressDialog = null;
    private int tag;

    public SendHttpRequestTask(HttpHelper helper, AsyncTaskListener listener, ProgressDialog progressDialog, int tag) {
        this(helper, listener, tag);
        this.progressDialog = progressDialog;
    }

    public SendHttpRequestTask(HttpHelper helper, AsyncTaskListener listener, int tag) {
        this.helper = helper;
        this.listener = listener;
        this.tag = tag;
        if (DEBUG) Log.d(Constants.LOG_TAG, "SendHttpRequestTask.Constructor helper.url = " + helper.getUrl());
    }

    @Override
    protected void onPreExecute() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        String data = null;

        try {
            helper.connectForMultipart();
            helper.finishMultipart();
            data = helper.getResponse();
            if (DEBUG) Log.d(Constants.LOG_TAG, "SendHttpRequestTask.doInBackground.Headers: " + helper.getHeaders());
            if (DEBUG) Log.d(Constants.LOG_TAG, "SendHttpRequestTask.doInBackground.Cookies" + helper.getCookies());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(String data) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        try {
            helper.disConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (DEBUG) Log.d(Constants.LOG_TAG, "SendHttpRequestTask.onPostExecute data.length() = " + data.length());
        listener.onAsyncTaskFinished(data, tag);
    }
}
