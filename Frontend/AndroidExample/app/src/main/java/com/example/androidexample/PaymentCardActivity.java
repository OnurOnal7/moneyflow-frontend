package com.example.androidexample;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.cardform.view.CardForm;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentCardActivity extends AppCompatActivity {
    private static String postUrl = "http://coms-309-056.class.las.iastate.edu:8080/cards/" + LoginActivity.UUID.replace("\"", "");

    private CardForm cardForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentcard);

        cardForm = findViewById(R.id.cardForm);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .actionLabel("Purchase")
                .setup(PaymentCardActivity.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        Button btnBuy = findViewById(R.id.btnBuy);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardForm.isValid()) {
                    Toast.makeText(PaymentCardActivity.this,
                            "Card number: " + cardForm.getCardNumber() +
                                    " Card expirationDate: " + cardForm.getExpirationDateEditText().getText() +
                                    " Card cvv: " + cardForm.getCvvEditText().getText() +
                                    " Card holder name: " + cardForm.getCardholderNameEditText().getText(),
                            Toast.LENGTH_LONG).show();
                    Log.d("DEFAULT_CARD_URL", postUrl + " is the ID!");

                    // Prepare the POST request
                    try {
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("cardNumber", cardForm.getCardNumber());
                        jsonBody.put("expirationDate", cardForm.getExpirationDateEditText().getText().toString());
                        jsonBody.put("cvv", cardForm.getCvvEditText().getText().toString());
                        jsonBody.put("name", cardForm.getCardholderNameEditText().getText().toString());

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                Request.Method.POST,
                                postUrl,
                                jsonBody,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Handle the response
                                        Toast.makeText(PaymentCardActivity.this, "Payment successful!", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // Handle the error
                                        Toast.makeText(PaymentCardActivity.this, "Payment failed: " + error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );

                        // Add the request to the RequestQueue.
                        Volley.newRequestQueue(PaymentCardActivity.this).add(jsonObjectRequest);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(PaymentCardActivity.this, "Please complete the form", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}