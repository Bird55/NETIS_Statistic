package ru.netis.android.netisstatistic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import ru.netis.android.netisstatistic.dialogs.ChPassDialogFragment;
import ru.netis.android.netisstatistic.dialogs.LoginDialogFragment;
import ru.netis.android.netisstatistic.fragments.InfoFragment;
import ru.netis.android.netisstatistic.helpers.Client;
import ru.netis.android.netisstatistic.tools.AsyncTaskListener;
import ru.netis.android.netisstatistic.tools.HttpHelper;
import ru.netis.android.netisstatistic.tools.SendHttpRequestTask;

public class MainActivity extends AppCompatActivity implements AsyncTaskListener, LoginDialogFragment.OnLoginCallback, ChPassDialogFragment.OnChPassCallback {

    private static final boolean AUTHO_LOGIN = true;
    private static final String NAME = "bah";
    private static final String PASSWORD = "nontronit";

    private Drawer.Result drawResult;

    private static final String URL_LOGIN = "login.pl";
//    private static final String URL_SALDO = "saldo.pl";
    private static final String URL_CH_PASS = "modify/stat-password.pl";

    CookieManager mCookieManager = NetisStatApplication.getInstance().getCookieManager();
    private AccountHeader.Result accHeaResult;
    public ProgressDialog progressDialog;
    private Client client = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initializeNavigationDrawer(toolbar);

        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.title_progress_dialog));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);

        String cookie = getCookie(Constants.BASE_URL);
        Log.d(Constants.LOG_TAG, "MainActivity onCreate cookie = " + (cookie == null ? "null" : cookie));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AUTHO_LOGIN) {
            onLogin(NAME, PASSWORD);
        } else if (client == null) {
                LoginDialogFragment loginDialog = new LoginDialogFragment();
                loginDialog.show(getSupportFragmentManager(), LoginDialogFragment.TAG);
            }
    }

    private void initializeNavigationDrawer(Toolbar toolbar) {

        accHeaResult = createAccountHeader();

        drawResult = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accHeaResult)
                .withDisplayBelowToolbar(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        initializeDrawerItems()
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
//                        Toast.makeText(MainActivity.this, "i=" + i + " l=" + l + " item=" + iDrawerItem.toString(), Toast.LENGTH_SHORT).show();
                        Intent intent;
                        switch (i) {
                            case 1:
                                intent = new Intent(MainActivity.this, ConsumeActivity.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(MainActivity.this, SessionsActivity.class);
                                startActivity(intent);
                        }
                    }
                })
                .build();
    }

    private IDrawerItem[] initializeDrawerItems() {
        return new IDrawerItem[]{new PrimaryDrawerItem()
                .withName(R.string.payments)
                .withIdentifier(1),
                new SecondaryDrawerItem()
                        .withName(R.string.consuming),
                new SecondaryDrawerItem()
                        .withName(R.string.sessions),
                new SecondaryDrawerItem()
                        .withName(R.string.services)
        };
    }

    private AccountHeader.Result createAccountHeader() {
        IProfile profile = new ProfileDrawerItem()
                .withName("NETIS Telecom")
                .withEmail("noc@netis.ru")
                .withIcon(ContextCompat.getDrawable(this, R.drawable.logo_footer_01));
//                .withIcon(getResources().getDrawable(R.drawable.logo_footer_01));
        return new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(R.drawable.background_material)
//                .addProfiles(profile)
                .build();
    }

    @Override
    public void onBackPressed() {
        if (drawResult != null && drawResult.isDrawerOpen()) {
            drawResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("client", client);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        client = savedInstanceState.getParcelable("client");
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
            case R.id.action_login:
                LoginDialogFragment loginDialog = new LoginDialogFragment();
                loginDialog.show(getSupportFragmentManager(), LoginDialogFragment.TAG);
                break;
            case R.id.action_change_password:
                ChPassDialogFragment chPassDialog = new ChPassDialogFragment();
                chPassDialog.show(getSupportFragmentManager(), ChPassDialogFragment.TAG);
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


/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(Constants.LOG_TAG, "onActivityResult ");
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.LOGIN_REQUEST) {
                AsyncTaskListener listener = this;
                HttpHelper helper = new HttpHelper(Constants.BASE_URL + URL_SALDO);
                SendHttpRequestTask t = new SendHttpRequestTask(helper, listener, progressDialog, Constants.TAG_SALDO);
                t.execute();
            } else if (requestCode == Constants.CHANGE_LOGIN_REQUEST){
                String s = data.getStringExtra("html");
                myTextView.setText(Html.fromHtml(s));
            }
        } else {
            myTextView.setText("Error!");
        }
    }
*/

    @Override
    public void onAsyncTaskFinished(String data, int tag) {
        if (tag == Constants.TAG_SALDO) {
            String cookie = getCookie(Constants.BASE_URL);
            Log.d(Constants.LOG_TAG, "MainActivity onAsyncTaskFinished cookie = " + (cookie == null ? "null" : cookie));
        } else if (tag == Constants.TAG_LOGIN || tag == Constants.TAG_CHANGE_PASSWORD) {
            AsyncTaskListener listener = this;
            HttpHelper helper = new HttpHelper(Constants.BASE_URL);
            SendHttpRequestTask t = new SendHttpRequestTask(helper, listener, progressDialog, Constants.TAG_INDEX);
            t.execute();
        } else if (tag == Constants.TAG_INDEX) {

            boolean isNewClient;

            FragmentManager mFragmentManager = getSupportFragmentManager();
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

            isNewClient = (client == null);

            client = Constants.getClient(data);

            InfoFragment infoFragment = InfoFragment.newInstance(client);
            if (isNewClient) {
                mFragmentTransaction.add(R.id.container, infoFragment);
            } else {
                mFragmentTransaction.replace(R.id.container, infoFragment);
            }
            mFragmentTransaction.commit();
        }
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

    @Override
    public void onLogin(String name, String password) {
        AsyncTaskListener listener = this;
        HttpHelper helper = new HttpHelper(Constants.BASE_URL + URL_LOGIN);
        helper.addFormPart("user", name);
        helper.addFormPart("password", password);
        SendHttpRequestTask t = new SendHttpRequestTask(helper, listener, progressDialog, Constants.TAG_LOGIN);
        t.execute();
        Log.d(Constants.LOG_TAG, "Name = \"" + name + "\" Password = \"" + password + "\"");
    }

    @Override
    public void onChPass(String oldPass, String newPass, String retPass) {
        AsyncTaskListener listener = this;
        HttpHelper helper = new HttpHelper(Constants.BASE_URL + URL_CH_PASS);
        helper.addFormPart("old_pass", oldPass);
        helper.addFormPart("new_pass", newPass);
        helper.addFormPart("new_pass1", retPass);
        helper.addFormPart("change", "Сменить");
        SendHttpRequestTask t = new SendHttpRequestTask(helper, listener, progressDialog, Constants.TAG_CHANGE_PASSWORD);
        t.execute();
        Log.d(Constants.LOG_TAG, "OldPass = \"" + oldPass + "\" NewPass = \"" + newPass + "\" RetypePass = \"" + retPass + "\"");
    }
}
