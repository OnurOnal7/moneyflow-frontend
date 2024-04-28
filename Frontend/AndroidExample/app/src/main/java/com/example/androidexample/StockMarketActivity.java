package com.example.androidexample;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class StockMarketActivity extends AppCompatActivity {

    private static final String API_KEY = "YOUR_IEX_CLOUD_API_KEY";
    private TextView stockDataTextView;
    private RequestQueue queue; // Volley request queue

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_stock_market);

        //stockDataTextView = findViewById(R.id.stockDataTextView);
        queue = Volley.newRequestQueue(this); // Initialize the request queue

       // Button fetchStockButton = findViewById(R.id.fetchStockButton);
       // fetchStockButton.setOnClickListener(v -> fetchMultipleStockData());
    }

    private void fetchMultipleStockData() {
        String baseUrl = "https://cloud.iexapis.com/stable/stock/market/batch?";
        String symbols = "symbols=AAPL,AMZN"; // Comma-separated list of stock symbols
        String types = "&types=quote";
        String token = "&token=" + API_KEY; // Append your actual API key
        String url = baseUrl + symbols + types + token;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, response -> {
            try {
                JSONObject aaplObject = response.getJSONObject("AAPL").getJSONObject("quote");
                JSONObject amznObject = response.getJSONObject("AMZN").getJSONObject("quote");
                String stockData = "AAPL Price: " + aaplObject.getString("latestPrice") +
                        "\nAMZN Price: " + amznObject.getString("latestPrice");
                stockDataTextView.setText(stockData);
            } catch (JSONException e) {
                e.printStackTrace();
                stockDataTextView.setText("Error parsing JSON data.");
            }
        }, error -> stockDataTextView.setText("Failed to fetch data: " + error.getMessage()));

        queue.add(jsonObjectRequest);
    }
}
