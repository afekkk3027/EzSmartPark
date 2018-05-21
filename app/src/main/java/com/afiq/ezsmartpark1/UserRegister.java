package com.afiq.ezsmartpark1;

/**
 * Created by Asus on 16/10/2017.
 */



import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.content.Context;
import android.widget.Toast;

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

import java.math.RoundingMode;

import static com.afiq.ezsmartpark1.R.string.emailpassword_status_fmt;



public class UserRegister extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private ProgressDialog pd;

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mRepasswordField;

    final Context context = this;
    private FirebaseAuth mAuth;
    Button b1,b2;

    private FirebaseAuth mauths;
    DatabaseReference rootRef,demoRef,dbPark,demoRef1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        mauths = FirebaseAuth.getInstance();
        FirebaseUser user = mauths.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        demoRef = rootRef.child("User");




        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);
        mEmailField = (EditText) findViewById(R.id.emailregtext);
        mPasswordField = (EditText) findViewById(R.id.passwordregtext);
        mRepasswordField = (EditText) findViewById(R.id.repasswordregtext);
        b1=(Button)findViewById(R.id.button4);
        b2=(Button)findViewById(R.id.agree);

       // ProgressDialog dialog = ProgressDialog.show(UserRegister.this, "",
               // "Loading. Please wait...", true);

        final Context context = this;
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, UserInterface.class);
                startActivity(intent);

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UserTerms.class);
                startActivity(intent);

            }
        });

        // Buttons
        //findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.nextbutton).setOnClickListener(this);
        //findViewById(R.id.sign_out_button).setOnClickListener(this);
        //findViewById(R.id.verify_email_button).setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        //mAuth.signOut();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mDetailTextView.setText(null);
        //updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void createAccount(final String email, String password) {
        pd = new ProgressDialog(this,R.style.MyTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            pd.dismiss();
            return;
        }

        //showProgressDialog();

        // [START create_user_with_email]

       // mDetailTextView.setText(R.string.firebase_status_fmt);


        mAuth.createUserWithEmailAndPassword(email, password)



                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");





                            mauths = FirebaseAuth.getInstance();
                            FirebaseUser user = mauths.getCurrentUser();
                            final String uid,email;
                            uid = user.getUid();
                            email = user.getEmail();


                            demoRef.child("/" + uid + "/").child("Email").setValue(email);
                            demoRef.child("/" + uid + "/").child("Plate Number").setValue("NoPark");
                            demoRef.child("/" + uid + "/").child("Time").setValue(null);
                            demoRef.child("/" + uid + "/").child("Locality").setValue(null);
                            demoRef.child("/" + uid + "/").child("Area").setValue(null);
                            demoRef.child("/" + uid + "/").child("City").setValue(null);
                            demoRef.child("/" + uid + "/").child("Address").setValue(null);
                            demoRef.child("/" + uid + "/").child("Date").setValue(null);
                            demoRef.child("/" + uid + "/").child("Balance").setValue("0");
                            demoRef.child("/" + uid + "/").child("Phone Number").setValue("Update your account");
                            demoRef.child("/" + uid + "/").child("Name").setValue("Update your account");




                            demoRef.child("/"+uid+"/").child("Balance").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String value = dataSnapshot.getValue(String.class);

                                    if (value !=null){

                                        //b1.setVisibility(View.VISIBLE);
                                    }
                                    // tv_creditbal.setText(value);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });


                          //  FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            pd.dismiss();
                            Toast.makeText(UserRegister.this, "Welcome! Don't Forget to Update your account",
                                    Toast.LENGTH_LONG).show();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(UserRegister.this, "Authentication failed. Invalid email address!",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            pd.dismiss();
                        }

                        // [START_EXCLUDE]
                        //              hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
       // b1.setVisibility(View.VISIBLE);
    }




    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

        // [START sign_in_with_email]
       /* mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(UserRegister.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                        //              hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });*/
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    /*private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.verify_email_button).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(EmailPasswordActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(EmailPasswordActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }*/

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailField.setError("enter a valid email address");
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

        if (password.isEmpty() || password.length() < 6 || password.length() > 100) {
            mPasswordField.setError("at least 6 characters");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }



        String repassword = mRepasswordField.getText().toString();
        if (repassword.equals(password)) {

            mRepasswordField.setError(null);

        }
        else {
            mRepasswordField.setError("Passwords do not match");
            valid = false;
        }





        return valid;
    }

    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {
            mStatusTextView.setText(getString(emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            Intent intent = new Intent(context, UserInterface.class);
            startActivity(intent);

           // findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
          //  findViewById(R.id.email_password_fields).setVisibility(View.GONE);
           // findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);

            //findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

           // findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
           // findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            //findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.nextbutton) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());

        //} else if (i == R.id.email_sign_in_button) {
            //signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
        //else if (i == R.id.button4) {
            //Intent intent = new Intent(context, UserPark.class);
           // startActivity(intent);
       // } else if (i == R.id.verify_email_button) {
          //  sendEmailVerification();

       //}

    }


    @Override
    public void onBackPressed() {

        Intent backMainTest = new Intent(this, UserInterface.class);
        startActivity(backMainTest);
        finish();

    }





}