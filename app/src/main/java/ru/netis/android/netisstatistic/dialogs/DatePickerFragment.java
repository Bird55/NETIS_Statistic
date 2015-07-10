package ru.netis.android.netisstatistic.dialogs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.DatePicker;

import ru.netis.android.netisstatistic.Constants;
import ru.netis.android.netisstatistic.fragments.BaseSetFragment;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private static final boolean DEBUG = true;
    int year, month, day;
    BaseSetFragment parent;
    private DatePickerDialog.OnDateSetListener mListener;

    public DatePickerFragment() {
    }

    public DatePickerFragment(BaseSetFragment parent, int year, int month, int day) {
        this.parent = parent;
        this.year = year;
        this.month = month;
        this.day = day;
        if(DEBUG) Log.d(Constants.LOG_TAG, "DatePickerFragment.Constructor day = " + day + " month = " + month + " year = " +year);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mListener = this;
        final DatePickerDialog dateDialog = new DatePickerDialog(getActivity(), this, year, month, day);

        if (hasJellyBeanAndAbove()) {
            dateDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                    getActivity().getResources().getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatePicker dp = dateDialog.getDatePicker();
                            mListener.onDateSet(dp, dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
                        }
                    });
            dateDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                    getActivity().getResources().getString(android.R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
        }
        return dateDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (DEBUG) Log.d(Constants.LOG_TAG, "DatePickerFragment.onDateSet view.getId() = " + view.getId());
        if (("datePickerFrom").equals(this.getTag())) {
            parent.setDateFrom(year, month, day);
        } else {
            if (("datePickerTo").equals(this.getTag())) {
                parent.setDateTo(year, month, day);
            } else
                Log.d(Constants.LOG_TAG, "Bla-bla-bla");
        }
    }

    private static boolean hasJellyBeanAndAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public interface DatePickerCallback {
        void setDateFrom(int year, int month, int day);
        void setDateTo(int year, int month, int day);
    }
}
