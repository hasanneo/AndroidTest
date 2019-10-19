package com.example.androidtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    public static RecyclerViewAdapter viewAdapter;
    private List<FeedItem> feedList;
    private FloatingActionButton fab;
    private RequestQueue requestQueue;
    private final int FAB_CLICK=0;//to know that the calling activity is from this class when calling DataScreen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Init ids
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        parseJSONArray();
    }

    /**
     * A method which will do a JSON request and fills a list with the JSON objects and sets the recyclerView to the list
     */
    private void parseJSONArray() {
        final String HOODINI_URL = "https://api.hoodini.in/GetRanks";
        JSONObject object = new JSONObject();//empty object to pass for post
        //JSON request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, HOODINI_URL, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    //fill list with the JSON objects
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        int id = data.getInt("id_rank");
                        String name = data.getString("name_rank");
                        String icon = data.getString("rank_icon");
                        String textcolor = data.getString("color_text");
                        String backcolor = data.getString("color_background");
                        feedList.add(new FeedItem(id, name, icon, textcolor, backcolor));
                    }
                    //set adapter to the recyclerview
                    viewAdapter = new RecyclerViewAdapter(MainActivity.this, feedList);
                    recyclerView.setAdapter(viewAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    /**
     * Setting click listeners after the creation
     */
    @Override
    protected void onStart() {
        super.onStart();
        final Context context = this;
        //Setting listeners...
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = viewAdapter.feedList.size() - 1;
                Intent intent = new Intent(context, DataScreen.class);
                intent.putExtra("activity", FAB_CLICK);//from the fab click
                intent.putExtra("idRank", index);
                context.startActivity(intent);
            }
        });
        //on scroll listener to make the button vanish on scroll down
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hide();
                else
                    fab.show();//show on scroll up
            }
        });
    }

}
