package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private EditText firstname;
    private EditText lastname;
    private Button loginButton;
    private Button signupButton;
    private EditText monthly;
    private EditText annual;

    private static final String URL = "http://coms-309-056.class.las.iastate.edu:8080/signup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameEditText = findViewById(R.id.lastname_signup);
        passwordEditText = findViewById(R.id.signup_password_edt);
        confirmEditText = findViewById(R.id.confirm);
        loginButton = findViewById(R.id.signup_login_btn);
        signupButton = findViewById(R.id.signup_signup_btn);
        firstname = findViewById(R.id.lastname_signup4);
        lastname = findViewById(R.id.lastname_signup2);
        monthly = findViewById(R.id.mon_exp);
        annual = findViewById(R.id.ann_exp);

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(v -> {
            try {
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("firstName", firstname.getText().toString());
                jsonBody.put("lastName", lastname.getText().toString());
                jsonBody.put("email", usernameEditText.getText().toString());
                jsonBody.put("password", passwordEditText.getText().toString());
                jsonBody.put("monthlyIncome", monthly.getText().toString());
                jsonBody.put("annualIncome", annual.getText().toString());

                sendPostRequest(jsonBody);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendPostRequest(JSONObject jsonBody) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    Toast.makeText(SignupActivity.this, "Response: " + response, Toast.LENGTH_LONG).show();
                    // Extract the UUID from the response and send initial expenses
                    UUID userId = UUID.fromString(response.replace("\"", ""));
                    sendInitialExpenses(userId, jsonBody);
                },
                error -> Toast.makeText(SignupActivity.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            public byte[] getBody() {
                return jsonBody.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void sendInitialExpenses(UUID userId, JSONObject userCredentials) {
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
                response -> {
                    Log.d("InitialExpenses", "Expenses added successfully");
                    // Automatically log in the user after successful signup and initial expenses setup
                    try {
                        loginUser(userCredentials.getString("email"), userCredentials.getString("password"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
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

    private void loginUser(String email, String password) {
        String loginUrl = "http://coms-309-056.class.las.iastate.edu:8080/login" + "?email=" + email + "&password=" + password;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginUrl,
                response -> {
                    Toast.makeText(SignupActivity.this, "Login Response: " + response, Toast.LENGTH_LONG).show();
                    LoginActivity.UUID = response;
                    GetUserTypeRequest(response);
                },
                error -> Toast.makeText(SignupActivity.this, "Login Error: " + error.toString(), Toast.LENGTH_LONG).show());

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void GetUserTypeRequest(String userId) {
        final String type_URL = "http://coms-309-056.class.las.iastate.edu:8080/userType/" + userId.replace("\"", "");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                type_URL,
                response -> {
                    Log.d("usertype", response);
                    MainActivity.userType = response;
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                },
                error -> Toast.makeText(SignupActivity.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show()
        );
        queue.add(stringRequest);
    }

}
