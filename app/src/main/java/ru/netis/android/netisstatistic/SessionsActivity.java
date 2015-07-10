package ru.netis.android.netisstatistic;

import android.os.Bundle;

public class SessionsActivity extends BaseActivity {

    private static final String URL_CONSUME = "view/sessions.pl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consume);
        super.url = Constants.BASE_URL + URL_CONSUME;
        super.tag = Constants.TAG_CONSUME;
        start();
    }
}
