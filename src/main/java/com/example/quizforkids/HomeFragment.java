package com.example.quizforkids;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class HomeFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button btn = (Button) (this.getView().findViewById(R.id.animalQ));
        final Button btn1 = (Button) (this.getView().findViewById(R.id.cartoonQ));

        btn.setOnClickListener((v -> {
            Intent intent  = new Intent(getActivity(), AnimalQuiz.class);
            startActivity(intent);
        }

        ));

        btn1.setOnClickListener((v -> {
            Intent intent1  = new Intent(getActivity(), CartoonsQuiz.class);
            startActivity(intent1);
        }

        ));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}