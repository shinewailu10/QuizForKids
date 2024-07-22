package com.example.quizforkids;


import static com.example.quizforkids.login.uname;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import pl.droidsonroids.gif.GifImageView;

public class ResultActivity extends AppCompatActivity {

    Button btn;

    TextView text;

    DBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        myDB = new DBHelper(this);

        text = (TextView) findViewById(R.id.textViewResult);
        Intent intent = getIntent();
        int correctCount = intent.getIntExtra("correctCount", 0);
        int incorrectCount = 4 - correctCount;
        btn = (Button) findViewById(R.id.homebtn);
        // Get the correctCount from the intent
        // Calculate points based on correctCount
        int points = correctCount * 3 - ((4 - correctCount) * 1); // Assuming 4 questions in the quiz

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String startTime = dateFormat.format(new Date());

        // Retrieve the logged-in username (you need to implement the method to get the username)
        System.out.println(uname);

        GifImageView imageView = findViewById(R.id.yay);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        imageView.startAnimation(animation);

        // Find the TextView in the layout
        String area = intent.getStringExtra("area");

        if (uname != null) {
            // Add the attempt to the database
            long result = myDB.insertAttempt(uname, area, startTime, points);

            if (result != -1) {
                // Update the user's total points in the login table
                System.out.println(points);
                myDB.updateTotalPoints(uname, points);
                System.out.println("hello");

                // Retrieve the user's overall points
                int overallPoints = myDB.getOverallPoints(uname);

                // Display a congratulatory message with the details of the current attempt and the overall points
                String message = "Well done, " + uname + ", you have finished the quiz with "
                        + correctCount + " correct and " + incorrectCount + " incorrect answers, earning "
                        + points + " points for this attempt.\n\n" +
                        "Overall, you have " + overallPoints + " points.";

                // Set the congratulatory message in the TextView
                text.setText(message);
            }else {
                Toast.makeText(this, "Failed to add attempt.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to retrieve username.", Toast.LENGTH_SHORT).show();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int overallPoints = myDB.getOverallPoints(uname);

        switch (item.getItemId()){
            case R.id.animalQz:
                Toast.makeText(this, "New Quiz!!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AnimalQuiz.class);
                startActivity(intent);
                return true;
            case R.id.cartoonQz:
                Toast.makeText(this, "Cartoon Quiz!!!", Toast.LENGTH_SHORT).show();
                Intent intentC = new Intent(getApplicationContext(), CartoonsQuiz.class);
                startActivity(intentC);
                return true;
            case R.id.leave:
                Toast.makeText(this, "Leave result page!", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                finish();
                return true;
            case R.id.logout:
                Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();
                String message = uname + ", you have overall " + overallPoints + " points.";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(getApplicationContext(), login.class);
                startActivity(intent2);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.out_r);
    }
}
