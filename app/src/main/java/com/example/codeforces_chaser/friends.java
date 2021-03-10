package com.example.codeforces_chaser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class friends extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Log.d("chaser_log","We are in friends activity now.");

        set_the_friends_list();
    }

    public String get_rating_color(int rating) {
        if(rating >= 3000) return ((String)"#DC143C");
        else if(rating >= 2600) return ((String)"#FF0000");
        else if(rating >= 2400) return ((String)"#FA8072");
        else if(rating >= 2300) return ((String)"#FFA500");
        else if(rating >= 2100) return ((String)"#FFFF00");
        else if(rating >= 1900) return ((String)"#FF1493");
        else if(rating >= 1600) return ((String)"#4682B4");
        else if(rating >= 1400) return ((String)"#008B8B");
        else if(rating >= 1200) return ((String)"#8FBC8F");
        return ((String)"#808080");
    }

    public void set_the_friends_list() {
        ListView lv = findViewById(R.id.list_of_friends);

        int total_friend = Integer.parseInt(get_data(get_data("current user")+"total_friend"));
        Log.d("chaser_log","total_friend = " + total_friend);


        String[] frns = new String[total_friend];
        int[] curat = new int[total_friend];
        String[] rank_color = new String[total_friend];

        String api_url = "https://codeforces.com/api/user.info?handles=";

        for(int i = 0; i < total_friend; i++) {
            String key = get_data("current user") + "friend" + i;
            String fh = get_data(key);

            frns[i] = fh;
            api_url = api_url + fh + ";";
        }

        Log.d("chaser_log", "api url is: " + api_url);

        //ArrayAdapter<String> fr_adapter = new ArrayAdapter<String>(this, R.layout.list_item, frns);

        //lv.setAdapter(fr_adapter);

        //Log.d("chaser_log","List view done!");


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                api_url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            Log.d("chaser_log","status is = "+status);

                            JSONArray usrlist = response.getJSONArray("result");
                            for(int i = 0; i < usrlist.length(); i++) {
                                curat[i] = usrlist.getJSONObject(i).getInt("rating");
                                rank_color[i] = get_rating_color(curat[i]);
                            }

                            friend_list_adapter adapter = new friend_list_adapter(friends.this, frns, curat, rank_color);
                            lv.setAdapter(adapter);
                        }
                        catch(JSONException e) {
                            Log.d("chaser_log","Something went wrong in friends.java -> a");
                            Toast.makeText(friends.this, "Something Went wrong\nPlease Try again", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("chaser_log","Something went wrong in friends.java -> b");
                        Toast.makeText(friends.this, "Something Went wrong\nPlease Try again", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        request_queue.getInstance(friends.this).add_to_request_queue(jsonObjectRequest);
    }

    public void save_data(String key, String data) {
        SharedPreferences shrd = getSharedPreferences("cf chaser", MODE_PRIVATE);
        SharedPreferences.Editor editor = shrd.edit();

        editor.putString(key, data);
        editor.apply();
    }

    public String get_data(String key) {
        SharedPreferences getShared = getSharedPreferences("cf chaser", MODE_PRIVATE);
        return getShared.getString(key,"0");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LinearLayout main_view = (LinearLayout) findViewById(R.id.friend_list_activity);

        if(item.getItemId() == R.id.about_menu)  {
            if (item.isChecked()) {
                item.setChecked(true);
            }
            else {
                item.setChecked(true);
                Intent it = new Intent(this, about.class);
                startActivity(it);
            }
            return true;
        }
        else if(item.getItemId() == R.id.logout_menu) {
            save_data("current user", "");
            Intent it = new Intent(this, login.class);
            startActivity(it);
        }
        else if(item.getItemId() == R.id.activity_population) {
            if (item.isChecked()) {
                item.setChecked(true);
            }
            else {
                item.setChecked(true);
                Intent it = new Intent(this, population.class);
                startActivity(it);
            }
            return true;
        }
        else if(item.getItemId() == R.id.chase_by_contest_menu) {
            if (item.isChecked()) {
                item.setChecked(true);
            }
            else {
                item.setChecked(true);
                Intent it = new Intent(this, chase_by_contest.class);
                startActivity(it);
            }
            return true;
        }
        else if(item.getItemId() == R.id.recent_performance_menu) {
            if (item.isChecked()) {
                item.setChecked(true);
            }
            else {
                item.setChecked(true);
                Intent it = new Intent(this, recent_performance.class);
                startActivity(it);
            }
            return true;
        }
        else if(item.getItemId() == R.id.profile_menu) {
            if (item.isChecked()) {
                item.setChecked(true);
            }
            else {
                item.setChecked(true);
                Intent it = new Intent(this, MainActivity.class);
                startActivity(it);
            }
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void add_del_onclick(View view) {
        startActivity(new Intent(this, friend_add_del.class));
    }
}