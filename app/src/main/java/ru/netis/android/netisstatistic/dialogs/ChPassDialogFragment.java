package ru.netis.android.netisstatistic.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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

public class ChPassDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "ChPassDialogFragment";

    private EditText oldPassword;
    private EditText newPassword;
    private EditText newPasswordRetype;

    private OnChPassCallback callback;

    public interface OnChPassCallback {
        void onChPass(String oldPass, String newPass, String retPass);
    }

    public ChPassDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (OnChPassCallback)activity;
        } catch (ClassCastException e) {
            Log.d(Constants.LOG_TAG, "The MainActivity should implement the OnLoginCallback interface");
            e.printStackTrace();
        }
            
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_ch_pass, container);
        oldPassword = (EditText) view.findViewById(R.id.oldPassword);
        newPassword = (EditText) view.findViewById(R.id.newPassword);
        newPasswordRetype = (EditText) view.findViewById(R.id.newPasswordRetype);
        Button btnChangePassword = (Button) view.findViewById(R.id.btnChangePassword);

        // Set the Dialog's title
        getDialog().setTitle(getResources().getString(R.string.title_change_password));

        btnChangePassword.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        String oldPass  = oldPassword.getText().toString();
        String newPass = newPassword.getText().toString();
        String retPass = newPasswordRetype.getText().toString();
        if (!TextUtils.isEmpty(oldPass) && !TextUtils.isEmpty(newPass) && !TextUtils.isEmpty(retPass)) {
            // Return the name and password to the calling activity
            callback.onChPass(oldPass, newPass, retPass);
            this.dismiss();
        } else
            Toast.makeText(getActivity(), "You should enter your name !", Toast.LENGTH_SHORT).show();
    }
}
