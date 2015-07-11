package ru.netis.android.netisstatistic;

import android.util.Log;

import java.util.ArrayList;

import ru.netis.android.netisstatistic.helpers.BaseSet;
import ru.netis.android.netisstatistic.helpers.Client;

public final class Constants {

    public static final String POST = "POST";
    public static final String GET = "GET";


    public static final boolean DEBUG = false;
    public static final String LOG_TAG = "myLog";

    static final String BASE_URL = "http://stat.netis.ru/";

//    public static final int LOGIN_REQUEST = 1;
//    public static final int CHANGE_LOGIN_REQUEST = 2;
    public static final int TAG_LOGIN = 0;
    public static final int TAG_SALDO = 1;
    public static final int TAG_CHANGE_PASSWORD = 2;
    public static final int TAG_INDEX = 3;
    public static final int TAG_CONSUME = 4;
    public static final int TAG_SESSIONS = 5;
    public static final int TAG_GET_CONSUME = 6;
    public static final int TAG_GET_SESSIONS = 7;

    public static Client getClient(String data) {
        boolean ownership;
        String name;
        String id;
        double saldo;
        String contract;
        String contractDate;
        String IPv4;

        String s1;
        int i1, i2;

        ownership = false;
        s1 ="Организация: <i>";
        i1 = data.indexOf(s1);
        if (DEBUG) Log.d(LOG_TAG, "getClient i1 = " + i1);
        if (i1 < 0) {
            ownership = true;
            s1 = "ФИО: <i>";
            i1 = data.indexOf(s1);
            if (DEBUG) Log.d(LOG_TAG, "getClient FIO");
        }
        i1 += s1.length();
        i2 = data.indexOf('<', i1);
        name = data.substring(i1, i2);
        if (DEBUG) Log.d(LOG_TAG, "Constants.getClient.name => \"" + name + "\"");


        s1 = "счёт: <b>";
        i1 = data.indexOf(s1) + s1.length();
        i2 = data.indexOf('<', i1);
        id = data.substring(i1, i2);
        if (DEBUG) Log.d(LOG_TAG, "Constants.getClient.id => \"" + id + "\"");

        s1 = "счёта: <b>";
        i1 = data.indexOf(s1) + s1.length();
        i2 = data.indexOf("руб", i1);
        if (DEBUG) Log.d(LOG_TAG, "Constants.getClient s1\"" + s1 + "\" i1 = " + i1 + " i2 = " + i2);
        s1 = data.substring(i1, i2);

        if (!s1.contains("color")) {
            i2 = data.indexOf('<', i1);
            saldo = Double.valueOf(data.substring(i1, i2).replace('−', '-'));
        } else {
            i1 = s1.indexOf("\">") + 2;
            i2 = s1.indexOf("</");
            saldo = Double.valueOf(s1.substring(i1, i2).replace('−', '-'));
        }
        if (DEBUG) Log.d(LOG_TAG, "Constants.getClient.saldo = " + saldo);

        s1 = "Номер договора: <i>";
        i1 = data.indexOf(s1);
        if (i1 < 0) {
            contract = "Без контракта";
        } else {
            i1 += s1.length();
            i2 = data.indexOf("</", i1);
            contract = data.substring(i1, i2);
        }
        if (DEBUG) Log.d(LOG_TAG, "Constants.getClient.contract = " + contract);

        s1 = "</i> от ";
        i1 = data.indexOf(s1) + s1.length();
        i2 = data.indexOf('<', i1);
        contractDate = data.substring(i1, i2);
        if (DEBUG) Log.d(LOG_TAG, "Constants.getClient.contractDate = " + contractDate);

        s1 = "Ваш IPv4 адрес:";
        i1 = data.indexOf(s1) + s1.length();
        s1 = "<span>";
        i1 = data.indexOf(s1, i1) + s1.length();
        i2 = data.indexOf('<', i1);
        IPv4 = data.substring(i1, i2);
        if (DEBUG) Log.d(LOG_TAG, "Constants.getClient.IPv4 = " + IPv4);

        return new Client(ownership, name, id, saldo, contract, contractDate, IPv4);
    }

    public static BaseSet getBaseSet(String data) {
        ArrayList<String> srv = new ArrayList<>();
        ArrayList<String> val = new ArrayList<>();
        String s1, s2 = "</s", s3 = "value=\"";
        int i1, i2, i3 = s3.length();
        if (DEBUG) Log.d(LOG_TAG, "Constants.getBaseSet data.length() = " + data.length() );

        s1 = "contr_srv_id";
        i1 = data.indexOf(s1) + s1.length();

        i1 = data.indexOf(s3, i1) + i3;
        do {

            i2 = data.indexOf('"', i1);
            val.add(data.substring(i1, i2));
            if (DEBUG) Log.d(LOG_TAG, "Constants.getBaseSet val = " + data.substring(i1, i2));

            i1 = data.indexOf('>', i2) + 1;
            i2 = data.indexOf('<', i1);
            s1 = data.substring(i1, i2);
            srv.add(s1.replace("&quot;", "\""));
            if (DEBUG) Log.d(LOG_TAG, "Constants.getBaseSet srv = " + data.substring(i1, i2));

        } while ((i1 = data.indexOf(s3, i2) + i3) < (data.indexOf(s2, i2)));
        if (DEBUG) Log.d(LOG_TAG, "Constants.getBaseSet srv = " + srv);
        if (DEBUG) Log.d(LOG_TAG, "Constants.getBaseSet val = " + val);

        String[] s = new String[srv.size()];
        s = srv.toArray(s);
        String[] v = new String[val.size()];
        v = val.toArray(v);

        return new BaseSet(s, v);
    }
}
