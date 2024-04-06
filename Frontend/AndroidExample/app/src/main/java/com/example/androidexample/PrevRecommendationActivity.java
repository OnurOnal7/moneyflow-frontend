package com.example.androidexample;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import org.json.JSONException;


import org.json.JSONException;
import org.json.JSONObject;

public class PrevRecommendationActivity extends AppCompatActivity {

    private String DATE_URL = "http://coms-309-056.class.las.iastate.edu:8080/recommendations/userId/"+LoginActivity.UUID.replace("\"", "")+"/dates";
    private String[] DateArr = new String[100];

    private Spinner datelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prev_recommendation);
        datelist = findViewById(R.id.date_list);
        GetRequest();

    }

    private void populateSpinner () {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DateArr);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        datelist.setAdapter(adapter);

        // Set item selection listener to handle selection events
        datelist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle the selected date here
                String selectedDate = DateArr[position];
                Toast.makeText(PrevRecommendationActivity.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected
            }
        });
    }

    private void GetRequest() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, DATE_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length() && i < DateArr.length; i++) {
                        String date = response.getString(i);
                        DateArr[i] = date;
                    }


                    populateSpinner();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PrevRecommendationActivity.this, "Get Error: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}


