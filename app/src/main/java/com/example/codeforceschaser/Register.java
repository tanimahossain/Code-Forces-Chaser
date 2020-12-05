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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;


public class Register extends AppCompatActivity {

    EditText reghandle,regemail,regpassword,regconfirmpassword;
    Button regbutton;
    TextView logintext,forgetpasstext;
    FirebaseAuth regAuth;
    ProgressBar regprogbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_register);
        reghandle= findViewById(R.id.registercfhandleet);
        regemail= findViewById(R.id.registeremailet);
        regpassword= findViewById(R.id.registerpasswordet);
        regconfirmpassword= findViewById(R.id.registerconfirmpassword);
        regbutton= findViewById(R.id.registerbutton);
        logintext= findViewById(R.id.registertologintw);
        forgetpasstext= findViewById(R.id.registerforgotpasswordtw);
        regAuth=FirebaseAuth.getInstance();
        regprogbar=findViewById(R.id.registerprogressBar);

        if(regAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        regbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email=regemail.getText().toString().trim();
                String password=regpassword.getText().toString().trim();
                String confirmpassword=regconfirmpassword.getText().toString().trim();
                String CFhandle=reghandle.getText().toString().trim();
                if(TextUtils.isEmpty(CFhandle)){
                    reghandle.setError("Code Forces Handle is required");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    regemail.setError("Email is required");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    regemail.setError("Please enter a valid email");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    regpassword.setError("Password is required");
                    return;
                }
                if(TextUtils.isEmpty(confirmpassword)){
                    regconfirmpassword.setError("You must confirm the password");
                    return;
                }
                if(password.length()<6){
                    regpassword.setError("Password can't be less than 6 characters");
                    return;
                }
                if(password.equals(confirmpassword)==false){
                    regconfirmpassword.setError("Passwords doesn't match");
                    return;
                }
                regprogbar.setVisibility(View.VISIBLE);
                regAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = regAuth.getCurrentUser();
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Register.this,"Successfully Registered. A verification Email has been sent. Please verify your Email.",Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register.this,"Couldn't send the email.",Toast.LENGTH_LONG).show();
                                }
                            });
                            //Toast.makeText(Register.this,"Successfully Registered",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Register.this, LogIn.class));
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(Register.this,"Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                            }
                            regprogbar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }
    public void GoToLogin(View v){
        Intent startIntent = new Intent(getApplication(),LogIn.class);
        startActivity(startIntent);
        finish();
    }
    public void GoToReset(View v){
        Intent startIntent = new Intent(getApplication(),ResetPassword.class);
        startActivity(startIntent);
    }
}