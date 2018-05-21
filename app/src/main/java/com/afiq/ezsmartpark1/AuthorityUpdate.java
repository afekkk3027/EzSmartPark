package com.afiq.ezsmartpark1;

/**
 * Created by Asus on 15/12/2017.
 */


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;


public class AuthorityUpdate extends AppCompatActivity {
    Button b1;
    private FirebaseAuth mauths;
    private TextView mEmail;
    DatabaseReference rootRef,demoRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_update);


        mauths = FirebaseAuth.getInstance();
        FirebaseUser user = mauths.getCurrentUser();

        final String email, uid;
        uid = user.getUid();
        email = user.getEmail();
        if (user == null) {
            Toast.makeText(AuthorityUpdate.this, "X SignIn lagi ni ! Hmmm...",
                    Toast.LENGTH_SHORT).show();
        }
        //final String key = FirebaseDatabase.getInstance().getReference().child(uid).push().getKey();
        final EditText names = (EditText) findViewById(R.id.updatenamebox1);
        final EditText phones = (EditText) findViewById(R.id.updatephonetext1);
        b1 = (Button) findViewById(R.id.updateprofilebutton1);
        //  String nama = names.getText().toString();
        //  String mobile = phones.getText().toString();

        mEmail = (TextView) findViewById(R.id.useremailupdateview1);
        mEmail.setText(email);


        rootRef = FirebaseDatabase.getInstance().getReference();
        final Context context = this;
        demoRef = rootRef.child("Authority");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = names.getText().toString();
                String value1 = phones.getText().toString();


                //push creates a unique id in database
                demoRef.child("/" + uid + "/").child("Name").setValue(value);
                demoRef.child("/" + uid + "/").child("Phone Number").setValue(value1);
                demoRef.child("/"+uid+"/").child("Email").setValue(email);
                Intent intent = new Intent(context, AuthorityAccount.class);
                startActivity(intent);


            }
        });


    }



















    @Override
    public void onBackPressed(){
        Intent backMainTest = new Intent(this,AuthorityAccount.class);
        startActivity(backMainTest);
        finish();
    }

}