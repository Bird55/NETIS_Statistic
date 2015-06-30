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


public class ChangePasswordActivity extends AppCompatActivity implements AsyncTaskListener {

    private static final String URL = "modify/stat-password.pl";
    private HttpHelper helper;
    MenuItem mActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        final EditText oldPassword = (EditText) findViewById(R.id.oldPassword);
        final EditText newPassword = (EditText) findViewById(R.id.newPassword);
        final EditText newPasswordRetype = (EditText) findViewById(R.id.newPasswordRetype);
        final Button btnChangePassword = (Button) findViewById(R.id.btnChangePassword);

        final AsyncTaskListener listener = (MainActivity)getApplicationContext();
        helper = new HttpHelper(Constants.BASE_URL + URL);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String param = oldPassword.getText().toString();
                helper.addFormPart("old_pass", param);
                param = newPassword.getText().toString();
                helper.addFormPart("new_pass", param);
                param = newPasswordRetype.getText().toString();
                helper.addFormPart("new_pass1", param);
                helper.addFormPart("change", "Сменить");

                SendHttpRequestTask t = new SendHttpRequestTask(helper, listener, Constants.TAG_CHANGE_PASSWORD);
                showProgressBar();
                t.execute();
            }
        });
    }

    @Override
    public void onAsyncTaskFinished(String data, int tag) {
        Log.d(Constants.LOG_TAG, "ChangePasswordActivity onAsyncTaskFinished " + helper.getCookies());
        Intent intent = new Intent();
        intent.putExtra("html", data);
        setResult(RESULT_OK, intent);
        hideProgressBar();
        finish();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_progress, menu);
        // Store instance of the menu item containing progress
        mActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(mActionProgressItem);
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        // Show progress item
        mActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        mActionProgressItem.setVisible(false);
    }
}
