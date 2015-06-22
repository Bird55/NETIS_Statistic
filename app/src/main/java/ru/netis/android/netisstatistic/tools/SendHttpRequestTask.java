package ru.netis.android.netisstatistic.tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import ru.netis.android.netisstatistic.tools.AsyncTaskListener;
import ru.netis.android.netisstatistic.tools.HttpHelper;


public class SendHttpRequestTask extends AsyncTask<Void, Void, String> {
    private static final String LOG_TAG = "myLog";
    HttpHelper helper;
    AsyncTaskListener listener;
    private ProgressDialog dialog = null;
    private Context context;
    private Activity activity;
    private ProgressBar bar = null;

    public SendHttpRequestTask(HttpHelper helper, AsyncTaskListener listener, Activity activity) {
        this.helper = helper;
        this.listener = listener;
        this.activity = activity;
        context = activity;
        dialog = new ProgressDialog(context);
    }

    public SendHttpRequestTask(HttpHelper helper, AsyncTaskListener listener, ProgressBar bar) {
        this.helper = helper;
        this.bar = bar;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        if (dialog != null) {
            dialog.setMessage("Progress start");
            dialog.show();
        } else if (bar != null) {
            bar.setVisibility(View.VISIBLE);
        } else {
            Log.d(LOG_TAG, "onPreExecute dialog == null and bar == null");
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
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        } else if (bar != null) {
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

