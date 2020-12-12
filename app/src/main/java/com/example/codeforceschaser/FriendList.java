package com.example.codeforceschaser;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class FriendList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    FirebaseAuth frndlistAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        toolbar= findViewById(R.id.toolbarfriendlist);
        setSupportActionBar(toolbar);
        drawer=findViewById(R.id.drawerlayout_friendlist);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView=findViewById(R.id.nav_view_friendlist);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_view_friendlist);
        frndlistAuth=FirebaseAuth.getInstance();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else super.onBackPressed();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        switch(menuItem.getItemId()){
            case R.id.nav_Profile:
                //finish();
                Intent intent= new Intent(getApplication(),MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_friendlist:
                break;
            case R.id.nav_friends:
                //finish();
                intent= new Intent(getApplication(),Friends.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_Contests:
                //finish();
                intent= new Intent(getApplication(),Contests.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_LogOut:
                //finish();
                FirebaseAuth.getInstance().signOut();
                intent= new Intent(getApplication(),LogIn.class);
                startActivity(intent);
                finish();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}