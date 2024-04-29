package com.example.androidexample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class GoalListActivity extends AppCompatActivity {

    private final ArrayList<String> items = new ArrayList<>();

    private final ArrayList<String> goalID = new ArrayList<>();
    private ArrayAdapter<String> itemsadapter;
    private ListView listView;
    private Button button, rtn_home, clr_a;
    public String item;
    private String URL = "http://coms-309-056.class.las.iastate.edu:8080/goals/" + LoginActivity.UUID.replace("\"", "");
    private final String DelAllURL = "http://coms-309-056.class.las.iastate.edu:8080/goals/deleteAll/"+LoginActivity.UUID.replace("\"", "");
    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goalview);
        GetRequest();

        builder = new AlertDialog.Builder(this);

        listView = findViewById(R.id.listView);
        button = findViewById(R.id.addButton);
        rtn_home = findViewById(R.id.return_home);
        clr_a = findViewById(R.id.clr_all);
        itemsadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsadapter);
        setUpListViewListener();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GoalListActivity.this, Goal_Add_Activity1.class));
            }
        });


        clr_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllGoals();

            }
        });

        rtn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GoalListActivity.this, MainActivity.class));
            }
        });
    }


    private void GetRequest()
    {
    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String goalString = jsonObject.getString("goalString");
                    String goalIDSTRING = jsonObject.getString("id");
                    items.add(goalString);
                    goalID.add(goalIDSTRING);
                }
                itemsadapter.notifyDataSetChanged(); // Notify adapter after adding all items
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(GoalListActivity.this, "Get Error: " + error.toString(), Toast.LENGTH_LONG).show();
        }
    });
    RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
}



    private void deleteAllGoals() {

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, DelAllURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful deletion
                        itemsadapter.clear();
                        itemsadapter.notifyDataSetChanged();
                        Toast.makeText(GoalListActivity.this, "All Goals Removed!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(GoalListActivity.this, "Error deleting goal: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(deleteRequest);
    }


    private void deleteGoal(String goalId) {
        String deleteURL = "http://coms-309-056.class.las.iastate.edu:8080/goals/" + goalId;

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, deleteURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful deletion
                        itemsadapter.notifyDataSetChanged();
                        Toast.makeText(GoalListActivity.this, "Goal Removed!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(GoalListActivity.this, "Error deleting goal: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(deleteRequest);
    }



    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getApplicationContext();
                String G_ID = goalID.get(position);
                builder.setTitle("Warning!").setMessage("Are you sure you would like to remove this goal?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                items.remove(position);
                                deleteGoal(G_ID);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                return true;
            }
        });
    }


}
