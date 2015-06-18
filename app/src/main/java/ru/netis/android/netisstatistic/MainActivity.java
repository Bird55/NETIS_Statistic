package ru.netis.android.netisstatistic;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.net.CookieManager;
import java.net.CookieStore;

import ru.netis.android.netisstatistic.util.HttpHelper;

public class MainActivity extends AppCompatActivity {

    boolean endTask;

    public static final String URL = "http://stat.netis.ru/saldo.pl";

    public static final int REQUEST_LOGIN = 1;
    public static final String LOG_TAG = "myLog";
    public static CookieStore cookies;
    static {
        CookieManager m = new CookieManager();
        cookies = m.getCookieStore();
    }
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
        setContentView(R.layout.activity_main);
        Log.d(MainActivity.LOG_TAG, "MainActivity0: " + cookies.getCookies());
        cookies.removeAll();
        Log.d(MainActivity.LOG_TAG, "MainActivity1: " + cookies.getCookies());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_LOGIN) {
            SendHttpRequestTask task = new SendHttpRequestTask();
            task.execute("http://stat.netis.ru/saldo.pl");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
            case R.id.action_change_user:
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN);
                break;
            case R.id.action_help:
                startActivity(new Intent(MainActivity.this, HelpActivity.class));
                break;
            case R.id.action_exit:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private class SendHttpRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
//            item.setActionView(R.layout.progress);
            endTask = false;
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String data = null;

            try {
                helper.doMultipartRequest();
                data = helper.getResponse();
//                Log.d(MainActivity.LOG_TAG, data);
//                Log.d(MainActivity.LOG_TAG, "\r\n" + helper.getHeaders());
                Log.d(MainActivity.LOG_TAG, "MainActivity2: " + helper.getCookies());
            } catch (Throwable t) {
                // TODO разобраться какие исключения тут могут быть
                t.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                helper.disConnect();
            } catch (Exception e) {
                // TODO разобраться какие исключения тут могут быть
                e.printStackTrace();
            }
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(s);
            endTask = true;
        }
    }

}
