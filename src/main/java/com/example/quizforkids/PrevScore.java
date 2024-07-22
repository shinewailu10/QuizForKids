package com.example.quizforkids;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quizforkids.DBHelper;

public class PrevScore extends Fragment {

    DBHelper db;
    private String sortOrder = "time DESC";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_prev_score, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DBHelper(requireContext());

        // Find the TextView in the layout
        TextView textView = view.findViewById(R.id.textViewAttempts);

        // Set up radio group and listener
        RadioGroup radioGroup = view.findViewById(R.id.radioGroupSortBy);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonDate) {
                sortOrder = "time DESC"; // Sort by date
            } else if (checkedId == R.id.radioButtonArea) {
                sortOrder = "category ASC"; // Sort by area
            }
            // Refresh the view with updated sorting order
            refreshView();
        });

        // Initially load attempts sorted by date (default)
        refreshView();
    }

    private void refreshView() {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Hi ").append(login.uname).append(", you have earned ").append(db.getOverallPoints(login.uname)).append(" points in the following attempts:\n\n");

        if (login.uname != null) {
            Cursor cursor = db.getAttemptsByUsername(login.uname, sortOrder);
            // Rest of the code...


            // Iterate through the cursor and format each attempt entry
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String area = cursor.getString(cursor.getColumnIndex("category"));
                    String startTime = cursor.getString(cursor.getColumnIndex("time"));
                    int pointsEarned = cursor.getInt(cursor.getColumnIndex("point"));

                    // Format the attempt entry
                    String attemptEntry = "\"" + area + "\" area - attempt started on " + startTime + " – points earned " + pointsEarned;
                    // Append the formatted attempt entry to the message
                    messageBuilder.append(attemptEntry).append("\n");
                } while (cursor.moveToNext());

                // Close the cursor after use
                cursor.close();
            }

            // Set the formatted message in the TextView
            TextView textView = requireView().findViewById(R.id.textViewAttempts);
            textView.setText(messageBuilder.toString());
        }
    }
}
