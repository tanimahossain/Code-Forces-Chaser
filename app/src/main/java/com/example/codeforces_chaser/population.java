package com.example.codeforces_chaser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class population extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_population);

        active_population();
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
        LinearLayout main_view = (LinearLayout) findViewById(R.id.activity_population);

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

    /*public void all_time_population(View view) {
        TextView txt = findViewById(R.id.a_cnt);
        txt.setText("32");
        txt = findViewById(R.id.b_cnt);
        txt.setText("149");
        txt = findViewById(R.id.c_cnt);
        txt.setText("206");
        txt = findViewById(R.id.d_cnt);
        txt.setText("189");
        txt = findViewById(R.id.e_cnt);
        txt.setText("830");
        txt = findViewById(R.id.f_cnt);
        txt.setText("1243");
        txt = findViewById(R.id.g_cnt);
        txt.setText("3493");
        txt = findViewById(R.id.h_cnt);
        txt.setText("3968");
        txt = findViewById(R.id.i_cnt);
        txt.setText("6206");
        txt = findViewById(R.id.j_cnt);
        txt.setText("22049");
    }
*/
    public void active_population(/*View view*/) {
        TextView txt = findViewById(R.id.a_cnt);
        txt.setText("32");
        txt = findViewById(R.id.b_cnt);
        txt.setText("149");
        txt = findViewById(R.id.c_cnt);
        txt.setText("206");
        txt = findViewById(R.id.d_cnt);
        txt.setText("189");
        txt = findViewById(R.id.e_cnt);
        txt.setText("830");
        txt = findViewById(R.id.f_cnt);
        txt.setText("1243");
        txt = findViewById(R.id.g_cnt);
        txt.setText("3493");
        txt = findViewById(R.id.h_cnt);
        txt.setText("3968");
        txt = findViewById(R.id.i_cnt);
        txt.setText("6206");
        txt = findViewById(R.id.j_cnt);
        txt.setText("22049");
    }

}