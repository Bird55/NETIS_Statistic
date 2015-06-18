package ru.netis.android.netisstatistic;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.netis.android.netisstatistic.util.HttpHelper;

public class LoginActivity extends Activity {

    private static final String URL ="http://stat.netis.ru/login.pl";
    private EditText mName, mPassword;
    private static final String LOG_TAG = "myLog";
    private TextView myTextView;
    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mName = (EditText) findViewById(R.id.nameEditText);
        mPassword = (EditText) findViewById(R.id.passwordEditText);
        myTextView = (TextView) findViewById(R.id.msgTextView);
        Button buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] params = new String[] {
                        URL, mName.getText().toString(), mPassword.getText().toString()
                };
                SendHttpRequestTask task = new SendHttpRequestTask();
                task.execute(params);
            }
        });
    }

    private class SendHttpRequestTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
//            item.setActionView(R.layout.progress);
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String param1 = params[1];
            String param2 = params[2];
            String data = null;
            HttpHelper helper = new HttpHelper();

            try {
                helper.connectForMultipart(url);
                helper.addFormPart("user", param1);
                helper.addFormPart("password", param2);
//                helper.addFormPart("submit", "%D0%92%D0%BE%D0%B9%D1%82%D0%B8");
//                helper.addFormPart("submit", "Submit");
                helper.finishMultipart();
                data = helper.getResponse();
                Log.d(LOG_TAG, "\r\n" + helper.getHeaders());
                Log.d(LOG_TAG, "\r\n" + helper.getCookies());
                helper.disConnect();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
//            item.setActionView(null);
            myTextView.setText(s);
        }
    }
}
