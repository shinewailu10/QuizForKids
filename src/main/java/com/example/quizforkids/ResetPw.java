package com.example.quizforkids;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class ResetPw extends Fragment {

    EditText oldPassword, newPassword;
    Button resetButton;

    DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_pw, container, false);

        oldPassword = view.findViewById(R.id.password2);
        newPassword = view.findViewById(R.id.password1);
        resetButton = view.findViewById(R.id.btnreset);

        dbHelper = new DBHelper(getActivity());

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPw = oldPassword.getText().toString().trim();
                String newPw = newPassword.getText().toString().trim();

                if (oldPw.equals(newPw)) {
                    Toast.makeText(getActivity(), "Old and new passwords cannot be the same", Toast.LENGTH_SHORT).show();
                } else {
                    // Call a method in DBHelper to update the password
                    boolean updated = dbHelper.updatePassword(oldPw, newPw);
                    if (updated) {
                        Toast.makeText(getActivity(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed to update password. Please check your old password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }
}
