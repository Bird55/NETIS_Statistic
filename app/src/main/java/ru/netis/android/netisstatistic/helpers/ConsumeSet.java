package ru.netis.android.netisstatistic.helpers;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import ru.netis.android.netisstatistic.Constants;
import ru.netis.android.netisstatistic.NetisStatApplication;
import ru.netis.android.netisstatistic.R;

public class ConsumeSet implements Parcelable {

    public static final int DATA_FROM = 0;
    public static final int DATA_TO = 1;
    private static final boolean DEBUG = false;

    String[] services;
    String[] values;

    DateOf dataFrom;
    DateOf dataTo;

    int indOfService;

    public ConsumeSet(String[] serv, String[] val) {
        ArrayList<String> list = new ArrayList<>();

        list.add(NetisStatApplication.getInstance().getResources().getString(R.string.service_prompt));
        if (DEBUG) Log.d(Constants.LOG_TAG, "ConsumeSet.Constructor \"" + list.get(0) + "\"");
        Collections.addAll(list, serv);
        services = new String[list.size()];
        services = list.toArray(services);

        list = new ArrayList<>();
        list.add("0");
        Collections.addAll(list, val);
        values = new String[list.size()];
        values = list.toArray(values);

        indOfService = 0;
    }

    public ConsumeSet(Parcel source) {
        source.readStringArray(services);
        source.readStringArray(values);
        dataFrom = source.readParcelable((ClassLoader) DateOf.CREATOR);
        dataTo = source.readParcelable((ClassLoader) DateOf.CREATOR);
        indOfService = source.readInt();
    }

    public String[] getServices() {
        return services;
    }

    public String[] getValues() {
        return values;
    }

    public DateOf getDateFrom() {
        return dataFrom;
    }

    public void setDateFrom(DateOf dataFrom) {
        this.dataFrom = dataFrom;
    }

    public DateOf getDateTo() {
        return dataTo;
    }

    public void setDateTo(DateOf dataTo) {
        this.dataTo = dataTo;
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
        dest.writeParcelable(dataFrom, DATA_FROM);
        dest.writeParcelable(dataTo, DATA_TO);
        dest.writeInt(indOfService);
    }


    public static final Parcelable.Creator<ConsumeSet> CREATOR = new Parcelable.Creator<ConsumeSet>() {

        @Override
        public ConsumeSet createFromParcel(Parcel source) {
            return new ConsumeSet(source);
        }

        @Override
        public ConsumeSet[] newArray(int size) {
            return new ConsumeSet[size];
        }
    };
}
