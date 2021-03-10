package com.example.codeforces_chaser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
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

public class friends_solves extends AppCompatActivity {

    public String cntst_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_solves);

        Log.d("chaser_log","We are in friends solve activity");

        Bundle gtex = getIntent().getExtras();
        String contest_id = gtex.getString("id");
        cntst_id = contest_id;
        String contest_name = gtex.getString("name");

        TextView txt = findViewById(R.id.fs_contest_name);
        txt.setText(contest_name);

        get_friends_solves(contest_id);
    }

    public void open_contest_link(View view) {
        String link = "https://codeforces.com/contest/" + cntst_id + "/";
        Intent open_link = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(open_link);
    }

    public String get_data(String key) {
        SharedPreferences getShared = getSharedPreferences("cf chaser", MODE_PRIVATE);
        return getShared.getString(key,"0");
    }

    public void get_friends_solves(String contest_id) {

        String url = "https://codeforces.com/api/contest.status?contestId=" + contest_id + "&handle=";

        ListView lv = findViewById(R.id.solves_list);
        List<String> solves = new ArrayList<String>();


        //------------------------------------------------------------------------------------------------
        int total_friend = Integer.parseInt(get_data(get_data("current user")+"total_friend"));
        Log.d("chaser_log","total_friend = " + total_friend);

        List<String> friends_list = new ArrayList<String>();

        friends_list.add(get_data("current user"));
        for(int j = 0; j < total_friend; j++) {
            String key = get_data("current user") + "friend" + j;
            String hndl = get_data(key);
            friends_list.add(hndl);
        }
        //------------------------------------------------------------------------------------------------

        for(int i = 0; i < friends_list.size(); i++) {

            long future_time = System.currentTimeMillis()+500;
            while(System.currentTimeMillis() < future_time) {
                synchronized (this) {
                    try{
                        wait(future_time - System.currentTimeMillis());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            String api_url = url + friends_list.get(i);
            Log.d("chaser_log","api url = " + api_url);

            List<String> prob_list = new ArrayList<String>();
            final int now_i = i;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    api_url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String status = response.getString("status");
                                Log.d("chaser_log", "status is = " + status + " in " + now_i);

                                JSONArray ar = response.getJSONArray("result");
                                for(int j = 0; j < ar.length(); j++) {
                                    String pidx = ar.getJSONObject(j).getJSONObject("problem").getString("index");
                                    if(!prob_list.contains(pidx) && (ar.getJSONObject(j).getString("verdict")).equals("OK")) {
                                        prob_list.add(pidx);
                                    }
                                }

                                String x = "";
                                for(int j = 0; j < prob_list.size(); j++) {
                                    if(x.length() > 0) {
                                        x = x + " , " + prob_list.get(j);
                                    }
                                    else {
                                        x = x + prob_list.get(j);
                                    }
                                }
                                solves.add(x);
                                Log.d("chaser_log", "solves = " + x);

                                if(now_i == (friends_list.size()-1)) {
                                    Log.d("chaser_log", "## setting list view");

                                    fs_adapter adapter = new fs_adapter(friends_solves.this, friends_list, solves);
                                    lv.setAdapter(adapter);

                                    Log.d("chaser_log", "## list view done");
                                }

                                long future_time = System.currentTimeMillis()+500;
                                while(System.currentTimeMillis() < future_time) {
                                    synchronized (this) {
                                        try{
                                            wait(future_time - System.currentTimeMillis());
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                            catch (JSONException e) {
                                Log.d("chaser_log", "Something went wrong in friends solves -> a | in" + now_i);
                                e.printStackTrace();

                                if(now_i == (friends_list.size()-1)) {
                                    Log.d("chaser_log", "## setting list view");

                                    fs_adapter adapter = new fs_adapter(friends_solves.this, friends_list, solves);
                                    lv.setAdapter(adapter);

                                    Log.d("chaser_log", "## list view done");
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("chaser_log", "Something went wrong in friends solves -> b | in" + now_i);

                            if(now_i == (friends_list.size()-1)) {
                                Log.d("chaser_log", "## setting list view");

                                fs_adapter adapter = new fs_adapter(friends_solves.this, friends_list, solves);
                                lv.setAdapter(adapter);

                                Log.d("chaser_log", "## list view done");
                            }
                        }
                    }
            );

            request_queue.getInstance(friends_solves.this).add_to_request_queue(jsonObjectRequest);
        }
    }
}