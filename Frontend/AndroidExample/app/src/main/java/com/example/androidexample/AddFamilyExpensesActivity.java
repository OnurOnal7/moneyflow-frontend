package com.example.androidexample;

import static com.example.androidexample.R.array;
import static com.example.androidexample.R.id;
import static com.example.androidexample.R.layout;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
public class AddFamilyExpensesActivity extends AppCompatActivity {

    private final String URL = "http://coms-309-056.class.las.iastate.edu:8080/expenses/" + LoginActivity.UUID.replace("\"", "");


    private Button conf;
    private EditText p, w, h, o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_add_family_expenses);

        conf = findViewById(id.btn_conf_fam);
        p = findViewById(id.fam_personal);
        w = findViewById(id.fam_work);
        h = findViewById(id.fam_home);
        o = findViewById(id.fam_other);


        conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("personal", Double.parseDouble(p.getText().toString()));
                    jsonBody.put("work", Double.parseDouble(w.getText().toString()));
                    jsonBody.put("home", Double.parseDouble(h.getText().toString()));
                    jsonBody.put("other", Double.parseDouble(o.getText().toString()));
                    sendPostRequest(jsonBody);
                    // Redirect to Main
                    Intent intent = new Intent(AddFamilyExpensesActivity.this, MainActivity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }


    private void sendPostRequest(JSONObject jsonBody) {
        if(LoginActivity.UUID != null) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    response -> Toast.makeText(AddFamilyExpensesActivity.this, "Response: " + response, Toast.LENGTH_LONG).show(),
                    error -> Toast.makeText(AddFamilyExpensesActivity.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show()) {
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

}




