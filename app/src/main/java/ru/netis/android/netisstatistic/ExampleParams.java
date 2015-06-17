package ru.netis.android.netisstatistic;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import ru.netis.android.netisstatistic.util.MyVolley;


/**
 * Demonstrates how to send GET and POST parameters.
 * Values of the EditText fields are send to the server, they are added and the sum is returned as result.
 * If you use {@see ExtHttpClientStack} as in {@see Act_NewHttpClient} you may use URIBuilder (which is
 * present only in newer versions of HttpClient)
 *
 * @author Ognyan Bankov
 *
 */
public class ExampleParams extends ActionBarActivity {
    private TextView mTvResult;
    private EditText mEtNum1;
    private EditText mEtNum2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_params);

        mTvResult = (TextView) findViewById(R.id.tv_result);
        mEtNum1 = (EditText) findViewById(R.id.et_num1);
        mEtNum2 = (EditText) findViewById(R.id.et_num2);

        Button btnGetRequest = (Button) findViewById(R.id.btn_get);
        btnGetRequest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = MyVolley.getRequestQueue();

                String num1 = mEtNum1.getText().toString();
                String num2 = mEtNum2.getText().toString();
                if (num1 != null && !num1.equals("") && num2 != null && !num2.equals("")) {
                    String uri = String
                            .format("http://ave.bolyartech.com/params.php?param1=%1$s&param2=%2$s",
                                    num1,
                                    num2);

                    StringRequest myReq = new StringRequest(Method.GET,
                            uri,
                            createMyReqSuccessListener(),
                            createMyReqErrorListener());
                    queue.add(myReq);
                }
            }
        });

        Button btnPostRequest = (Button) findViewById(R.id.btn_post);
        btnPostRequest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = MyVolley.getRequestQueue();

                final String num1 = mEtNum1.getText().toString();
                final String num2 = mEtNum2.getText().toString();
                if (num1 != null && !num1.equals("") && num2 != null && !num2.equals("")) {
                    StringRequest myReq = new StringRequest(Method.POST,
                            "http://ave.bolyartech.com/params.php",
                            createMyReqSuccessListener(),
                            createMyReqErrorListener()) {

                        protected Map<String, String> getParams() throws com.android.volley.error.AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("param1", num1);
                            params.put("param2", num2);
                            return params;
                        };
                    };
                    queue.add(myReq);
                }
            }
        });

    }


    private Response.Listener<String> createMyReqSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setTvResultText(response);
            }
        };
    }


    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTvResult.setText(error.getMessage());
            }
        };
    }


    private void setTvResultText(String str) {
        mTvResult.setText(String.format(getString(R.string.act__params__sum), str));
    }
}
