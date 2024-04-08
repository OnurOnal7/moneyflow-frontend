package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FirstPageActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signupButton;
    private Button guestButton;
    private final String GuestURL = "http://coms-309-056.class.las.iastate.edu:8080/login/type/guest";
    private String userType;
    public static String ID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        loginButton = findViewById(R.id.startup_login_btn);
        signupButton = findViewById(R.id.startup_signup_btn);
        guestButton = findViewById(R.id.startup_guest_btn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstPageActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstPageActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPostRequest();
            }
        });

    }

    private void sendPostRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GuestURL,
                response -> {
                    if (response != null && !response.isEmpty()) {
                        Toast.makeText(FirstPageActivity.this, "Login Response: " + response, Toast.LENGTH_LONG).show();
                        LoginActivity.UUID = response;
                        ID = response;
                        GetUserTypeRequest();
                        // Send initial expenses after creating the guest user
                        sendInitialExpenses(UUID.fromString(response.replace("\"", "")));
                        // Redirect to MainActivity
                        Intent intent = new Intent(FirstPageActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(FirstPageActivity.this, "UUID is null or empty", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(FirstPageActivity.this, "Login Error: " + error.toString(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Since the endpoint does not require any parameters, we return an empty map.
                return new HashMap<>();
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void sendInitialExpenses(UUID userId) {
        String url = "http://coms-309-056.class.las.iastate.edu:8080/expenses/" + userId.toString();
        JSONObject postData = new JSONObject();
        try {
            postData.put("personal", 0.0);
            postData.put("work", 0.0);
            postData.put("home", 0.0);
            postData.put("other", 0.0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> Log.d("InitialExpenses", "Expenses added successfully"),
                error -> Log.e("InitialExpenses", "Error adding expenses: " + error.getMessage())) {
            @Override
            public byte[] getBody() {
                return postData.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        Volley.newRequestQueue(this).add(stringRequest);
    }


    private void GetUserTypeRequest() {
        if(ID != null) {
            final String type_URL = "http://coms-309-056.class.las.iastate.edu:8080/userType/" + ID.replace("\"", "");

            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(
                    Request.Method.GET,
                    type_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("usertype", response);
                            MainActivity.userType = response;

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle the error
                            Toast.makeText(FirstPageActivity.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
            );
            queue.add(stringRequest);
        }
    }


}