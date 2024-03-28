package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class StockMarketActivity extends AppCompatActivity {

    private WebSocket webSocket;
    private TextView stockMarketDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_market);

        stockMarketDataView = findViewById(R.id.stockMarketDataView);
        startWebSocket();
    }

    /**
     * This Method Initializes the Websocket
     */
    private void startWebSocket() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("wss://ws.finnhub.io?token=cnvih01r01qmeb8tmfp0cnvih01r01qmeb8tmfpg").build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {


                webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"BINANCE:BTCUSDT\"}");


            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                runOnUiThread(() -> {
                    try {
                        JSONObject jsonObject = new JSONObject(text);
                        if (jsonObject.has("data")) {
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            JSONObject dataObject = dataArray.getJSONObject(0);
                            String symbol = dataObject.getString("s");
                            double price = dataObject.getDouble("p");
                            stockMarketDataView.setText(symbol + ": " + price);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                // You can also receive bytes, not used here
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                runOnUiThread(() -> {

                });
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(1000, null);

            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {

            }
        });
    }

    /**
     * This Method Destroys the websocket
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocket != null) {
            webSocket.close(1000, "Activity destroyed");

        }
    }
}
