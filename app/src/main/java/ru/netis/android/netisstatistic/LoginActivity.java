package ru.netis.android.netisstatistic;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

import ru.netis.android.netisstatistic.util.AsyncTaskListener;
import ru.netis.android.netisstatistic.util.HttpHelper;
import ru.netis.android.netisstatistic.util.MultiPartParameter;

public class LoginActivity extends AppCompatActivity implements AsyncTaskListener {

    private static final String URL ="http://stat.netis.ru/login.pl";
    private static final String CONTENT_TYPE = "text/plain";
    private EditText mName, mPassword;
    private TextView myTextView;
    private MenuItem item;
    int count = 0;
    //CookieStore cookieStore;
    private HttpHelper helper;
    {
        try {
            helper = new HttpHelper(URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final AppCompatActivity activity = this;

        mName = (EditText) findViewById(R.id.nameEditText);
        mPassword = (EditText) findViewById(R.id.passwordEditText);
        myTextView = (TextView) findViewById(R.id.msgTextView);
        Button buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    helper.addFormPart(new MultiPartParameter("name", CONTENT_TYPE, mName.getText().toString()));
                    helper.addFormPart("password", mPassword.getText().toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                SendHttpRequestTask task = new SendHttpRequestTask(activity);
                task.execute();
            }
        });
    }

    @Override
    public void onAsyncTaskFinished() {
        Intent intent = new Intent();

        Log.d(MainActivity.LOG_TAG, "LoginActivity: onAsyncTaskFinished: " + helper.getCookies());
        setResult(RESULT_OK, intent);
    }

    private class SendHttpRequestTask extends AsyncTask<Void, Void, String> {
        private Context activity;

        SendHttpRequestTask(Context activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
//            item.setActionView(R.layout.progress);
        }

        @Override
        protected String doInBackground(Void... params) {
            String data = null;

            try {
                helper.doMultipartRequest();
                data = helper.getResponse();
//                Log.d(MainActivity.LOG_TAG, data);
//                Log.d(MainActivity.LOG_TAG, "\r\n" + helper.getHeaders());
                Log.d(MainActivity.LOG_TAG, "LoginActivity: " + helper.getCookies());
//                count = ("" + helper.getCookies()).length();
//                Log.d(MainActivity.LOG_TAG, "LoginActivity: " + count);
                if (count > 5) {
                    count = RESULT_OK;
                } else {
                    count = RESULT_CANCELED;
                }
            } catch (Throwable t) {
                // TODO разобраться какие исключения тут могут быть
                t.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
//            item.setActionView(null);

            try {
                helper.disConnect();
            } catch (Exception e) {
                // TODO разобраться какие исключения тут могут быть
                e.printStackTrace();
            }
            myTextView.setText(s);
            onAsyncTaskFinished();
        }
    }
}
