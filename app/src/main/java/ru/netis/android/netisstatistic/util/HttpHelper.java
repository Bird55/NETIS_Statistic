package ru.netis.android.netisstatistic.util;


import android.text.TextUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import ru.netis.android.netisstatistic.MainActivity;

/**
 * Created by bird on 02.06.2015
 */
public class HttpHelper {

    private HttpURLConnection connection;
    private StringBuilder stringBuilder;

    private static final String delimiter = "--";
    private static final String boundary =  "SwA"+Long.toString(System.currentTimeMillis())+"SwA";
    private static final String lineEnd = "\r\n";

    private static final String COOKIES_HEADER = "Set-Cookie";
    private static CookieManager msCookieManager;
    static {
        msCookieManager = new CookieManager(MainActivity.cookies, CookiePolicy.ACCEPT_ALL);
        CookieManager.setDefault(msCookieManager);
    }

    private final String url;

    public HttpHelper(String url) throws Exception {
        this.url = url;
        stringBuilder = new StringBuilder();

        connection = (HttpURLConnection) ( new URL(url)).openConnection();
        connection.setInstanceFollowRedirects(false);
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        if(msCookieManager.getCookieStore().getCookies().size() > 0) {
            connection.setRequestProperty("Cookie:",
                    TextUtils.join(",", msCookieManager.getCookieStore().getCookies()));
        } else {
            connection.setRequestProperty("Cookie", "SID=-;path=/");
        }

        connection.setRequestProperty("Connection", "Keep-Alive");
    }

    public HttpHelper addFormPart(String paramName, String value) throws UnsupportedEncodingException {
//        Log.d(MainActivity.LOG_TAG, "addFormPart " + paramName + " = " + value);
        stringBuilder.append(delimiter).append(boundary).append("\r\n");
        stringBuilder.append("Content-Type: text/plain\r\n");
        stringBuilder.append("Content-Disposition: form-data; name=\"").append(paramName).append("\"\r\n");
        stringBuilder.append("\r\n").append(URLEncoder.encode(value, "UTF-8")).append("\r\n");
        return this;
    }

    public HttpHelper addFormPart(MultiPartParameter param) throws UnsupportedEncodingException {
//        Log.d(MainActivity.LOG_TAG, "addFormPart " + paramName + " = " + value);
        stringBuilder.append(delimiter).append(boundary).append("\r\n");
        stringBuilder.append("Content-Type: ").append(param.getContentType()).append("\r\n");
        stringBuilder.append("Content-Disposition: form-data; name=\"").append(param.getName()).append("\"\r\n");
        stringBuilder.append("\r\n").append(URLEncoder.encode(param.getContent(), "UTF-8")).append("\r\n");
        return this;
    }

    public void doMultipartRequest() throws Exception {
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        stringBuilder.append(delimiter).append(boundary).append(delimiter).append(lineEnd);
//        Log.d(MainActivity.LOG_TAG, "doMultipartRequest: " + stringBuilder);

        connection.connect();
        OutputStream os;
        os = connection.getOutputStream();
        os.write((stringBuilder.toString()).getBytes());
    }

    public String getResponse() throws Exception {
        InputStream is = connection.getInputStream();
        byte[] b1 = new byte[1024];
        StringBuilder buffer = new StringBuilder();

        while ( is.read(b1) != -1)
            buffer.append(new String(b1));

        return buffer.toString();
    }

    public Map<String, List<String>> getHeadrFields() {
        return connection.getHeaderFields();
    }

    public String getHeaders(){
        StringBuilder ret = new StringBuilder();
        Map<String, List<String>> headerFields = connection.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
            if (entry.getKey() != null) {
                ret.append(entry.getKey()).append(":").append(lineEnd);
            } else {
                ret.append(lineEnd);
            }
            List<String> list = entry.getValue();
            for (String l : list) {
                ret.append(l).append(lineEnd);
            }
        }
        return ret.toString();
    }

    public String getCookies() {
/*
        CookieStore cookieStore = msCookieManager.getCookieStore();
        List<HttpCookie> cookies = cookieStore.getCookies();
        for (HttpCookie cookie: cookies) {
            Log.d(MainActivity.LOG_TAG, "getCookies: " + cookie);
        }

        Map<String, List<String>> headerFields = connection.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

        if(cookiesHeader != null) {
            for (String cookie : cookiesHeader)
            {
                msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
            }
        }
*/

        if(msCookieManager.getCookieStore().getCookies().size() > 0) {
//            Log.d(MainActivity.LOG_TAG, "getCookies " + msCookieManager.getCookieStore().toString());
            return TextUtils.join(",", msCookieManager.getCookieStore().getCookies());
        } else {
            return "No Cookies";
        }
    }
    public void disConnect() throws Exception {
        connection.disconnect();
    }

    public CookieStore getCookieStore() {
        return msCookieManager.getCookieStore();
    }
}
