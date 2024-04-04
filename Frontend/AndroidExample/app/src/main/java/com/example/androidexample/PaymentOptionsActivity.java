package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentOptionsActivity extends AppCompatActivity {

    private Button btnAddNewCard, btnDefaultPayment;
    private String DEFAULT_CARD_URL; // Moved from a static initializer to an instance variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);

        if (LoginActivity.UUID != null) {
            DEFAULT_CARD_URL = "http://localhost:8080/cards/userId/" + LoginActivity.UUID.replace("\"", "");
        } else {
            // Handle the case where UUID is null, for example, by showing an error message or setting a default URL
        }

        btnAddNewCard = findViewById(R.id.btnAddNewCard);
        btnDefaultPayment = findViewById(R.id.btnDefaultPayment);

        btnAddNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the card form activity
                Intent intent = new Intent(PaymentOptionsActivity.this, MainActivity.class);
                startActivity(intent);
                Log.d("UUID", DEFAULT_CARD_URL + " is the ID!"); // Now used inside an instance method
            }
        });

        btnDefaultPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to DefaultCardActivity
                Intent intent = new Intent(PaymentOptionsActivity.this, DefaultCardActivity.class);
                startActivity(intent);
            }
        });
    }
}