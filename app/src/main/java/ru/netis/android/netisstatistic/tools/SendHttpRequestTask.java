package ru.netis.android.netisstatistic.tools;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;

public class SendHttpRequestTask extends AsyncTask<Void, Void, String> {


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
//            Log.d(Constants.LOG_TAG, "\r\n" + helper.getHeaders());
//            Log.d(Constants.LOG_TAG, "\r\n" + helper.getCookies());
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
//        Log.d(Constants.LOG_TAG, "\r\n" + data);
        listener.onAsyncTaskFinished(data, tag);
    }
}
