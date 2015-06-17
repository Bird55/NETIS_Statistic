package ru.netis.android.netisstatistic;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;

import ru.netis.android.netisstatistic.request.MyMultiPartRequest;
import ru.netis.android.netisstatistic.util.MyVolley;

public class LoginActivity extends Activity {

    private static final String URL ="http://stat.netis.ru/login.pl";
    private EditText mName, mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mName = (EditText) findViewById(R.id.nameEditText);
        mPassword = (EditText) findViewById(R.id.passwordEditText);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSubmit:
                RequestQueue queue = MyVolley.getRequestQueue();
                MyMultiPartRequest myRequest = new MyMultiPartRequest(URL, createMyReqSuccessListener(), createMyReqErrorListener());
                myRequest.addMultipartParam("user", "text/plain", mName.getText().toString())
                        .addMultipartParam("password", "text/plain", mPassword.getText().toString());
                queue.add(myRequest);
                break;
        }
    }

    private Response.Listener<String> createMyReqSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // TODO здесь смотрим залогинились или нет, если да, то сохраняем SID
                Log.d(MainActivity.LOG_TAG, "onResponse: " + response);
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO надо вставить обработку ошибки
                Log.d(MainActivity.LOG_TAG, "onErrorResponse: " + error.getMessage());
            }
        };
    }
}
