package ru.netis.android.netisstatistic.helpers;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import ru.netis.android.netisstatistic.Constants;
import ru.netis.android.netisstatistic.NetisStatApplication;
import ru.netis.android.netisstatistic.R;

public class BaseSet implements Parcelable {

    public static final int DATA_FROM = 0;
    public static final int DATA_TO = 1;
    private static final boolean DEBUG = false;

    String[] services;
    String[] values;

    DateOf dateFrom;
    DateOf dateTo;

    int indOfService;

    public BaseSet(String[] serv, String[] val) {
        ArrayList<String> list = new ArrayList<>();

        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseSet.Constructor serv[1] is \"" + serv[1] + "\"");
        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseSet.Constructor val[1] is \"" + val[1] + "\"");

        list.add(NetisStatApplication.getInstance().getResources().getString(R.string.service_prompt));
        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseSet.Constructor \"" + list.get(0) + "\"");
        Collections.addAll(list, serv);
        services = new String[list.size()];
        services = list.toArray(services);

        list = new ArrayList<>();
        list.add("0");
        Collections.addAll(list, val);
        values = new String[list.size()];
        values = list.toArray(values);

        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseSet.Constructor services[1] is \"" + services[1] + "\"");
        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseSet.Constructor values[1] is \"" + values[1] + "\"");

        indOfService = 0;
    }

    public BaseSet(Parcel source) {
        source.readStringArray(services);
        source.readStringArray(values);
        dateFrom = source.readParcelable((ClassLoader) DateOf.CREATOR);
        dateTo = source.readParcelable((ClassLoader) DateOf.CREATOR);
        indOfService = source.readInt();
    }

    public String[] getServices() {
        return services;
    }
    
    public String getService(int ind) {
        return services[ind];
    }

    public String[] getValues() {
        return values;
    }

    public String getValue(int ind) {
        return values[ind];
    }

    public DateOf getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(DateOf dateFrom) {
        this.dateFrom = dateFrom;
    }

    public DateOf getDateTo() {
        return dateTo;
    }

    public void setDateTo(DateOf dateTo) {
        this.dateTo = dateTo;
    }

    public int getIndOfService() {
        return indOfService;
    }

    public void setIndOfService(int indOfService) {
        this.indOfService = indOfService;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(services);
        dest.writeStringArray(values);
        dest.writeParcelable(dateFrom, DATA_FROM);
        dest.writeParcelable(dateTo, DATA_TO);
        dest.writeInt(indOfService);
    }


    public static final Creator<BaseSet> CREATOR = new Creator<BaseSet>() {

        @Override
        public BaseSet createFromParcel(Parcel source) {
            return new BaseSet(source);
        }

        @Override
        public BaseSet[] newArray(int size) {
            return new BaseSet[size];
        }
    };
}
