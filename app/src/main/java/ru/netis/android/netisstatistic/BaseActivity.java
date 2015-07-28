package ru.netis.android.netisstatistic;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Calendar;

import ru.netis.android.netisstatistic.fragments.BaseSetFragment;
import ru.netis.android.netisstatistic.helpers.BaseSet;
import ru.netis.android.netisstatistic.helpers.DateOf;
import ru.netis.android.netisstatistic.tools.AsyncTaskListener;
import ru.netis.android.netisstatistic.tools.HttpHelper;
import ru.netis.android.netisstatistic.tools.SendHttpRequestTask;

public class BaseActivity extends AppCompatActivity implements AsyncTaskListener {
    private static final boolean DEBUG = true;
    private BaseSet baseSet = null;

    protected String url;
    protected int tag;
    protected ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void start() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.title_progress_dialog));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);

        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseActivity.start url = " + url);
        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseActivity.start tag = " + tag);

        AsyncTaskListener listener = this;
        HttpHelper helper = new HttpHelper(url, Constants.POST);
        SendHttpRequestTask t = new SendHttpRequestTask(helper, listener, progressDialog, tag);
        t.execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("baseSet", baseSet);
        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseActivity.onSaveInstanceState baseSet is " + baseSet);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        baseSet = savedInstanceState.getParcelable("baseSet");
        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseActivity.onRestoreInstanceState baseSet is " + baseSet);
    }

    @Override
    public void onAsyncTaskFinished(String data, int tag) {

        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseActivity.onAsyncTaskFinished start: data.length() = " + data.length());
        if (tag == Constants.TAG_CONSUME || tag == Constants.TAG_SESSIONS) {

            int year;
            int month;
            int day;

            boolean isFirstTime  = (baseSet == null);

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            if (isFirstTime) {
                if (DEBUG) Log.d(Constants.LOG_TAG, "BaseActivity.onAsyncTaskFinished first");
                baseSet = Constants.getBaseSet(data);

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = 1;
                DateOf dateOf = new DateOf(day, month, year);
                baseSet.setDateFrom(dateOf);
                day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                dateOf = new DateOf(day, month, year);
                baseSet.setDateTo(dateOf);

                BaseSetFragment baseSetFragment = BaseSetFragment.newInstance(baseSet);
                transaction.add(R.id.baseSetContainer, baseSetFragment);

            } else {
                if (DEBUG) Log.d(Constants.LOG_TAG, "BaseActivity.onAsyncTaskFinished not first");
                BaseSetFragment baseSetFragment = BaseSetFragment.newInstance(baseSet);
                transaction.replace(R.id.baseSetContainer, baseSetFragment);
            }
            transaction.commit();
        }
        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseActivity.onAsyncTaskFinished finish:");
    }


}
