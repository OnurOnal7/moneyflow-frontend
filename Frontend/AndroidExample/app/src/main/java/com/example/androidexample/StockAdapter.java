package com.example.androidexample;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    private List<StockData> stocksList;
    private Context context;

    public StockAdapter(List<StockData> stocksList, Context context) {
        this.stocksList = stocksList;
        this.context = context;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_item, parent, false);
        return new StockViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        StockData stockData = stocksList.get(position);
        String displayText = stockData.getSymbol() + ": $" + String.format("%.2f", stockData.getPrice());
        holder.textViewStockSymbolPrice.setText(displayText);
        holder.buttonBuy.setOnClickListener(v -> {
            String numberOfSharesText = holder.editTextNumberShares.getText().toString();
            if (!numberOfSharesText.isEmpty()) {
                double shares = Double.parseDouble(numberOfSharesText);
                sendDataToBackend(stockData.getSymbol(), shares, stockData.getPrice());
            } else {
                Toast.makeText(context, "Please enter the number of shares", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return stocksList.size();
    }

    public static class StockViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewStockSymbolPrice;
        public EditText editTextNumberShares;
        public Button buttonBuy;

        public StockViewHolder(View view) {
            super(view);
            textViewStockSymbolPrice = view.findViewById(R.id.textViewStockSymbolPrice);
            editTextNumberShares = view.findViewById(R.id.editTextNumberShares);
            buttonBuy = view.findViewById(R.id.buttonBuy);
        }
    }

    private void sendDataToBackend(String stockSymbol, double shares, double price) {
        OkHttpClient client = new OkHttpClient();
        String jsonData = "{\"symbol\": \"" + stockSymbol + "\", \"shares\": " + shares + ", \"price\": " + price + "}";
        RequestBody body = RequestBody.create(jsonData, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("http://coms-309-056.class.las.iastate.edu:8080/portfolio/" +  LoginActivity.UUID.replace("\"", "") + "/buy")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Handle failure, potentially also on UI thread if you're informing the user
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    performPostSuccessAction();  // This calls your method to show the toast
                } else {
                    // Handle response errors, potentially also needing runOnUiThread if updating UI
                }
            }
        });
    }
    private void performPostSuccessAction() {
        // Check if the context is an instance of Activity
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(() -> {
                // Run on UI thread: Show a toast
                Toast.makeText(context, "Purchase successful", Toast.LENGTH_LONG).show();
            });
        }
    }
}
