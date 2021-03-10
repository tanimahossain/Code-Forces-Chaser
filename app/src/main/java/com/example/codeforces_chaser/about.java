package com.example.codeforces_chaser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class about extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

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
        LinearLayout main_view = (LinearLayout) findViewById(R.id.about_activity);

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
}