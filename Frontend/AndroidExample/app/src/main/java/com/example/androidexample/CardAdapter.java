package com.example.androidexample;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<Card> cardList;
    private CardListActivity activity;

    public CardAdapter(List<Card> cardList, CardListActivity activity) {
        this.cardList = cardList;
        this.activity = activity;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Card card = cardList.get(position);
        holder.cardNumberTextView.setText(card.getMaskedNumber()); // Assume Card class has getMaskedNumber method

        holder.editButton.setOnClickListener(view -> {
            // Intent to start EditCardActivity, passing the card info for editing
            Intent intent = new Intent(activity, EditCardActivity.class);
            String cardDataJson = card.toJson().toString();
            Log.d("CardAdapter", "Card data being sent: " + cardDataJson);
            intent.putExtra("card_data", cardDataJson);
            activity.startActivity(intent);
        });

        holder.useButton.setOnClickListener(view -> {
            // Here, implement the logic to set this card as default, e.g., via an API call
            String cardId = card.getId(); // Replace with method to get card ID
            activity.setDefaultCard(cardId);
        });

        holder.deleteButton.setOnClickListener(view -> {
            // Logic to delete the card
            deleteCard(card, position);
        });
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView cardNumberTextView;
        public Button editButton, useButton, deleteButton;

        public CardViewHolder(View view) {
            super(view);
            cardNumberTextView = view.findViewById(R.id.cardNumberTextView);
            editButton = view.findViewById(R.id.editButton);
            useButton = view.findViewById(R.id.useButton);
            deleteButton = view.findViewById(R.id.deleteButton);
        }
    }

    // Helper method to remove a card from the RecyclerView
    public void removeAt(int position) {
        cardList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cardList.size());
    }
    public void deleteCard(Card card, int position) {
        String url = "http://coms-309-056.class.las.iastate.edu:8080/cards/id/" + LoginActivity.UUID.replace("\"", "") + "/" + card.getId();

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    // Response handling code
                    cardList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, cardList.size());
                    Toast.makeText(activity, "Card deleted successfully", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Error handling code
                    Toast.makeText(activity, "Error deleting card: " + error.toString(), Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(activity).add(stringRequest);
    }
}
