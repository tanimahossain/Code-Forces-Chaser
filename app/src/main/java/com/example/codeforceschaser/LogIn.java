package com.example.codeforceschaser;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class LogIn extends AppCompatActivity {

    EditText loginemail,loginpassword;
    Button loginbutton;
    TextView registertext,loginforgetpasstext;
    FirebaseAuth loginAuth;
    ProgressBar loginprogbar;
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

        loginAuth=FirebaseAuth.getInstance();
        loginprogbar=findViewById(R.id.loginprogressBar);

        if(loginAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        loginbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email=loginemail.getText().toString().trim();
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
                                return;
                            }
                            Toast.makeText(LogIn.this,"Logged In Successfully",Toast.LENGTH_SHORT).show();
                            loginprogbar.setVisibility(View.INVISIBLE);
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
}