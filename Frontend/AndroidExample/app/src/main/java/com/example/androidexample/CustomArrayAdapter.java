package com.example.androidexample;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<String> {

    private List<String> items;
    private List<String> goalIds;
    private String userId;

    public CustomArrayAdapter(Context context, List<String> items, List<String> goalIds, String userId) {
        super(context, R.layout.custom_list_item, items);
        this.items = items;
        this.goalIds = goalIds;
        this.userId = userId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_item, parent, false);
            holder = new ViewHolder();
            holder.cardNumberTextView = convertView.findViewById(R.id.cardNumberTextView);
            holder.progressBar = convertView.findViewById(R.id.progressBar);
            holder.editButton = convertView.findViewById(R.id.addProg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // Set data to views
        holder.cardNumberTextView.setText(items.get(position));
        holder.progressBar.setProgress(0); // Set progress value here
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the EditGoalActivity when edit button is clicked
                Context context = getContext();
                Intent intent = new Intent(context, AddGoalProgressActivity.class);
                intent.putExtra("goalId", goalIds.get(position));
                intent.putExtra("userId", MainActivity.selectedMemberId);
                context.startActivity(intent);
                context.startActivity(intent);
            }
        });

        return convertView;
    }


    static class ViewHolder {
        TextView cardNumberTextView;
        ProgressBar progressBar;
        Button editButton;
    }
}
