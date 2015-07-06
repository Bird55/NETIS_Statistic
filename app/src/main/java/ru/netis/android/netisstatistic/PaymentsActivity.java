package ru.netis.android.netisstatistic;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import java.util.Calendar;

import ru.netis.android.netisstatistic.dialogs.DatePickerCallback;
import ru.netis.android.netisstatistic.tools.HttpHelper;


public class PaymentsActivity extends AppCompatActivity implements View.OnClickListener, DatePickerCallback {
    String strFormat = "%02d.%02d.%4d";
    int yearFrom, yearTo;
    int monthFrom, monthTo;
    int dayFrom, dayTo;

    Button btnFrom;
    Button btnTo;

    private static final String URL = "view/pmnt.pl";
    private HttpHelper helper;
    MenuItem mActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        btnFrom = (Button) findViewById(R.id.btnFrom);
        btnFrom.setOnClickListener(this);

        btnTo = (Button) findViewById(R.id.btnTo);
        btnTo.setOnClickListener(this);

        final Calendar c = Calendar.getInstance();
        yearFrom = yearTo = c.get(Calendar.YEAR);
        monthFrom = monthTo = c.get(Calendar.MONTH);
        dayFrom = 1;
        dayTo = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        btnFrom.setText(String.format(strFormat, dayFrom, monthFrom + 1, yearFrom));
        btnTo.setText(String.format(strFormat, dayTo, monthTo + 1, yearTo));



    }

    @Override
    public void onClick(View v) {

        DialogFragment newFragment;

        switch (v.getId()){
            case R.id.btnFrom:
                newFragment = new DatePickerFragment(yearFrom, monthFrom, dayFrom);
                newFragment.show(getSupportFragmentManager(), "datePickerFrom");
                break;
            case R.id.btnTo:
                newFragment = new DatePickerFragment(yearTo, monthTo, dayTo);
                newFragment.show(getSupportFragmentManager(), "datePickerTo");
                break;
            case R.id.btnSubmit:
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_progress, menu);
        // Store instance of the menu item containing progress
        mActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(mActionProgressItem);
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showProgressBar() {
        // Show progress item
        mActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        mActionProgressItem.setVisible(false);
    }

    @Override
    public void setDateFrom(int year, int month, int day) {
        yearFrom = year; monthFrom = month; dayFrom = day;
    }

    @Override
    public void setDateTo(int year, int month, int day) {
        yearTo = year; monthTo = month; dayTo = day;
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        int year, month, day;

        public DatePickerFragment() {
        }

        public DatePickerFragment(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            if (("datePickerFrom").equals(this.getTag())) {
                ((PaymentsActivity) getActivity()).setDateFrom(year, month, day);
                ((PaymentsActivity) getActivity())
                        .btnFrom.setText(String.format(((PaymentsActivity) getActivity()).strFormat,
                                day, month + 1, year));
            } else {
                if (("datePickerTo").equals(this.getTag())) {
                    ((PaymentsActivity) getActivity()).setDateTo(year, month, day);
                    ((PaymentsActivity) getActivity())
                            .btnTo.setText(String.format(((PaymentsActivity) getActivity()).strFormat,
                            day, month + 1, year));
                } else
                    Log.d(Constants.LOG_TAG, "Bla-bla-bla");
            }
        }
    }
}
