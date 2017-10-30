package com.dev.insu.uwagakanar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class WelcomeScreen extends AppCompatActivity {

    AppCompatButton signIn, signUp;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        SharedPreferences sp1 =this.getSharedPreferences("Login", MODE_PRIVATE);
        if (sp1.getBoolean("Is",false)) {
            Intent signIn = new Intent(WelcomeScreen.this, SignIn.class);
            startActivity(signIn);
        }

        signIn = findViewById(R.id.signInText);
        signUp = findViewById(R.id.signUpText);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signIn = new Intent(WelcomeScreen.this, SignIn.class);
                startActivity(signIn);

            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(WelcomeScreen.this, SignUp.class);
                startActivity(signUp);
            }
        });
    }


}
