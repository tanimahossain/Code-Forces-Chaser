package com.example.codeforces_chaser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String current_user = get_data("current user");
        if(current_user.equals("")) {
            ;
        }
        else{
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void chaser_login(View view) {
        EditText txt = findViewById(R.id.handle);
        String s = txt.getText().toString();
        save_data("current user", s);

        TextView txtv = findViewById(R.id.handle);
        txtv.setText("");

        startActivity(new Intent(login.this, MainActivity.class));
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
}