package com.dev.insu.uwagakanar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.LinearLayout;
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


    FirebaseUser user;
    String miastoS;
    String a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ButterKnife.bind(this);
        user = SignIn.user;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child(user.getUid()).child("name").getValue().toString();

                miastoS = dataSnapshot.child(user.getUid()).child("miasto").getValue().toString();
                username.setText(name);
                miasto.setText(miastoS);
                btnMiasto.setText(miastoS);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(HomeScreen.this);
                mDialog.setMessage("Wysyłanie...");
                mDialog.show();
                if (btnLinia.getText().toString().isEmpty())
                    Toast.makeText(HomeScreen.this, "Musisz podać numer linii", Toast.LENGTH_SHORT).show();
                else {

                    final Warning war = new Warning(btnLinia.getText().toString(), btnGodz.getText().toString(), user.getUid().toString());

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference myRef = database.getReference("warnings");
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            a = dataSnapshot.child(miastoS).child("count").getValue().toString();
                            int id = Integer.parseInt(a);
                            id++;
                            if (id>20) id = 0;
                            a = Integer.toString(id);
                            myRef.child(miastoS).child("count").setValue(a);
                            myRef.child(miastoS).child(a).setValue(war);
                            mDialog.dismiss();

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
        Intent i = new Intent(HomeScreen.this, WelcomeScreen.class);
        startActivity(i);
    }
}
