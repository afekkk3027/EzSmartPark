package com.afiq.ezsmartpark1;

/**
 * Created by Asus on 16/10/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.app.Activity;
import android.view.View.OnClickListener;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.afiq.ezsmartpark1.R.string.emailpassword_status_fmt;


public class AuthorityInterface extends AppCompatActivity implements
        View.OnClickListener {
    Button b2,b3;


    final Context context = this;

    private static final String TAG = "EmailPassword";

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    // [START declare_auth]
    private FirebaseAuth mAuth,mauths;
    // [END declare_auth]
    DatabaseReference dbPark,rootRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_interface);



        // Views
        mStatusTextView = (TextView) findViewById(R.id.authstatus123);
        mDetailTextView = (TextView) findViewById(R.id.authdetail123);
        mEmailField = (EditText) findViewById(R.id.authemailfield);
        mPasswordField = (EditText) findViewById(R.id.authpaswordfield);

        // Buttons
        findViewById(R.id.authloginbutton).setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        mauths = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();



        b2=(Button)findViewById(R.id.authregisterlink);



        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AuthorityRegister.class);
                startActivity(intent);

            }
        });



    }


    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = mAuth.getCurrentUser();



    }




    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            checkUI(user);
                           // checkUI(user);
                            Intent intent = new Intent(context, AuthorityMenu.class);
                            startActivity(intent);
                            //updateUI1(user);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(AuthorityInterface.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI1(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                        //              hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }



    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }





    private void updateUI1(FirebaseUser user) {
        //hideProgressDialog();

        if (user != null) {

            FirebaseUser currentUser = mAuth.getCurrentUser();
            // updateUI(currentUser);
            Intent intent = new Intent(context, AuthorityMenu.class);
            startActivity(intent);
            // mStatusTextView.setText("signed in");
            return;


        }

        if (user == null) {




        }
    }







    private void checkUI(FirebaseUser user) {
        //hideProgressDialog();


        if (user != null) {

            mAuth = FirebaseAuth.getInstance();
            rootRef = FirebaseDatabase.getInstance().getReference();
            dbPark = rootRef.child("Authority");
            final String uid, emailss;
            //  FirebaseUser user = mAuth.getCurrentUser();
            uid = user.getUid();
            emailss = user.getEmail();
            //  mcheck.setText(email);

            dbPark.child("/" + uid + "/").child("Email").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value123 = dataSnapshot.getValue(String.class);
                    // mcheck2.setText(value);

                    if (!emailss.equals(value123)) {

                        mAuth.signOut();
                        Toast.makeText(AuthorityInterface.this, "authorized personnel only.",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity1.class);
                        startActivity(intent);

                    }

                    if(emailss.equals(value123)){



                    }


                    // tv_creditbal.setText(value);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


            return;


        }

        if (user == null) {

            mAuth.signOut();
            Toast.makeText(AuthorityInterface.this, "authorized personnel only.",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, MainActivity1.class);
            startActivity(intent);


        }
    }





    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.authloginbutton) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }

    }

    @Override
    public void onBackPressed(){

        Intent backMainTest = new Intent(this,MainActivity1.class);
        startActivity(backMainTest);
        finish();
    }





}