package com.example.patrick.ediety;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.*;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView registerTv;
    Button logInBtn;
    EditText emailEt;
    EditText passwordEt;
    ProgressBar progressBar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        registerTv = (TextView) findViewById(R.id.tvNoAccount);
        registerTv.setOnClickListener(this);

        logInBtn = (Button) findViewById(R.id.btnLogIn);
        logInBtn.setOnClickListener(this);

        emailEt = (EditText) findViewById(R.id.etEmailLogin);
        passwordEt = (EditText) findViewById(R.id.etPasswordLogin);
        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);

        mAuth=FirebaseAuth.getInstance();
    }

    private void userLogin(){
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

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                   // Intent intent = new Intent(getApplicationContext(), ProfileActivity.class); Ta klasa ma dodawania i wyswietlanie produktow
                    Intent intent = new Intent(getApplicationContext(), DietActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //potrzebne zeby nie cofalo do loginu po nacisnieciu wstecz
                    String user_id = mAuth.getCurrentUser().getUid();

                    intent.putExtra("user_id",user_id);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.equals(registerTv)){
            startActivity(new Intent(this,RegistrationActivity.class));
        }else if(view.equals(logInBtn)){
            userLogin();
        }
    }
}
