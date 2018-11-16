package com.example.christiansoltero.fireprojectlogin;

import android.media.MediaPlayer;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    String TAG = "FirebaseTest";

    FirebaseApp mApp;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    TextView mDisplayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG,"Now is the winter of our discontent");
        mDisplayText = (TextView)findViewById(R.id.displayText);
        mDisplayText.setText("Unknown Auth State");


        initFirebase();
//        registerUser();
//        logOut();
        loginUser();
//        logOut();
//        readDatabaseData();
//        writeDatabaseData();
//        writeobject();
//        readObjects();
    }

    private void initFirebase(){
        mApp = FirebaseApp.getInstance();
        mDatabase = FirebaseDatabase.getInstance(mApp);
        mAuth = FirebaseAuth.getInstance(mApp);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null){
                    Log.e(TAG,"User is logged in: " + user.getEmail().toString());
                }else{
                    Log.e(TAG,"No user logged in");
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void readDatabaseData() {

        DatabaseReference ref = mDatabase.getReference("chatMessages");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "Snapshot received: Children " + dataSnapshot.getChildrenCount() + " key: " + dataSnapshot.getKey().toString() + " value: " + dataSnapshot.getValue().toString());

                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Log.e(TAG, "Snapshot recieved: Grandchildren " + child.getChildrenCount() + " key: " + dataSnapshot.getKey().toString() + "value: " + dataSnapshot.getValue().toString());
                    for(DataSnapshot grandchild : child.getChildren()){
                        Log.e(TAG, "Snapshot recieved: Great-Grandchildren " + child.getChildrenCount() + " key: " + dataSnapshot.getKey().toString() + "value: " + dataSnapshot.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        ref.addValueEventListener(eventListener);

    }

    private void writeDatabaseData(){
        DatabaseReference ref = mDatabase.getReference("chatMessages").child("Andrew 12:43:23 05-25-2018");
        ref.child("sendTime").setValue("12:43:23 05-25-2018");
        ref.child("sender").setValue("Andrew");
        ref.child("chatMessages").setValue("Today is Friday and it's sunny");
    }

    private void writeobject(){
        ChatMessage msg = new ChatMessage();
        msg.sender = "Sheila";
        msg.sendTime = "11:26:17 07-22-2018";
        msg.chatMessage = "Pull my finger";
        DatabaseReference ref = mDatabase.getReference("chatMessages").child("Sheila 11:26:17 07-22-2018");
        ref.setValue(msg);
    }

    private void readObjects(){
        DatabaseReference ref = mDatabase.getReference("chatMessages");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG,"Data Recieved: " + dataSnapshot.getChildrenCount());
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    ChatMessage msg = new ChatMessage();
                    msg = child.getValue(ChatMessage.class);
                    Log.e(TAG, "Child: " + msg.chatMessage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        ref.addValueEventListener(listener);
    }
    private void registerUser() {

        OnCompleteListener<AuthResult> complete = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Log.e(TAG, "User registered ");
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "The user has been registered",
                            Toast.LENGTH_SHORT);

                    toast.show();
                }
                else {
                    Log.e(TAG, "User registration response, but failed ");
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "The user registration has failed",
                            Toast.LENGTH_SHORT);

                    toast.show();
                }
            }
        };

        OnFailureListener failure = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"Register user failure");
                Toast toast = Toast.makeText(getApplicationContext(),
                        "The user registration has failed",
                        Toast.LENGTH_SHORT);

                toast.show();
            }
        };

        String email = "put an email here";
        String password = "put a password here";

        Log.e(TAG, "Registering : eMail " + email + " password " + password);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(complete).addOnFailureListener(failure);
    }

    private void logout(){
        mAuth.signOut();
    }
    private void loginUser(){

        OnCompleteListener<AuthResult> complete = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Log.e(TAG, "User logged on");
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "The user has logged on",
                            Toast.LENGTH_SHORT);

                    toast.show();
                }
                else {
                    Log.e(TAG, "User log on response, but failed ");
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "The user log on has failed",
                            Toast.LENGTH_SHORT);

                    toast.show();
                }
            }
        };

        OnFailureListener failure = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"Log on user failure");
                Toast toast = Toast.makeText(getApplicationContext(),
                        "The user log on has failed",
                        Toast.LENGTH_SHORT);

                toast.show();
            }
        };

        String email = "put an email here";
        String password = "put a password here";

        Log.e(TAG, "Logging in : eMail " + email + " password " + password);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(complete).addOnFailureListener(failure);

    }
}
