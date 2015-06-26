package ru.netis.android.netisstatistic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DataFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = inflater.getContext();
//        initVolley(context);
        update();
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        volley.stop();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        if (activity instanceof ArticleCustomer) {
//            articleCustomer = (ArticleCustomer)activity;
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        articleCustomer = null;
    }

//    private void initVolley(Context context) {
//        if (volley == null) {
//            volley = VolleySingleton.getInstance(context);
//        }
//    }

    private void update() {
        // TODO begin to make frame content
    }
}
