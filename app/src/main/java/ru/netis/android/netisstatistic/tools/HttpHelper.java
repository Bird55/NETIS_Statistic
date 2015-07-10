package ru.netis.android.netisstatistic.tools;

import android.text.TextUtils;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import ru.netis.android.netisstatistic.Constants;
import ru.netis.android.netisstatistic.NetisStatApplication;

public class HttpHelper {

    private HttpURLConnection connection;
    private StringBuilder stringBuilder;
    private String url;
    private static final String delimiter = "--";
    private static final String boundary =  "SwA"+Long.toString(System.currentTimeMillis())+"SwA";

    private CookieManager msCookieManager;
    private static final String lineEnd = "\r\n";


    public HttpHelper(String url) {
        this.url = url;
        msCookieManager = NetisStatApplication.getInstance().getCookieManager();
        stringBuilder = new StringBuilder();
    }

    public String getUrl() {
        return url;
    }

    public void connectForMultipart() throws Exception {
        connection = (HttpURLConnection) ( new URL(url)).openConnection();
        connection.setInstanceFollowRedirects(false);
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        if(msCookieManager.getCookieStore().getCookies().size() == 0) {
            Log.d(Constants.LOG_TAG, "connectForMultipart: Set cookie");
            connection.setRequestProperty("Cookie", "SID=-;path=/");
        }

        connection.setRequestProperty("Referer", "stat.netis.ru/");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
    }

    public HttpHelper addFormPart(String paramName, String value) {
        stringBuilder.append(delimiter).append(boundary).append(lineEnd);
//        stringBuilder.append("Content-Type: text/plain\r\n");
        stringBuilder.append("Content-Disposition: form-data; name=\"").append(paramName).append("\"").append(lineEnd);
        stringBuilder.append(lineEnd).append(value).append(lineEnd);
        return this;
    }

    public HttpHelper addFormPart(MultipartParameter param) {
        stringBuilder.append(delimiter).append(boundary).append(lineEnd);
        stringBuilder.append("Content-Type: ").append(param.getContentType()).append("\r\n");
        stringBuilder.append("Content-Disposition: form-data; name=\"").append(param.getName()).append("\"").append(lineEnd);
        stringBuilder.append(lineEnd).append(param.getContent()).append(lineEnd);
        return this;
    }

    public void finishMultipart() throws Exception {
        stringBuilder.append(delimiter).append(boundary).append(delimiter).append(lineEnd);

        connection.connect();
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

        outputStream.write((stringBuilder.toString()).getBytes());
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

    public String getResponse() throws Exception {
        InputStream is = connection.getInputStream();
        byte[] b1 = new byte[1024];
        StringBuilder buffer = new StringBuilder();

        while ( is.read(b1) != -1)
            buffer.append(new String(b1));

        return buffer.toString();
    }

    public String getCookies() {

        if(msCookieManager.getCookieStore().getCookies().size() > 0) {
            return "Cookie: " + TextUtils.join(",", msCookieManager.getCookieStore().getCookies());
        } else {
            return "No Cookies";
        }
    }

    public void disConnect() throws Exception {
        connection.disconnect();
    }
}
