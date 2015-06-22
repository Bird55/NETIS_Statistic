package ru.netis.android.netisstatistic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import ru.netis.android.netisstatistic.tools.AsyncTaskListener;
import ru.netis.android.netisstatistic.tools.HttpHelper;
import ru.netis.android.netisstatistic.tools.SendHttpRequestTask;

public class LoginActivity extends AppCompatActivity implements AsyncTaskListener {

    private static final String URL = "http://stat.netis.ru/login.pl";
    private static final String LOG_TAG = "myLog";
    private ProgressBar bar;
    private HttpHelper helper;
    MenuItem miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        final EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        final Button buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        bar = (ProgressBar) findViewById(R.id.progressBar);

        final AsyncTaskListener listener = this;
        helper = new HttpHelper(URL);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String param = nameEditText.getText().toString();
//                helper.addFormPart(new MultipartParameter("name", Constants.CONTENT_TYPE, param));
                helper.addFormPart("user", param);
                param = passwordEditText.getText().toString();
//                helper.addFormPart(new MultipartParameter("password", Constants.CONTENT_TYPE, param));
                helper.addFormPart("password", param);
//                helper.addFormPart("submit", "Войти");
//                helper.addFormPart("return", "//stat.netis.ru/index.pl");

                SendHttpRequestTask t = new SendHttpRequestTask(helper, listener, bar);
                showProgressBar();
                t.execute();
            }
        });
    }

    @Override
    public void onAsyncTaskFinished(String data) {
        Log.d(LOG_TAG, "onAsyncTaskFinished " + helper.getCookies());
        Intent intent = new Intent();
        intent.putExtra("html", data);
        setResult(RESULT_OK, intent);
        hideProgressBar();
        finish();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }
    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }
}
