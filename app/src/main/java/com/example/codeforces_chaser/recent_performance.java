package com.example.codeforces_chaser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class recent_performance extends AppCompatActivity {

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_performance);
        Log.d("chaser_log", "we are in recent performance activity.");

        progressDialog = new ProgressDialog(recent_performance.this);
    }

    public void start_progress_dialog() {
        progressDialog.show();

        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        progressDialog.setCancelable(false);
    }

    public void stop_progress_dialog() {
        progressDialog.dismiss();
    }

    public void get_recent_performance(View view) {
        Log.d("chaser_log", "getting performance info.");
        start_progress_dialog();
        calculate_performance();
    }

    public void save_data(String key, String data) {
        SharedPreferences shrd = getSharedPreferences("cf chaser", MODE_PRIVATE);
        SharedPreferences.Editor editor = shrd.edit();

        editor.putString(key, data);
        editor.apply();
    }

    public String get_data(String key) {
        SharedPreferences getShared = getSharedPreferences("cf chaser", MODE_PRIVATE);
        return getShared.getString(key,"");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LinearLayout main_view = (LinearLayout) findViewById(R.id.recent_performance_activity);

        if(item.getItemId() == R.id.profile_menu) {
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
        else if(item.getItemId() == R.id.about_menu) {
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
        else if(item.getItemId() == R.id.friends_menu) {
            if (item.isChecked()) {
                item.setChecked(true);
            }
            else {
                item.setChecked(true);
                Intent it = new Intent(this, friends.class);
                startActivity(it);
            }
            return true;
        }
        else if(item.getItemId() == R.id.logout_menu) {
            save_data("current user", "");
            Intent it = new Intent(this, login.class);
            startActivity(it);
        }
        else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void calculate_performance() {
        String handle = get_data("current user");
        Log.d("chaser_log", "user handle is: " + handle);

        String api_url = "https://codeforces.com/api/user.status?handle=" + handle;
        Log.d("chaser_log", "api url is: " + api_url);

        EditText tx = findViewById(R.id.last_x_days);

        long x = Integer.parseInt(tx.getText().toString());
        tx.setText("");

        long y = x * 24;

        x = x * 24 * 60 * 60 * 1000;
        x = System.currentTimeMillis() - x;

        final long lim = x;

        Log.d("chaser_log", "time limit = " + lim);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                api_url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            Log.d("chaser_log", "Status: "+status);

                            JSONArray sub_list = response.getJSONArray("result");

                            List<Integer> prob_rating = new ArrayList<Integer>();
                            List<String> prob_name = new ArrayList<String>();

                            Log.d("chaser_log", "List created.");

                            Log.d("chaser_log", "## total submission = "+sub_list.length());

                            for(int i = 0; i < sub_list.length(); i++) {

                                try {
                                    long sub_time = sub_list.getJSONObject(i).getLong("creationTimeSeconds") * 1000;

                                    if (sub_time >= lim) {
                                        if (sub_list.getJSONObject(i).getString("verdict").equals("OK")) {

                                            String name = sub_list.getJSONObject(i).getJSONObject("problem").getString("index") + sub_list.getJSONObject(i).getJSONObject("problem").getString("name");
                                            Integer rating = (Integer) sub_list.getJSONObject(i).getJSONObject("problem").getInt("rating");

                                            if (!prob_name.contains(name)) {
                                                prob_name.add(name);
                                                prob_rating.add(rating);
                                            }
                                        }
                                    }
                                }
                                catch(Exception e) {
                                    Log.d("chaser_log", "something wrong in loop.");
                                }
                            }

                            Log.d("chaser_log", "All data fatched");

                            long total_point = 0, prob_a = 0, prob_b = 0, prob_c = 0, prob_d = 0, prob_e = 0, prob_f = 0;
                            for(int i = 0; i < prob_name.size(); i++) {

                                int ratt = prob_rating.get(i);
                                total_point += ratt;

                                if(ratt > 3000) prob_f++;
                                else if(ratt > 2500) prob_e++;
                                else if(ratt > 2000) prob_d++;
                                else if(ratt > 1500) prob_c++;
                                else if(ratt > 1000) prob_b++;
                                else prob_a++;
                            }

                            Log.d("chaser_log", "All data processed");
                            Log.d("chaser_log", "total point = "+total_point+", a="+prob_a+", b="+prob_b+", c="+prob_c+", d="+prob_d+", e="+prob_e+", f="+prob_f);

                            TextView txt = findViewById(R.id.avg_point);
                            int avg_point = (int)(total_point / y);

                            txt.setText(""+avg_point);
                            Log.d("chaser_log", "avg point seted");


                            txt = findViewById(R.id.prob_a);
                            txt.setText(""+prob_a);
                            Log.d("chaser_log", "prob a sated");

                            txt = findViewById(R.id.prob_b);
                            txt.setText(""+prob_b);
                            Log.d("chaser_log", "prob b sated");

                            txt = findViewById(R.id.prob_c);
                            txt.setText(""+prob_c);
                            Log.d("chaser_log", "prob c sated");

                            txt = findViewById(R.id.prob_d);
                            txt.setText(""+prob_d);
                            Log.d("chaser_log", "prob d sated");

                            txt = findViewById(R.id.prob_e);
                            txt.setText(""+prob_e);
                            Log.d("chaser_log", "prob e sated");

                            txt = findViewById(R.id.prob_f);
                            txt.setText(""+prob_f);
                            Log.d("chaser_log", "prob f sated");

                            stop_progress_dialog();


                            Log.d("chaser_log", "\n## Done ##\n\n");
                        }
                        catch(JSONException e) {
                            Log.d("chaser_log","Something went wrong in recent performance -> a");
                            stop_progress_dialog();
                            Toast.makeText(recent_performance.this, "Something Went wrong\nPlease Try again", Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("chaser_log","Something went wrong in recent performance -> b");
                        stop_progress_dialog();
                        Toast.makeText(recent_performance.this, "Something Went wrong\nPlease Try again", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        request_queue.getInstance(recent_performance.this).add_to_request_queue(jsonObjectRequest);
    }
}