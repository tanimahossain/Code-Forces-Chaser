package com.example.codeforces_chaser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String current_user = get_data("current user");

        Log.d("chaser_log", "setting profile data.");
        set_profile_data(current_user);
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
        LinearLayout main_view = (LinearLayout) findViewById(R.id.profile_activity);

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
        else if(item.getItemId() == R.id.logout_menu) {
            save_data("current user", "");
            Intent it = new Intent(this, login.class);
            startActivity(it);
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
        else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void set_profile_data(String handle) {

        Log.d("chaser_log", "user handle is: " + handle);

        String api_url = "https://codeforces.com/api/user.info?handles=" + handle;

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
                            Log.d("chaser_log", "Status: "+status);

                            if(status.equals("OK")) {
                                ;
                            }
                            else {
                                save_data("current user", "");
                                startActivity(new Intent(MainActivity.this, login.class));
                            }

                            JSONObject jsob = response.getJSONArray("result").getJSONObject(0);

                            String profile_pic_url = "https:" + jsob.getString("titlePhoto");
                            Log.d("chaser_log", "profile pic url: "+profile_pic_url);

                            TextView txt = findViewById(R.id.profile_handle);
                            String data = jsob.getString("handle");
                            txt.setText(data);
                            Log.d("chaser_log", "handle: "+data);

                            txt = findViewById(R.id.profile_name);
                            String fname = "", lname = "";
                            try {
                                fname = jsob.getString("firstName");
                            }
                            catch (Exception e) {
                                ;
                            }
                            try {
                                lname = jsob.getString("lastName");
                            }
                            catch (Exception e) {
                                ;
                            }
                            data =  fname + " " + lname;
                            txt.setText(data);
                            Log.d("chaser_log", "name: "+data);

                            try {
                                txt = findViewById(R.id.profile_inistitute);
                                data = jsob.getString("organization");
                                txt.setText(data);
                                Log.d("chaser_log", "inistite: " + data);
                            }
                            catch (Exception e) {
                                ;
                            }

                            txt = findViewById(R.id.max_max);
                            txt.setText("MAX");

                            txt = findViewById(R.id.max_rating);
                            Integer mxrat = (Integer) jsob.getInt("maxRating");
                            txt.setText(""+mxrat);
                            Log.d("chaser_log", "max rating: " + mxrat);

                            txt = findViewById(R.id.max_rank);
                            data = jsob.getString("maxRank");
                            txt.setText(""+data);
                            Log.d("chaser_log", "max rank: "+data);

                            txt = findViewById(R.id.cur_cur);
                            txt.setText("Current");

                            txt = findViewById(R.id.current_rating);
                            Integer currat = (Integer) jsob.getInt("rating");
                            txt.setText(""+currat);
                            Log.d("chaser_log", "current rating: "+currat);

                            txt = findViewById(R.id.current_rank);
                            data = jsob.getString("rank");
                            txt.setText(""+data);
                            Log.d("chaser_log", "current rank: "+data);

                            txt = findViewById(R.id.friend_of_cnt);
                            Integer intdata = (Integer) jsob.getInt("friendOfCount");
                            txt.setText("Friend of " + intdata + " user");
                            Log.d("chaser_log", "friend of: "+intdata);

                            try {
                                txt = findViewById(R.id.address);
                                data = jsob.getString("city") + ", " + jsob.getString("country");
                                txt.setText(data);
                                Log.d("chaser_log", "address: " + data);
                            }
                            catch (Exception e) {
                                ;
                            }

                            color_max(mxrat);
                            color_cur(currat);

                            set_profile_pic(profile_pic_url);

                        }
                        catch(JSONException e) {
                            Log.d("chaser_log","Something went wrong in profile info -> a");
                            Toast.makeText(MainActivity.this, "Something Went wrong\nPlease Try again", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("chaser_log","Something went wrong in profile info -> b");
                        Toast.makeText(MainActivity.this, "Something Went wrong\nPlease Try again", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        request_queue.getInstance(MainActivity.this).add_to_request_queue(jsonObjectRequest);
    }

    public void set_profile_pic(String icon_url) {
        Log.d("chaser_log","Setting profile picture");
        Log.d("chaser_log","url: "+icon_url);

        ImageRequest imageRequest = new ImageRequest(
                icon_url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        Log.d("chaser_log","Got the profile picture");

                        ImageView img = findViewById(R.id.profile_picture);
                        img.setImageBitmap(response);
                    }
                },
                0,
                0,
                ImageView.ScaleType.CENTER_CROP,
                null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("chaser_log","something went wrong in profile pic setting.");
                        Toast.makeText(MainActivity.this, "Something Went wrong\nPlease Try again", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        request_queue.getInstance(MainActivity.this).add_to_request_queue(imageRequest);
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

    public void color_max(int rating) {
        TextView mx_mx = findViewById(R.id.max_max);
        TextView mxrat = findViewById(R.id.max_rating);
        TextView mxran = findViewById(R.id.max_rank);

        String color = get_rating_color(rating);
        Log.d("chaser_log", "got mx color = "+ color);

        mx_mx.setTextColor(Color.parseColor(color));
        mxrat.setTextColor(Color.parseColor(color));
        mxran.setTextColor(Color.parseColor(color));

        Log.d("chaser_log", "mx color set done");
    }

    public void color_cur(int rating) {
        TextView cur_cur = findViewById(R.id.cur_cur);
        TextView mnrat = findViewById(R.id.current_rating);
        TextView mnran = findViewById(R.id.current_rank);
        TextView hndl = findViewById(R.id.profile_handle);

        String color = get_rating_color(rating);
        Log.d("chaser_log", "got cur color = " +  color);

        hndl.setTextColor(Color.parseColor(color));

        cur_cur.setTextColor(Color.parseColor(color));

        mnrat.setTextColor(Color.parseColor(color));

        mnran.setTextColor(Color.parseColor(color));

        Log.d("chaser_log", "cur color set done");
    }
}