package com.example.patrick.ediety;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DietActivity extends AppCompatActivity {

    TextView mealsTv;
    String user_id;
    DatabaseReference current_user_db;
    Button showMySqlBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        user_id = getIntent().getExtras().getString("user_id");
        current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mealsTv = (TextView) findViewById(R.id.tvMeals);
        showMySqlBtn = (Button) findViewById(R.id.btnShowMySql);

        showMySqlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        current_user_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {
                    Posilek posilek = child.getValue(Posilek.class);

                    mealsTv.append(child.getKey()+": \n");

                    mealsTv.append(posilek.getBialko()+" z "+posilek.getWegle()+" i "+ posilek.getTluszcze()+". \n");
                    mealsTv.append("Białko: "+posilek.getBialko()+"\n");
                    mealsTv.append("Weglowodany: "+posilek.getWegle()+"\n");
                    mealsTv.append("Tłuszcze: "+posilek.getTluszcze()+"\n");
                    mealsTv.append("Wartosc kaloryczna: "+posilek.getWartosc()+"\n");
                    mealsTv.append("------------------------ \n");


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
