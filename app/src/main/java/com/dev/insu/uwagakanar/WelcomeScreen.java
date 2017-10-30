package com.dev.insu.uwagakanar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WelcomeScreen extends AppCompatActivity {


    @BindView(R.id.signInText)
    AppCompatButton signInText;
    @BindView(R.id.signUpText)
    AppCompatButton signUpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        ButterKnife.bind(this);
        SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
        if (sp1.getBoolean("Is", false)) {
            Intent signIn = new Intent(WelcomeScreen.this, SignIn.class);
            startActivity(signIn);
        }

    }

    @OnClick({R.id.signInText, R.id.signUpText})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.signInText: {
                Intent signIn = new Intent(WelcomeScreen.this, SignIn.class);
                startActivity(signIn);
                break;
            }
            case R.id.signUpText: {
                Intent signUp = new Intent(WelcomeScreen.this, SignUp.class);
                startActivity(signUp);
                break;
            }
        }
    }
}
