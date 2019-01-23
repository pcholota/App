package com.example.patrick.ediety;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText emailEt;
    EditText passwordEt;
    TextView existAccountTv;
    Button registerBtn;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        existAccountTv = (TextView) findViewById(R.id.tvExistAccount);
        existAccountTv.setOnClickListener(this);

        registerBtn = (Button) findViewById(R.id.btnRegistration);
        registerBtn.setOnClickListener(this);

        emailEt = (EditText) findViewById(R.id.etEmail);
        passwordEt = (EditText) findViewById(R.id.etPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
    }

    private void registerUser(){
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();

        if(email.isEmpty()){
            emailEt.setError("Email jest potrzebny!");
            emailEt.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordEt.setError("Haslo jest potrzebne");
            passwordEt.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEt.setError("Podaj poprawny adres email!");
            emailEt.requestFocus();
            return;
        }
        if(password.length()<6){
            passwordEt.setError("Haslo musi sie skladac przynajmniej z 6 znaków");
            passwordEt.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(), DietActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //potrzebne zeby nie cofalo do loginu po nacisnieciu wstecz

                    //dodanie podstawowych informacji do bazy danych
                    String user_id = mAuth.getCurrentUser().getUid();

                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                    intent.putExtra("user_id",user_id);


                    HashMap newData = new HashMap();
                    /*
                    newData.put("Wartosc Kaloryczna" ,"1000");
                    newData.put("Plec" ,"");
                    newData.put("Wiek" ,"");
                    current_user_db.setValue(newData);

                    */

                    current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Sniadanie");

                    newData.clear();
                    newData.put("bialko", "Brak");
                    newData.put("wegle","Brak");
                    newData.put("tluszcze", "Brak");
                    newData.put("wartosc", " Brak");
                    current_user_db.setValue(newData);

                    current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("2Sniadanie");

                    newData.clear();
                    newData.put("bialko", "Brak");
                    newData.put("wegle","Brak");
                    newData.put("tluszcze", "Brak");
                    newData.put("wartosc", " Brak");
                    current_user_db.setValue(newData);

                    current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Obiad");

                    newData.clear();
                    newData.put("bialko", "Brak");
                    newData.put("wegle","Brak");
                    newData.put("tluszcze", "Brak");
                    newData.put("wartosc", " Brak");
                    current_user_db.setValue(newData);

                    current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Podwieczorek");

                    newData.clear();
                    newData.put("bialko", "Brak");
                    newData.put("wegle","Brak");
                    newData.put("tluszcze", "Brak");
                    newData.put("wartosc", " Brak");
                    current_user_db.setValue(newData);

                    current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Kolacja");

                    newData.clear();
                    newData.put("bialko", "Brak");
                    newData.put("wegle","Brak");
                    newData.put("tluszcze", "Brak");
                    newData.put("wartosc", " Brak");
                    current_user_db.setValue(newData);


                    startActivity(intent);
                }else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "Ten email jest już Zarejestrowany!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Wystapil Blad!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.equals(existAccountTv)){
            startActivity(new Intent(this,MainActivity.class));
        }else if(view.equals(registerBtn)){
            registerUser();
        }
    }
}
