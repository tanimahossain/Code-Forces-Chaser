package com.example.codeforceschaser;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class LogIn extends AppCompatActivity {

    EditText loginemail,loginpassword;
    Button loginbutton;
    TextView registertext,loginforgetpasstext,navname,navhandle,navemail;
    FirebaseAuth loginAuth;
    FirebaseFirestore loginfstore;
    ProgressBar loginprogbar;
    String LoginUserID,UserName,UserCFHandle,UserEmail,UserMaxRank;
    Integer UserMaxRating;
    ArrayList<String> frndlistfromfirestore= new ArrayList<String>();
    boolean dataupdated=false;
    final public String TAG="Login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_log_in);

        loginemail= findViewById(R.id.loginemailet);
        loginpassword= findViewById(R.id.loginpasswordet);
        loginbutton= findViewById(R.id.loginbutton);
        registertext= findViewById(R.id.logintoregistertw);
        loginforgetpasstext= findViewById(R.id.loginforgotpasswordtw);
        navname= findViewById(R.id.navprofilename);
        navhandle= findViewById(R.id.navcfhandle);
        navemail= findViewById(R.id.navemail);

        loginAuth=FirebaseAuth.getInstance();
        loginprogbar=findViewById(R.id.loginprogressBar);

        if(loginAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        loginbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String email=loginemail.getText().toString().trim();
                String password=loginpassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    loginemail.setError("Email is required");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    loginemail.setError("Please enter a valid email");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    loginpassword.setError("Password is required");
                    return;
                }
                if(password.length()<6){
                    loginpassword.setError("Password can't be less than 6 characters");
                    return;
                }
                loginprogbar.setVisibility(View.VISIBLE);
                loginAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user=loginAuth.getCurrentUser();
                            if(!user.isEmailVerified()){
                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(LogIn.this,"Please Verify the email. A verification mail has been sent to your email.",Toast.LENGTH_LONG).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LogIn.this,"Couldn't send the email.",Toast.LENGTH_LONG).show();
                                    }
                                });
                                FirebaseAuth.getInstance().signOut();
                                finish();
                                startActivity(new Intent(LogIn.this, LogIn.class));
                                finish();
                            }
                            Toast.makeText(LogIn.this,"Logged In Successfully",Toast.LENGTH_SHORT).show();
                            loginprogbar.setVisibility(View.INVISIBLE);
                            UserEmail=email;
                            UserName="Update Your Name";
                            UserCFHandle="Update Your CF Handle";
                            UserMaxRating=0;
                            UserMaxRank="N/A";
                            startActivity(new Intent(LogIn.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LogIn.this,"Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            loginprogbar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }
    public void GoToRegister(View v){
        Intent startIntent = new Intent(getApplication(),Register.class);
        startActivity(startIntent);
        finish();
    }
    public void GoToProfile(View v){
        Intent startIntent = new Intent(getApplication(),MainActivity.class);
        startActivity(startIntent);
    }
    public void GoToReset(View v){
        Intent startIntent = new Intent(getApplication(),ResetPassword.class);
        startActivity(startIntent);
    }
    public void GetAllTheUserData(){

        LoginUserID=loginAuth.getCurrentUser().getUid();
        DocumentReference documentReference=loginfstore.collection("users").document(LoginUserID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    UserEmail=documentSnapshot.getString("email");
                    UserName=documentSnapshot.getString("name");
                    UserCFHandle=documentSnapshot.getString("cfhandle");
                    UserMaxRating=(Integer)documentSnapshot.get("cfmaxrating");
                    UserMaxRank=documentSnapshot.getString("cfmaxrank");
                    //frndlistfromfirestore= (ArrayList<String>) documentSnapshot.get("cffriends");
                    navemail.setText(UserEmail);
                    navhandle.setText(UserCFHandle);
                    navname.setText(UserName);
                    dataupdated=true;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,e.toString());
                UserName="Update Your Name";
                UserCFHandle="Update Your CF Handle";
                UserMaxRating=0;
                UserMaxRank="N/A";
                //frndlistfromfirestore.clear();
                navemail.setText(UserEmail);
                navhandle.setText(UserCFHandle);
                navname.setText(UserName);
                Log.d(TAG,e.toString());
            }
        });
        return;
    }
    public String getUserEmail(){
        return UserEmail;
    }
    public String getUserName(){
        return UserName;
    }
    public String getUserCFHandle(){
        return UserCFHandle;
    }
    public Integer getUserMaxRating(){
        return UserMaxRating;
    }
    public String getUserMaxRank(){
        return UserMaxRank;
    }
    //public ArrayList<String> getUserFriedList(){
        //return frndlistfromfirestore;
    //}
    public void clearall(){
        UserEmail="";
        UserName="";
        UserCFHandle="";
        UserMaxRating=0;
        UserMaxRank="";
        dataupdated=false;
        //frndlistfromfirestore.clear();
        return;
    }
}