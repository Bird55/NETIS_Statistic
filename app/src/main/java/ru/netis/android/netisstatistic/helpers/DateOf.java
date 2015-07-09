package ru.netis.android.netisstatistic.helpers;


import android.os.Parcel;
import android.os.Parcelable;

public class DateOf implements Parcelable {
    public int day, month, year;

    public DateOf(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public DateOf(Parcel source) {
        day = source.readInt();
        month = source.readInt();
        year = source.readInt();
    }

    public int getDay() {
        return day;
    }

    public DateOf setDay(int day) {
        this.day = day;
        return this;
    }

    public int getMonth() {
        return month;
    }

    public DateOf setMonth(int month) {
        this.month = month;
        return this;
    }

    public int getYear() {
        return year;
    }

    public DateOf setYear(int year) {
        this.year = year;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(day);
        dest.writeInt(month);
        dest.writeInt(year);
    }

    public static final Parcelable.Creator<DateOf> CREATOR = new Parcelable.Creator<DateOf>() {

        @Override
        public DateOf createFromParcel(Parcel source) {
            return new DateOf(source);
        }

        @Override
        public DateOf[] newArray(int size) {
            return new DateOf[size];
        }
    };
}
