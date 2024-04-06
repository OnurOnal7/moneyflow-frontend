package com.example.androidexample;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.cardform.view.CardForm;

import org.json.JSONException;
import org.json.JSONObject;

public class EditCardActivity extends AppCompatActivity {

    private CardForm cardForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        cardForm = findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .actionLabel("Save")
                .setup(this);

        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        if (getIntent().hasExtra("card_data")) {
            String cardJsonString = getIntent().getStringExtra("card_data");
            try {
                JSONObject cardJsonObject = new JSONObject(cardJsonString);
                Card card = Card.fromJson(cardJsonObject);
                populateCardForm(card);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading card information", Toast.LENGTH_LONG).show();
            }
        }

        Button btnSaveCard = findViewById(R.id.btn_save_card);
        btnSaveCard.setOnClickListener(v -> {
            if (cardForm.isValid()) {
                String cardNumber = cardForm.getCardNumber();
                String expirationMonth = cardForm.getExpirationMonth();
                String expirationYear = cardForm.getExpirationYear();
                String cvv = cardForm.getCvv();
                String cardholderName = cardForm.getCardholderName();

                Card newCard = new Card(cardholderName, cardNumber, expirationMonth + "/" + expirationYear, cvv);
                sendCardToBackend(newCard);

                finish();
            } else {
                Toast.makeText(EditCardActivity.this, "Please fill out the card form correctly.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateCardForm(Card card) {
        cardForm.getCardEditText().setText(card.getCardNumber());
        String[] expirationDateParts = card.getExpirationDate().split("/");
        String expirationDate = expirationDateParts[0] + (expirationDateParts.length > 1 ? expirationDateParts[1] : "");
        cardForm.getExpirationDateEditText().setText(expirationDate);
        cardForm.getCvvEditText().setText(card.getCvv());
        cardForm.getCardholderNameEditText().setText(card.getName());
    }

    private void sendCardToBackend(Card card) {
        JSONObject cardJson = card.toJson();
        String url = "http://coms-309-056.class.las.iastate.edu:8080/cards/" + LoginActivity.UUID.replace("\"", "");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, cardJson,
                response -> Toast.makeText(EditCardActivity.this, "Card added successfully!", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(EditCardActivity.this, "Error adding card: " + error.toString(), Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
