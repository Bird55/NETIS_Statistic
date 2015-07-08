package ru.netis.android.netisstatistic;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ru.netis.android.netisstatistic.fragments.ConsumeSetFragment;
import ru.netis.android.netisstatistic.tools.AsyncTaskListener;
import ru.netis.android.netisstatistic.tools.HttpHelper;
import ru.netis.android.netisstatistic.tools.SendHttpRequestTask;


public class ConsumeActivity extends AppCompatActivity implements AsyncTaskListener, ConsumeSetFragment.OnConsumeSetFragmentListener {

    private static final String URL_CONSUME = "view/consume.pl";
    public ProgressDialog progressDialog;

    private ConsumeSet consumeSet = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consume);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.title_progress_dialog));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);

        AsyncTaskListener listener = this;
        HttpHelper helper = new HttpHelper(Constants.BASE_URL + URL_CONSUME);
        SendHttpRequestTask t = new SendHttpRequestTask(helper, listener, progressDialog, Constants.TAG_CONSUME);
        t.execute();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("consumeSet", consumeSet);
        Log.d(Constants.LOG_TAG, "ConsumeActivity.onSaveInstanceState consumeSet is " + consumeSet);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        consumeSet = savedInstanceState.getParcelable("consumeSet");
        Log.d(Constants.LOG_TAG, "ConsumeActivity.onRestoreInstanceState consumeSet is " + consumeSet);
    }

    @Override
    public void onAsyncTaskFinished(String data, int tag) {

        Log.d(Constants.LOG_TAG, "Consume.onAsyncTaskFinished.start: consumeSet is " + consumeSet);
        if (tag == Constants.TAG_CONSUME) {

            boolean isFirstTime  = (consumeSet == null);

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            if (isFirstTime) {
                consumeSet = Constants.getConsumeSet(data);
                ConsumeSetFragment consumeSetFragment = ConsumeSetFragment.newInstance(consumeSet);
                transaction.add(R.id.consumeSetContainer, consumeSetFragment);
            } else {
                ConsumeSetFragment consumeSetFragment = ConsumeSetFragment.newInstance(consumeSet);
                transaction.replace(R.id.consumeSetContainer, consumeSetFragment);
            }
            transaction.commit();
        }
        Log.d(Constants.LOG_TAG, "Consume.onAsyncTaskFinished.end: consumeSet is " + consumeSet);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
