package com.afiq.ezsmartpark1;

/**
 * Created by Asus on 11/12/2017.
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
import android.widget.SeekBar;
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
import java.text.StringCharacterIterator;


public class UserPay extends AppCompatActivity {
    Button bPay;

    TextView masa1, masa2, tvRadius, tvRadius2, total, totalhour, totalbalance, totallast;
    SeekBar seekbar1, seekbar3;

    Double tth, jj, mm, mm2, hh, jh, hm, bal12, update;

    DecimalFormat df = new DecimalFormat("0.00");


    final Context context = this;


    private FirebaseAuth mauths;
    DatabaseReference rootRef,demoUser,dbPark,demoAuthority,demoHistory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_time_select);


        mauths = FirebaseAuth.getInstance();
        FirebaseUser user = mauths.getCurrentUser();
        final String uid;
        uid = user.getUid();


        totallast = (TextView) findViewById(R.id.latestbalance);
        totalbalance = (TextView) findViewById(R.id.jumlahbalance);
        bPay = (Button) findViewById(R.id.paybutton);
        totalhour = (TextView) findViewById(R.id.jumlahjam);
        total = (TextView) findViewById(R.id.jumlahmasa);
        masa1 = (TextView) findViewById(R.id.masa);
        masa2 = (TextView) findViewById(R.id.masajam);
      //  tvRadius = (TextView) findViewById(R.id.tvrad);
      //  tvRadius2 = (TextView) findViewById(R.id.tvrad3);
        seekbar1 = (SeekBar) findViewById(R.id.seekBar);
        seekbar3 = (SeekBar) findViewById(R.id.seekBar2);
    //    seekbar1.setMin(0);
        seekbar1.setProgress(0);
        seekbar1.incrementProgressBy(0);
        seekbar1.setMax(45);
        masa1.setText ("0");
       // masa1.setText(tvRadius.getText().toString().trim());

    //    seekbar3.setMin(0);
        seekbar3.setProgress(0);
        seekbar3.incrementProgressBy(0);
        seekbar3.setMax(24);
        masa2.setText ("0");
       // masa2.setText(tvRadius2.getText().toString().trim());
        total.setText("0");
        totalhour.setText("0");

        rootRef = FirebaseDatabase.getInstance().getReference();
        demoAuthority = rootRef.child("Authority");
        demoUser = rootRef.child("User");
        demoUser.child("/"+uid+"/").child("Balance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                bal12 = Double.parseDouble(value);
                df.setRoundingMode(RoundingMode.HALF_UP);
                String ball = df.format(bal12);
                totalbalance.setText(String.valueOf(ball));
                // String str12 = df.format(counting);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / +15;
                progress = progress * +15;
                masa1.setText(String.valueOf(progress));
                calc();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 1;
                progress = progress * 1;
                masa2.setText(String.valueOf(progress));
                calc();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




        demoUser.child("/"+uid+"/").child("Time Left").setValue(null);

        bPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String th = totalhour.getText().toString();
                tth = Double.parseDouble(th);

                if (tth == 0){

                    Toast.makeText(UserPay.this, "Please select the time. *minimum 15 minutes!",
                            Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(context, UserPay.class);
                    startActivity(intent2);

                }

                if (tth != 0 ) {

                    String tol = total.getText().toString();
                    mm2 = Double.parseDouble(tol);

                    update = bal12 - mm2;

                    String lastprice = df.format(update);


                    if (update <= 0) {

                        Toast.makeText(UserPay.this, "Insufficient Credit, Please Topup!",
                                Toast.LENGTH_SHORT).show();

                        mauths = FirebaseAuth.getInstance();
                        FirebaseUser user = mauths.getCurrentUser();
                        final String uid;
                        uid = user.getUid();


                        demoUser.child("/" + uid + "/").child("Time").setValue(null);
                        demoUser.child("/" + uid + "/").child("Locality").setValue(null);
                        demoUser.child("/" + uid + "/").child("Area").setValue(null);
                        demoUser.child("/" + uid + "/").child("City").setValue(null);
                        demoUser.child("/" + uid + "/").child("Address").setValue(null);
                        demoUser.child("/" + uid + "/").child("Date").setValue(null);
                        demoUser.child("/" + uid + "/").child("Total Price").setValue(null);
                        demoUser.child("/" + uid + "/").child("Time Left").setValue(null);

                        Intent intent = new Intent(context, UserAccount.class);
                        startActivity(intent);
                        demoUser.child("/" + uid + "/").child("Plate Number").setValue("NoPark");
                    }

                    if (update >= 0) {

                        rootRef = FirebaseDatabase.getInstance().getReference();
                        demoUser = rootRef.child("User");

                        String tl = totalhour.getText().toString();
                        demoUser.child("/" + uid + "/").child("Time Left").setValue(tl);

                        String tp = total.getText().toString();
                        demoUser.child("/" + uid + "/").child("Total Price").setValue(tp);

                        demoUser.child("/" + uid + "/").child("Balance").setValue(lastprice);
                        Intent intent = new Intent(context, UserActive.class);
                        startActivity(intent);

                    }

                }
            }
        });









    }


    private void   calc(){

        String m = masa1.getText().toString();
        String h = masa2.getText().toString();

        mm = Double.parseDouble(m);
        hh = Double.parseDouble(h);

        hm = mm / (60);

        jj = hm + hh;

        jh = jj * (0.8);

        // String j = (jj);
        String price = df.format(jh);
        total.setText(String.valueOf(price));

        String hour = df.format(jj);
        totalhour.setText(String.valueOf(hour));

        String tol = total.getText().toString();
        mm2 = Double.parseDouble(tol);

        update = bal12 - mm2;

        String lastprice = df.format(update);
        totallast.setText(String.valueOf(lastprice));


    }


    @Override
    public void onBackPressed(){


        mauths = FirebaseAuth.getInstance();
        FirebaseUser user = mauths.getCurrentUser();
        final String uid;
        uid = user.getUid();


        demoUser.child("/" + uid + "/").child("Time").setValue(null);
        demoUser.child("/" + uid + "/").child("Locality").setValue(null);
        demoUser.child("/" + uid + "/").child("Area").setValue(null);
        demoUser.child("/" + uid + "/").child("City").setValue(null);
        demoUser.child("/" + uid + "/").child("Address").setValue(null);
        demoUser.child("/" + uid + "/").child("Date").setValue(null);
        demoUser.child("/"+uid+"/").child("Total Price").setValue(null);
        demoUser.child("/"+uid+"/").child("Time Left").setValue(null);
        demoUser.child("/" + uid + "/").child("Plate Number").setValue("NoPark");


        Intent backMainTest = new Intent(this,UserPark.class);
        startActivity(backMainTest);
        finish();






    }





}
