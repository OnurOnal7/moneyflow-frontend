package com.example.androidexample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
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
            for (StockData stock : stockDataBuffer.values()) {
                updateStockData(stock, false);
            }
            stockDataBuffer.clear();
            stockAdapter.notifyDataSetChanged();
            handler.postDelayed(this, 5000);
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
        handler.postDelayed(updateStocksRunnable, 5000);
    }

    private void startWebSocket() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("wss://ws.finnhub.io?token=co7n979r01qgik2hbdkgco7n979r01qgik2hbdl0").build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                // Subscribe to symbols
                webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"AAPL\"}");   // Apple Inc.
                webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"AMZN\"}");  // Amazon.com Inc.
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