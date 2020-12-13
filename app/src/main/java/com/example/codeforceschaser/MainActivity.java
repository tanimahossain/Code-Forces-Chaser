package com.example.codeforceschaser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    Toolbar toolbar;
    Button resetpassbtn,addremovefrndbtn;
    NavigationView navigationView;
    FirebaseAuth mainAuth;
    FirebaseFirestore mainfstore;
    static public String MainUserID,UserName,UserCFHandle,UserEmail,UserMaxRank;
    public Integer UserMaxRating;
    public TextView ShowUserName,ShowCFHandle,ShowEmail,ShowMaxRating,ShowMaxRank;
    static public ArrayList<String> frndlistfromfirestore= new ArrayList<String>();
    final public String TAG="Profile";
    boolean dataupdated=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer=findViewById(R.id.drawerlayout_profile);
        resetpassbtn = findViewById(R.id.Profile_ResetPass_Button);
        addremovefrndbtn = findViewById(R.id.Profile_AddorRemove_Friends);
        navigationView=findViewById(R.id.nav_view_profile);
        View headerView = navigationView.getHeaderView(0);
        toolbar= findViewById(R.id.toolbarprofile);
        ShowUserName=findViewById(R.id.Profile_name);
        ShowCFHandle=findViewById(R.id.Code_Forces_Handle_EditText);
        ShowEmail=findViewById(R.id.Profile_Email_editText);
        ShowMaxRating=findViewById(R.id.Profile_MaxRating_editText);
        ShowMaxRank=findViewById(R.id.Profile_MaxRank_editText);

        setSupportActionBar(toolbar);
        navigationView.setCheckedItem(R.id.nav_Profile);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_view_profile);
        mainAuth=FirebaseAuth.getInstance();
        mainfstore=FirebaseFirestore.getInstance();
        resetpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplication(),ResetPassword.class);
                startActivity(startIntent);
            }
        });
        addremovefrndbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplication(),AddOrRemoveFriends.class);
                startActivity(startIntent);
            }
        });
        if(dataupdated==false) GetAllTheUserData();
        //ShowUserName.setText("Tanima Hossain");
        //ShowCFHandle.setText(UserCFHandle);
        //ShowEmail.setText(UserEmail);
        //ShowMaxRating.setText(String.valueOf(UserMaxRating));
        //ShowMaxRating.setText("UserMaxRating");
        //ShowMaxRank.setText(UserMaxRank);
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_Profile:
                break;
            case R.id.nav_friendlist:
                //finish();
                Intent intent= new Intent(getApplication(),FriendList.class);
                startActivity(intent);
                finish();
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

    public void GetAllTheUserData(){
        MainUserID=mainAuth.getCurrentUser().getUid();
        DocumentReference documentReference=mainfstore.collection("users").document(MainUserID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    ShowUserName.setText(documentSnapshot.getString("name"));
                    UserName=ShowUserName.getText().toString();

                    ShowEmail.setText(documentSnapshot.getString("email"));
                    UserEmail=ShowEmail.getText().toString().trim();

                    ShowCFHandle.setText(documentSnapshot.getString("cfhandle"));
                    UserCFHandle= ShowCFHandle.getText().toString().trim();

                    ShowMaxRating.setText(String.valueOf(documentSnapshot.get("cfmaxrating")));
                    UserMaxRating=Integer.parseInt(ShowMaxRating.getText().toString().trim());

                    ShowMaxRank.setText(documentSnapshot.getString("cfmaxrank"));
                    UserMaxRank=ShowMaxRank.getText().toString();
                    frndlistfromfirestore= (ArrayList<String>) documentSnapshot.get("cffriends");
                    dataupdated=true;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,e.toString());
                ShowUserName.setText("Update Your Name");
                UserName="Update Your Name";
                ShowEmail.setText("Update Your CF Handle");
                UserCFHandle="Update Your CF Handle";
                ShowMaxRating.setText("0");
                UserMaxRating=0;
                ShowMaxRank.setText("N/A");
                UserMaxRank="N/A";
                //frndlistfromfirestore.clear();
                Log.d(TAG,e.toString());
            }
        });
        return;
    }
}