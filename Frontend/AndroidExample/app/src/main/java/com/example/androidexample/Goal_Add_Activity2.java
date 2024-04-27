package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class Goal_Add_Activity2 extends AppCompatActivity {

    private Button redirect;
    private TextView prompt_d;
    private String category;
    private String prompt_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_add_2);

        category = Goal_Add_Activity1.selectedGoalCategory;
        prompt_d = (TextView) findViewById(R.id.prompt_display);

        switch (category) {
            case "savings":
                prompt_d.setText("I want to save [amount] in the next [timeframe] months.");
                break;
            case "investment":
                prompt_d.setText("I aim to invest [amount] over the course of [timeframe] months.");
                break;
            case "vacation":
                prompt_d.setText("I plan to save [amount] every [timeframe] months for a vacation.");
                break;
            case "education":
                prompt_d.setText("I will save [amount] every [timeframe] months for education costs.");
                break;
            case "charity":
                prompt_d.setText("I plan to donate [amount] every [timeframe] months to a chosen charity.");
                break;
            case "lifestyle":
                prompt_d.setText("For lifestyle upgrades, I will allocate [amount] every [timeframe] months.");
                break;
        }



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
