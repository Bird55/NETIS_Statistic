package ru.netis.android.netisstatistic;

import android.os.Parcel;
import android.os.Parcelable;

public class ConsumeSet implements Parcelable {

    String[] services;
    String[] values;

    public ConsumeSet(String[] services, String[] values) {
        this.services = services;
        this.values = values;
    }

    public ConsumeSet(Parcel source) {
        source.readStringArray(services);
        source.readStringArray(values);
    }

    public String[] getServices() {
        return services;
    }

    public String[] getValues() {
        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(services);
        dest.writeStringArray(values);
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
