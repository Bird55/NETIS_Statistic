package ru.netis.android.netisstatistic.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Calendar;

import ru.netis.android.netisstatistic.Constants;
import ru.netis.android.netisstatistic.ConsumeSet;
import ru.netis.android.netisstatistic.R;
import ru.netis.android.netisstatistic.dialogs.DatePickerCallback;
import ru.netis.android.netisstatistic.dialogs.DatePickerFragment;

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
    private ConsumeSet consumeSet;
    private OnConsumeSetFragmentListener mListener;

    String strFormat = "%02d.%02d.%4d";
    int yearFrom, yearTo;
    int monthFrom, monthTo;
    int dayFrom, dayTo;

    Button btnFrom;
    Button btnTo;

    private static final String URL_CONSUME = "view/consume.pl";
    public ProgressDialog progressDialog;

    private AppCompatActivity activity;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param consumeSet Parameter 1.
     * @return A new instance of fragment ConsumeSetFragment.
     */
    public static ConsumeSetFragment newInstance(ConsumeSet consumeSet) {
        Log.d(Constants.LOG_TAG, "ConsumeSetFragment.newInstance consumeSet is " + consumeSet);
        ConsumeSetFragment fragment = new ConsumeSetFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CONSUME_SET, consumeSet);
        fragment.setArguments(args);
        return fragment;
    }

    public ConsumeSetFragment() {
        Log.d(Constants.LOG_TAG, "ConsumeSetFragment.ConsumeSetFragment consumeSet is " + consumeSet);
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            consumeSet = getArguments().getParcelable(ARG_CONSUME_SET);
        }
        Log.d(Constants.LOG_TAG, "ConsumeSetFragment.onCreate consumeSet is " + consumeSet);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(getArguments() != null) {
            consumeSet = getArguments().getParcelable(ARG_CONSUME_SET);
        }
        Log.d(Constants.LOG_TAG, "ConsumeSetFragment.onViewStateRestore consumeSet is " + consumeSet);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consume_set, container, false);

        Spinner dropdown = (Spinner)view.findViewById(R.id.spinnerServices);
        String[] items = consumeSet.getServices();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        dropdown.setAdapter(adapter);

        btnFrom = (Button) view.findViewById(R.id.btnFrom);
        btnFrom.setOnClickListener(this);

        btnTo = (Button) view.findViewById(R.id.btnTo);
        btnTo.setOnClickListener(this);

        final Calendar c = Calendar.getInstance();
        yearFrom = yearTo = c.get(Calendar.YEAR);
        monthFrom = monthTo = c.get(Calendar.MONTH);
        dayFrom = 1;
        dayTo = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        btnFrom.setText(String.format(strFormat, dayFrom, monthFrom + 1, yearFrom));
        btnTo.setText(String.format(strFormat, dayTo, monthTo + 1, yearTo));

        Log.d(Constants.LOG_TAG, "ConsumeSetFragment.onCreateView consumeSet is " + consumeSet);
        return view;
    }

    @Override
    public void onClick(View v) {

        DialogFragment newFragment;

        switch (v.getId()){
            case R.id.btnFrom:
                newFragment = new DatePickerFragment(this,yearFrom, monthFrom, dayFrom);
                newFragment.show(activity.getSupportFragmentManager(), "datePickerFrom");
                break;
            case R.id.btnTo:
                newFragment = new DatePickerFragment(this, yearTo, monthTo, dayTo);
                newFragment.show(activity.getSupportFragmentManager(), "datePickerTo");
                break;
//            case R.id.btnSubmit:
//                break;
        }
        Log.d(Constants.LOG_TAG, "ConsumeSetFragment.onClick consumeSet is " + consumeSet);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        Log.d(Constants.LOG_TAG, "ConsumeSetFragment.onAttach consumeSet is " + consumeSet);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.d(Constants.LOG_TAG, "ConsumeSetFragment.onDetach consumeSet is " + consumeSet);
    }

    @Override
    public void setDateFrom(int year, int month, int day) {
        yearFrom = year; monthFrom = month; dayFrom = day;
        btnFrom.setText(String.format(strFormat, dayFrom, monthFrom + 1, yearFrom));
    }

    @Override
    public void setDateTo(int year, int month, int day) {
        yearTo = year; monthTo = month; dayTo = day;
        btnTo.setText(String.format(strFormat, dayTo, monthTo + 1, yearTo));
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
