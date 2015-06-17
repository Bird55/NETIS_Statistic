package ru.netis.android.netisstatistic.request;

import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;

/**
 * A Simple request for making a Multi Part request whose response is retrieve as String
 */
public class MyMultiPartRequest extends SimpleMultiPartRequest {

    private static final boolean DEBUG = true;

    public MyMultiPartRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }
}
