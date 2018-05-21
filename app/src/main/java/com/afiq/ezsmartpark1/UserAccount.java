package com.afiq.ezsmartpark1;

/**
 * Created by Asus on 16/10/2017.
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

public class UserAccount extends AppCompatActivity {//} implements View.OnClickListener {
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
        setContentView(R.layout.activity_user_account);

        bal2 = (TextView) findViewById(R.id.tv_bal);
        balancecredit = (EditText) findViewById(R.id.balanceview);
        balance = (TextView) findViewById(R.id.tv_balance);
        nama = (TextView) findViewById(R.id.usernamebox);
        phone = (TextView) findViewById(R.id.userphonebox);
        mauth = FirebaseAuth.getInstance();
        FirebaseUser user = mauth.getCurrentUser();
        b1=(Button)findViewById(R.id.topupbutton);
        b2=(Button)findViewById(R.id.userupdatebutton);
        b3=(Button)findViewById(R.id.confirmtopup);
        rootRef = FirebaseDatabase.getInstance().getReference();
        final Context context = this;
        final String email,uid;

        uid=user.getUid();
        demoRef = rootRef.child("User");
      //  demoRef2 = rootRef.child("User").child("/"+uid+"/").child("Balance");

        if(user!=null) {



            email = user.getEmail();
            mEmail = (TextView) findViewById(R.id.useremailupdateview);
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


        demoRef.child("/"+uid+"/").child("Balance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                _count = Double.parseDouble(value);
                df.setRoundingMode(RoundingMode.HALF_UP);
                String str1 = df.format(_count);

                balance.setText(str1);
              //  balance.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UserUpdate.class);
                startActivity(intent);

            }
        });


        b1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                final AlertDialog.Builder builder = new AlertDialog.Builder(UserAccount.this);
                builder.setTitle("Your bank account will be deducted");
                builder.setMessage("Are You Sure want to top up ??");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        balancecredit.setText("");
                        dialogInterface.dismiss();
                    }
                });

                builder.setPositiveButton("Yes, Top up now!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        num1 = Double.parseDouble(balancecredit.getText().toString());
                        num2 = Double.parseDouble(balance.getText().toString());
                        sum = num1 + num2;
                        bal2.setText(Double.toString(sum));
                        String valuebal2 = bal2.getText().toString();

                        demoRef.child("/" + uid + "/").child("Balance").setValue(valuebal2);



                        demoRef.child("/"+uid+"/").child("Balance").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String value = dataSnapshot.getValue(String.class);
                                _count = Double.parseDouble(value);
                                df.setRoundingMode(RoundingMode.HALF_UP);
                                String str1 = df.format(_count);

                                balance.setText(str1);
                               // balance.setText(value);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                        balancecredit.setText("");

                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();



            }
        });



    }





    @Override
    public void onBackPressed(){
        Intent backMainTest = new Intent(this,UserMenu.class);
        startActivity(backMainTest);
        finish();
    }


}
