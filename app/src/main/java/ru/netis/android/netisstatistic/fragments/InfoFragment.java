package ru.netis.android.netisstatistic.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.netis.android.netisstatistic.Client;
import ru.netis.android.netisstatistic.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {

    private static final String ARG_CLIENT = "client";

    private Client client;


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param client Parameter 1.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(Client client) {
        InfoFragment fragment = new InfoFragment(client);
        Bundle args = new Bundle();
        args.putParcelable(ARG_CLIENT, client);
        fragment.setArguments(args);
        return fragment;
    }

    public InfoFragment() {
    }

    public InfoFragment(Client client) {
        this.client = client;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            client = getArguments().getParcelable(ARG_CLIENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        TextView tv;

        tv = (TextView) view.findViewById(R.id.owner1);
        if (client.isPerson()) {
            tv.setText(getResources().getString(R.string.name_of_field_person));
        } else {
            tv.setText(getResources().getString(R.string.name_of_field_organization));
        }
        tv = (TextView) view.findViewById(R.id.owner2);
        tv.setText(client.getName());

        tv = (TextView) view.findViewById(R.id.Contract2);
        tv.setText(client.getContract() + " от " + client.getContractDate());

        tv = (TextView) view.findViewById(R.id.id2);
        tv.setText(client.getId());

        double d = client.getSaldo();
        String s = String.valueOf(d);
        Spannable text = new SpannableString(d +  " \u20BD");
        tv = (TextView) view.findViewById(R.id.Saldo2);
        if (d < 0) {
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#cc0000")), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#118800")), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        text.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(text);

        tv = (TextView) view.findViewById(R.id.ipv42);
        tv.setText(client.getIPv4());

        return view;
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
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
