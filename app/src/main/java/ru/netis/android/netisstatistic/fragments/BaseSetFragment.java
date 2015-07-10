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
import ru.netis.android.netisstatistic.R;
import ru.netis.android.netisstatistic.dialogs.DatePickerFragment;
import ru.netis.android.netisstatistic.helpers.BaseSet;
import ru.netis.android.netisstatistic.helpers.DateOf;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BaseSetFragment.OnBaseSetFragmentListener} interface
 * to handle interaction events.
 * Use the {@link BaseSetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BaseSetFragment extends Fragment implements View.OnClickListener, DatePickerFragment.DatePickerCallback {

    private static final String ARG_BASE_SET = "consume_set";
    private static final boolean DEBUG = true;
    public static final String strFormat = "%02d.%02d.%4d";

    private BaseSet baseSet;
    private OnBaseSetFragmentListener mListener;

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
     * @param baseSet Parameter 1.
     * @return A new instance of fragment BaseSetFragment.
     */
    public static BaseSetFragment newInstance(BaseSet baseSet) {
        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseSetFragment.newInstance baseSet is " + baseSet);
        BaseSetFragment fragment = new BaseSetFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BASE_SET, baseSet);
        fragment.setArguments(args);
        return fragment;
    }

    public BaseSetFragment() {
        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseSetFragment.Constructor baseSet is " + baseSet);
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            baseSet = getArguments().getParcelable(ARG_BASE_SET);
        }
        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseSetFragment.onCreate baseSet is " + baseSet);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(getArguments() != null) {
            baseSet = getArguments().getParcelable(ARG_BASE_SET);
            dateFrom = baseSet.getDateFrom();
            dateTo = baseSet.getDateTo();

            dropdown.setSelection(baseSet.getIndOfService(), true);
            btnFrom.setText(String.format(strFormat, dateFrom.day, dateFrom.month + 1, dateFrom.year));
            btnTo.setText(String.format(strFormat, dateTo.day, dateTo.month + 1, dateTo.year));
        }
        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseSetFragment.onViewStateRestore baseSet is " + baseSet);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base_set, container, false);

        dropdown = (Spinner)view.findViewById(R.id.spinnerServices);
        String[] items = baseSet.getServices();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, items);
        adapter.setDropDownViewResource(R.layout.dropdown_item_1line);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(baseSet.getIndOfService(), true);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (DEBUG) Log.d(Constants.LOG_TAG, "onItemSelected position = " + position);
                baseSet.setIndOfService(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dateFrom = baseSet.getDateFrom();
        dateTo = baseSet.getDateTo();

        btnFrom = (Button) view.findViewById(R.id.btnFrom);
        btnFrom.setOnClickListener(this);

        btnTo = (Button) view.findViewById(R.id.btnTo);
        btnTo.setOnClickListener(this);

        btnFrom.setText(String.format(strFormat, dateFrom.day, dateFrom.month + 1, dateFrom.year));
        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseSetFragment.onCreateView. baseSet is " + baseSet);
        return view;
    }

    @Override
    public void onClick(View v) {

        DialogFragment newFragment;

        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseSetFragment.onClick v.getId() is " + v.getId());

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
                    mListener.onFragmentInteraction(baseSet);
                }
                break;
        }
        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseSetFragment.onClick baseSet is " + baseSet);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (AppCompatActivity)activity;
        try {
            mListener = (OnBaseSetFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseSetFragment.onAttach baseSet is " + baseSet);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (DEBUG) Log.d(Constants.LOG_TAG, "BaseSetFragment.onDetach baseSet is " + baseSet);
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
    public interface OnBaseSetFragmentListener {
        void onFragmentInteraction(BaseSet set);
    }


}
