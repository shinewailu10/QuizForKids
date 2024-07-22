package com.example.quizforkids;

import static com.example.quizforkids.login.uname;

import android.content.Intent;
import android.os.Bundle;



import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LogOff extends Fragment {

    DBHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log_off, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DBHelper(requireContext());
        int overallPoints = db.getOverallPoints(uname);


        final Button btn = (Button) (this.getView().findViewById(R.id.logout));

        btn.setOnClickListener((v -> {
            String message = uname + ", you have overall " + overallPoints + " points.";
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            Intent intent  = new Intent(getActivity(), login.class);
            startActivity(intent);
            getActivity().finish();
        }

                ));


    }
}