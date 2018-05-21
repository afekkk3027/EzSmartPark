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


public class UserInterface extends AppCompatActivity implements
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
        setContentView(R.layout.activity_user_interface);



        // Views
        mStatusTextView = (TextView) findViewById(R.id.status123);
        mDetailTextView = (TextView) findViewById(R.id.detail123);
        mEmailField = (EditText) findViewById(R.id.emailfield);
        mPasswordField = (EditText) findViewById(R.id.paswordfield);

        // Buttons
        findViewById(R.id.loginbutton).setOnClickListener(this);
        //findViewById(R.id.email_create_account_button).setOnClickListener(this);
        //findViewById(R.id.sign_out_button).setOnClickListener(this);
        //findViewById(R.id.verify_email_button).setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        mauths = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();


        //b1=(Button)findViewById(R.id.closebutton);
        b2=(Button)findViewById(R.id.registerlink);
        //b3=(Button)findViewById(R.id.closebutton);
        //tx1=(TextView)findViewById(R.id.textView3);
        //tx1.setVisibility(View.GONE);


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UserRegister.class);
                startActivity(intent);

            }
        });

        //b3.setOnClickListener(new View.OnClickListener() {
           // @Override
            //public void onClick(View v) {

                //Intent intent = new Intent(context, MainActivity1.class);
               // startActivity(intent);


           // }
       // });

    }


    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        updateUI1(currentUser);
      // updateUI(currentUser);

    }
    // [END on_start_check_user]



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
                            updateUI1(user);
                            //Intent intent = new Intent(context, UserMenu.class);
                            //startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(UserInterface.this, "Authentication failed.",
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


    private void updateUI(FirebaseUser user) {


            final String uid;
            uid = user.getUid();
            dbPark = rootRef.child("User");//.child("/"+uid+"/").child("Plate Number");

            dbPark.child("/" + uid + "/").child("Plate Number").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String valueplate = dataSnapshot.getValue(String.class);

                    if (!valueplate.equals("NoPark")) {

                        Intent intent = new Intent(context, UserActive.class);
                        startActivity(intent);
                        //  return;


                    }
                    else if (valueplate.equals("NoPark")) {

                        Intent intent = new Intent(context, UserMenu.class);
                        startActivity(intent);

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });




    }

    private void updateUI1(FirebaseUser user) {
        //hideProgressDialog();

        if (user != null) {


            checkUI(user);
            FirebaseUser currentUser = mAuth.getCurrentUser();
           // updateUI(currentUser);
            Intent intent = new Intent(context, UserMenu.class);
            startActivity(intent);
            // mStatusTextView.setText("signed in");
            return;


        }

        if (user == null) {




        }
    }


/*


    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();

      //  FirebaseUser user1 = mauths.getCurrentUser();
       // final String uid;
       // uid = user1.getUid();

      // dbPark = rootRef.child("User");
        //dbPark = rootRef;


        if (user != null) {

        //    dbPark.child("/"+uid+"/").child("Plate Number").addListenerForSingleValueEvent(new ValueEventListener() {
          //      @Override
            //    public void onDataChange(DataSnapshot dataSnapshot) {
              //      String parkvalue = dataSnapshot.getValue(String.class);
                //    if (parkvalue != null) {
//
  //                      Intent intent = new Intent(context, UserActive.class);
    //                    startActivity(intent);
      //              }

        //            else if (parkvalue == null) {
                      //  FirebaseUser user = mauths.getCurrentUser();
                      //  final String uid;
                     //   uid = user.getUid();

                        mStatusTextView.setText(getString(emailpassword_status_fmt,
                                user.getEmail(), user.isEmailVerified()));
                        mDetailTextView.setText(getString(R.string.firebase_status1_fmt, user.getUid()));
                        Intent intent = new Intent(context, UserMenu.class);
                        startActivity(intent);
          //          }

            //    }
              //  @Override
                //public void onCancelled(DatabaseError databaseError) {
                //}
            //});



        }
        else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);
            // findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
    } */


    @Override
    public void onClick(View v) {
        int i = v.getId();
        //if (i == R.id.email_create_account_button) {
            //createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        //}
        if (i == R.id.loginbutton) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
        //else if (i == R.id.sign_out_button) {
            //signOut();
        //}
        //else if (i == R.id.verify_email_button) {
            //sendEmailVerification();
        //}
    }





    private void checkUI(FirebaseUser user) {
        //hideProgressDialog();


        if (user != null) {

            mAuth = FirebaseAuth.getInstance();
            rootRef = FirebaseDatabase.getInstance().getReference();
            dbPark = rootRef.child("User");
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
                        Toast.makeText(UserInterface.this, "User personnel only.",
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

            //  String checkemails = mcheck.getText().toString();
            //  String checkemails2 = mcheck2.getText().toString();




            return;


        }

        if (user == null) {

            mAuth.signOut();
            Toast.makeText(UserInterface.this, "User personnel only.",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, MainActivity1.class);
            startActivity(intent);


        }
    }









    @Override
    public void onBackPressed(){
        Intent backMainTest = new Intent(this,MainActivity1.class);
        startActivity(backMainTest);
        finish();
    }





}