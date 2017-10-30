package com.dev.insu.uwagakanar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignIn extends AppCompatActivity {
    public static FirebaseUser user;

    @BindView(R.id.loginT)
    EditText loginT;
    @BindView(R.id.hasloT)
    EditText hasloT;
    @BindView(R.id.okB)
    AppCompatButton okB;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        SharedPreferences sp1 =this.getSharedPreferences("Login", MODE_PRIVATE);
        loginT.setText(sp1.getString("Unm", null));

        hasloT.setText(sp1.getString("Psw", null));
        if (!loginT.getText().toString().isEmpty() && !hasloT.getText().toString().isEmpty()) {
            signIn(loginT.getText().toString(),hasloT.getText().toString());
        }

    }

    @OnClick(R.id.okB)
    public void onViewClicked() {
       // Toast.makeText(this, hasloT.getText() + " " + loginT.getText(), Toast.LENGTH_SHORT).show();
        signIn(loginT.getText().toString(),hasloT.getText().toString());
    }
    private void signIn(String email, String password) {
        final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
        mDialog.setMessage("Logowanie...");
        mDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    public static final String TAG = "TAG";
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            user = mAuth.getCurrentUser();
//po zalogowaniu
                           // Toast.makeText(SignIn.this, "Zalogowano.", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();

                            SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
                            SharedPreferences.Editor Ed=sp.edit();
                            Ed.putString("Unm",loginT.getText().toString() );
                            Ed.putString("Psw",hasloT.getText().toString());
                            Ed.putBoolean("Is",true);
                            Ed.commit();

                            Intent i = new Intent(SignIn.this, HomeScreen.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }

                    }
                });
    }
}
