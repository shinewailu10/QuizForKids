package com.example.quizforkids;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AnimalQuiz extends AppCompatActivity {

    private List<Pair<String, String>> quizQuestions;
    private int questionIndex = 0;
    private EditText editTextUserAnswer;
    private ImageView imageViewQuestion;
    private Button buttonPrevious;
    private Button buttonNext;
    private Button buttonSubmit;

    private String[] userAnswers;

    AnimalDBHelper animalsDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_quiz);

        animalsDB = new AnimalDBHelper(this, AnimalDBHelper.DATABASE_NAME, null, 1);

        // Initialize UI elements
        editTextUserAnswer = findViewById(R.id.editTextUserAnswer);
        imageViewQuestion = findViewById(R.id.imageViewQuestion);
        buttonPrevious = findViewById(R.id.buttonPrev);
        buttonNext = findViewById(R.id.buttonNext);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Initialize userAnswers array
        userAnswers = new String[4];

         //Load quiz questions from the database
        loadQuizQuestions();
    }

    private void loadQuizQuestions() {
        // Generate 4 unique random numbers between 1 and 10
        List<Integer> randomIndices = generateRandomIndices(1, 10, 4);

        // Fetch records corresponding to the generated indices from the database
        quizQuestions = new ArrayList<>();
        for (int index : randomIndices) {
            Cursor cursor = animalsDB.viewRecord(index);
            if (cursor != null && cursor.moveToFirst()) {
                String imagePath = cursor.getString(cursor.getColumnIndex(AnimalDBHelper.COL_2));
                String answer = cursor.getString(cursor.getColumnIndex(AnimalDBHelper.COL_3));
                quizQuestions.add(new Pair<>(imagePath, answer));
                cursor.close();
            }
        }

        // Display the first question
        loadQuestion(questionIndex);
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

    private void loadQuestion(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < quizQuestions.size()) {
            Pair<String, String> question = quizQuestions.get(questionIndex);

            // Display the question image
            String imageName = question.first;
            int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if (resourceId != 0) {
                imageViewQuestion.setImageResource(resourceId);
            } else {
                // Handle the case where the resource ID is not found
                Toast.makeText(getApplicationContext(), "Image resource not found: " + imageName, Toast.LENGTH_SHORT).show();
            }

            // Set OnClickListener for navigation buttons
            buttonPrevious.setEnabled(questionIndex > 0);
            buttonPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Store the user's answer before moving to the previous question
                    userAnswers[AnimalQuiz.this.questionIndex] = editTextUserAnswer.getText().toString().trim();
                    AnimalQuiz.this.questionIndex--; // Move to the previous question
                    loadQuestion(questionIndex - 1);
                }
            });

            buttonNext.setEnabled(questionIndex < quizQuestions.size() - 1);
            buttonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Store the user's answer
                    userAnswers[AnimalQuiz.this.questionIndex] = editTextUserAnswer.getText().toString().trim();

                    String toastMessage = "Your answer: " + userAnswers[AnimalQuiz.this.questionIndex];
                    Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                    editTextUserAnswer.setText("");

                    AnimalQuiz.this.questionIndex++; // Move to the next question
                    loadQuestion(questionIndex + 1);
                }
            });

            // Enable submit button only on the last question
            buttonSubmit.setEnabled(questionIndex == quizQuestions.size() - 1);
            buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Store the user's answer for the last question
                    userAnswers[AnimalQuiz.this.questionIndex] = editTextUserAnswer.getText().toString().trim();

                    // Validate the answers
                    validateAnswers();
                }
            });
            // Set the text of the EditText to the user's answer for the current question
            editTextUserAnswer.setText(userAnswers[questionIndex]);
        }
    }

    private void validateAnswers() {
        int correctCount = 0;
        // Iterate through the array of user answers and validate each answer
        for (int i = 0; i < userAnswers.length; i++) {
            String userAnswer = userAnswers[i];
            String correctAnswer = quizQuestions.get(i).second;
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
        System.out.println("Correct Answers: " + quizQuestions);

        // Pass data to ResultActivity
        Intent intent = new Intent(AnimalQuiz.this, ResultActivity.class);
        intent.putExtra("correctCount", correctCount);
        intent.putExtra("area", "Animal Quiz");

        startActivity(intent);
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.out_r);
    }
}