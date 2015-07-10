package ru.netis.android.netisstatistic;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ru.netis.android.netisstatistic.helpers.BaseSet;
import ru.netis.android.netisstatistic.tools.AsyncTaskListener;
import ru.netis.android.netisstatistic.tools.HttpHelper;
import ru.netis.android.netisstatistic.tools.SendHttpRequestTask;

public class SessionsActivity extends AppCompatActivity implements AsyncTaskListener {

    private static final String URL_SESSIONS = "view/sessions.pl";
    private static final boolean DEBUG = false;

    private BaseSet baseSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.title_progress_dialog));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);

        AsyncTaskListener listener = this;
        HttpHelper helper = new HttpHelper(Constants.BASE_URL + URL_SESSIONS);
        SendHttpRequestTask t = new SendHttpRequestTask(helper, listener, progressDialog, Constants.TAG_CONSUME);
        t.execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("baseSet", baseSet);
        if (DEBUG) Log.d(Constants.LOG_TAG, "ConsumeActivity.onSaveInstanceState consumeSet is " + baseSet);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        baseSet = savedInstanceState.getParcelable("baseSet");
        if (DEBUG) Log.d(Constants.LOG_TAG, "ConsumeActivity.onRestoreInstanceState consumeSet is " + baseSet);
    }

    @Override
    public void onAsyncTaskFinished(String data, int tag) {

    }
}
