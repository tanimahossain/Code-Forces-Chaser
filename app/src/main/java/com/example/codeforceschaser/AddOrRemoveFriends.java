package com.example.codeforceschaser;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AddOrRemoveFriends extends AppCompatActivity {

    Button addfrndbtn,rmvfrndbtn;
    EditText addrmvfrndhandle;
    TextView DumpText;
    FirebaseAuth addrmvAuth;
    FirebaseFirestore addrmvfstore;
    ProgressBar addrmvprogbar;
    String UserID;
    static public ArrayList<String> frndlistfromfirestore= new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_add_or_remove_friends);
        addrmvfrndhandle=findViewById(R.id.AddorRemoveET);
        DumpText=findViewById(R.id.dumptext);
        addfrndbtn = findViewById(R.id.AddFriendButton);
        rmvfrndbtn = findViewById(R.id.RemoveFriendButton);
        addrmvprogbar=findViewById(R.id.AddorRemoveFriendProgressBar);
        addrmvAuth = FirebaseAuth.getInstance();
        addrmvfstore=FirebaseFirestore.getInstance();
        addfrndbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addrmvfrndhandle.getText().toString().isEmpty()){
                    addrmvfrndhandle.setError("Enter a handle");
                    return;
                } else if(frndlistfromfirestore.size()>19){
                    DumpText.setText("Max Limit can't anymore Friend");
                    return;
                }
                String friend=addrmvfrndhandle.getText().toString().trim();
                boolean flag=false;
                for(int i=0;i<frndlistfromfirestore.size();i++){
                    if(friend.toLowerCase().equals(frndlistfromfirestore.get(i).toLowerCase())){
                        flag=true; break;
                    }
                }
                if(flag){
                    String subs="";
                    if(frndlistfromfirestore.size()==0) subs="You have no friends :(";
                    else {
                        subs=friend+" is already your friend. Your current friends are: ";
                        subs += frndlistfromfirestore.get(0);
                        for(int i=1;i<frndlistfromfirestore.size();i++){
                            subs+=" , ";
                            subs+=frndlistfromfirestore.get(i);
                        }
                    }
                    DumpText.setText(subs);
                    return;
                }
                if(!flag){
                    frndlistfromfirestore.add(friend);
                    String subs="";
                    if(frndlistfromfirestore.size()==0) subs="You have no friends :(";
                    else {
                        subs=friend+" is added. Your current friends are: ";
                        subs += frndlistfromfirestore.get(0);
                        for(int i=1;i<frndlistfromfirestore.size();i++){
                            subs+=" , ";
                            subs+=frndlistfromfirestore.get(i);
                        }
                    }
                    DumpText.setText(subs);
                    return;
                }
                ///Firestore craps////
                frndlistfromfirestore.clear();
                Collections.copy(frndlistfromfirestore, getthefriendlist());
                //if(frndlistfromfirestore.size()==0||frndlistfromfirestore.size()>0) return;
                //Toast.makeText(AddOrRemoveFriends.this, frndlistfromfirestore.get(0),Toast.LENGTH_SHORT).show();
                //frndlistfromfirestore.remove(0);
                if(frndlistfromfirestore.size()>19){
                    Toast.makeText(AddOrRemoveFriends.this,"Max limit for adding friends is 20. Can't anymore.",Toast.LENGTH_SHORT).show();
                    return;
                }
                for(int i=0;i<frndlistfromfirestore.size();i++){
                    if(friend.toLowerCase().equals(frndlistfromfirestore.get(i).toLowerCase())){
                        flag=true; break;
                    }
                }
                if(flag){
                    Toast.makeText(AddOrRemoveFriends.this,"Friend already Exists.",Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,Object> datauser=new HashMap<>();
                frndlistfromfirestore.add(friend);
                datauser.putAll(getthewholeuserdata());
                datauser.put("friends",frndlistfromfirestore);
                String dataUserID=FirebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference documentReference= addrmvfstore.collection("users").document(dataUserID);
                documentReference.set(datauser).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Add: ","Error: "+e.toString());
                        Toast.makeText(AddOrRemoveFriends.this,"Couldn't save profile info."+e.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        rmvfrndbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addrmvfrndhandle.getText().toString().isEmpty()){
                    addrmvfrndhandle.setError("Enter a handle");
                    return;
                }
                String friend=addrmvfrndhandle.getText().toString().trim();
                boolean flag=true; int ind=0;
                for(int i=0;i<frndlistfromfirestore.size();i++){
                    if(friend.toLowerCase().equals(frndlistfromfirestore.get(i).toLowerCase())){
                        flag=false; ind=i; break;
                    }
                }
                if(flag){
                    String subs="";
                    if(frndlistfromfirestore.size()==0) subs="You have no friends :(";
                    else {
                        subs=friend+" is not your friend. Your current friends are: ";
                        subs += frndlistfromfirestore.get(0);
                        for(int i=1;i<frndlistfromfirestore.size();i++){
                            subs+=" , ";
                            subs+=frndlistfromfirestore.get(i);
                        }
                    }
                    DumpText.setText(subs);
                    return;
                }
                if(!flag){
                    frndlistfromfirestore.remove(ind);
                    String subs="";
                    if(frndlistfromfirestore.size()==0) subs="You have no friends :(";
                    else {
                        subs=friend+" is removed. Your current friends are: ";
                        subs += frndlistfromfirestore.get(0);
                        for(int i=1;i<frndlistfromfirestore.size();i++){
                            subs+=" , ";
                            subs+=frndlistfromfirestore.get(i);
                        }
                    }
                    DumpText.setText(subs);
                    return;
                }
                ///FirestoreCrap///
                frndlistfromfirestore.clear();
                Collections.copy(frndlistfromfirestore, getthefriendlist());
                //boolean flag=true; int ind=0;
                for(int i=0;i<frndlistfromfirestore.size();i++){
                    if(friend.toLowerCase()==frndlistfromfirestore.get(i).toLowerCase()){
                        flag=false; ind=i; break;
                    }
                }
                if(flag){
                    Toast.makeText(AddOrRemoveFriends.this,"Friend doesn't Exist.",Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,Object> datauser=new HashMap<>();
                frndlistfromfirestore.remove(ind);
                datauser.putAll(getthewholeuserdata());
                datauser.put("friends",frndlistfromfirestore);
                String dataUserID=FirebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference documentReference= addrmvfstore.collection("users").document(dataUserID);
                documentReference.set(datauser).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Remove: ","Error: "+e.toString());
                        Toast.makeText(AddOrRemoveFriends.this,"Couldn't save profile info."+e.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    Map<String,Object> getthewholeuserdata(){
        final Map<String,Object> temp=new HashMap<>();
        String UserID=addrmvAuth.getCurrentUser().getUid();
        DocumentReference documentReference=addrmvfstore.collection("users").document(UserID);
        //datauser.put("name",CFhandle);
        //datauser.put("email",email);
        //datauser.put("cfhandle",CFhandle);
        //datauser.put("cfmaxrating",0);
        //datauser.put("cfmaxrank","Newbie");
        //datauser.put("friends",frndlistfromfirestore);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    //temp.put("name",documentSnapshot.getString("name"));
                    DumpText.setText(documentSnapshot.getString("name"));
                    temp.put("name",DumpText.getText().toString().trim());

                    //temp.put("email",documentSnapshot.getString("email"));
                    DumpText.setText(documentSnapshot.getString("email"));
                    temp.put("email",DumpText.getText().toString().trim());

                    //temp.put("cfhandle",documentSnapshot.getString("cfhandle"));
                    DumpText.setText(documentSnapshot.getString("cfhandle"));
                    temp.put("cfhandle",DumpText.getText().toString().trim());

                    //temp.put("cfmaxrating",Integer.parseInt(String.valueOf(documentSnapshot.get("cfmaxrating"))));
                    DumpText.setText(String.valueOf(documentSnapshot.get("cfmaxrating")));
                    temp.put("cfmaxrating",Integer.parseInt(DumpText.getText().toString().trim()));

                    //temp.put("cfmaxrank",documentSnapshot.getString("cfmaxrank"));
                    DumpText.setText(documentSnapshot.getString("cfmaxrank"));
                    temp.put("cfmaxrank",DumpText.getText().toString().trim());

                    temp.put("friends",(ArrayList<String>) documentSnapshot.get("friends"));
                    //DumpText.setText(String.valueOf((ArrayList<String>) documentSnapshot.get("friends")));
                    //temp.put("friends",(ArrayList<String>) DumpText.getText().toString());
                }else{
                    Log.d("No data Available","Error: "+task.getException().getMessage());
                }
            }
        });
        return temp;
    }
    public ArrayList<String> getthefriendlist(){
        ///FireBase Craps///
        UserID=addrmvAuth.getCurrentUser().getUid();
        final ArrayList<String> tmp=new ArrayList<String>(); tmp.clear();
        //addrmvfrndhandle.setText(UserID);
        DocumentReference documentReference=addrmvfstore.collection("users").document(UserID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    tmp.add(documentSnapshot.getString("cfhandle"));
                    Collections.copy(frndlistfromfirestore,(ArrayList<String>) documentSnapshot.get("friends"));
                    for(int i=0;i<frndlistfromfirestore.size();i++){
                        tmp.add(frndlistfromfirestore.get(i));
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                tmp.clear();
                Log.d("Get FriendList: ",e.toString());
            }
        });
        return tmp;
    }
}