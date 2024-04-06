package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class FirstPageActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signupButton;
    private Button guestButton;

    private final String GuestURL = "http://coms-309-056.class.las.iastate.edu:8080/login/type/guest";

    private String ID;

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
                Intent intent = new Intent(FirstPageActivity.this, MainActivity.class);
                sendPostRequest();
                LoginActivity.UUID = ID;
                startActivity(intent);
            }
        });

    }

    private void sendPostRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GuestURL,
                response -> {
                    Toast.makeText(FirstPageActivity.this, "Login Response: " + response, Toast.LENGTH_LONG).show();
                    ID = response;
                    // Navigate to the next activity or perform further actions
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

}