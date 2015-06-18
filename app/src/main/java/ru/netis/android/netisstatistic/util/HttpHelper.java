package ru.netis.android.netisstatistic.util;


import android.util.Log;

import org.apache.http.cookie.Cookie;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by bird on 02.06.2015
 */
public class HttpHelper {
    private static final String LOG_TAG = "myLog";
    private HttpURLConnection con;
    private OutputStream os;
    private StringBuilder stringBuilder;
    private List<String> cookies;

    String delimiter = "--";
    String boundary =  "SwA"+Long.toString(System.currentTimeMillis())+"SwA";

    public HttpHelper() {
        stringBuilder = new StringBuilder();
        cookies = null;
    }

    public void connectForMultipart(String url) throws Exception {
        con = (HttpURLConnection) ( new URL(url)).openConnection();
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        if (cookies == null) {
            con.setRequestProperty("Cookie", "SID=-; path=/");
        } else {
            for (String cookie : cookies) {
                con.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
            }
        }
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
    }

    public void addFormPart(String paramName, String value) {
        Log.d(LOG_TAG, "addFormPart " + paramName + " = " + value + ", " + stringBuilder);
        stringBuilder.append(delimiter).append(boundary).append("\r\n");
        stringBuilder.append("Content-Type: text/plain\r\n");
        stringBuilder.append("Content-Disposition: form-data; name=\"").append(paramName).append("\"\r\n");
        stringBuilder.append("\r\n").append(value).append("\r\n");
    }

    public void finishMultipart() throws Exception {
        int l = stringBuilder.length();

        con.setRequestProperty("Content-Length", ""+l);

        con.connect();
        os = con.getOutputStream();

        os.write((stringBuilder.toString()).getBytes());
    }

    public String getResponse() throws Exception {
        InputStream is = con.getInputStream();
        byte[] b1 = new byte[1024];
        StringBuilder buffer = new StringBuilder();

        while ( is.read(b1) != -1)
            buffer.append(new String(b1));

        return buffer.toString();
    }

    public void disConnect() throws Exception {
        con.disconnect();
    }
}
