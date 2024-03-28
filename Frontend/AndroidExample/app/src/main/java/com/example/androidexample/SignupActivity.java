package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private EditText firstname;
    private EditText lastname;
    private Button loginButton;
    private Button signupButton;
    private static final String URL = "http://coms-309-056.class.las.iastate.edu:8080/signup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameEditText = findViewById(R.id.lastname_signup);
        passwordEditText = findViewById(R.id.signup_password_edt);
        confirmEditText = findViewById(R.id.signup_confirm_edt);
        loginButton = findViewById(R.id.signup_login_btn);
        signupButton = findViewById(R.id.signup_signup_btn);
        firstname = findViewById(R.id.lastname_signup4);
        lastname = findViewById(R.id.lastname_signup2);

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

                sendPostRequest(jsonBody);
                // Start the IncomeActivity after sending the signup request
                Intent intent = new Intent(SignupActivity.this, IncomeActivity.class);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This Method takes a jsonObject and sends a POST request to BACKEND
     * @param jsonBody
     */
    private void sendPostRequest(JSONObject jsonBody) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> Toast.makeText(SignupActivity.this, "Response: " + response, Toast.LENGTH_LONG).show(),
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
}