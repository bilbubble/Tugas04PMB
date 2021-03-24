package com.example.tugas04pmb;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailid,pass;
    CheckBox remember;
    Button signin;
    TextView tvdaftar;
    TextView forpas;
    FirebaseAuth mFirebaseAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefsEditor;
    int savedLoginState;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailid = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        remember = findViewById(R.id.rememberMe);
        signin = findViewById(R.id.button);
        tvdaftar = findViewById(R.id.daftar);
        forpas = findViewById(R.id.forgotpass);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();

        emailid.setText(sharedPreferences.getString("email",""));
        pass.setText(sharedPreferences.getString("password",""));

        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mFirebaseUser != null){
                } else{
                }
            }
        };

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailid.getText().toString();
                String password = pass.getText().toString();
                if(email.isEmpty()){
                    emailid.setText("Tolong masukkan email");
                    emailid.requestFocus();
                }
                else if(password.isEmpty()){
                    pass.setText("Tolong masukkan password");
                    pass.requestFocus();
                }
                else if(email.isEmpty() && password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Isi yang kosong", Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && password.isEmpty())){
                    boolean checkedRemember = remember.isChecked();
                    if(checkedRemember){
                        prefsEditor.putString("email", email);
                        prefsEditor.putString("password", password);
                    } else{
                        prefsEditor.clear();
                    }
                    prefsEditor.apply();


                    mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(LoginActivity.this, "Username atau Password tidak sesuai", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(i);
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(LoginActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvdaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dftr = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(dftr);
            }
        });
        forpas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forp = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(forp);
            }
        });

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

//                if(compoundButton.isChecked()){
//                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("remember","true");
//                    editor.apply();
//                    Toast.makeText(LoginActivity.this,"tersimpan", Toast.LENGTH_SHORT).show();
//                } else if(compoundButton.isChecked()){
//                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("remember","false");
//                    editor.apply();
//                    Toast.makeText(LoginActivity.this,"tidak tersimpan", Toast.LENGTH_SHORT).show();
//
//                }
            }
        });
    }

    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}