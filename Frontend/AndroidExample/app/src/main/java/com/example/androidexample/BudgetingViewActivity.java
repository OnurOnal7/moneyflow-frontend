package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

public class BudgetingViewActivity extends AppCompatActivity {

    private double P,W, H, O;
    private double PBud,WBud, HBud, OBud;

    private double Personal, Work, Home, Other;
    private TextView Pin, Win, Oin, Hin;


    private Button backhome;

    private ProgressBar pProgress, wProgress, hProgress, oProgress;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgeting_view);

        GetRequest();
        GetExpensesRequest();
        GetBudgetsRequest();
        Hin = findViewById(R.id.HomLim);
        Pin = findViewById(R.id.persLim);
        Win =  findViewById(R.id.WorkLim);
        Oin = findViewById(R.id.OthLim);
        pProgress = findViewById(R.id.personalprog);
        wProgress = findViewById(R.id.workprog);
        hProgress = findViewById(R.id.homeprog);
        oProgress = findViewById(R.id.otherprog);

        backhome = (Button) findViewById(R.id.bkhome);

        pProgress.setMax((int)PBud);
        wProgress.setMax((int)WBud);
        hProgress.setMax((int)HBud);
        oProgress.setMax((int)OBud);

        pProgress.setMin(Integer.MIN_VALUE);
        wProgress.setMin(Integer.MIN_VALUE);
        hProgress.setMin(Integer.MIN_VALUE);
        oProgress.setMin(Integer.MIN_VALUE);

        pProgress.setProgress((int)Personal);
        wProgress.setProgress((int) Work);
        hProgress.setProgress((int) Home);
        oProgress.setProgress((int) Other);


        if ((int)Personal == (int)PBud) {
            pProgress.setProgressDrawable(getResources().getDrawable(R.drawable.custom_progress_bar_completed));
        } else if ((int)Personal >= (int)PBud * 0.9) { // Change color when approaching max value
            pProgress.setProgressDrawable(getResources().getDrawable(R.drawable.custom_progress_bar_approaching_max));
        } else {
            pProgress.setProgressDrawable(getResources().getDrawable(R.drawable.custom_progress_bar));
        }

        if ((int)Work == (int)WBud) {
            wProgress.setProgressDrawable(getResources().getDrawable(R.drawable.custom_progress_bar_completed));
        } else if ((int)Work >= (int)WBud * 0.9) { // Change color when approaching max value
            wProgress.setProgressDrawable(getResources().getDrawable(R.drawable.custom_progress_bar_approaching_max));
        } else {
            wProgress.setProgressDrawable(getResources().getDrawable(R.drawable.custom_progress_bar));
        }

        if ((int)Home == (int)HBud) {
            hProgress.setProgressDrawable(getResources().getDrawable(R.drawable.custom_progress_bar_completed));
        } else if ((int)Home >= (int)HBud * 0.9) { // Change color when approaching max value
            hProgress.setProgressDrawable(getResources().getDrawable(R.drawable.custom_progress_bar_approaching_max));
        } else {
            hProgress.setProgressDrawable(getResources().getDrawable(R.drawable.custom_progress_bar));
        }

        if ((int)Home == (int)HBud) {
            hProgress.setProgressDrawable(getResources().getDrawable(R.drawable.custom_progress_bar_completed));
        } else if ((int)Home >= (int)HBud * 0.9) { // Change color when approaching max value
            hProgress.setProgressDrawable(getResources().getDrawable(R.drawable.custom_progress_bar_approaching_max));
        } else {
            hProgress.setProgressDrawable(getResources().getDrawable(R.drawable.custom_progress_bar));
        }


        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BudgetingViewActivity.this, BudgetingActivity.class));
            }
        });



    }

    private void GetRequest()
    {
        String url = "http://coms-309-056.class.las.iastate.edu:8080/budget/check/" + LoginActivity.UUID.replace("\"", "");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response ->{

            try {
                if (response.has("Personal")) {
                    P = response.getDouble("Personal");
                    Pin.setText(Double.toString(P));
                }
                if (response.has("Work")) {
                    W = response.getDouble("Work");
                    Win.setText(Double.toString(W));
                }
                if (response.has("Home")) {
                    H = response.getDouble("Home");
                    Hin.setText(Double.toString(H));
                }
                if (response.has("Other")) {
                    O = response.getDouble("Other");
                    Oin.setText(Double.toString(O));
                }


            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


        }, error -> {
            Toast.makeText(BudgetingViewActivity.this, "Failed: " + error.toString(), Toast.LENGTH_LONG).show();
            error.printStackTrace(); // Print error details for debugging

        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    private void GetExpensesRequest()
    {
        String url = "http://coms-309-056.class.las.iastate.edu:8080/expenses/" + LoginActivity.UUID.replace("\"", "");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response ->{

            try {
                if (response.has("personal")) {
                    Personal = response.getDouble("personal");
                }
                else
                {
                    Personal = 0.0;
                }
                if (response.has("work")) {
                    Work = response.getDouble("work");
                }
                else
                {
                    Work = 0.0;
                }
                if (response.has("home")) {
                    Home = response.getDouble("home");
                }
                else {
                    Home = 0.0;
                }
                if (response.has("other")) {
                    Other = response.getDouble("other");
                }
                else{
                    Other = 0.0;
                }


            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


        }, error -> {
            Toast.makeText(BudgetingViewActivity.this, "Failed: " + error.toString(), Toast.LENGTH_LONG).show();
            error.printStackTrace(); // Print error details for debugging

        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }
    private void GetBudgetsRequest() {
        String url = "http://coms-309-056.class.las.iastate.edu:8080/budget/" + LoginActivity.UUID.replace("\"", "");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                PBud = response.getDouble("personalLimit");
                WBud = response.getDouble("workLimit");
                HBud = response.getDouble("homeLimit");
                OBud = response.getDouble("otherLimit");

            } catch (JSONException e) {
                e.printStackTrace(); // Instead of throwing a runtime exception, just print the error
            }
        }, error -> {
            Toast.makeText(BudgetingViewActivity.this, "Failed: " + error.toString(), Toast.LENGTH_LONG).show();
            error.printStackTrace(); // Print error details for debugging
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

}
