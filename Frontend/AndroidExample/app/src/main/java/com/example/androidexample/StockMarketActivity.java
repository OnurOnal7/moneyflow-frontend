package com.example.androidexample;

import static com.example.androidexample.StockAdapter.symbolMap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class StockMarketActivity extends AppCompatActivity {

    private WebSocket webSocket;
    private RecyclerView stocksRecyclerView;
    private StockAdapter stockAdapter;
    private List<StockData> stocksList = new ArrayList<>();
    private Map<String, StockData> stockDataBuffer = new HashMap<>();

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable updateStocksRunnable = new Runnable() {
        @Override
        public void run() {
            // Process each stock data for UI update
            for (StockData stock : stockDataBuffer.values()) {
                updateStockData(stock, false);
            }
            sendStockUpdatesToBackend(); // Send the updates to backend
            stockDataBuffer.clear(); // Clear the buffer after processing
            stockAdapter.notifyDataSetChanged(); // Notify the adapter to refresh the view
            handler.postDelayed(this, 5000); // Schedule next update after 5 seconds
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_market);

        stocksRecyclerView = findViewById(R.id.stocksRecyclerView);
        stocksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stockAdapter = new StockAdapter(stocksList, this);
        stocksRecyclerView.setAdapter(stockAdapter);

        startWebSocket();
        handler.postDelayed(updateStocksRunnable, 5000); // Start periodic updates
    }

    private void startWebSocket() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("wss://ws.finnhub.io?token=co7n979r01qgik2hbdkgco7n979r01qgik2hbdl0").build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                // Subscribe to stock symbols
                webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"AAPL\"}");
                webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"AMZN\"}");
                webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"BINANCE:BTCUSDT\"}");
                webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"BINANCE:DOGEUSDT\"}");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    if (dataArray != null) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObject = dataArray.getJSONObject(i);
                            String symbol = dataObject.getString("s");
                            double price = dataObject.getDouble("p");
                            StockData newStockData = new StockData(symbol, price);
                            updateStockData(newStockData, true);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                // Handle failure
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                // Handle the closing of the WebSocket
            }
        });
    }

    private void updateStockData(StockData stockData, boolean buffer) {
        if (buffer) {
            stockDataBuffer.put(stockData.getSymbol(), stockData);
        } else {
            boolean exists = false;
            for (int i = 0; i < stocksList.size(); i++) {
                StockData existingStock = stocksList.get(i);
                if (existingStock.getSymbol().equals(stockData.getSymbol())) {
                    existingStock.setPrice(stockData.getPrice());
                    stockAdapter.notifyItemChanged(i);
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                stocksList.add(stockData);
                stockAdapter.notifyItemInserted(stocksList.size() - 1);
            }
        }
    }

    private void sendStockUpdatesToBackend() {
        OkHttpClient client = new OkHttpClient();
        String url = "http://coms-309-056.class.las.iastate.edu:8080/portfolio/" + LoginActivity.UUID.replace("\"", "");

        JSONObject stockPrices = new JSONObject();
        try {
            Log.d("StockMarketActivity", "Preparing to send updated stock prices:");
            for (Map.Entry<String, StockData> entry : stockDataBuffer.entrySet()) {
                String backendSymbol = getBackendSymbol(entry.getKey());
                double price = entry.getValue().getPrice();
                stockPrices.put(backendSymbol + "Price", price);
                Log.d("StockMarketActivity", "Adding to JSON: " + backendSymbol + "Price: " + price);
            }
            Log.d("StockMarketActivity", "JSON to send: " + stockPrices.toString());
        } catch (JSONException e) {
            Log.e("StockMarketActivity", "Failed to build JSON for stock updates", e);
            return;
        }

        RequestBody body = RequestBody.create(stockPrices.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(url).put(body).build();
        Log.d("StockMarketActivity", "Sending request to URL: " + request.url());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("StockMarketActivity", "Network call failed", e);
                performPostFailureAction("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("StockMarketActivity", "Network call unsuccessful with response code: " + response.code() + " and body: " + response.body().string());
                    performPostFailureAction("Server error: " + response.code() + " - " + response.body().string());
                } else {
                    Log.d("StockMarketActivity", "Successfully updated stock prices: " + response.body().string());
                    performPostSuccessAction("Update successful");
                }
            }
        });
    }

    private String getBackendSymbol(String stockSymbol) {
        // Assume a similar mapping is needed as in StockAdapter.
        if (symbolMap.containsKey(stockSymbol.toLowerCase())) {
            return symbolMap.get(stockSymbol.toLowerCase());
        }
        return stockSymbol;
    }

    private void performPostSuccessAction(String message) {
        runOnUiThread(() -> Toast.makeText(StockMarketActivity.this, message, Toast.LENGTH_LONG).show());
    }

    private void performPostFailureAction(String message) {
        runOnUiThread(() -> Toast.makeText(StockMarketActivity.this, message, Toast.LENGTH_LONG).show());
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(updateStocksRunnable);
        if (webSocket != null) {
            // Unsubscribe from all symbols before closing the WebSocket
            webSocket.send("{\"type\":\"unsubscribe\",\"symbol\":\"AAPL\"}");
            webSocket.send("{\"type\":\"unsubscribe\",\"symbol\":\"AMZN\"}");
            webSocket.send("{\"type\":\"unsubscribe\",\"symbol\":\"BINANCE:BTCUSDT\"}");
            webSocket.send("{\"type\":\"unsubscribe\",\"symbol\":\"BINANCE:DOGEUSDT\"}");
            webSocket.close(5001, "Activity destroyed");
        }
    }
}
