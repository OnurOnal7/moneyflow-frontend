package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Goal_Add_Activity2 extends AppCompatActivity {

    private Button redirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_add_2);



        redirect = (Button) findViewById(R.id.set_goal);


        redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Goal Created!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Goal_Add_Activity2.this, GoalListActivity.class));
            }
        });



    }
}
