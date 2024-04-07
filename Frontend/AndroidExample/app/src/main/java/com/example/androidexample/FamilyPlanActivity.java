package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FamilyPlanActivity extends AppCompatActivity {

    private String fam_URL =  "http://coms-309-056.class.las.iastate.edu:8080/family/addMember/"+LoginActivity.UUID.replace("\"", "");
    private EditText fn,ln,m,a;
    private Button confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_plan);

        fn = findViewById(R.id.fam_first);
        ln = findViewById(R.id.fam_last);
        m = findViewById(R.id.fam_mon);
        a = findViewById(R.id.fam_ann);

        confirm = findViewById(R.id.btn_confirm);



        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("firstName", fn.getText().toString());
                    jsonBody.put("lastName", ln.getText().toString());
                    jsonBody.put("monthlyIncome",m.getText().toString());
                    jsonBody.put("annualIncome", a.getText().toString());

                    sendPostRequest(jsonBody);
                    // Redirect to Main
                    Intent intent = new Intent(FamilyPlanActivity.this, MainActivity.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });





    }
    private void sendPostRequest(JSONObject jsonBody) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, fam_URL,
                response -> Toast.makeText(FamilyPlanActivity.this, "Response: " + response, Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(FamilyPlanActivity.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show()) {
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






