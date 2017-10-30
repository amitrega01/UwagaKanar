package com.dev.insu.uwagakanar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.Console;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUp extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private String miasto;
    @BindView(R.id.loginT)
    EditText loginT;
    @BindView(R.id.hasloT)
    EditText hasloT;
    @BindView(R.id.okB)
    AppCompatButton okB;
    @BindView(R.id.imieT)
    EditText imieT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        Spinner dropdown = (Spinner) findViewById(R.id.spinner);
        String[] items = getResources().getStringArray(R.array.miasta);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        miasto = adapter.getItem(0).toString();
        //spinner
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                miasto = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                miasto = adapterView.getItemAtPosition(0).toString();
            }
        });


    }

    @OnClick(R.id.okB)
    public void onViewClicked() {
        createAccount(loginT.getText().toString(), hasloT.getText().toString());
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    private void createAccount(final String email, String password) {
        final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
        mDialog.setMessage("Rejestruje...");
        mDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    public static final String TAG = "TAG";

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(SignUp.this, "Konto utworzono",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users");

                            myRef.child(user.getUid()).child("email").setValue(user.getEmail());
                            myRef.child(user.getUid()).child("name").setValue(imieT.getText().toString());
                            myRef.child(user.getUid()).child("miasto").setValue(miasto);
                            mDialog.dismiss();
                            SharedPreferences sp = getSharedPreferences("FirstRun", MODE_PRIVATE);
                            SharedPreferences.Editor Ed = sp.edit();

                            Ed.putBoolean("Is", true);
                            Ed.commit();

                            Intent i = new Intent(SignUp.this, SignIn.class);
                            startActivity(i);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Konto już istnieje",
                                    Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }
                    }
                });
    }
}
