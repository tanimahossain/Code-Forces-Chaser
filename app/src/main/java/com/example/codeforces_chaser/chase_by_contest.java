package com.example.codeforces_chaser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class chase_by_contest extends AppCompatActivity {

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chase_by_contest);

        progressDialog = new ProgressDialog(chase_by_contest.this);
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
        LinearLayout main_view = (LinearLayout) findViewById(R.id.chase_contest_activity);

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

    public void start_progress_dialog() {
        progressDialog.show();

        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        progressDialog.setCancelable(false);
    }

    public void stop_progress_dialog() {
        progressDialog.dismiss();
    }

    public void contest_list_limit(View view) {

        ListView lv = findViewById(R.id.cbc_contest_list);
        List<String> names = new ArrayList<String>();
        List<String> ids = new ArrayList<String>();
        cbc_list_adapter adapter = new cbc_list_adapter(chase_by_contest.this, ids, names);
        lv.setAdapter(adapter);

        TextView tx = findViewById(R.id.textView3);
        //tx.setBackgroundColor(Color.parseColor("#5669D5"));
        tx.setText("");

        start_progress_dialog();


        EditText txt = findViewById(R.id.get_lim);
        String s = txt.getText().toString();
        txt.setText("");

        int lim = 200;
        if(!s.equals("")) lim = Integer.parseInt(s);

        get_contest_list(lim);
    }

    public void get_contest_list(int lim) {

        String api_url = "https://codeforces.com/api/contest.list";
        Log.d("chaser_log", "api url is: " + api_url);

        ListView lv = findViewById(R.id.cbc_contest_list);

        List<String> names = new ArrayList<String>();
        List<String> ids = new ArrayList<String>();


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

        final int recent_lim = lim;


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

                            JSONArray js = response.getJSONArray("result");

                            for(int i = 0; i < js.length() && i <= recent_lim; i++) {
                                final int cur_idx = i;
                                JSONObject job = js.getJSONObject(i);

                                String phase = job.getString("phase");
                                if(phase.equals("FINISHED")) {

                                    String id = (job.getInt("id") + "");
                                    String name = job.getString("name");

                                    for(int j = 0; j < friends_list.size(); j++) {

                                        String api_url2 = "https://codeforces.com/api/contest.status?contestId=" + id + "&handle=" + friends_list.get(j);
                                        Log.d("chaser_log","api url 2 : "+api_url2);

                                        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(
                                                Request.Method.GET,
                                                api_url2,
                                                null,
                                                new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject resp) {
                                                        try {
                                                            String status2 = resp.getString("status");
                                                            Log.d("chaser_log","status2 is = "+status2);

                                                            JSONArray js2 = resp.getJSONArray("result");

                                                            for(int k = 0; k < js2.length(); k++) {

                                                                JSONObject job2 = js2.getJSONObject(k);
                                                                if(job2.getString("verdict").equals("OK")) {

                                                                    if(!ids.contains(id)) {
                                                                        ids.add(id);
                                                                        names.add(name);
                                                                    }
                                                                    break;
                                                                }
                                                            }

                                                            //update_progress_dialog((cur_idx*100)/recent_lim);

                                                            if(id.equals("1") || cur_idx == recent_lim) {
                                                                cbc_list_adapter adapter = new cbc_list_adapter(chase_by_contest.this, ids, names);
                                                                lv.setAdapter(adapter);
                                                                Log.d("chaser_log","List View seted");

                                                                TextView tx = findViewById(R.id.textView3);
                                                                //tx.setBackgroundColor(Color.parseColor("#ED165F"));
                                                                tx.setText("List of Contest");

                                                                stop_progress_dialog();
                                                            }
                                                        }
                                                        catch(JSONException e) {
                                                            Log.d("chaser_log","Something went wrong in perticipation check -> a in "+id);
                                                            e.printStackTrace();


                                                            if(id.equals("1") || cur_idx == recent_lim) {
                                                                cbc_list_adapter adapter = new cbc_list_adapter(chase_by_contest.this, ids, names);
                                                                lv.setAdapter(adapter);
                                                                Log.d("chaser_log","List View seted");

                                                                TextView tx = findViewById(R.id.textView3);
                                                                //tx.setBackgroundColor(Color.parseColor("#ED165F"));
                                                                tx.setText("List of Contest");

                                                                stop_progress_dialog();
                                                            }
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.d("chaser_log","Something went wrong in  perticipation check -> b in "+id);


                                                        if(id.equals("1") || cur_idx == recent_lim) {
                                                            cbc_list_adapter adapter = new cbc_list_adapter(chase_by_contest.this, ids, names);
                                                            lv.setAdapter(adapter);
                                                            Log.d("chaser_log","List View seted");

                                                            TextView tx = findViewById(R.id.textView3);
                                                            //tx.setBackgroundColor(Color.parseColor("#ED165F"));
                                                            tx.setText("List of Contest");

                                                            stop_progress_dialog();
                                                        }
                                                    }
                                                }
                                        );
                                        request_queue.getInstance(chase_by_contest.this).add_to_request_queue(jsonObjectRequest2);

                                        long future_time = System.currentTimeMillis()+5;
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
                                }
                            }

                        }
                        catch(JSONException e) {
                            Log.d("chaser_log","Something went wrong in cbc -> a");
                            stop_progress_dialog();
                            Toast.makeText(chase_by_contest.this, "Something Went wrong\nPlease Try again", Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        stop_progress_dialog();Toast.makeText(chase_by_contest.this, "Something Went wrong\nPlease Try again", Toast.LENGTH_SHORT).show();

                        Log.d("chaser_log","Something went wrong in cbc -> b");
                    }
                }
        );

        request_queue.getInstance(chase_by_contest.this).add_to_request_queue(jsonObjectRequest);
    }
}