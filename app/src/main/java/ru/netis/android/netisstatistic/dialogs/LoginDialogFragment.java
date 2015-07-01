package ru.netis.android.netisstatistic.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.netis.android.netisstatistic.Constants;
import ru.netis.android.netisstatistic.R;

public class LoginDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "LoginDialogFragment";

    private EditText nameEditText;
    private EditText passwordEditText;

    private OnLoginCallback callback;

    public interface OnLoginCallback {
        void onLogin(String name, String password);
    }

    public LoginDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (OnLoginCallback)activity;
        } catch (ClassCastException e) {
            Log.d(Constants.LOG_TAG, "The MainActivity should implement the OnLoginCallback interface");
            e.printStackTrace();
        }
            
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_login, container);
        nameEditText = (EditText) view.findViewById(R.id.nameEditText);
        passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);
        Button buttonSubmit = (Button) view.findViewById(R.id.buttonSubmit);

        // Set the progressDialog's title
        getDialog().setTitle(getResources().getString(R.string.title_login));

        buttonSubmit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Editable name = nameEditText.getText();
        Editable password = passwordEditText.getText();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
            // Return the name and password to the calling activity
            callback.onLogin(name.toString(), password.toString());
            this.dismiss();
        } else
            Toast.makeText(getActivity(), "You should enter your name !", Toast.LENGTH_SHORT).show();
    }
}
