package ru.netis.android.netisstatistic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.net.CookieManager;

import ru.netis.android.netisstatistic.tools.AsyncTaskListener;
import ru.netis.android.netisstatistic.tools.HttpHelper;
import ru.netis.android.netisstatistic.tools.SendHttpRequestTask;

public class MainActivity extends AppCompatActivity implements AsyncTaskListener {

    private static final String URL = "http://stat.netis.ru/saldo.pl";
    private TextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTextView = (TextView) findViewById(R.id.textView);
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
                startActivityForResult(intent, Constants.LOGIN_REQUEST);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.LOGIN_REQUEST && resultCode == RESULT_OK) {
            AsyncTaskListener listener = this;
            HttpHelper helper = new HttpHelper(URL);
            SendHttpRequestTask t = new SendHttpRequestTask(helper, listener, this);
            t.execute();
//            String s = data.getStringExtra("html");
//            myTextView.setText(Html.fromHtml(s));
        } else {
            myTextView.setText("Error!");
        }
    }

    @Override
    public void onAsyncTaskFinished(String data) {
        CookieManager msCookieManager = NetisStatApplication.getInstance().getCookieManager();
        myTextView.setText(Html.fromHtml(data));
        if(msCookieManager.getCookieStore().getCookies().size() > 0) {
            Log.d(Constants.LOG_TAG, "onAsyncTaskFinished Cookie: " + TextUtils.join(",", msCookieManager.getCookieStore().getCookies()));
        } else {
            Log.d(Constants.LOG_TAG,  "No Cookies");
        }
    }
}
