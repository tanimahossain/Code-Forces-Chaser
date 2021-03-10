package com.example.codeforces_chaser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class friend_add_del extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_add_del);



        Button fr_add = findViewById(R.id.add_but), fr_del = findViewById(R.id.delete_but);
        fr_add.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                EditText txt = findViewById(R.id.add_del_handle);
                evaluate_new_friend(txt.getText().toString());

                return true;
            }
        });

        fr_del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                EditText txt = findViewById(R.id.add_del_handle);
                delete_a_friend(txt.getText().toString());

                return true;
            }
        });

    }

    public void evaluate_new_friend(String friends_handle) {
        Log.d("chaser_log", "friends handle is: " + friends_handle);

        String api_url = "https://codeforces.com/api/user.info?handles=" + friends_handle;

        Log.d("chaser_log", "api url is: " + api_url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                api_url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");

                            if(status.equals("OK")) {
                                Log.d("chaser_log", "Status is OK.");

                                int total_friend = Integer.parseInt(get_data(get_data("current user") + "total_friend"));
                                Log.d("chaser_log", "total friend = "+total_friend);


                                String key = get_data("current user") + "friend" + total_friend;
                                Log.d("chaser_log", "key = "+key);
                                total_friend += 1;

                                save_data(get_data("current user") + "total_friend", ""+total_friend);
                                save_data(key, friends_handle);

                                Log.d("chaser_log", "Now total_friend = " + get_data(get_data("current user") + "total_friend"));

                                startActivity(new Intent(friend_add_del.this, friends.class));
                            }
                            else {
                                Log.d("chaser_log", "Status is not OK.");
                            }
                        }
                        catch(JSONException e) {
                            Log.d("chaser_log","Something went wrong in profile info -> a");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("chaser_log","Something went wrong in profile info -> b");
                    }
                }
        );

        request_queue.getInstance(friend_add_del.this).add_to_request_queue(jsonObjectRequest);
    }


    public void delete_a_friend(String friend_name) {
        int total_friend = Integer.parseInt(get_data(get_data("current user") + "total_friend"));
        List<String> ls = new ArrayList<String>();
        for(int i = 0; i < total_friend; i++) {
            String key = get_data("current user") + "friend" + i;
            String fh = get_data(key);
            if(fh.equals(friend_name)) {
                ;
            }
            else {
                ls.add(fh);
            }
        }
        save_data(get_data("current user") + "total_friend", ""+ls.size());
        for(int i = 0; i < ls.size(); i++) {
            String key = get_data("current user") + "friend" + i;
            save_data(key, ls.get(i));
        }

        startActivity(new Intent(friend_add_del.this, friends.class));
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

}