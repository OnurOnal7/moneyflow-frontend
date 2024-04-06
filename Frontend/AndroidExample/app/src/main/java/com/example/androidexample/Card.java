package com.example.androidexample;

import org.json.JSONException;
import org.json.JSONObject;

public class Card {
    private String id;
    private String name;
    private String cardNumber;
    private String expirationDate;
    private String cvv;

    public Card(String name, String cardNumber, String expirationDate, String cvv) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getCvv() {
        return cvv;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("cardNumber", cardNumber);
            jsonObject.put("expirationDate", expirationDate);
            jsonObject.put("cvv", cvv);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static Card fromJson(JSONObject jsonObject) throws JSONException {
        Card card = new Card(
                jsonObject.getString("name"),
                jsonObject.getString("cardNumber"),
                jsonObject.getString("expirationDate"),
                jsonObject.getString("cvv")
        );
        if (jsonObject.has("id")) {
            card.id = jsonObject.getString("id");
        }
        return card;
    }

    public String getMaskedNumber() {
        // Mask all but the last 4 digits of the card number for security
        return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
    }
}
