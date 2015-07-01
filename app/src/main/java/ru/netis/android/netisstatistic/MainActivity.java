package ru.netis.android.netisstatistic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

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
import ru.netis.android.netisstatistic.tools.AsyncTaskListener;
import ru.netis.android.netisstatistic.tools.HttpHelper;
import ru.netis.android.netisstatistic.tools.SendHttpRequestTask;

public class MainActivity extends AppCompatActivity implements AsyncTaskListener, LoginDialogFragment.OnLoginCallback, ChPassDialogFragment.OnChPassCallback {
    private Drawer.Result drawResult;

    private static final String URL_LOGIN = "login.pl";
    private static final String URL_SALDO = "saldo.pl";
    private static final String URL_CHPASS = "modify/stat-password.pl";
    private TextView myTextView;
    CookieManager mCookieManager = NetisStatApplication.getInstance().getCookieManager();
    public ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTextView = (TextView) findViewById(R.id.textView);

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

    private void initializeNavigationDrawer(Toolbar toolbar) {
        AccountHeader.Result accHeaResult = createAccountHeader();

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
                        Toast.makeText(MainActivity.this, "i=" + i + " l=" + l + " item=" + iDrawerItem.toString(), Toast.LENGTH_SHORT).show();
                        switch (i) {
                            case 0:
                                Intent intent = new Intent(MainActivity.this, PaymentsActivity.class);
                                startActivity(intent);
                                break;
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
                .withIcon(getResources().getDrawable(R.drawable.logo_footer_01));
        return new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(R.drawable.background_material)
                .addProfiles(profile)
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

/*
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
*/

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    @Override
    public void onAsyncTaskFinished(String data, int tag) {
        if (tag == Constants.TAG_SALDO) {
            myTextView.setText(Html.fromHtml(data));
            String cookie = getCookie(Constants.BASE_URL);
            Log.d(Constants.LOG_TAG, "MainActivity onAsyncTaskFinished cookie = " + (cookie == null ? "null" : cookie));
        } else if (tag == Constants.TAG_LOGIN || tag == Constants.TAG_CHANGE_PASSWORD) {
            AsyncTaskListener listener = this;
            HttpHelper helper = new HttpHelper(Constants.BASE_URL + URL_SALDO);
            SendHttpRequestTask t = new SendHttpRequestTask(helper, listener, progressDialog, Constants.TAG_SALDO);
            t.execute();
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
        HttpHelper helper = new HttpHelper(Constants.BASE_URL + URL_CHPASS);
        helper.addFormPart("old_pass", oldPass);
        helper.addFormPart("new_pass", newPass);
        helper.addFormPart("new_pass1", retPass);
        helper.addFormPart("change", "Сменить");
        SendHttpRequestTask t = new SendHttpRequestTask(helper, listener, progressDialog, Constants.TAG_CHANGE_PASSWORD);
        t.execute();

    }
}
