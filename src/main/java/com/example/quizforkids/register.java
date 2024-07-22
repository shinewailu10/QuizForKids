package com.example.quizforkids;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class register extends AppCompatActivity {

    EditText  username, password, repassword;
    EditText email;
    Button signup;

    TextView signin;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById(R.id.password1);
        repassword = (EditText) findViewById(R.id.inputConformPassword);
        signin = (TextView) findViewById(R.id.alreadyHaveAccount);
        signup = (Button) findViewById(R.id.btnRegister);
        DB = new DBHelper(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString();
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                if(user.equals("")||pass.equals("")||mail.equals(""))
                    Toast.makeText(register.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(repass)) {
                        Boolean checkuser = DB.checkusername(user);
                        Boolean checkmail = DB.checkemail(mail);
                        if (checkuser == false || checkmail ==false) {
                            Boolean insert = DB.insertData(mail, user, pass);
                            if (insert == true) {
                                Toast.makeText(register.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(register.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                        }
                    }
                else{
                    Toast.makeText(register.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                }
            } }
        });



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.out_r);
    }
}