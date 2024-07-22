package com.example.quizforkids;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CartoonsQuiz extends AppCompatActivity {

    private List<Pair<String, String[]>> quizQuestions;
    private int currentQuestionIndex = 0;

    private String[] correctAnswers;
    private String[] userAnswers;
    private RadioGroup radioGroup;
    private ImageView imageViewQuestion;
    private Button buttonPrevious;
    private Button buttonNext;
    private Button buttonSubmit;

    CartoonDBHelper cartoonDBHElper;

    private int correctCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoon_quiz);

         cartoonDBHElper = new CartoonDBHelper(this, CartoonDBHelper.DATABASE_NAME, null, 1);


        // Initialize UI elements
        radioGroup = findViewById(R.id.radioGroup);
        imageViewQuestion = findViewById(R.id.imageViewQuestion);
        buttonPrevious = findViewById(R.id.buttonPrev);
        buttonNext = findViewById(R.id.buttonNext);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Initialize selectedAnswers list
        // Initialize userAnswers array
        userAnswers = new String[4];

        correctAnswers = new String[4];

        // Load quiz questions from the database
        loadQuizQuestions();
    }

    private List<Integer> generateRandomIndices(int min, int max, int count) {
        if (count > max - min + 1) {
            throw new IllegalArgumentException("Count cannot be greater than the range of indices.");
        }

        List<Integer> indices = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);
        return indices.subList(0, count);
    }

    private void loadQuizQuestions() {
        // Fetch all records from the cartoons quiz database
        // Generate 4 unique random numbers between 0 and totalQuestions - 1
        List<Integer> randomIndices = generateRandomIndices(1, 10, 4);

        // Fetch records corresponding to the generated indices from the database
        quizQuestions = new ArrayList<>();
        int count = 0;
        for (int index : randomIndices) {
            Cursor cursor = cartoonDBHElper.viewRecord(index);
            if (cursor != null && cursor.moveToFirst()) {
                String imagePath = cursor.getString(cursor.getColumnIndex(CartoonDBHelper.COL_2));
                String answer = cursor.getString(cursor.getColumnIndex(CartoonDBHelper.COL_3));
                String choice1 = cursor.getString(cursor.getColumnIndex(CartoonDBHelper.COL_4));
                String choice2 = cursor.getString(cursor.getColumnIndex(CartoonDBHelper.COL_5));
                String[] choices = {answer, choice1, choice2};
                quizQuestions.add(new Pair<>(imagePath, choices));
                correctAnswers[count] = answer;
                count++;
                cursor.close();
            }
        }

        // Display the first question
        navigateToQuestion(currentQuestionIndex);
    }

    private void navigateToQuestion(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < quizQuestions.size()) {
            currentQuestionIndex = questionIndex;
            Pair<String, String[]> question = quizQuestions.get(questionIndex);

            // Display the question image
            String imageName = question.first;
            int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if (resourceId != 0) {
                imageViewQuestion.setImageResource(resourceId);
            } else {
                // Handle the case where the resource ID is not found
                Toast.makeText(getApplicationContext(), "Image resource not found: " + imageName, Toast.LENGTH_SHORT).show();
            }

            // Shuffle choices randomly
            String[] choices = question.second;
            List<String> shuffledChoices = Arrays.asList(choices);
            Collections.shuffle(shuffledChoices);

            // Display choices as RadioButtons
            radioGroup.removeAllViews();
            for (String choice : choices) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(choice);
                radioGroup.addView(radioButton);

                // Check if this choice was the previously selected answer
                if (userAnswers[currentQuestionIndex] != null && userAnswers[currentQuestionIndex].equals(choice)) {
                    radioButton.setChecked(true);
                }
            }

            // Set OnClickListener for navigation buttons
            buttonPrevious.setEnabled(questionIndex > 0);
            buttonPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton selectedRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                    if (selectedRadioButton != null) {
                        userAnswers[currentQuestionIndex] = selectedRadioButton.getText().toString();
                    }
                    else {
                        userAnswers[currentQuestionIndex] = "0";
                    }
                    currentQuestionIndex--; // Move to the previous question
                    navigateToQuestion(questionIndex - 1);
                }
            });

            buttonNext.setEnabled(questionIndex < quizQuestions.size() - 1);
            buttonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Store the user's answer before moving to the previous question
                    RadioButton selectedRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                    if (selectedRadioButton != null) {
                        userAnswers[currentQuestionIndex] = selectedRadioButton.getText().toString();
                    }
                    else {
                        userAnswers[currentQuestionIndex] = "0";
                    }

                    String toastMessage = "Your answer: " + userAnswers[currentQuestionIndex];
                    Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();

                    currentQuestionIndex++; // Move to the next question
                    navigateToQuestion(questionIndex + 1);
                }


            });

            // Enable submit button only on the last question
            buttonSubmit.setEnabled(questionIndex == quizQuestions.size() - 1);
            buttonSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Store the user's answer for the last question
                        RadioButton selectedRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                        if (selectedRadioButton != null) {
                            userAnswers[currentQuestionIndex] = selectedRadioButton.getText().toString();
                        } else {
                            // If RadioButton is null, assign a default value
                            userAnswers[currentQuestionIndex] = "0";
                        }

                        // Validate the answers
                        validateAnswers();
                    }
                });

            // Set it to default choice
        }
    }

    private void validateAnswers() {
        int correctCount = 0;
        // Iterate through the array of user answers and validate each answer
        for (int i = 0; i < userAnswers.length; i++) {
            String userAnswer = userAnswers[i];
            System.out.println("hello");
            System.out.println(quizQuestions.get(i).second[0]);
            String correctAnswer = correctAnswers[i];
            // Check if the user's answer matches the correct answer
            if (userAnswer != null && userAnswer.equalsIgnoreCase(correctAnswer)) {
                correctCount++;
            }
            // Provide appropriate feedback to the user based on the correctness of each answer
            System.out.println("Question ID: " + i);
            System.out.println("User Answer: " + userAnswer);
            System.out.println("Correct Answer: " + correctAnswer);
        }
        // Display a toast message with the number of correct answers
        String message = "You got " + correctCount + " out of " + userAnswers.length + " correct!";
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        // Print user answers and correct answers
        System.out.println("User Answers: " + Arrays.toString(userAnswers));
        System.out.println("Correct Answers: " + Arrays.toString(correctAnswers));

        // Pass data to ResultActivity
        Intent intent = new Intent(CartoonsQuiz.this, ResultActivity1.class);
        intent.putExtra("correctCount", correctCount);
        intent.putExtra("area", "Cartoon Quiz");
        startActivity(intent);
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.out_r);
    }
}