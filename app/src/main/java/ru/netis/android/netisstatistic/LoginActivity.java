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

/**
 * Класс служит для получения доступа к сайту статистики. При удачном результате в
 * хранилище менеджера <b>Cooies</b> заносится значение для <b>SID</b>
 * @author Alexey Solovyev
 * @version 1.0
 */

public class LoginActivity extends AppCompatActivity implements AsyncTaskListener {
    /** Часть URL, указывающая скрипт подключения к сайту*/
    private static final String URL = "login.pl";
    /** ProgressBar используется, если прогресс пророцесса отображается в отдельном диалоге */
    private ProgressBar bar;
    /** Пункт меню, в котором отображается ProgressBar */
    MenuItem mActionProgressItem;
    /** Вспомогательный класс, который формирует запрос к серверу статистики и получает ответ*/
    private HttpHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        final EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        final Button buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        bar = (ProgressBar) findViewById(R.id.progressBar);

        final AsyncTaskListener listener = this;
        helper = new HttpHelper(Constants.BASE_URL + URL);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String param = nameEditText.getText().toString();
                helper.addFormPart("user", param);
                param = passwordEditText.getText().toString();
                helper.addFormPart("password", param);
//                helper.addFormPart("submit", "Войти");

//                SendHttpRequestTask t = new SendHttpRequestTask(helper, listener, bar);
                SendHttpRequestTask t = new SendHttpRequestTask(helper, listener);
                showProgressBar();
                t.execute();
            }
        });
    }

    /**
     * Реализация метода интерфейса {@link AsyncTaskListener}, который вызывается
     * после окончания работы класса {@link SendHttpRequestTask}
     * @param data ответ сервера
     */
    @Override
    public void onAsyncTaskFinished(String data) {
        Log.d(Constants.LOG_TAG, "LoginActivity onAsyncTaskFinished " + helper.getCookies());
        Intent intent = new Intent();
        intent.putExtra("html", data);
        setResult(RESULT_OK, intent);
        hideProgressBar();
        finish();
    }

    /**
     * <p>Устанавливает ProgressBar в меню Activity</p>
     * <p></p>За основу взят материал из этого
     * <a href="https://guides.codepath.com/android/Handling-ProgressBars">ресурса</a></p>
     *
     * @param menu Меню в которое подключается ProgressBar
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        // Store instance of the menu item containing progress
        mActionProgressItem = menu.findItem(R.id.miActionProgress);
        if (mActionProgressItem == null) {
            Log.d(Constants.LOG_TAG, "onPrepareOptionsMenu: mActionProgressItem == null");
        }else {
            Log.d(Constants.LOG_TAG, "onPrepareOptionsMenu: mActionProgressItem != null");
        }
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(mActionProgressItem);
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Включает видимость Progressbar
     */
    public void showProgressBar() {
        // Show progress item
        mActionProgressItem.setVisible(true);
    }

    /**
     * Выключает видимость Progressbar
     */
    public void hideProgressBar() {
        // Hide progress item
        mActionProgressItem.setVisible(false);
    }
}
