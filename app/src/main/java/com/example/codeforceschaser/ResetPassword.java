package com.example.codeforceschaser;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    Button resetpassbtn;
    EditText resetemail;
    FirebaseAuth resetAuth;
    ProgressBar resetpassprogbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_reset_password);
        resetemail=findViewById(R.id.editTextTextEmailAddress);
        resetpassbtn = findViewById(R.id.ResetPassButton);
        resetpassprogbar=findViewById(R.id.ResetPasswordProgressBar);
        resetAuth = FirebaseAuth.getInstance();
        resetpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = resetemail.getText().toString().trim();
                resetpassprogbar.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    resetpassprogbar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "Reset password instructions has sent to your email",Toast.LENGTH_SHORT).show();
                                } else {
                                    resetpassprogbar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(),"Email don't exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        resetpassprogbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        resetpassprogbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                resetpassprogbar.setVisibility(View.INVISIBLE);
            }
        });
    }
}