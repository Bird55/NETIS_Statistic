package ru.netis.android.netisstatistic.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import ru.netis.android.netisstatistic.Constants;
import ru.netis.android.netisstatistic.helpers.ConsumeSet;
import ru.netis.android.netisstatistic.R;
import ru.netis.android.netisstatistic.dialogs.DatePickerCallback;
import ru.netis.android.netisstatistic.dialogs.DatePickerFragment;
import ru.netis.android.netisstatistic.helpers.DateOf;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConsumeSetFragment.OnConsumeSetFragmentListener} interface
 * to handle interaction events.
 * Use the {@link ConsumeSetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsumeSetFragment extends Fragment implements View.OnClickListener, DatePickerCallback {

    private static final String ARG_CONSUME_SET = "consume_set";
    private static final boolean DEBUG = true;
    public static final String strFormat = "%02d.%02d.%4d";
    private static final String URL_CONSUME = "view/consume.pl";

    private ConsumeSet consumeSet;
    private OnConsumeSetFragmentListener mListener;

    Spinner dropdown;
    Button btnFrom;
    Button btnTo;

    public ProgressDialog progressDialog;
    DateOf dateFrom;
    DateOf dateTo;
    private AppCompatActivity activity;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param consumeSet Parameter 1.
     * @return A new instance of fragment ConsumeSetFragment.
     */
    public static ConsumeSetFragment newInstance(ConsumeSet consumeSet) {
        if (DEBUG) Log.d(Constants.LOG_TAG, "ConsumeSetFragment.newInstance consumeSet is " + consumeSet);
        ConsumeSetFragment fragment = new ConsumeSetFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CONSUME_SET, consumeSet);
        fragment.setArguments(args);
        return fragment;
    }

    public ConsumeSetFragment() {
        if (DEBUG) Log.d(Constants.LOG_TAG, "ConsumeSetFragment.Constructor consumeSet is " + consumeSet);
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            consumeSet = getArguments().getParcelable(ARG_CONSUME_SET);
        }
        if (DEBUG) Log.d(Constants.LOG_TAG, "ConsumeSetFragment.onCreate consumeSet is " + consumeSet);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(getArguments() != null) {
            consumeSet = getArguments().getParcelable(ARG_CONSUME_SET);
            dateFrom = consumeSet.getDateFrom();
            dateTo = consumeSet.getDateTo();

            dropdown.setSelection(consumeSet.getIndOfService(), true);
            btnFrom.setText(String.format(strFormat, dateFrom.day, dateFrom.month + 1, dateFrom.year));
            btnTo.setText(String.format(strFormat, dateTo.day, dateTo.month + 1, dateTo.year));
        }
        if (DEBUG) Log.d(Constants.LOG_TAG, "ConsumeSetFragment.onViewStateRestore consumeSet is " + consumeSet);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consume_set, container, false);

        dropdown = (Spinner)view.findViewById(R.id.spinnerServices);
        String[] items = consumeSet.getServices();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, items);
        adapter.setDropDownViewResource(R.layout.dropdown_item_1line);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(consumeSet.getIndOfService(), true);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (DEBUG) Log.d(Constants.LOG_TAG, "onItemSelected position = " + position);
                consumeSet.setIndOfService(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dateFrom = consumeSet.getDateFrom();
        dateTo = consumeSet.getDateTo();

        btnFrom = (Button) view.findViewById(R.id.btnFrom);
        btnFrom.setOnClickListener(this);

        btnTo = (Button) view.findViewById(R.id.btnTo);
        btnTo.setOnClickListener(this);

        btnFrom.setText(String.format(strFormat, dateFrom.day, dateFrom.month + 1, dateFrom.year));
        if (DEBUG) Log.d(Constants.LOG_TAG, "ConsumeSetFragment.onCreateView. consumeSet is " + consumeSet);
        return view;
    }

    @Override
    public void onClick(View v) {

        DialogFragment newFragment;

        if (DEBUG) Log.d(Constants.LOG_TAG, "ConsumeSetFragment.onClick v.getId() is " + v.getId());

        switch (v.getId()){
            case R.id.btnFrom:
                newFragment = new DatePickerFragment(this, dateFrom.year, dateFrom.month, dateFrom.day);
                newFragment.show(activity.getSupportFragmentManager(), "datePickerFrom");
                break;
            case R.id.btnTo:
                newFragment = new DatePickerFragment(this, dateTo.year, dateTo.month, dateTo.day);
                newFragment.show(activity.getSupportFragmentManager(), "datePickerTo");
                break;
            case R.id.btnSubmit:
                if (mListener != null) {
                    mListener.onFragmentInteraction(consumeSet);
                }
                break;
        }
        if (DEBUG) Log.d(Constants.LOG_TAG, "ConsumeSetFragment.onClick consumeSet is " + consumeSet);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (AppCompatActivity)activity;
        try {
            mListener = (OnConsumeSetFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if (DEBUG) Log.d(Constants.LOG_TAG, "ConsumeSetFragment.onAttach consumeSet is " + consumeSet);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (DEBUG) Log.d(Constants.LOG_TAG, "ConsumeSetFragment.onDetach consumeSet is " + consumeSet);
    }

    @Override
    public void setDateFrom(int year, int month, int day) {
        dateFrom.setDay(day).setMonth(month).setYear(year);
        btnFrom.setText(String.format(strFormat, day, month + 1, year));
    }

    @Override
    public void setDateTo(int year, int month, int day) {
        dateTo.setDay(day).setMonth(month).setYear(year);
        btnTo.setText(String.format(strFormat, day, month + 1, year));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnConsumeSetFragmentListener {
        void onFragmentInteraction(ConsumeSet set);
    }
}
