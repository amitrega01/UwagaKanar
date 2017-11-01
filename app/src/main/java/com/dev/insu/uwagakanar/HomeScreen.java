package com.dev.insu.uwagakanar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.insu.uwagakanar.Models.Warning;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeScreen extends AppCompatActivity {

    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.miasto)
    TextView miasto;
    @BindView(R.id.ostrzezenia)
    LinearLayout warnings;
    @BindView(R.id.btnMiasto)
    TextView btnMiasto;
    @BindView(R.id.btnGodz)
    TextClock btnGodz;
    @BindView(R.id.btnLinia)
    AppCompatEditText btnLinia;
    @BindView(R.id.confirm)
    AppCompatButton confirm;
    @BindView(R.id.btnLogout)
    AppCompatImageButton btnLogout;


    FirebaseDatabase database;

    FirebaseUser user;
    String miastoS;
    String a;
    int btn;

    @BindView(R.id.war1)
    AppCompatButton war1;
    @BindView(R.id.war2)
    AppCompatButton war2;
    @BindView(R.id.war3)
    AppCompatButton war3;
    @BindView(R.id.war4)
    AppCompatButton war4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ButterKnife.bind(this);
        final ProgressDialog mDialog = new ProgressDialog(HomeScreen.this);
        mDialog.setMessage("Wczytywanie dabych...");
        mDialog.show();
        btn = 0;
        user = SignIn.user;
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child(user.getUid()).child("name").getValue().toString();

                miastoS = dataSnapshot.child(user.getUid()).child("miasto").getValue().toString();
                username.setText(name);
                miasto.setText(miastoS);
                btnMiasto.setText(miastoS);
                getWarnings();
                mDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (btnLinia.getText().toString().isEmpty()) {
                    Toast.makeText(HomeScreen.this, "Musisz podać numer linii", Toast.LENGTH_SHORT).show();


                } else {

                    final Warning war = new Warning(btnLinia.getText().toString(), btnGodz.getText().toString(), user.getUid().toString());


                    final DatabaseReference myRef = database.getReference("warnings");
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            a = dataSnapshot.child(miastoS).child("count").getValue().toString();
                            int id = Integer.parseInt(a);
                            id++;
                            if (id > 20) id = 0;
                            a = Integer.toString(id);
                            myRef.child(miastoS).child("count").setValue(a);
                            myRef.child(miastoS).child(a).setValue(war);
                            Toast.makeText(HomeScreen.this, "Wysłano ostrzeżenie. Dziękujemy <3", Toast.LENGTH_SHORT).show();
                            getWarnings();


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }
        });


    }

    @OnClick(R.id.btnLogout)
    public void onViewClicked() {
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putBoolean("Is", false);
        Ed.apply();
        Intent i = new Intent(HomeScreen.this, WelcomeScreen.class);
        startActivity(i);
        return;
    }

    private void getWarnings() {

        final DatabaseReference myRef = database.getReference("warnings");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int a = Integer.parseInt(dataSnapshot.child(miastoS).child("count").getValue().toString());
                int c = 0;
                for (int i = a; i > a - 4; i--) {
                    try {
                        if (i <= -1) c = 21 + i;
                        else c = i;
                        String nr = Integer.toString(c);
                        Warning war = dataSnapshot.child(miastoS).child(nr).getValue(Warning.class);
                        showWarning(war);
                    } catch (NullPointerException d) {
                        Toast.makeText(HomeScreen.this, "Brak", Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return;
    }

    private void showWarning(Warning warning) {

        war4.setText(war3.getText());
        war3.setText(war2.getText());
        war2.setText(war1.getText());
        war1.setText(warning.getNrLini() + "\n\r" + warning.getGodz());
        return;
    }

    @OnClick({R.id.war1, R.id.war2, R.id.war3, R.id.war4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.war1:
                break;
            case R.id.war2:
                break;
            case R.id.war3:
                break;
            case R.id.war4:
                break;
        }
    }
}

