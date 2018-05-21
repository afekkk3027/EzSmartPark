package com.afiq.ezsmartpark1;

/**
 * Created by Asus on 15/12/2017.
 */


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class AuthorityAccount extends AppCompatActivity {//} implements View.OnClickListener {
    Button b1,b2,b3;
    private FirebaseAuth mauth;
    private TextView mEmail;
    DatabaseReference rootRef,demoRef,demoRef2;
    TextView nama, phone,balance,bal2;
    EditText balancecredit;

    double num1,num2,sum;

    final Context context = this;

    double _count;
    DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_account);

        bal2 = (TextView) findViewById(R.id.tv_bal1);


        nama = (TextView) findViewById(R.id.usernamebox1);
        phone = (TextView) findViewById(R.id.userphonebox1);
        mauth = FirebaseAuth.getInstance();
        FirebaseUser user = mauth.getCurrentUser();

        b2=(Button)findViewById(R.id.userupdatebutton1);

        rootRef = FirebaseDatabase.getInstance().getReference();
        final Context context = this;
        final String email,uid;

        uid=user.getUid();
        demoRef = rootRef.child("Authority");


        if(user!=null) {



            email = user.getEmail();
            mEmail = (TextView) findViewById(R.id.useremailupdateview1);
            mEmail.setText(email);
        }

        demoRef.child("/"+uid+"/").child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                nama.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        demoRef.child("/"+uid+"/").child("Phone Number").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                phone.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AuthorityUpdate.class);
                startActivity(intent);

            }
        });




    }





    @Override
    public void onBackPressed(){
        Intent backMainTest = new Intent(this,AuthorityMenu.class);
        startActivity(backMainTest);
        finish();
    }


}