package com.example.androidexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
public class GoalListActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsadapter;
    private ListView listView;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goalview);

        listView = findViewById(R.id.listView);
        button = findViewById(R.id.addButton);

        items = new ArrayList<>();
        itemsadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsadapter);
        setUpListViewListener();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(GoalListActivity.this, GoalListAddActivity.class));

            }
        });



    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Goal Removed", Toast.LENGTH_LONG).show();

                items.remove(position);
                itemsadapter.notifyDataSetChanged();
                return true;
            }
        });
    }





}
