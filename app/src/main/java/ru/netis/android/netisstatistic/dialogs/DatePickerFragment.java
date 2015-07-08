package ru.netis.android.netisstatistic.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import ru.netis.android.netisstatistic.Constants;
import ru.netis.android.netisstatistic.fragments.ConsumeSetFragment;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    int year, month, day;
    ConsumeSetFragment parent;

    public DatePickerFragment() {
    }

    public DatePickerFragment(ConsumeSetFragment parent, int year, int month, int day) {
        this.parent = parent;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        if (("datePickerFrom").equals(this.getTag())) {
            parent.setDateFrom(year, month, day);
        } else {
            if (("datePickerTo").equals(this.getTag())) {
                parent.setDateTo(year, month, day);
            } else
                Log.d(Constants.LOG_TAG, "Bla-bla-bla");
        }
    }
}
