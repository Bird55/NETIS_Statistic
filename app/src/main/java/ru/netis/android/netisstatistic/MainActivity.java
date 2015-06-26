package ru.netis.android.netisstatistic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import ru.netis.android.netisstatistic.tools.AsyncTaskListener;
import ru.netis.android.netisstatistic.tools.HttpHelper;
import ru.netis.android.netisstatistic.tools.SendHttpRequestTask;

public class MainActivity extends AppCompatActivity implements AsyncTaskListener {
    private static final String DATA_FRAGMENT_TAG = DataFragment.class.getCanonicalName();
    private static final int MENU_GROUP = 0;

    private static final String URL = "saldo.pl";
    private TextView myTextView;
    CookieManager mCookieManager = NetisStatApplication.getInstance().getCookieManager();

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTextView = (TextView) findViewById(R.id.textView);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        setupToolbar();
        setupNavigationView();
        setupDataFragment();
        setupNavigationMenu();

        String cookie = getCookie(Constants.BASE_URL);
        Log.d(Constants.LOG_TAG, "onCreate cookie = " + (cookie == null ? "null" : cookie));
    }

    private void setupNavigationMenu() {
        Menu menu = navigationView.getMenu();
        menu.clear();
        String[] menuList = getResources().getStringArray(R.array.navigation_menu);
        int item = 0;
        for (String strItem : menuList) {
            menu.add(MENU_GROUP, item, item, strItem);
            if (item == 0) {
                menu.getItem(0).setChecked(true);
//                setCurrentArticle(articles.getArticle(articleTitle));
            }
            Log.d(Constants.LOG_TAG, "setupNavigationMenu " + item + " " + strItem);
            item++;
        }
        menu.setGroupCheckable(MENU_GROUP, true, true);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupNavigationView() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        // TODO my menu insert here
//                        selectArticle(menuItem.getTitle());
                        return true;
                    }
                });
    }

    private void setupDataFragment() {
        DataFragment dataFragment = (DataFragment) getSupportFragmentManager().findFragmentByTag(DATA_FRAGMENT_TAG);
        if (dataFragment == null) {
            dataFragment = (DataFragment) Fragment.instantiate(this, DataFragment.class.getName());
            dataFragment.setRetainInstance(true);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(dataFragment, DATA_FRAGMENT_TAG);
            transaction.commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("saldo", myTextView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        myTextView.setText(savedInstanceState.getString("saldo"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
            case R.id.action_login:
                intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivityForResult(intent, Constants.LOGIN_REQUEST);
                break;
            case R.id.action_change_password:
                intent = new Intent(getBaseContext(), ChangePasswordActivity.class);
                startActivityForResult(intent, Constants.CHANGE_LOGIN_REQUEST);
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
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.LOGIN_REQUEST) {
                AsyncTaskListener listener = this;
                HttpHelper helper = new HttpHelper(Constants.BASE_URL + URL);
                SendHttpRequestTask t = new SendHttpRequestTask(helper, listener);
                t.execute();
            } else if (requestCode == Constants.CHANGE_LOGIN_REQUEST){
                String s = data.getStringExtra("html");
                myTextView.setText(Html.fromHtml(s));
            }
        } else {
            myTextView.setText("Error!");
        }
    }

    @Override
    public void onAsyncTaskFinished(String data) {
        myTextView.setText(Html.fromHtml(data));
        String cookie = getCookie(Constants.BASE_URL);
        Log.d(Constants.LOG_TAG, "MainActivity onAsyncTaskFinished cookie = " + (cookie == null ? "null" : cookie));
/*
        if(mCookieManager.getCookieStore().getCookies().size() > 0) {
            Log.d(Constants.LOG_TAG, "MainActivity onAsyncTaskFinished Cookie: " + TextUtils.join(",", mCookieManager.getCookieStore().getCookies()));
        } else {
            Log.d(Constants.LOG_TAG, "No Cookies");
        }
*/
    }

    private String getCookie(String url ){
        String ret = null;
        List<HttpCookie> listCookies = null;
        try {
            listCookies = mCookieManager.getCookieStore().get(new URI(url));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        assert listCookies != null;
        if (listCookies.size() != 0) {
            ret = listCookies.toString();
        }

        return ret;
    }
}
