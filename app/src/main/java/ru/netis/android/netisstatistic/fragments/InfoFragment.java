package ru.netis.android.netisstatistic.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
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

import ru.netis.android.netisstatistic.helpers.Client;
import ru.netis.android.netisstatistic.R;

public class InfoFragment extends Fragment {

    private static final String ARG_CLIENT = "client";

    private Client client;

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
}
