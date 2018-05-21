package com.afiq.ezsmartpark1;

/**
 * Created by Asus on 26/11/2017.
 */
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



public class AuthorityRegister extends AppCompatActivity implements
        View.OnClickListener {

    private ProgressDialog pd;

    private static final String TAG = "EmailPassword";

    private TextView mStatusTextView,mpin,mpin2,mpin3,mpin4;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mRepasswordField;
    private EditText mPinCode;

    final Context context = this;
    private FirebaseAuth mAuth;
    Button b1,b2;

    private FirebaseAuth mauths;
    DatabaseReference rootRef,demoRef,demo3,dbPark,demoRef1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_register);

        mauths = FirebaseAuth.getInstance();
        FirebaseUser user = mauths.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        demoRef = rootRef.child("Authority");
        demo3 = rootRef.child("pin");


        mpin = (TextView) findViewById(R.id.tv_pin);
        mpin2 = (TextView) findViewById(R.id.tv_pin2);
        mpin3 = (TextView) findViewById(R.id.tv_pin3);
        mpin4 = (TextView) findViewById(R.id.tv_pin4);
        mStatusTextView = (TextView) findViewById(R.id.authstatus);
        mDetailTextView = (TextView) findViewById(R.id.authdetail);
        mEmailField = (EditText) findViewById(R.id.authemailregtext);
        mPasswordField = (EditText) findViewById(R.id.authpasswordregtext);
        mRepasswordField = (EditText) findViewById(R.id.authrepasswordregtext);
        mPinCode = (EditText) findViewById(R.id.et_authpincode);
        b1=(Button)findViewById(R.id.authbutton4);
        b2=(Button)findViewById(R.id.authagree);



        final Context context = this;
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, AuthorityInterface.class);
                startActivity(intent);

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AuthorityTerms.class);
                startActivity(intent);

            }
        });

        // Buttons
        //findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.authnextbutton).setOnClickListener(this);


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

    private void createAccount(String email, String password) {
        pd = new ProgressDialog(this,R.style.MyTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            pd.dismiss();
            return;
        }



        mDetailTextView.setText(R.string.firebase_status_fmt);

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
                            demoRef.child("/" + uid + "/").child("Phone Number").setValue("Update your account");
                            demoRef.child("/" + uid + "/").child("Name").setValue("Update your account");




                            demoRef.child("/"+uid+"/").child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String value = dataSnapshot.getValue(String.class);

                                    if (value !=null){

                                       // b1.setVisibility(View.VISIBLE);
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
                            Toast.makeText(AuthorityRegister.this, "Welcome! Don't Forget to Update your account",
                                    Toast.LENGTH_LONG).show();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(AuthorityRegister.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            pd.dismiss();
                        }


                    }
                });

    }




    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }


    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
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


        String pincode = mPinCode.getText().toString();
        if (TextUtils.isEmpty(pincode)) {
            mPinCode.setError("Required.");
            valid = false;
        }

        if (!TextUtils.isEmpty(pincode) && !pincode.equals("123456")) {

            mPinCode.setError("Wrong pin");
            valid = false;
        }

        if (pincode.equals("123456")) {

            mPinCode.setError(null);

        }




        return valid;
    }




    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {
            mStatusTextView.setText(getString(emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            Intent intent = new Intent(context, AuthorityInterface.class);
            startActivity(intent);


        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);


        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.authnextbutton) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());

            //} else if (i == R.id.email_sign_in_button) {
            //signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }


    }


    @Override
    public void onBackPressed(){
        Intent backMainTest = new Intent(this,AuthorityInterface.class);
        startActivity(backMainTest);
        finish();
    }





}